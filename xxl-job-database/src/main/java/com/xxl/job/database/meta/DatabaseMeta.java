
package com.xxl.job.database.meta;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 数据库元数据
 *
 */
public class DatabaseMeta {

    /**
     * Use this length in a String value to indicate that you want to use a CLOB in stead of a normal text field.
     */
    public static final int CLOB_LENGTH = 9999999;

    public static final int DB2_BLOB_LENGTH = 1048576;

    private String name;

    protected String dbType;

    private String driverClass;

    private String userName;

    private String Password;

    private String url;

    private Properties connectionProperties;

    private Properties connectionPoolingProperties;

    private boolean usingConnectionPool = true;

    private String connectSQL = "";

    protected String testSQL = "";

    protected List<String> pkConflictStrs = new ArrayList<String>();

    {
        pkConflictStrs.add("ORA-00001");
    }

    protected int databaseMajorVersion;

    protected int databaseMinorVersion;

    protected String databaseName;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public Properties getConnectionProperties() {
        return connectionProperties;
    }

    public void setConnectionProperties(Properties connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    public Properties getConnectionPoolingProperties() {
        return connectionPoolingProperties;
    }

    public void setConnectionPoolingProperties(Properties connectionPoolingProperties) {
        this.connectionPoolingProperties = connectionPoolingProperties;
    }

    public boolean isUsingConnectionPool() {
        return usingConnectionPool;
    }

    public void setUsingConnectionPool(boolean usingConnectionPool) {
        this.usingConnectionPool = usingConnectionPool;
    }

    public String getConnectSQL() {
        return connectSQL;
    }

    public void setConnectSQL(String connectSQL) {
        this.connectSQL = connectSQL;
    }

    /**
     * @return Whether or not the database can use auto increment type of fields (pk)
     */
    public boolean supportsAutoInc() {
        return true;
    }

    /**
     * Checks whether or not the command setFetchSize() is supported by the JDBC driver...
     *
     * @return true is setFetchSize() is supported!
     */
    public boolean isFetchSizeSupported() {
        return true;
    }

    /**
     * @return true if the database supports schemas
     */
    public boolean supportsSchemas() {
        return false;
    }

    /**
     * @return true if the database supports catalogs
     */
    public boolean supportsCatalogs() {
        return true;
    }

    /**
     * @return true when the database engine supports empty transaction.
     * (for example Informix does not on a non-ANSI database type!)
     */
    public boolean supportsEmptyTransactions() {
        return true;
    }

    /**
     * Get the schema-table combination to query the right table.
     * Usually that is SCHEMA.TABLENAME, however there are exceptions to this rule...
     *
     * @param schema_name The schema name
     * @param table_part  The tablename
     * @return the schema-table combination to query the right table.
     */
    public String getSchemaTableCombination(String schema_name, String table_part) {
        return schema_name + "." + table_part;
    }

    /**
     * @return true if the database supports transactions.
     */
    public boolean supportsTransactions() {
        return true;
    }

    /**
     * @return true if the database supports sequences
     */
    public boolean supportsSequences() {
        return false;
    }

    /**
     * @return true if the database supports bitmap indexes
     */
    public boolean supportsBitmapIndex() {
        return true;
    }

    /**
     * @return true if the database JDBC driver supports the setLong command
     */
    public boolean supportsSetLong() {
        return true;
    }

    /**
     * @return an array of reserved words for the database type...
     */
    public String[] getReservedWords() {
        return new String[]{};
    }

    /**
     * @return true if reserved words need to be double quoted ("password", "select", ...)
     */
    public boolean quoteReservedWords() {
        return true;
    }

    /**
     * @return The start quote sequence, mostly just double quote, but sometimes [, ...
     */
    public String getStartQuote() {
        return "\"";
    }

    /**
     * @return The end quote sequence, mostly just double quote, but sometimes ], ...
     */
    public String getEndQuote() {
        return "\"";
    }

    /**
     * @return true if the database JDBC driver supports batch updates
     * For example Interbase doesn't support this!
     */
    public boolean supportsBatchUpdates() {
        return true;
    }

    /**
     * @return true if the database supports setting the maximum number of return rows in a resultset.
     */
    public boolean supportsSetMaxRows() {
        return true;
    }

    /**
     * @return true if the database supports a boolean, bit, logical, ... datatype
     * The default is false: map to a string.
     */
    public boolean supportsBooleanDataType() {
        return true;
    }

    /**
     * @return true if the database JDBC driver supports getBlob on the resultset.  If not we must use getBytes() to get the data.
     */
    public boolean supportsGetBlob() {
        return true;
    }

    /**
     * @return true if the database supports timestamp to date conversion.
     * For example Interbase doesn't support this!
     */
    public boolean supportsTimeStampToDateConversion() {
        if ("mysql".equals(this.dbType)) {
            return true;
        } else if ("oracle".equals(this.dbType)) {
            return true;
        } else if ("sqlserver".equals(this.dbType)) {
            return true;
        } else if ("db2".equals(this.dbType)) {
            return true;
        } else {
            return true;
        }

    }

    /**
     * 针对Mysql的处理，现在默认把Mysql的year类型当做String处理
     * <功能详细描述>
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    public boolean yearIsDateType() {
        if ("mysql".equals(this.dbType)) {
            return false;
        } else {
            return false;
        }
    }

    /**
     * null 与 ''是等价的吗
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    public boolean isEmptyStringNulled() {
        if ("mysql".equals(this.dbType)) {
            return false;
        } else if ("oracle".equals(this.dbType)) {
            return true;
        } else {
            return true;
        }
    }


    public boolean supportsFloatRoundingOnUpdate() {
        return true;
    }

    /**
     * 大部分都支持，更改默认为true.
     *
     * @return
     */
    public boolean supportBitStringSetBoolean() {
        return true;
    }

    /**
     * Get the maximum length of a text field for this database connection.
     * This includes optional CLOB, Memo and Text fields. (the maximum!)
     *
     * @return The maximum text field length for this database type. (mostly CLOB_LENGTH)
     */
    public int getMaxTextFieldLength() {
        return DatabaseMeta.CLOB_LENGTH;
    }

    /**
     * Get the maximum length of a text field (VARCHAR) for this database connection.
     * If this size is exceeded use a CLOB.
     *
     * @return The maximum VARCHAR field length for this database type. (mostly identical to getMaxTextFieldLength() - CLOB_LENGTH)
     */
    public int getMaxVARCHARLength() {
        return DatabaseMeta.CLOB_LENGTH;
    }

    public boolean supportsSetCharacterStream() {
        return true;
    }


    private String handleCase(String field) {
        if (isDefaultingToUppercase()) {
            return field.toUpperCase();
        } else {
            return field.toLowerCase();
        }
    }

    private boolean isDefaultingToUppercase() {
        return false;
    }


    public boolean isForcingIdentifiersToUpperCase() {
        return false;
    }

    public boolean isForcingIdentifiersToLowerCase() {
        return false;
    }

    public boolean hasSpacesInField(String field) {
        return false;
    }

    public String getPreferredSchemaName() {
        return null;
    }

    public boolean isQuoteAllFields() {
        return false;
    }

    public boolean hasDotInField(String fieldname) {
        if (fieldname == null)
            return false;
        if (fieldname.indexOf('.') >= 0)
            return true;
        return false;
    }

    /**
     * Detects if a field has spaces in the name.  We need to quote the field in that case.
     *
     * @param fieldname The fieldname to check for spaces
     * @return true if the fieldname contains spaces
     */
    public boolean hasSpecialCharInField(String fieldname) {
        if (fieldname == null)
            return false;
        if (fieldname.indexOf('/') >= 0)
            return true;
        if (fieldname.indexOf('-') >= 0)
            return true;
        if (fieldname.indexOf('+') >= 0)
            return true;
        if (fieldname.indexOf(',') >= 0)
            return true;
        if (fieldname.indexOf('*') >= 0)
            return true;
        if (fieldname.indexOf('(') >= 0)
            return true;
        if (fieldname.indexOf(')') >= 0)
            return true;
        if (fieldname.indexOf('{') >= 0)
            return true;
        if (fieldname.indexOf('}') >= 0)
            return true;
        if (fieldname.indexOf('[') >= 0)
            return true;
        if (fieldname.indexOf(']') >= 0)
            return true;
        if (fieldname.indexOf('%') >= 0)
            return true;
        if (fieldname.indexOf('@') >= 0)
            return true;
        if (fieldname.indexOf('?') >= 0)
            return true;
        return false;
    }

    public List<String> getPkConflictStrs() {
        return pkConflictStrs;
    }

    public void setPkConflictStrs(List<String> pkConflictStrs) {
        this.pkConflictStrs = pkConflictStrs;
    }

    public void addPkConflictStr(String s) {
        pkConflictStrs.add(s);
    }

    public boolean isPkConfictError(SQLException e) {
        for (String key : pkConflictStrs) {
            if (e.getMessage().indexOf(key) != -1) {
                return true;
            }
        }

        if ("mysql".equals(this.dbType) && e.getErrorCode() == 1062) {
            return true;
        }

        return false;
    }

    public String getTestSQL() {
        return testSQL;
    }

    public void setTestSQL(String testSQL) {
        this.testSQL = testSQL;
    }

    public int getDatabaseMajorVersion() {
        return databaseMajorVersion;
    }

    public void setDatabaseMajorVersion(int databaseMajorVersion) {
        this.databaseMajorVersion = databaseMajorVersion;
    }

    public int getDatabaseMinorVersion() {
        return databaseMinorVersion;
    }

    public void setDatabaseMinorVersion(int databaseMinorVersion) {
        this.databaseMinorVersion = databaseMinorVersion;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

}
