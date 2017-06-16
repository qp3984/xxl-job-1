package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.executor.util.KylinUtil;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 对接kylin REST API的调度器
 */
@JobHander(value = "kylinJobHandler")
@Service
public class KylinJobHandler extends IJobHandler {

    @Override
    public ReturnT<String> execute(String... params) throws Exception {
        XxlJobLogger.log("XXL-JOB, Hello Kylin.传递过来的参数为：" + params[0]);

        try {
            if (params[0].split("&").length != 3) {
                String outStr = "传递参数个数有误，需要3个参数，分别为cubeName、timecycle(1、2、3、4)、buildType（‘BUILD’, ‘MERGE’, ‘REFRESH’）";
                XxlJobLogger.log(outStr);
                throw new Exception(outStr);
            }
            for (int i = 0; i < params.length; i++) {

                String cubeName = params[0].split("&")[0].split("=")[1];
                String timecycle = params[0].split("&")[1].split("=")[1];
                String buildType = params[0].split("&")[2].split("=")[1];


                if (cubeName == null || timecycle == null || buildType == null) {
                    String outStr = "必要的请求参数不能为空,cubeName[" + cubeName + "],timecycle[" + timecycle + "],buildType[" + buildType + "]";
                    XxlJobLogger.log(outStr);
                    throw new Exception(outStr);
                }
                if (!("BUILD".equals(buildType) || "MERGE".equals(buildType) || "REFRESH".equals(buildType))) {
                    String outStr = "数据构建类型错误！buildType[" + buildType + "]";
                    XxlJobLogger.log(outStr);
                    throw new Exception(outStr);
                }

                long startTimeLong = 0;
                long endTimeLong = 0;

                //		前一天的数据：1，前二天的数据：2，前三天的数据：3.过去7天的数据
                if ("1".equals(timecycle.trim())) {
                    Date startTime = KylinUtil.beforeDateTime(1, 0, 0, 0);
                    Date endTime = KylinUtil.beforeDateTime(1, 23, 59, 59);
                    startTimeLong = startTime.getTime();
                    endTimeLong = endTime.getTime();
                }
                if ("2".equals(timecycle.trim())) {
                    Date startTime = KylinUtil.beforeDateTime(2, 0, 0, 0);
                    Date endTime = KylinUtil.beforeDateTime(2, 23, 59, 59);
                    startTimeLong = startTime.getTime();
                    endTimeLong = endTime.getTime();
                }
                if ("3".equals(timecycle.trim())) {
                    Date startTime = KylinUtil.beforeDateTime(3, 0, 0, 0);
                    Date endTime = KylinUtil.beforeDateTime(3, 23, 59, 59);
                    startTimeLong = startTime.getTime();
                    endTimeLong = endTime.getTime();
                }
                if ("4".equals(timecycle.trim())) {
                    Date startTime = KylinUtil.beforeDateTime(7, 0, 0, 0);
                    Date endTime = KylinUtil.beforeDateTime(7, 23, 59, 59);
                    startTimeLong = startTime.getTime();
                    endTimeLong = endTime.getTime();
                }

                //记录日志
                String bodyout = "{\"startTime\":\"" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTimeLong) + "\",\"endTime\":\"" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTimeLong) + "\",\"buildType\":\"" + buildType + "\"}";
                String outStr = "Cube[" + cubeName + "],参数为：" + bodyout;
                XxlJobLogger.log(outStr);

                //身份验证
                KylinRESTfulMethod.login("ADMIN", "KYLIN");

                //执行构建cube
                String body = "{\"startTime\":\"" + String.valueOf(startTimeLong) + "\",\"endTime\":\"" + String.valueOf(endTimeLong) + "\",\"buildType\":\"" + buildType + "\"}";
                KylinRESTfulMethod.buildCube(cubeName, body);
            }
        } catch (Exception e) {
            //针对子任务的情况，向上抛出
            if (e instanceof InterruptedException) {
                XxlJobLogger.log("手动终止执行，堆栈信息.");
                throw e;
            }
            XxlJobLogger.log(KylinUtil.CR + KylinUtil.getStackTracker(e));
        }
        return ReturnT.SUCCESS;
    }

}
