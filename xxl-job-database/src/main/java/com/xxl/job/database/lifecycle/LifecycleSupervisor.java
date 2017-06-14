package com.xxl.job.database.lifecycle;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 生命周期监管员
 */
public class LifecycleSupervisor implements LifecycleAware {

    private static final Logger logger = LoggerFactory.getLogger(LifecycleSupervisor.class);
    /**
     * 以被监管对象component为key，以监管器对象为value
     */
    private Map<LifecycleAware, Supervisoree> supervisedProcesses;

    /**
     * 以被监管对象component为key，以返回的结果为值
     */
    private Map<LifecycleAware, ScheduledFuture<?>> monitorFutures;
    private ScheduledThreadPoolExecutor monitorService;// 线程执行对象
    private LifecycleState lifecycleState;// 生命周期状态对象

    public LifecycleSupervisor() {
        // 生命周期状态设置为空闲
        lifecycleState = LifecycleState.IDLE;
        // 初始化监管器Map
        supervisedProcesses = new HashMap<LifecycleAware, Supervisoree>();
        // 初始化返回结果Map
        monitorFutures = new HashMap<LifecycleAware, ScheduledFuture<?>>();
        // 初始化线程执行Service
        monitorService = new ScheduledThreadPoolExecutor(5, new ThreadFactoryBuilder()
                .setNameFormat("lifecycleSupervisor-" + Thread.currentThread().getId() + "-%d").build());
        monitorService.setMaximumPoolSize(20);// 设置线程执行service中的最大线程数
        monitorService.setKeepAliveTime(30, TimeUnit.SECONDS);// 线程在终止前可以保持的空闲时间
    }

    /**
     * 启动supervisor. 1、启动空闲Future的清除线程。Server启动后2小时执行第一次，以后每两个小时执行一次
     * 2、生命周期状态设置为Start。
     */
    @Override
    public synchronized void start() {
        logger.info("Starting lifecycle supervisor {}", Thread.currentThread().getId());
        lifecycleState = LifecycleState.START;
        logger.debug("Lifecycle supervisor started");
    }

    /**
     * 停止supervisor. 1、首先停止线程执行Service. 2、停止Components，并设置{@link Supervisoree}
     * 的status. 3、清空监控对象列表(supervisedProcesses)以及结果列表(monitorFutures)。
     */
    @Override
    public synchronized void stop() {
        logger.info("Stopping lifecycle supervisor {}", Thread.currentThread().getId());

        // 停止所有的Components，并设置{@link Supervisoree}的status.
        for (final Entry<LifecycleAware, Supervisoree> entry : supervisedProcesses.entrySet()) {
            if (entry.getKey().getLifecycleState().equals(LifecycleState.START)) {
                entry.getValue().status.desiredState = LifecycleState.STOP;
                entry.getKey().stop();
            }
        }
        // 清空监控对象列表(supervisedProcesses)以及结果列表(monitorFutures)。
        if (lifecycleState.equals(LifecycleState.START)) {
            lifecycleState = LifecycleState.STOP;
        }

        // 首先停止Executor以及其中执行的MonitorRunnable线程
        if (monitorService != null) {
            monitorService.shutdown();
            try {
                monitorService.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logger.error("Interrupted while waiting for monitor service to stop");
            }
            if (!monitorService.isTerminated()) {
                monitorService.shutdownNow();
                try {
                    while (!monitorService.isTerminated()) {
                        monitorService.awaitTermination(10, TimeUnit.SECONDS);
                    }
                } catch (InterruptedException e) {
                    logger.error("Interrupted while waiting for monitor service to stop");
                }
            }
        }

        supervisedProcesses.clear();
        monitorFutures.clear();
        logger.debug("Lifecycle supervisor stopped");
    }

    /**
     * 失败方法，主要就是将生命周期状态设置为错误
     */
    public synchronized void fail() {
        lifecycleState = LifecycleState.ERROR;
    }

