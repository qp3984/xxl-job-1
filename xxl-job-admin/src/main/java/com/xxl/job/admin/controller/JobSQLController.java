package com.xxl.job.admin.controller;

import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.admin.core.model.XxlJobSql;
import com.xxl.job.admin.core.thread.JobRegistryMonitorHelper;
import com.xxl.job.admin.dao.IXxlJobGroupDao;
import com.xxl.job.admin.dao.IXxlJobInfoDao;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.enums.RegistryConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

/**
 * SQL控制器
 */
@Controller
@RequestMapping("/jobsql")
public class JobSQLController {

    @Resource
    public IXxlJobInfoDao xxlJobInfoDao;
    @Resource
    public IXxlJobGroupDao xxlJobGroupDao;

    @RequestMapping
    public String index(Model model) {

        // job group (executor)
        List<XxlJobGroup> list = xxlJobGroupDao.findAll();

        if (CollectionUtils.isNotEmpty(list)) {
            for (XxlJobGroup group : list) {
                List<String> registryList = null;
                if (group.getAddressType() == 0) {
                    registryList = JobRegistryMonitorHelper.discover(RegistryConfig.RegistType.EXECUTOR.name(), group.getAppName());
                } else {
                    if (StringUtils.isNotBlank(group.getAddressList())) {
                        registryList = Arrays.asList(group.getAddressList().split(","));
                    }
                }
                group.setRegistryList(registryList);
            }
        }

        model.addAttribute("list", list);
        return "jobsql/jobsql.index";
    }

    @RequestMapping("/save")
    @ResponseBody
    public ReturnT<String> save(XxlJobGroup xxlJobGroup) {

        // valid
        if (xxlJobGroup.getAppName() == null || StringUtils.isBlank(xxlJobGroup.getAppName())) {
            return new ReturnT<String>(500, "请输入AppName");
        }
        if (xxlJobGroup.getAppName().length() > 64) {
            return new ReturnT<String>(500, "AppName长度限制为4~64");
        }
        if (xxlJobGroup.getTitle() == null || StringUtils.isBlank(xxlJobGroup.getTitle())) {
            return new ReturnT<String>(500, "请输入名称");
        }
        if (xxlJobGroup.getAddressType() != 0) {
            if (StringUtils.isBlank(xxlJobGroup.getAddressList())) {
                return new ReturnT<String>(500, "手动录入注册方式，机器地址不可为空");
            }
            String[] addresss = xxlJobGroup.getAddressList().split(",");
            for (String item : addresss) {
                if (StringUtils.isBlank(item)) {
                    return new ReturnT<String>(500, "机器地址非法");
                }
            }
        }

        int ret = xxlJobGroupDao.save(xxlJobGroup);
        return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    @RequestMapping("/update")
    @ResponseBody
    public ReturnT<String> update(XxlJobGroup xxlJobGroup) {
        // valid
        if (xxlJobGroup.getAppName() == null || StringUtils.isBlank(xxlJobGroup.getAppName())) {
            return new ReturnT<String>(500, "请输入AppName");
        }
        if (xxlJobGroup.getAppName().length() > 64) {
            return new ReturnT<String>(500, "AppName长度限制为4~64");
        }
        if (xxlJobGroup.getTitle() == null || StringUtils.isBlank(xxlJobGroup.getTitle())) {
            return new ReturnT<String>(500, "请输入名称");
        }
        if (xxlJobGroup.getAddressType() != 0) {
            if (StringUtils.isBlank(xxlJobGroup.getAddressList())) {
                return new ReturnT<String>(500, "手动录入注册方式，机器地址不可为空");
            }
            String[] addresss = xxlJobGroup.getAddressList().split(",");
            for (String item : addresss) {
                if (StringUtils.isBlank(item)) {
                    return new ReturnT<String>(500, "机器地址非法");
                }
            }
        }

        int ret = xxlJobGroupDao.update(xxlJobGroup);
        return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
    }
    @RequestMapping("/testsql")
    @ResponseBody
    public ReturnT<String> testSql(XxlJobSql jobSql) {
    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@172.16.136.253:1521:otestdb";
			String user = "query";// 用户名,系统默认的账户名
			String password = "query_on";// 你安装时选设置的密码
			Connection con = DriverManager.getConnection(url, user, password);// 获取连接
			PreparedStatement pre = con.prepareStatement(jobSql.getSql());// 实例化预编译语句
			String[] sqls = jobSql.getSql().split(" ");
			if(sqls[0].equals("select")){
				ResultSet rs = pre.executeQuery();// 执行查询
				if(rs.next()){
					return ReturnT.SUCCESS;
				}else{
					return ReturnT.FAIL;
				}
			}else{
				int rs = pre.executeUpdate();
				if(rs!=-1){
					return ReturnT.SUCCESS;
				}else{
					return ReturnT.FAIL;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ReturnT.FAIL;
		}
    }

    @RequestMapping("/updateSub")
    @ResponseBody
    public ReturnT<String> updateSub(XxlJobGroup xxlJobGroup) {
        // valid
        if (xxlJobGroup.getAppName() == null || StringUtils.isBlank(xxlJobGroup.getAppName())) {
            return new ReturnT<String>(500, "请输入AppName");
        }
        if (xxlJobGroup.getAppName().length() > 64) {
            return new ReturnT<String>(500, "AppName长度限制为4~64");
        }
        if (xxlJobGroup.getTitle() == null || StringUtils.isBlank(xxlJobGroup.getTitle())) {
            return new ReturnT<String>(500, "请输入名称");
        }
        if (xxlJobGroup.getAddressType() != 0) {
            if (StringUtils.isBlank(xxlJobGroup.getAddressList())) {
                return new ReturnT<String>(500, "手动录入注册方式，机器地址不可为空");
            }
            String[] addresss = xxlJobGroup.getAddressList().split(",");
            for (String item : addresss) {
                if (StringUtils.isBlank(item)) {
                    return new ReturnT<String>(500, "机器地址非法");
                }
            }
        }

        int ret = xxlJobGroupDao.update(xxlJobGroup);
        return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    @RequestMapping("/remove")
    @ResponseBody
    public ReturnT<String> remove(int id) {

        // valid
        int count = xxlJobInfoDao.pageListCount(0, 10, id, null);
        if (count > 0) {
            return new ReturnT<String>(500, "该分组使用中, 不可删除");
        }

        List<XxlJobGroup> allList = xxlJobGroupDao.findAll();
        if (allList.size() == 1) {
            return new ReturnT<String>(500, "删除失败, 系统需要至少预留一个默认分组");
        }

        int ret = xxlJobGroupDao.remove(id);
        return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

}
