package com.xxl.job.admin.controller;

import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.admin.core.model.XxlJobSQL;
import com.xxl.job.admin.dao.IXxlJobGroupDao;
import com.xxl.job.admin.dao.IXxlJobInfoDao;
import com.xxl.job.admin.dao.IXxlJobSQLDao;
import com.xxl.job.core.biz.model.ReturnT;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
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

    @Resource
    public IXxlJobSQLDao xxlJobSQLDao;


    @RequestMapping
    public String index(Model model) {

        List<XxlJobSQL> list = xxlJobSQLDao.findAll();

//        if (CollectionUtils.isNotEmpty(list)) {
//            for (XxlJobSQL sqllist : list) {
//
//            }
//        }

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