    /**
     * 监控某组件
     *
     * @param lifecycleAware 被监控组件
     * @param policy         监控策略
     * @param desiredState   期望的状态
     */
    public synchronized void supervise(LifecycleAware lifecycleAware, SupervisorPolicy policy,
                                       LifecycleState desiredState) {
        // 线程执行service不能处于停止或者终止的状态
        if (this.monitorService.isShutdown() || this.monitorService.isTerminated()
                || this.monitorService.isTerminating()) {
            throw new RuntimeException("Supervise called on " + lifecycleAware + " " + "after shutdown has been initiated. "
                    + lifecycleAware + " will not" + " be started");
        }
        // 检测component是否已经设置到了supervisedProcesses中，如果已经设置，则抛出异常
        Preconditions.checkState(!supervisedProcesses.containsKey(lifecycleAware),
                "Refusing to supervise " + lifecycleAware + " more than once");
        if (logger.isDebugEnabled()) {
            logger.debug("Supervising service:{} policy:{} desiredState:{}",
                    new Object[]{lifecycleAware, policy, desiredState});
        }

        // 实例化监管器
        Supervisoree process = new Supervisoree();
        process.status = new Status();// 初始化监管器的状态
        process.policy = policy;// 设置process使用的策略
        process.status.desiredState = desiredState;// 设置process期望的状态
        process.status.error = false;// 不设置错误

        // 实例化监管线程
        MonitorRunnable monitorRunnable = new MonitorRunnable();
        monitorRunnable.lifecycleAware = lifecycleAware;
        monitorRunnable.supervisoree = process;
        monitorRunnable.monitorService = monitorService;
        monitorRunnable.lifecycleSupervisor = this;
        // 将componnet及其使用的processor添加到map中
        supervisedProcesses.put(lifecycleAware, process);

        // 执行监管线程:间隔几秒使用线程检查组件的状态，实现对具体组件的生命周期的管理。主要是停止->启动。启动->停止
        // 每一个组件都会有一个线程进行监控，线程池的最大大小是20，基本上能够满足需求
        ScheduledFuture<?> future = monitorService.scheduleWithFixedDelay(monitorRunnable, 0, 10, TimeUnit.SECONDS);
        monitorFutures.put(lifecycleAware, future);
    }

    /**
     * 停止对某个组件的监控
     *
     * @param lifecycleAware
     */
    public synchronized void unsupervise(LifecycleAware lifecycleAware) {
        // 检测某个component是否已经设置到processor中，如果没有设置，则抛出异常
        Preconditions.checkState(supervisedProcesses.containsKey(lifecycleAware),
                "Unaware of " + lifecycleAware + " - can not unsupervise");
        logger.debug("Unsupervising service:{}", lifecycleAware);
        // 设置状态，并执行停止操作
        synchronized (lifecycleAware) {
            Supervisoree supervisoree = supervisedProcesses.get(lifecycleAware);// 获取监控器
            supervisoree.status.discard = true;// 设置为被抛弃
            this.setDesiredState(lifecycleAware, LifecycleState.STOP);// 期望状态设置为STOP
            logger.info("Stopping component: {}", lifecycleAware);
            lifecycleAware.stop();// 停止component
        }
        supervisedProcesses.remove(lifecycleAware);// 从processor列表中移除component
        monitorFutures.get(lifecycleAware).cancel(false);// 取消任务的执行
        monitorFutures.remove(lifecycleAware);// 移除
    }

    /**
     * 给指定的被监控对象设置期望的状态
     *
     * @param lifecycleAware 被设置的component对象
     * @param desiredState   期望的状态
     */
    public synchronized void setDesiredState(LifecycleAware lifecycleAware, LifecycleState desiredState) {
        Preconditions.checkState(supervisedProcesses.containsKey(lifecycleAware),
                "Unaware of " + lifecycleAware + " - can not set desired state to " + desiredState);
        logger.debug("Setting desiredState:{} on service:{}", desiredState, lifecycleAware);
        Supervisoree supervisoree = supervisedProcesses.get(lifecycleAware);// 从map中获取被设置对象当前的监控器
        supervisoree.status.desiredState = desiredState;// 设置监控器对象的状态
    }

    @Override
    public synchronized LifecycleState getLifecycleState() {
        return lifecycleState;
    }

    /**
     * 判断component当前是否处于error的状态
     *
     * @param component
     * @return
     */
    public synchronized boolean isComponentInErrorState(LifecycleAware component) {
        return supervisedProcesses.get(component).status.error;

    }

    /**
     * 监控线程
     *
     * @author dongpo.jia 2015年6月16日 下午2:33:57
     */
    public static class MonitorRunnable implements Runnable {
        // 执行线程的service
        public ScheduledExecutorService monitorService;
        // 组件
        public LifecycleAware lifecycleAware;
        // 监控器对象
        public Supervisoree supervisoree;

        LifecycleSupervisor lifecycleSupervisor;

