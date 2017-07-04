package com.xxl.job.admin.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.model.XxlJobMetaData;
import com.xxl.job.admin.core.schedule.XxlJobDynamicScheduler;
import com.xxl.job.admin.dao.IXxlJobMetaDataDao;

@Repository
public class XxlJobMetaDataDaoImpl implements IXxlJobMetaDataDao{
	
	@Resource
	public SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public List<XxlJobMetaData> pageList(int offset, int pagesize,  String tableName) {
	
      
    	HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("pagesize", pagesize);
		params.put("tableName", tableName);
		
		return sqlSessionTemplate.selectList("XxlJobMetaDataMapper.pageList", params);
	}
	
	@Override
	public int pageListCount(int offset, int pagesize, String tableName) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("pagesize", pagesize);
		params.put("tableName", tableName);
		
		return sqlSessionTemplate.selectOne("XxlJobMetaDataMapper.pageListCount", params);
	}


	@Override
	public List<XxlJobMetaData> getJobsByMetadata(String tableName) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		params.put("tableName", tableName);
	
		return sqlSessionTemplate.selectList("XxlJobMetaDataMapper.selectList", params);
	}

	@Override
	public List<XxlJobMetaData> getJobsByColumn(String ip,String dcmOwner,String  tableName ,String columnName) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("ip", ip);
		params.put("dcmOwner", dcmOwner);
		params.put("tableName", tableName);
		params.put("columnName", columnName);
		return  sqlSessionTemplate.selectList("XxlJobMetaDataMapper.select", params);
	}

	@Override
	public List<XxlJobMetaData> findAll() {
		
		return  sqlSessionTemplate.selectList("XxlJobMetaDataMapper.findAll");
		}

	
}
