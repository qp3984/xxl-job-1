package com.xxl.job.admin.controller;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.model.XxlJobMetaData;
import com.xxl.job.admin.dao.IXxlJobMetaDataDao;
import com.xxl.job.admin.service.IXxlJobService;
import com.xxl.job.core.biz.model.ReturnT;

/**
 * 元数据管理
 */
@Controller
@RequestMapping("/jobmetadata")
public class JobMetaDataController {

	@Resource
	public  IXxlJobMetaDataDao xxlJobMetaData;

	@Resource
	private IXxlJobService xxlJobService;
	
    @RequestMapping
    public String index(Model model) {

        
       
        return "jobmetadata/jobmetadata.index";
//        return null;
    }
    
    @RequestMapping("/pageList")
	@ResponseBody
	public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,  
			@RequestParam(required = false, defaultValue = "10") int length,
			 String tableName) {
		
		return xxlJobService.metaDataPageList(start, length, tableName);
	}
    
    @RequestMapping("/selectList")
	@ResponseBody
	public List<XxlJobMetaData> selectList(String tableName) {
    	
		return xxlJobMetaData.getJobsByMetadata(tableName);
	}
    
    
    @RequestMapping("/select")
  	@ResponseBody
  	public List<XxlJobMetaData> select(String ip,String dcmOwner,String  tableName ,String columnName) {
  		return xxlJobMetaData.getJobsByColumn(ip, dcmOwner, tableName, columnName);
  	}
}
