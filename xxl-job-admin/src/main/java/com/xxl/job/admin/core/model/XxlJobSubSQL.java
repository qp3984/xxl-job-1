package com.xxl.job.admin.core.model;

public class XxlJobSubSQL {

    private int id;
    private String subTaskName;
    private String sql;

    public int getId() {
        return id;
    }

    public String getSubTaskName() {
        return subTaskName;
    }

    public String getSql() {
        return sql;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSubTaskName(String subTaskName) {
        this.subTaskName = subTaskName;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
