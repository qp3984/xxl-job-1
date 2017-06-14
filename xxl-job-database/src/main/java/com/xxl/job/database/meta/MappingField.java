
package com.xxl.job.database.meta;

/**
 * 字段映射信息
 *
 */
public class MappingField {
	
	private String sourceName;//源数据名
	private String fieldName;//目标字段
	private boolean update;//目标字段是否更新
	private String matchCondition; //匹配条件
	private int sqlType;
	private String dbTypeName;

	/**
	 * 
	 */
	public MappingField() {
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public String getMatchCondition() {
		return matchCondition;
	}

	public void setMatchCondition(String matchCondition) {
		this.matchCondition = matchCondition;
	}

	public int getSqlType() {
		return sqlType;
	}

	public void setSqlType(int sqlType) {
		this.sqlType = sqlType;
	}

    public String getDbTypeName() {
        return dbTypeName;
    }

    public void setDbTypeName(String dbTypeName) {
        this.dbTypeName = dbTypeName;
    }
	
}
