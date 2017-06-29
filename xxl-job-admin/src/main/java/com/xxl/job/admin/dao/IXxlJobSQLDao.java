package com.xxl.job.admin.dao;

import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.model.XxlJobSQL;

import java.util.List;



public interface IXxlJobSQLDao {

    public List<XxlJobSQL> findAll();

    public List<XxlJobInfo> pageList(int offset, int pagesize, int jobGroup, String executorHandler);

    public int pageListCount(int offset, int pagesize, int jobGroup, String executorHandler);

    public int save(String sqlList);

    public XxlJobInfo loadById(int id);

    public int update(XxlJobSQL xxlJobSQL);

    public int remove(int id);

    public List<XxlJobInfo> getJobsByGroup(String jobGroup);

    public int findAllCount();


    /**
     * 任务id
     * @param id
     * @return
     */
    public String querySubTasks(int id);


}
