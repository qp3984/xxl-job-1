package com.xxl.job.database;

import com.xxl.job.database.dboperate.DBManager;
import com.xxl.job.database.meta.DatabaseMeta;
import com.xxl.job.database.pool.DBConnectionPool;
import com.xxl.job.database.util.Const;
import oracle.jdbc.OracleConnection;
import org.apache.commons.dbcp.DelegatingConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;


/**
 * 数据库操作类
 *
 * @author dongpo.jia
 */
public class Database {

    Logger log = LoggerFactory.getLogger(Database.class);

    private DatabaseMeta databaseMeta;

    private DatabaseMetaData dbmd;

    private Connection connection;

    private int commitsize;

    private int written;
    private PreparedStatement prepStatementUpdate;
    private PreparedStatement prepStatementInsert;
    private String connectionGroup;

    public Database(DatabaseMeta databaseMeta) {
        this.databaseMeta = databaseMeta;
    }

    public DatabaseMeta getDatabaseMeta() {
        return databaseMeta;
    }

    public void setDatabaseMeta(DatabaseMeta databaseMeta) {
        this.databaseMeta = databaseMeta;
    }

    public DatabaseMetaData getDbmd() {
        return dbmd;
    }

    public void setDbmd(DatabaseMetaData dbmd) {
        this.dbmd = dbmd;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public int getCommitsize() {
        return commitsize;
    }

    /**
     * Connect using the correct classname
     *
     * @param classname for example "org.gjt.mm.mysql.Driver"
     * @return true if the connect was succesfull, false if something went
     * wrong.
     */
    private void connectUsingClass(String classname)
            throws DatabaseException {

        try {
            synchronized (java.sql.DriverManager.class) {
                Class.forName(classname);
            }
        } catch (NoClassDefFoundError e) {
            throw new DatabaseException("Exception while loading class", e);
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("Exception while loading class", e);
        } catch (Exception e) {
            throw new DatabaseException("Exception while loading class", e);
        }

        try {
            connection =
                    DriverManager.getConnection(databaseMeta.getUrl(),
                            databaseMeta.getUserName(),
                            databaseMeta.getPassword());
        } catch (SQLException e) {
            throw new DatabaseException("连接数据库时发生错误: (使用类 " + classname + ")", e);
        } catch (Throwable e) {
            throw new DatabaseException("连接数据库时发生错误: (使用类 " + classname + ")", e);
        }
    }

    /**
     * Open the database connection.
     *
     * @throws DatabaseException if something went wrong.
     */
    public void connect()
            throws DatabaseException {
        if (databaseMeta == null) {
            throw new DatabaseException("No valid database connection defined!");
        }

        try {
            // First see if we use connection pooling...
            //
            if (databaseMeta.isUsingConnectionPool()) {
                try {
                    this.connection = DBConnectionPool.getConnection(databaseMeta.getName());
                } catch (Exception e) {
                    log.error("尝试连接数据库时发生错误", e);
                    throw new DatabaseException("尝试连接数据库时发生错误", e);
                }
            } else {
                connectUsingClass(databaseMeta.getDriverClass());
                if (log.isDebugEnabled()) {
                    log.debug("Connected to database.");
                }

                // See if we need to execute extra SQL statemtent...
                //                String sql = databaseMeta.getConnectSQL();

                //                // only execute if the SQL is not empty, null and is not just a bunch of spaces, tabs, CR etc.
                //                if (!Const.isEmpty(sql) && !Const.onlySpaces(sql)) {
                //                    execStatements(sql);
                //                    if (log.isDebugEnabled()) {
                //                        log.debug("Executed connect time SQL statements:" + Const.CR + sql);
                //                    }
                //                }
            }
            //对oracle连接sessiontimezone的设定，用于读取类型为timestamp with local time zone的字段
            if (databaseMeta.getDbType() != null && databaseMeta.getDbType().equals(DBManager.ORACLE_DB)) {
                //将使用连接池和不使用连接池的情况分开来，使用连接池需要解封才能强制转换为OracleConnection
                if (!databaseMeta.isUsingConnectionPool()) {
                    ((OracleConnection) connection).setSessionTimeZone("Asia/Shanghai");
                } else {
                    ((OracleConnection) ((DelegatingConnection) connection).getInnermostDelegate()).setSessionTimeZone("Asia/Shanghai");
                }
            }
        } catch (Exception e) {
            throw new DatabaseException("尝试连接数据库时发生错误", e);
        }
    }


    /**
     * Disconnect from the database and close all
     * open prepared statements.
     */
    public synchronized void disconnect() {
        try {
            if (connection == null) {
                return;
            }
            if (connection.isClosed()) {
                return;
            }
            closeConnectionOnly();
        } catch (SQLException ex) {
            log.error("Error disconnecting from database:" + Const.CR + ex.getMessage());
            log.error(Const.getStackTracker(ex));
        } catch (DatabaseException dbe) {
            log.error("Error disconnecting from database:" + Const.CR + dbe.getMessage());
            log.error(Const.getStackTracker(dbe));
        }
    }

    /**
     * 关闭连接
     *
     * @throws DatabaseException
     */
    public synchronized void closeConnectionOnly()
            throws DatabaseException {
        try {
            if (connection != null) {
                boolean usingConnectionPool = databaseMeta.isUsingConnectionPool();
                //               if (usingConnectionPool) {
                //                   ((PoolableConnection) ((DelegatingConnection) connection).getDelegate()).reallyClose();
                //               } else {
                connection.close();
                connection = null;
                //               }
            }

            if (log.isDebugEnabled()) {
                log.debug("Connection to database closed!");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error disconnecting from database '" + toString() + "'", e);
        }
    }

    /**
     * Cancel an open/running SQL statement
     *
     * @param statement the statement to cancel
     * @throws DatabaseException
     */
    public void cancelStatement(Statement statement)
            throws DatabaseException {
        try {
            if (statement != null) {
                statement.cancel();
            }
            if (log.isDebugEnabled()) {
                log.debug("Statement canceled!");
            }
        } catch (SQLException ex) {
            throw new DatabaseException("Error cancelling statement", ex);
        }
    }

    /**
     * 获得数据库元数据
     *
     * @return
     * @throws DatabaseException
     */
    public DatabaseMetaData getDatabaseMetaData()
            throws DatabaseException {
        try {
            if (dbmd == null && connection != null) {
                dbmd = connection.getMetaData();
            }
        } catch (Exception e) {
            throw new DatabaseException("Unable to get database metadata from this database connection", e);
        }

        return dbmd;
    }

    /**
     * Specify after how many rows a commit needs to occur when inserting or
     * updating values.
     *
     * @param commsize The number of rows to wait before doing a commit on the
     *                 connection.
     */
    public void setCommitsize(int commsize) {
        commitsize = commsize;
    }
    
    /*public void setAutoCommit(boolean useAutoCommit)
        throws DatabaseException {
        try {
            connection.setAutoCommit(useAutoCommit);
        } catch (SQLException e) {
            throw new DatabaseException("Can't set autoCommit[" + useAutoCommit + "]", e);
        }
    }*/

    /**
     * Perform a commit the connection if this is supported by the database
     */
    public void commit()
            throws DatabaseException {
        commit(false);
    }

    public void commit(boolean force)
            throws DatabaseException {
        try {
            if (getDatabaseMetaData().supportsTransactions()) {
                if (log.isDebugEnabled()) {
                    log.debug("Commit on database connection [" + toString() + "]");
                }
                if (!connection.getAutoCommit()) {
                    connection.commit();
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("No commit possible on database connection [" + toString() + "]");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

    }

    public void rollback()
            throws DatabaseException {
        rollback(false);
    }

    public void rollback(boolean force)
            throws DatabaseException {
        try {
            if (getDatabaseMetaData() != null && getDatabaseMetaData().supportsTransactions()) {
                if (connection != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Rollback on database connection [" + toString() + "]");
                    }
                    if (!connection.getAutoCommit()) {
                        connection.rollback();
                    }
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("No rollback possible on database connection [" + toString() + "]");
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error performing rollback on connection", e);
        }
    }

    /**
     * 判断连接是否关闭
     *
     * @return
     */
    public boolean isConnected() {
        try {
            return (this.connection != null) && (!this.connection.isClosed());
        } catch (SQLException e) {
            return false;
        }
    }


    public void printSQLException(SQLException ex) {
        log.error("==> SQLException: ");
        while (ex != null) {
            log.error("Message:   " + ex.getMessage());
            log.error("SQLState:  " + ex.getSQLState());
            log.error("ErrorCode: " + ex.getErrorCode());
            ex = ex.getNextException();
            log.error("");
        }
    }

    public void closePreparedStatement(PreparedStatement ps)
            throws DatabaseException {
        if (ps != null) {
            try {
                ps.close();
                ps = null;
            } catch (SQLException e) {
                throw new DatabaseException("Error closing prepared statement", e);
            }
        }
    }


    public boolean absolute(ResultSet rs, int position)
            throws DatabaseException {
        try {
            return rs.absolute(position);
        } catch (SQLException e) {
            throw new DatabaseException("Unable to move resultset to position " + position, e);
        }
    }

    public boolean relative(ResultSet rs, int rows)
            throws DatabaseException {
        try {
            return rs.relative(rows);
        } catch (SQLException e) {
            throw new DatabaseException("Unable to move the resultset forward " + rows + " rows", e);
        }
    }

    public void afterLast(ResultSet rs)
            throws DatabaseException {
        try {
            rs.afterLast();
        } catch (SQLException e) {
            throw new DatabaseException("Unable to move resultset to after the last position", e);
        }
    }

    public void first(ResultSet rs)
            throws DatabaseException {
        try {
            rs.first();
        } catch (SQLException e) {
            throw new DatabaseException("Unable to move resultset to the first position", e);
        }
    }


    public boolean isAutoCommit() {
        return commitsize <= 0;
    }

    public String toString() {
        if (databaseMeta != null) {
            return databaseMeta.getName();
        } else {
            return "-";
        }
    }


    public static void main(String[] args) {
        /*DBConnectionPool pool = new DBConnectionPool();
        System.setProperty("ds.deploy.path", "F:/deploy");
        pool.start();
        DatabaseMeta dbMeta = new DatabaseMeta();
        dbMeta.setDbType(DBManager.ORACLE_DB);
        //		dbMeta.setDriverClass(driverClass);
        dbMeta.setName("oracle");
        dbMeta.setUsingConnectionPool(true);
        Database db = new Database(dbMeta);
        db.connect();
        db.disconnect();*/
    }

}