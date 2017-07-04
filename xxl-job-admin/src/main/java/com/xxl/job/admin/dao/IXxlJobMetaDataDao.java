package com.xxl.job.admin.dao;


import java.util.List;
import java.util.Map;

import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.admin.core.model.XxlJobMetaData;


/**
 * 元数据字典
 */
public interface IXxlJobMetaDataDao {

    public List<XxlJobMetaData> pageList(int offset, int pagesize, String tableName);
    
    public int pageListCount(int offset, int pagesize, String tableName);
    
    public List<XxlJobMetaData> findAll();
    
    public List<XxlJobMetaData> getJobsByMetadata(String tableName);

    public List<XxlJobMetaData> getJobsByColumn(String ip,String dcmOwner,String  tableName ,String columnName);
}
