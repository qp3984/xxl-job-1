package com.xxl.job.database.dboperate;


import com.xxl.job.database.DatabaseException;

/**
 * 操作异常
 */
public class OperateException extends DatabaseException {

    /**
     *
     */
    private static final long serialVersionUID = -2983389680492693351L;

    /**
     * @param msg
     */
    public OperateException(String msg) {
        super(msg);
    }

    /**
     * @param msg
     * @param th
     */
    public OperateException(String msg, Throwable th) {
        super(msg, th);
    }

    /**
     * @param th
     */
    public OperateException(Throwable th) {
        super(th);
    }

}
