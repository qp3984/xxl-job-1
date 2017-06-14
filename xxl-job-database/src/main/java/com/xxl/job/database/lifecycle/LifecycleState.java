package com.xxl.job.database.lifecycle;

/**
 * 生命周期的状态
 *
 * @author 
 */
public enum LifecycleState {
    /**
     * 空闲
     */
    IDLE,

    /**
     * 启动
     */
    START,

    /**
     * 停止
     */
    STOP,

    /**
     * 错误
     */
    ERROR;

    /**
     * 启动或错误
     */
    public static final LifecycleState[] START_OR_ERROR = new LifecycleState[]{
            START, ERROR
    };

    /**
     * 停止或错误
     */
    public static final LifecycleState[] STOP_OR_ERROR = new LifecycleState[]{
            STOP, ERROR
    };

}
