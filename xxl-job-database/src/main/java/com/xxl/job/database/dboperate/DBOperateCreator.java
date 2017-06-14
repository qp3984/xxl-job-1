package com.xxl.job.database.dboperate;

/**
 * DBOperateCreator类通过静态方法得到一个对具体数据库的DBOperate类
 */
public class DBOperateCreator {

    /**
     * @param dbtype 表示数据库类型
     * @return 返回一个数据库操作的具体类，若类型不匹配返回null
     */
    public static DBOperate getInstance(String dbtype) {
        if (dbtype.equalsIgnoreCase(DBManager.ORACLE_DB)) {
            return new OracleDBOperate();
        }

        if (dbtype.equalsIgnoreCase(DBManager.MYSQL_DB)) {
            return new MySqlDBOperate();
        }
        return null;
    }
}
