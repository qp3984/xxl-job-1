package com.xxl.job.admin.controller;

import com.xxl.job.admin.core.model.XxlJobDataSource;
import com.xxl.job.core.biz.model.ReturnT;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 数据源管理器
 */
@Controller
@RequestMapping("/jobdatasource")
public class JobDataSourceController {
    public static final String datasourceDeploy = JobDataSourceController.class.getClassLoader().getResource("").getPath() + "deploy/datasource";

    @RequestMapping
    public String index(Model model) {
        try {
            List<XxlJobDataSource> list = new ArrayList<>();
            File file = new File(datasourceDeploy.substring(1, datasourceDeploy.length()));
            System.out.println("数据源路径：" + datasourceDeploy.substring(1, datasourceDeploy.length()));
            File[] listFiles = file.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
            	Properties prop = new Properties();
                InputStream in = new BufferedInputStream(new FileInputStream(listFiles[i]));
                prop.load(in);
                in.close();
                XxlJobDataSource XxlJobDataSource = new XxlJobDataSource();
                XxlJobDataSource.setConnectionName(listFiles[i].getName());
                String url = (String) prop.get("url");
                if(url==null){
                	XxlJobDataSource.setType("");
                	XxlJobDataSource.setHostName("");
                	XxlJobDataSource.setCode("");
                	XxlJobDataSource.setDbName("");
                }else{
                String[] strings = url.split(":");
                if(strings.length==4){
                    XxlJobDataSource.setType("mysql");
                    XxlJobDataSource.setHostName(strings[2].substring(2));
                    XxlJobDataSource.setCode(strings[3].split("/")[0]);
                    XxlJobDataSource.setDbName((strings[3].split("/")[1]).split("\\?")[0]);
                }
                if(strings.length==6){
                    XxlJobDataSource.setType("oracle");
                    XxlJobDataSource.setHostName(strings[3].substring(1));
                    XxlJobDataSource.setCode(strings[4]);
                    XxlJobDataSource.setDbName(strings[5]);
                }
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
            if (XxlJobDataSource.getType().equals("mysql")) {
                String url = "jdbc:" + XxlJobDataSource.getType() + "://" + XxlJobDataSource.getHostName() + ":" + XxlJobDataSource.getCode() + "/" + XxlJobDataSource.getDbName() +
                        "?useUnicode=true&characterEncoding=utf8&autoReconnect=false&auoReconnectForPools=false&yearIsDateType=false";
                String[] strings = url.split(":");
                if (strings.length < 4) {
                    return new ReturnT<>(500, "输入信息不完整！");
                }
                if (strings.length > 4) {
                    return new ReturnT<>(500,  "输入格式有误，请不要输入“:”");
                }
                Properties prop = new Properties();
                File file = new File(datasourceDeploy.substring(1, datasourceDeploy.length()));
                FileOutputStream os = new FileOutputStream(file + "/" + XxlJobDataSource.getConnectionName());
                prop.setProperty("driverClassName", "com.mysql.jdbc.Driver");
                prop.setProperty("url", url);
                prop.setProperty("username", XxlJobDataSource.getUsername());
                prop.setProperty("password", XxlJobDataSource.getPassword());
                prop.setProperty("maxActive", "30");
                prop.setProperty("maxIdle", "10");
                prop.setProperty("maxWait", "1000");
                prop.setProperty("removeAbandoned", "true");
                prop.setProperty("removeAbandonedTimeout", "180");
                prop.store(os, null);
                os.close();
            } else {
                String url = "jdbc:" + XxlJobDataSource.getType() + ":thin:@" + XxlJobDataSource.getHostName() + ":" + XxlJobDataSource.getCode() + ":" + XxlJobDataSource.getDbName();
                String[] strings = url.split(":");
                if (strings.length < 6) {
                    return new ReturnT<>(500, "输入信息不完整！");
                }
                if (strings.length > 6) {
                    return new ReturnT<>(500,  "输入格式有误，请不要输入“:”");
                }
                Properties prop = new Properties();
                File file = new File(datasourceDeploy.substring(1, datasourceDeploy.length()));
                FileOutputStream os = new FileOutputStream(file + "/" + XxlJobDataSource.getConnectionName());
                prop.setProperty("driverClassName", "oracle.jdbc.driver.OracleDriver");
                prop.setProperty("url", url);
                prop.setProperty("username", XxlJobDataSource.getUsername());
                prop.setProperty("password", XxlJobDataSource.getPassword());
                prop.setProperty("maxActive", "30");
                prop.setProperty("maxIdle", "10");
                prop.setProperty("maxWait", "1000");
                prop.setProperty("removeAbandoned", "true");
                prop.setProperty("removeAbandonedTimeout", "180");

                prop.store(os, null);
                os.close();
            }

            return new ReturnT<>(200, "新增成功");

        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnT<>(500, "新增失败");
        }

    }

    @RequestMapping("/update")
    @ResponseBody
    public ReturnT<String> update(XxlJobDataSource XxlJobDataSource,String oldName) {

        try {
            if (XxlJobDataSource.getType().equals("mysql")) {
                String url = "jdbc:" + XxlJobDataSource.getType() + "://" + XxlJobDataSource.getHostName() + ":" + XxlJobDataSource.getCode() + "/" + XxlJobDataSource.getDbName() +
                        "?useUnicode=true&characterEncoding=utf8&autoReconnect=false&auoReconnectForPools=false&yearIsDateType=false";
                String[] strings = url.split(":");
                if (strings.length < 4) {
                   
                    return new ReturnT<>(500, "输入信息不完整！");
                }
                if (strings.length > 4) {
                    
                    return new ReturnT<>(500, "输入格式有误，请不要输入“:”");
                }
                remove(oldName);
                File file = new File(datasourceDeploy.substring(1, datasourceDeploy.length()));
                FileOutputStream os = new FileOutputStream(file + "/" + XxlJobDataSource.getConnectionName());
                Properties prop = new Properties();
                prop.setProperty("driverClassName", "com.mysql.jdbc.Driver");
                prop.setProperty("url", url);
                prop.setProperty("username", XxlJobDataSource.getUsername());
                prop.setProperty("password", XxlJobDataSource.getPassword());
                prop.setProperty("maxActive", "30");
                prop.setProperty("maxIdle", "10");
                prop.setProperty("maxWait", "1000");
                prop.setProperty("removeAbandoned", "true");
                prop.setProperty("removeAbandonedTimeout", "180");
                prop.store(os, "");
                os.close();
            } else {
                String url = "jdbc:" + XxlJobDataSource.getType() + ":thin:@" + XxlJobDataSource.getHostName() + ":" + XxlJobDataSource.getCode() + ":" + XxlJobDataSource.getDbName();
                String[] strings = url.split(":");
                if (strings.length < 6) {
                   
                    return new ReturnT<>(500, "输入信息不完整！");
                }
                if (strings.length > 6) {
                    
                    return new ReturnT<>(500, "输入格式有误，请不要输入“:”");
                }
                remove(oldName);
                File file = new File(datasourceDeploy.substring(1, datasourceDeploy.length()));
                FileOutputStream os = new FileOutputStream(file + "/" + XxlJobDataSource.getConnectionName());
                Properties prop = new Properties();
                prop.setProperty("driverClassName", "oracle.jdbc.driver.OracleDriver");
                prop.setProperty("url", url);
                prop.setProperty("username", XxlJobDataSource.getUsername());
                prop.setProperty("password", XxlJobDataSource.getPassword());
                prop.setProperty("maxActive", "30");
                prop.setProperty("maxIdle", "10");
                prop.setProperty("maxWait", "1000");
                prop.setProperty("removeAbandoned", "true");
                prop.setProperty("removeAbandonedTimeout", "180");

                prop.store(os, "");
                os.close();
            }

        	
        	return new ReturnT<>(200, "更新成功");
        } catch (Exception e) {

            e.printStackTrace();
            return new ReturnT<>(500, "更新失败");
        }

    }

    @RequestMapping("/test")
    @ResponseBody
    public ReturnT<String> test(XxlJobDataSource XxlJobDataSource) {
        String url = "";
        String username = "";
        String password = "";
       
        if (XxlJobDataSource.getType().equals("mysql")) {
            url = "jdbc:" + XxlJobDataSource.getType() + "://" + XxlJobDataSource.getHostName() + ":" + XxlJobDataSource.getCode() + "/" + XxlJobDataSource.getDbName() +
                    "?useUnicode=true&characterEncoding=utf8&autoReconnect=false&auoReconnectForPools=false&yearIsDateType=false";
            username = XxlJobDataSource.getUsername();
            password = XxlJobDataSource.getPassword();
        } else {
            url = "jdbc:" + XxlJobDataSource.getType() + ":thin:@" + XxlJobDataSource.getHostName() + ":" + XxlJobDataSource.getCode() + ":" + XxlJobDataSource.getDbName();
            System.out.println(url);
            username = XxlJobDataSource.getUsername();
            password = XxlJobDataSource.getPassword();
        }
        try {
        	 Class.forName("oracle.jdbc.driver.OracleDriver");
        	 Connection con=DriverManager.getConnection(url, username, password);
            return ReturnT.SUCCESS;

        } catch (Exception se) {
          
            se.printStackTrace();
            return ReturnT.FAIL;
        }
    }

    @RequestMapping("/remove")
    @ResponseBody
    public ReturnT<String> remove(String connectionName) {
        System.out.println(connectionName);
        try {
            File file = new File(datasourceDeploy.substring(1, datasourceDeploy.length()));
            File[] listFiles = file.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                if (listFiles[i].getName().equals(connectionName)) {
                    file = new File(file + "/" + connectionName);
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
