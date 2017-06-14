
package com.xxl.job.database.meta;


import com.xxl.job.database.dboperate.DBManager;

/**
 * Oracle元数据
 */
public class OracleDatabaseMeta extends DatabaseMeta {

    public OracleDatabaseMeta() {
        this.dbType = DBManager.ORACLE_DB;
        this.testSQL = "select 1 from dual";
        this.pkConflictStrs.add("ORA-00001");
//        sqlTemplate = new OracleSqlTemplate();
    }

    public String[] getReservedWords() {
        return new String[]{"ACCESS", "ADD", "ALL", "ALTER", "AND", "ANY", "ARRAYLEN", "AS", "ASC", "AUDIT",
                "BETWEEN", "BY", "CHAR", "CHECK", "CLUSTER", "COLUMN", "COMMENT", "COMPRESS", "CONNECT", "CREATE",
                "CURRENT", "DATE", "DECIMAL", "DEFAULT", "DELETE", "DESC", "DISTINCT", "DROP", "ELSE", "EXCLUSIVE",
                "EXISTS", "FILE", "FLOAT", "FOR", "FROM", "GRANT", "GROUP", "HAVING", "IDENTIFIED", "IMMEDIATE", "IN",
                "INCREMENT", "INDEX", "INITIAL", "INSERT", "INTEGER", "INTERSECT", "INTO", "IS", "LEVEL", "LIKE", "LOCK",
                "LONG", "MAXEXTENTS", "MINUS", "MODE", "MODIFY", "NOAUDIT", "NOCOMPRESS", "NOT", "NOTFOUND", "NOWAIT",
                "NULL", "NUMBER", "OF", "OFFLINE", "ON", "ONLINE", "OPTION", "OR", "ORDER", "PCTFREE", "PRIOR",
                "PRIVILEGES", "PUBLIC", "RAW", "RENAME", "RESOURCE", "REVOKE", "ROW", "ROWID", "ROWLABEL", "ROWNUM",
                "ROWS", "SELECT", "SESSION", "SET", "SHARE", "SIZE", "SMALLINT", "SQLBUF", "START", "SUCCESSFUL",
                "SYNONYM", "SYSDATE", "TABLE", "THEN", "TO", "TRIGGER", "UID", "UNION", "UNIQUE", "UPDATE", "USER",
                "VALIDATE", "VALUES", "VARCHAR", "VARCHAR2", "VIEW", "WHENEVER", "WHERE", "WITH"};
    }

    /**
     * @return true if the database supports sequences
     */
    @Override
    public boolean supportsSequences() {
        return true;
    }

    /**
     * @return true if the database supports schemas
     */
    @Override
    public boolean supportsSchemas() {
        return true;
    }

    /**
     * @return true if the database supports catalogs
     */
    @Override
    public boolean supportsCatalogs() {
        return true;
    }
}
