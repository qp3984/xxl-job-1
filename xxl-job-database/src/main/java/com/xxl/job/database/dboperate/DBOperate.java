package com.xxl.job.database.dboperate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;

/**
 * 数据源操作类
 */
public abstract class DBOperate {

    protected Logger log = LoggerFactory.getLogger(DBOperate.class);
    // attributes
    protected Connection con = null;
    protected ArrayList objal = null;
    // protected String schema ="";
    protected String dbname = "";

    protected String _url = "";
    protected String _driver = "";
    protected String _usrname = "";
    protected String _usrpwd = "";

    /**
     * open函数用于打开一个数据库可连接。
     *
     * @param url     数据库url。
     * @param driver  JDBC dirver。
     * @param usrname 用户名。
     * @param usrpwd  用户密码。
     * @return 如果连接成功返回true，否则返回false。
     */
    public boolean open(String url, String driver, String usrname, String usrpwd)
            throws OperateException {

        this._url = url;
        this._driver = driver;
        this._usrname = usrname;
        this._usrpwd = usrpwd;

        try {//载入JDBC driver的类
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            log.error("Failed to load db driver.", e);
            throw new OperateException(e);
        }

        try {//打开连接
            con = DriverManager.getConnection(url, usrname, usrpwd);

            con.setAutoCommit(false);
        } catch (SQLException e) {
            log.error("Failed to open db connection.", e);
            throw new OperateException(e);
        }
        //打开成功返回true.
        return true;
    }

    /**
     * 关闭连接
     */
    public void close() {
        if (con != null) {
            try {
                con.commit();
                con.setAutoCommit(true);
            } catch (SQLException e) {
                log.error("Failed to commit.", e);
            } finally {
                try {
                    con.close();
                } catch (SQLException e) {
                    log.error("Failed to close db connection:", e);
                }
            }
        }
    }


    /**
     * addService函数用于添加一个服务
     *
     * @param servicename 服务名
     * @return 如果添加成功返回true，否则返回false
     * @throws OperateException
     */
    public boolean addService(String servicename) throws OperateException {
        return false;
    }


    /**
     * addType函数用于添加一个typename
     *
     * @param typename MsgTypeName
     * @return 如果添加成功返回true，否则返回false
     * @throws OperateException
     */
    public boolean addType(String typename) throws OperateException {
        return false;
    }


    /**
     * 判断要添加的服务名是否已经存在
     *
     * @param servicename 服务名
     * @return 如果存在返回true，否则返回false
     */
    public boolean isServiceExist(String servicename) throws OperateException {
        if (!isValid())//若当前连接不可用返回false
            throw new OperateException("DB connection is not available.");
        Statement stm = null;
        ResultSet rs = null;
        String sql = null;

        sql = "select * from BCG_MsgService where MsgServiceName = '" + servicename + "'";
        try {
            stm = con.createStatement();
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            log.error("Failed to select servicename :", e);
            throw new OperateException(e);
        } finally {
            //清理Statement
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
            } catch (SQLException e) {
                log.error("Failed to clear resource:", e);
            }
        }
    }

    /**
     * 判断要添加的typeName是否已经存在
     *
     * @param typeName
     * @return 如果存在返回true，否则返回false
     */
    public boolean isTypeNameExist(String typeName) throws OperateException {
        if (!isValid())//若当前连接不可用返回false
            throw new OperateException("DB connection is not available.");
        Statement stm = null;
        ResultSet rs = null;
        String sql = null;

        sql = "select * from BCG_MsgType where MsgTypeName = '" + typeName + "'";
        try {
            stm = con.createStatement();
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            log.error("Failed to select typename:", e);
            throw new OperateException(e);
        } finally {
            //清理Statement
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
            } catch (SQLException e) {
                log.error("Failed to clear:", e);
            }
        }
    }


    /**
     * 判断当前的连接是否可用，如果可用返回真。
     *
     * @return 可用返回true, 否则返回false.
     */
    public boolean isValid() {
        try {
            if ((con == null) || con.isClosed()) {
                log.error("DB connection is not available.");
                return false;
            }
        } catch (SQLException e) {
            //		throw new OperateException("DB connection is not available");
            return false;
        }
        return true;
    }

    /**设置schema.如果没有给定schema,则schema="";如果给定则schema=schema2+"."
     * @param schema shcema字符串
     */

    /**
     * 获取连接
     */
    public Connection getConnection() throws OperateException {
        if (isValid()) {
            return this.con;
        } else {
            throw new OperateException("DB connection is not available.");
        }
    }

    /**
     * 判断表或存储过程是否存在于objal.
     *
     * @param cdbname    数据库名，没有的用null ,目前没有用都用null.
     * @param objname    要判断BCG的表或存储过程名。
     * @param refreshing 是否要更新当前objal，如果为true则更新当前objal,再判断是否存在于objal中.
     * @return 存在返回ture, 否则返回false.
     */
    public boolean isObjExist(String cdbname, String objname, boolean refreshing)
            throws OperateException {
        if (!isValid())//若当前连接不可用返回false
            throw new OperateException("DB connection is not available");
        if (objal == null || refreshing == true) {
            objal = new ArrayList();
            DatabaseMetaData gmd = null;
            ResultSet rsp = null;//save table metadata
            ResultSet rst = null;//save proc metadata
            String strobj = null;//save current table name or procedure name
            if (objal != null)
                objal.clear();
            try {
//				String cata = null;
//				if (!dbname.equalsIgnoreCase(""))//如果有数据库名则设置catalog,否则为空。
//					cata = dbname;

                gmd = con.getMetaData();

                String username = gmd.getUserName();//得到用户名，以用户名作schema

                //	zhq test by 20101210 TIS-1016
                String schemaName = null;
                if (this instanceof OracleDBOperate) {
                    schemaName = username;
                }

                rst = gmd.getTables(null, schemaName, "%", null);//get Table names in this schema.
                rsp = gmd.getProcedures(null, schemaName, "BCG%");//get Procedure names in this schema.
                while (rst.next()) {
                    strobj = rst.getString(3);
                    objal.add(strobj.toLowerCase());//add current table name to objal.
                }
                while (rsp.next()) {
                    strobj = rsp.getString(3);
                    objal.add((strobj).toLowerCase());// add current procedure name to objal.
                }

            } catch (SQLException e) {
                log.error("Failed to get db table and procedure context :", e);
                throw new OperateException(e);
            } finally {
                try {
                    if (rsp != null) rsp.close();
                    if (rst != null) rst.close();
                } catch (SQLException e) {
                    log.error("Failed to clear after getting db table and procedure context :", e);
                }
            }

        }//end if

        if (objal == null)
            return false;
        if (-1 == objal.indexOf(objname.toLowerCase()))
            return false;

        //存在
        return true;
    }


}
