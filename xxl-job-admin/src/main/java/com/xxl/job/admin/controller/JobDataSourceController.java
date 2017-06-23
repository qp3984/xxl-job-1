package com.xxl.job.admin.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxl.job.admin.core.model.XxlJobDataSource;
import com.xxl.job.core.biz.model.ReturnT;

/**
 * 数据源管理器
 */
@Controller
@RequestMapping("/jobdatasource")
public class JobDataSourceController {
	
	@RequestMapping
	public String index(Model model) {
		try {
          List<XxlJobDataSource>list=new ArrayList<>();
            Properties prop=new Properties();
            File file=new File("D:/workspase-Work/xxl-job/xxl-job-admin/target/xxl-job-admin-1.8.0-SNAPSHOT/WEB-INF/classes/deploy/datasource");
            File[] listFiles = file.listFiles();
            for(int i=0;i<listFiles.length;i++){
			InputStream in=new BufferedInputStream(new FileInputStream(listFiles[i]));
			prop.load(in);
			in.close();
			XxlJobDataSource XxlJobDataSource=new XxlJobDataSource();
			XxlJobDataSource.setConnectionName(listFiles[i].getName()); 
			 String url = (String) prop.get("url"); 
			 String[] strings = url.split(":");
			 if(strings[1].equals("mysql")){
				 XxlJobDataSource.setType("mysql");
				 XxlJobDataSource.setHostName(strings[2].substring(2));
				 XxlJobDataSource.setCode(strings[3].split("/")[0]);
				 XxlJobDataSource.setDbName((strings[3].split("/")[1]).split("\\?")[0]);
			 }else{
				 XxlJobDataSource.setType("oracle");
				 XxlJobDataSource.setHostName(strings[3].substring(1));
				 XxlJobDataSource.setCode(strings[4]);
				 XxlJobDataSource.setDbName(strings[5]);
			 }
			 XxlJobDataSource.setUsername(prop.getProperty("username"));
			 XxlJobDataSource.setPassword(prop.getProperty("password"));
			list.add(XxlJobDataSource);
			model.addAttribute("list", list);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		  
		return "jobdatasource/jobdatasource.index";
	}
	@RequestMapping("/add")
	@ResponseBody
	public ReturnT<String> add(XxlJobDataSource XxlJobDataSource) {
			
			try {
				Properties prop=new Properties();
				File file=new File("D:/workspase-Work/xxl-job/xxl-job-admin/target/xxl-job-admin-1.8.0-SNAPSHOT/WEB-INF/classes/deploy/datasource");
				FileOutputStream  os=new FileOutputStream(file+"/"+XxlJobDataSource.getConnectionName());
				if(XxlJobDataSource.getType().equals("mysql")){
					String url="jdbc:"+XxlJobDataSource.getType()+"://"+XxlJobDataSource.getHostName()+":"+XxlJobDataSource.getCode()+"/"+XxlJobDataSource.getDbName()+
							"?useUnicode=true&characterEncoding=utf8&autoReconnect=false&auoReconnectForPools=false&yearIsDateType=false";
				    String[] strings = url.split(":");
				    if(strings.length<4){
				    	os.close();
				    	return new ReturnT<>(500, "输入信息不完整！");
				    }
				    if(strings.length>4){
				    	os.close();
				    	return new ReturnT<>(500, "输入格式有误，请按提示输入！");
				    }
					prop.setProperty("driverClassName", "com.mysql.jdbc.Driver");
					prop.setProperty("url", url);
					prop.setProperty("username", XxlJobDataSource.getUsername());
					prop.setProperty("password", XxlJobDataSource.getPassword());
					prop.setProperty("maxActive","30");
					prop.setProperty("maxIdle","10");
					prop.setProperty("maxWait", "1000");
					prop.setProperty("removeAbandoned","true");
					prop.setProperty("removeAbandonedTimeout","180");
				}else{
					String url="jdbc:"+XxlJobDataSource.getType()+":thin:@"+XxlJobDataSource.getHostName()+":"+XxlJobDataSource.getCode()+":"+XxlJobDataSource.getDbName();
					String[] strings = url.split(":");
					if(strings.length<6){
						os.close();
						return new ReturnT<>(500, "输入信息不完整！");
					}
				    if(strings.length>6){
				    	os.close();
				    	return new ReturnT<>(500, "输入格式有误，请按提示输入！");
				    }
					prop.setProperty("driverClassName", "oracle.jdbc.driver.OracleDriver");
					prop.setProperty("url", url);
					prop.setProperty("username", XxlJobDataSource.getUsername());
					prop.setProperty("password", XxlJobDataSource.getPassword());
					prop.setProperty("maxActive","30");
					prop.setProperty("maxIdle","10");
					prop.setProperty("maxWait", "1000");
					prop.setProperty("removeAbandoned","true");
					prop.setProperty("removeAbandonedTimeout","180");
					
				}
				
				prop.store(os,null);
				os.close();
				return new ReturnT<>(200, "新增成功");
				
			} catch (Exception e) {
				e.printStackTrace();
				return new ReturnT<>(500, "新增失败");
			}
	
	}
	@RequestMapping("/update")
	@ResponseBody
	public ReturnT<String> update(XxlJobDataSource XxlJobDataSource) {
		
			try {
				Properties prop=new Properties();
				File file=new File("D:/workspase-Work/xxl-job/xxl-job-admin/target/xxl-job-admin-1.8.0-SNAPSHOT/WEB-INF/classes/deploy/datasource");
				FileOutputStream  os=new FileOutputStream(file+"/"+XxlJobDataSource.getConnectionName());
				if(XxlJobDataSource.getType().equals("mysql")){
					String url="jdbc:"+XxlJobDataSource.getType()+"://"+XxlJobDataSource.getHostName()+":"+XxlJobDataSource.getCode()+"/"+XxlJobDataSource.getDbName()+
							"?useUnicode=true&characterEncoding=utf8&autoReconnect=false&auoReconnectForPools=false&yearIsDateType=false";
					String[] strings = url.split(":");
					if(strings.length<4){
						os.close();
						return new ReturnT<>(500, "输入信息不完整！");
					}
				    if(strings.length>4){
				    	os.close();
				    	return new ReturnT<>(500, "输入格式有误，请不要输入“：”！");
				    }
					prop.setProperty("driverClassName", "com.mysql.jdbc.Driver");
					prop.setProperty("url", url);
					prop.setProperty("username", XxlJobDataSource.getUsername());
					prop.setProperty("password", XxlJobDataSource.getPassword());
					prop.setProperty("maxActive","30");
					prop.setProperty("maxIdle","10");
					prop.setProperty("maxWait", "1000");
					prop.setProperty("removeAbandoned","true");
					prop.setProperty("removeAbandonedTimeout","180");
				}else{
					String url="jdbc:"+XxlJobDataSource.getType()+":thin:@"+XxlJobDataSource.getHostName()+":"+XxlJobDataSource.getCode()+":"+XxlJobDataSource.getDbName();
					String[] strings = url.split(":");
					if(strings.length<6){
						os.close();
						return new ReturnT<>(500, "输入信息不完整！");
					}
				    if(strings.length>6){
				    	os.close();
				    	return new ReturnT<>(500, "输入格式有误，请不要输入“：”！");
				    }
					prop.setProperty("driverClassName", "oracle.jdbc.driver.OracleDriver");
					prop.setProperty("url", url);
					prop.setProperty("username", XxlJobDataSource.getUsername());
					prop.setProperty("password", XxlJobDataSource.getPassword());
					prop.setProperty("maxActive","30");
					prop.setProperty("maxIdle","10");
					prop.setProperty("maxWait", "1000");
					prop.setProperty("removeAbandoned","true");
					prop.setProperty("removeAbandonedTimeout","180");
					
				}
				
				prop.store(os, "");
				os.close();
				return new ReturnT<>(200, "更新成功");
				
			} catch (Exception e) {
				
				e.printStackTrace();
				return new ReturnT<>(500, "更新失败");
			}
		
	}
	@RequestMapping("/test")
	@ResponseBody
	public ReturnT<String> test(XxlJobDataSource XxlJobDataSource) {
		String url="";
		String username="";
		String password="";
		if(XxlJobDataSource.getType().equals("mysql")){
			url="jdbc:"+XxlJobDataSource.getType()+"://"+XxlJobDataSource.getHostName()+":"+XxlJobDataSource.getCode()+"/"+XxlJobDataSource.getDbName()+
					"?useUnicode=true&characterEncoding=utf8&autoReconnect=false&auoReconnectForPools=false&yearIsDateType=false";
			username = XxlJobDataSource.getUsername() ;   
			password = XxlJobDataSource.getPassword() ;  
		}else{
			url = "jdbc:"+XxlJobDataSource.getType()+":"+XxlJobDataSource.getHostName()+":"+XxlJobDataSource.getCode()+":"+XxlJobDataSource.getDbName();    
			username = XxlJobDataSource.getUsername() ;   
			password = XxlJobDataSource.getPassword() ;
		}
		try{   
			Connection con =    
					DriverManager.getConnection(url , username , password ) ;  
			return  ReturnT.SUCCESS;
			
		}catch(SQLException se){
			
			 se.printStackTrace();
			return ReturnT.FAIL;
		}   
	}
	@RequestMapping("/remove")
	@ResponseBody
	public ReturnT<String> remove(String connectionName) {
		System.out.println(connectionName);
		try {
			File file=new File("D:/workspase-Work/xxl-job/xxl-job-admin/target/xxl-job-admin-1.8.0-SNAPSHOT/WEB-INF/classes/deploy/datasource");
			File[] listFiles = file.listFiles();
			for(int i=0;i<listFiles.length;i++){
				if(listFiles[i].getName().equals(connectionName)){
					 file=new File(file+"/"+connectionName);
					System.out.println(file);
					boolean b = file.delete();
					System.out.println(b);
					return ReturnT.SUCCESS;
				}
			}
			return ReturnT.FAIL;
			
		} catch (Exception e) {
			e.printStackTrace();
			return ReturnT.FAIL;
		}
		
	}
	
}
