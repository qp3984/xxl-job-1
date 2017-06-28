package com.xxl.job.admin.core.model;

import java.util.ArrayList;
import java.util.List;

public class XxlJobSQL {

	private int id;
	private String taskName;
	private String dataSource;
	private String sqlList;
	private List<XxlJobSubSQL> xxlJobSubSQLs = new ArrayList<XxlJobSubSQL>();

	public int getId() {
		return id;
	}

	public String getTaskName() {
		return taskName;
	}

	public String getDataSource() {
		return dataSource;
	}

	public String getSqlList() {
		return sqlList;
	}

	public void setSqlList(String sqlList) {
		this.sqlList = sqlList;
	}

	public List<XxlJobSubSQL> getXxlJobSubSQLs() {
		return xxlJobSubSQLs;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public void setXxlJobSubSQLs(List<XxlJobSubSQL> xxlJobSubSQLs) {
		this.xxlJobSubSQLs = xxlJobSubSQLs;
	}
}