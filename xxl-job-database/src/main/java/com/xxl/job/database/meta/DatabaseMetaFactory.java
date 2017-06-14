package com.xxl.job.database.meta;

import com.xxl.job.database.dboperate.DBManager;

/**
 * DatabaseMeta工厂类
 *
 */
public class DatabaseMetaFactory {

    /**
     * 创建Database的元数据
     *
     * @param dbType
     * @return
     */
    public static DatabaseMeta createDatabaseMeta(String dbType) {
        if (DBManager.ORACLE_DB.equals(dbType)) {
            return new OracleDatabaseMeta();
        } else if (DBManager.MYSQL_DB.equals(dbType)) {
            return new MySQLDatabaseMeta();
        } else {
            return new DatabaseMeta();
        }
    }

}