        @Override
        public void run() {
            logger.trace("checking process:{} supervisoree:{}", lifecycleAware, supervisoree);
            long now = System.currentTimeMillis();

            try {
                // 如果是第一次的探视
                if (supervisoree.status.firstSeen == null) {
                    logger.debug("first time seeing {}", lifecycleAware);
                    supervisoree.status.firstSeen = now;// 将第一次探视的时间设置为当前时间
                }
                supervisoree.status.lastSeen = now;
                synchronized (lifecycleAware) {
                    if (supervisoree.status.discard) {// 如果该组件是被抛弃的，则直接返回
                        logger.info("Component has already been stopped {}", lifecycleAware);
                        return;
                    } else if (supervisoree.status.error) {// 如果该组件是错误的，则字节返回
                        logger.info("Component {} is in error state, and xxl-job will not" + "attempt to change its state",
                                lifecycleAware);
                        return;
                    }
                    // 设置最后一次的状态
                    supervisoree.status.lastSeenState = lifecycleAware.getLifecycleState();
                    // 如果component的当前状态与期望的状态不一致，表明对生命周期进行了启动或者停止操作（当前启动，则期望停止；当前停止，则期望启动）
                    logger.trace(lifecycleAware + ",状态为:" + lifecycleAware.getLifecycleState());
                    if (!lifecycleAware.getLifecycleState().equals(supervisoree.status.desiredState)) {
                        logger.debug("Want to transition {} from {} to {} (failures:{})",
                                new Object[]{lifecycleAware, supervisoree.status.lastSeenState,
                                        supervisoree.status.desiredState, supervisoree.status.failures});

                        switch (supervisoree.status.desiredState) {
                            case START:// 如果期望的状态是start
                                try {
                                    lifecycleAware.start();// 启动component
                                } catch (Exception e) {
                                    logger.error("不能正常启动 " + lifecycleAware + " - 异常信息是{},停止整个Application.....", e);
                                    // 之前的逻辑是不能成功启动的话，仅仅计数启动失败，不进行别的动作，后续继续重试启动，现在如果发现了启动失败，则直接停止Application
                                    // 因此，对于重试类型的异常，需要在组件的启动方法中处理完毕，只要传递到此处的异常，均停止Application
                                    this.lifecycleSupervisor.stop();
                                }
                                break;
                            case STOP:// 如果期望的状态是stop
                                try {
                                    lifecycleAware.stop();// 停止component
                                } catch (Throwable e) {
                                    logger.error("Unable to stop " + lifecycleAware + " - Exception follows.", e);
                                    if (e instanceof Error) {
                                        throw (Error) e;
                                    }
                                    // 停止component的时候如果出现了异常，则对失败的计数器+1
                                    supervisoree.status.failures++;
                                }
                                break;
                            default:
                                logger.warn("I refuse to acknowledge {} as a desired state",
                                        supervisoree.status.desiredState);
                        }

                        if (!supervisoree.policy.isValid(lifecycleAware, supervisoree.status)) {
                            logger.error("Policy {} of {} has been violated - supervisor should exit!",
                                    supervisoree.policy, lifecycleAware);
                        }
                    }
                }
            } catch (Throwable t) {
                logger.error("Unexpected error", t);
            }
            logger.trace("Status check complete");
        }
    }

    /**
     * 监管到的状态
     *
     * @author dongpo.jia 2015年6月16日 上午10:13:21
     */
    public static class Status {
        public Long firstSeen;// 第一次看到的值
        public Long lastSeen;// 最后一次看到的值
        public LifecycleState lastSeenState;// 最后一次 看到的状态
        public LifecycleState desiredState;// 期望的状态
        public int failures;// 失败的次数
        public boolean discard;// 是否被抛弃的
        public volatile boolean error;// 是否错误的

        @Override
        public String toString() {
            return "{ lastSeen:" + lastSeen + " lastSeenState:" + lastSeenState + " desiredState:" + desiredState
                    + " firstSeen:" + firstSeen + " failures:" + failures + " discard:" + discard + " error:" + error
                    + " }";
        }

    }

    /**
     * 监管策略
     *
     * @author dongpo.jia 2015年6月16日 上午9:58:00
     */
    public static abstract class SupervisorPolicy {

        /**
         * 该方法返回ture时重启，返回false不再重启
         *
         * @param object 被监管的对象
         * @param status {@link Status}
         * @return
         */
        abstract boolean isValid(LifecycleAware object, Status status);

        /**
         * 一直重启的策略对象
         *
         * @author dongpo.jia 2015年6月16日 上午10:07:16
         */
        public static class AlwaysRestartPolicy extends SupervisorPolicy {

            /**
             * 一直返回true，表名需要一直重启
             */
            @Override
            boolean isValid(LifecycleAware object, Status status) {
                return true;
            }
        }

        /**
         * 仅仅重启一次的策略对象
         *
         * @author dongpo.jia 2015年6月16日 上午10:07:38
         */
        public static class OnceOnlyPolicy extends SupervisorPolicy {

            /**
             * 仅仅第一次失败的时候重启一次，之后不再重启
             */
            @Override
            boolean isValid(LifecycleAware object, Status status) {
                return status.failures == 0;
            }
        }

    }

    /**
     * 监管器
     *
     * @author dongpo.jia 2015年6月16日 上午9:55:59
     */
    private static class Supervisoree {

        /**
         * 监管的策略对象
         */
        public SupervisorPolicy policy;

        /**
         * 监管的状态对象
         */
        public Status status;

        @Override
        public String toString() {
            return "{ status:" + status + " policy:" + policy + " }";
        }

    }

}
