package com.xxl.job.database.dboperate;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 */
public class OracleDBOperate extends DBOperate {

    /**
     * 判断是否是8i数据库，
     * 8i在处理时间戳时，不支持毫秒级，系统当前时间也不能用systimestamp。
     *
     * @throws Exception
     * @return: String oracle8 或者 oracle
     * @author
     */
    public static String getOracleVersion(Connection con) {
        String ver = DBManager.ORACLE_DB;
        String testStr = "select systimestamp from dual";

        Statement stm = null;
        try {
            stm = con.createStatement();
            stm.executeQuery(testStr);
        } catch (SQLException ex) {
            //TODO "数据库版本不支持！"
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) {
                }
            }
        }

        return ver;

    }

}
