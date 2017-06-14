
package com.xxl.job.database;


/**
 * 数据库相关异常类
 */
public class DatabaseException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -1378007985975078051L;

    /**
     * @param msg
     */
    public DatabaseException(String msg) {
        super(msg);
    }

    /**
     * @param msg
     * @param th
     */
    public DatabaseException(String msg, Throwable th) {
        super(msg, th);
    }

    /**
     * @param th
     */
    public DatabaseException(Throwable th) {
        super(th);
    }

}
