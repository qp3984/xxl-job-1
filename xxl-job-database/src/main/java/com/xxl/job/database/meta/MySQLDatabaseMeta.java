package com.xxl.job.database.meta;

import com.xxl.job.database.dboperate.DBManager;

/**
 * 基于mysql的一些特殊处理定义
 *
 */
public class MySQLDatabaseMeta extends DatabaseMeta {

    public MySQLDatabaseMeta() {
        this.dbType = DBManager.MYSQL_DB;
    }
}
