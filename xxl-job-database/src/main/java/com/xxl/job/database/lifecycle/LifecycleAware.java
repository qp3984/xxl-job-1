package com.xxl.job.database.lifecycle;

/**
 * 负责管理所有对象的生命周期的接口。
 *
 */

public interface LifecycleAware {

    /**
     * <p>
     * 启动服务或组件。<br>
     * 实现类在启动服务或者组件之后，应该一并设置lifecycleState的值，<br>
     * 以保证{@link #getLifecycleState()}能返回正确的值<br>
     * </p>
     *
     * @throws LifecycleException
     * @throws InterruptedException
     */
    public void start();

    /**
     * <p>
     * 停止服务或者组件。<br>
     * 实现类在启动服务或者组件之后，应该一并设置lifecycleState的值，<br>
     * 以保证{@link #getLifecycleState()}能返回正确的值<br>
     * </p>
     *
     * @throws LifecycleException
     * @throws InterruptedException
     */
    public void stop();

    /**
     * <p>
     * 返回当前服务或者组件的lifecycleState.
     * </p>
     */
    public LifecycleState getLifecycleState();

}
