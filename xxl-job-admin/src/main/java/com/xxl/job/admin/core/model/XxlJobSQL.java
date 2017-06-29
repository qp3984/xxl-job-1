package com.xxl.job.admin.core.model;

import java.util.ArrayList;
import java.util.List;

public class XxlJobSQL {

	private transient int id;
	private String task_name;
	private String datasource_name;
	private String sqlList;
	private String recipient_lists;
	private List<XxlJobSubSQL> xxlJobSubSQLs = new ArrayList<XxlJobSubSQL>();
	private String cc_lists;
	public String getTask_name() {
		return task_name;
	}


	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}


	public String getDatasource_name() {
		return datasource_name;
	}


	public void setDatasource_name(String datasource_name) {
		this.datasource_name = datasource_name;
	}


	public String getRecipient_lists() {
		return recipient_lists;
	}


	public void setRecipient_lists(String recipient_lists) {
		this.recipient_lists = recipient_lists;
	}


	public String getCc_lists() {
		return cc_lists;
	}


	public void setCc_lists(String cc_lists) {
		this.cc_lists = cc_lists;
	}







	

	public int getId() {
		return id;
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

	

	
	public void setXxlJobSubSQLs(List<XxlJobSubSQL> xxlJobSubSQLs) {
		this.xxlJobSubSQLs = xxlJobSubSQLs;
	}
}
