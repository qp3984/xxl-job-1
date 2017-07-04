package com.xxl.job.admin.core.model;




public class XxlJobMetaData {


    private String ip;//链接地址
    private String dcmOwner;//库名
    private String tableName;//表名
    private String columnName;// 字段名
    private String columnClass;    // 字段类型
    private String columnSize;    // 字段长度
    private String columnExplain;    // 这字段解释
    private String falg;    // 这字段新增状态 1不是新增 2是新增
    private String dateTime;    // 插入时间
    
    
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getFalg() {
		return falg;
	}
	public void setFalg(String falg) {
		this.falg = falg;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getDcmOwner() {
		return dcmOwner;
	}
	public void setDcmOwner(String dcmOwner) {
		this.dcmOwner = dcmOwner;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getColumnClass() {
		return columnClass;
	}
	public void setColumnClass(String columnClass) {
		this.columnClass = columnClass;
	}
	public String getColumnSize() {
		return columnSize;
	}
	public void setColumnSize(String columnSize) {
		this.columnSize = columnSize;
	}
	public String getColumnExplain() {
		return columnExplain;
	}
	public void setColumnExplain(String columnExplain) {
		this.columnExplain = columnExplain;
	}
	
	
   
}
