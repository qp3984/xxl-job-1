<!DOCTYPE html>
<html>
<head>
    <title>任务调度中心</title>
    <style type="text/css" rel="stylesheet">
        .table th, .table td {
            text-align: center;
            vertical-align: middle !important;
        }
    </style>
<#import "/common/common.macro.ftl" as netCommon>
<@netCommon.commonStyle />
    <!-- DataTables -->
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/datatables/dataTables.bootstrap.css">
</head>
<body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && "off" == cookieMap["adminlte_settings"].value >sidebar-collapse</#if> ">

<div class="wrapper">
    <!-- header -->
<@netCommon.commonHeader />
    <!-- left -->
<@netCommon.commonLeft "jobsql" />

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>数据源管理
                <small>任务调度中心</small>
            </h1>
        </section>

        <!-- Main content -->
        <section class="content">

            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-header">
                            <h3 class="box-title">SQL任务列表</h3>&nbsp;&nbsp;
                            <button class="btn btn-info btn-xs pull-left2 add">+新增</button>
                        <#--<button class="btn btn-info btn-xs pull-left2 save">+保存</button>-->
                        </div>
                        <div class="box-body">
                            <table id="joblog_list" class="table table-bordered table-striped display" width="100%">
                                <thead>
                                <tr>
                                <#--<th name="id" >ID</th>-->
                                    <th name="order">序号</th>
                                    <th name="appName">任务名称</th>
                                    <th name="appName">数据源名称</th>
                                    <th name="operate">操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <#if list?exists && list?size gt 0>
                                    <#list list as group>
                                        <#assign sqllist="${group.sqlList}"?eval />

                                    <tr>
                                        <td>${group.id}</td>
                                        <td>${sqllist.task_name}</td>
                                        <td>${sqllist.datasource_name}</td>
                                    <#--<td>${sqllist.subtasks[0].subtask_name} </td>-->
                                    <#--<td> <#assign sqllist222="sqllist.subtasks"?eval />-->
                                    <#--${sqllist222[0].subtask_name}-->
                                    <#--<td>-->
                                    <#--<#assign subtaskslists="sqllist.subtasks"?eval />-->
                                    <#--<#list sqllist.subtasks as item>-->
                                    <#--id:${item.subtask_name}, name:${item.sql}-->
                                    <#--</#list>-->
                                    <#--</td>-->
                                        <td>
                                            <button class="btn btn-warning btn-xs update" data-toggle="modal"
                                                    id="${group.id}"
                                                    appName="${group.appName}"
                                                    title="${group.title}"
                                                    order="${group.order}"
                                                    addressType="${group.addressType}"
                                                    addressList="${group.addressList}">编辑任务
                                            </button>
                                            <button class="btn btn-danger btn-xs remove" id="${group.id}">删除</button>
                                            <button class="btn btn-warning btn-xs updateSub" data-toggle="modal"
                                                    id="${group.id}" subtasks="${sqllist.subtasks[0].subtask_name}">
                                                编辑子任务
                                            </button>

                                        </td>
                                    </tr>
                                    </#list>
                                </#if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>

    <!-- 新增.模态框 -->
    <div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog ">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title">新增执行器</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal form" role="form">
                        <div class="form-group">
                            <label for="lastname" class="col-sm-2 control-label">任务名称：<font color="red">*</font></label>
                            <div class="col-sm-10"><input type="text" class="form-control" name="order"
                                                          placeholder="请输入“排序”" maxlength="50"></div>
                        </div>


                        <div class="form-group">
                            <label for="lastname" class="col-sm-2 control-label">选择数据源：<font
                                    color="red">*</font></label>
                            <div class="col-sm-10">
                                <select class="form-control glueType" name="glueType">
                                <#--<#list GlueTypeEnum as item>-->
                                <#--<option value="${item}" >${item.desc}</option>-->
                                    <option value="mysql_101">测试101Mysql数据源</option>
                                    <option value="oracle23">生产报表oracle数据源</option>
                                <#--</#list>-->
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="lastname" class="col-sm-2 control-label">发送邮件列表：<font
                                    color="red">*</font></label>
                            <div class="col-sm-10"><input type="text" class="form-control" name="appName"
                                                          placeholder="请输入“AppName”" maxlength="64"></div>
                        </div>
                        <div class="form-group">
                            <label for="lastname" class="col-sm-2 control-label">抄送邮件列表：<font
                                    color="red">*</font></label>
                            <div class="col-sm-10"><input type="text" class="form-control" name="title"
                                                          placeholder="请输入“名称”" maxlength="12"></div>
                        </div>


                        <div class="form-group">
                            <div class="col-sm-offset-3 col-sm-6">
                                <button type="button" class="btn btn-default" data-dismiss="modal">保存</button>
                                <button type="button" id="misbut" class="btn btn-default" data-dismiss="modal">取消
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- 更新.模态框 -->
    <div class="modal fade" id="updateModal" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog ">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title">任务</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal form" role="form">
                        <div class="form-group">
                            <label for="lastname" class="col-sm-2 control-label">任务名称：<font color="red">*</font></label>
                            <div class="col-sm-10"><input type="text" class="form-control" name="order"
                                                          placeholder="请输入任务名称" maxlength="50"></div>
                        </div>
                        <div class="form-group">
                            <label for="lastname" class="col-sm-2 control-label">选择数据源：<font
                                    color="red">*</font></label>
                            <div class="col-sm-10">
                                <select class="form-control glueType" name="glueType">
                                <#--<#list GlueTypeEnum as item>-->
                                <#--<option value="${item}" >${item.desc}</option>-->
                                    <option value="mysql_101">测试101Mysql数据源</option>
                                    <option value="oracle23">生产报表oracle数据源</option>
                                <#--</#list>-->
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="lastname" class="col-sm-2 control-label">发送邮件列表：<font
                                    color="red">*</font></label>
                            <div class="col-sm-10"><input type="text" class="form-control" name="appName"
                                                          placeholder="请输入发件人列表”" maxlength="64"></div>
                        </div>
                        <div class="form-group">
                            <label for="lastname" class="col-sm-2 control-label">抄送邮件列表：<font
                                    color="red">*</font></label>
                            <div class="col-sm-10"><input type="text" class="form-control" name="title"
                                                          placeholder="请输入“名称”" maxlength="12"></div>
                        </div>

                        <hr>
                        <div class="form-group">
                            <div class="col-sm-offset-3 col-sm-6">
                                <button type="button" class="btn btn-default" data-dismiss="modal">保存</button>
                                <button type="button" id="misbut" class="btn btn-default" data-dismiss="modal">取消
                                </button>
                                <input type="hidden" name="id">
                            </div>
                        </div>
                    </form>
                </div>
            </div>

        </div>

    </div>

    <!-- 子更新.模态框 -->
    <div class="modal fade" id="updateSubModal" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">子任务</h4>
                </div>

                <div class="modal-body">
                    <form class="form-horizontal form" role="form">
                        <div class="form-group">
                            <label for="lastname" class="col-sm-2 control-label">子任务名称：<font
                                    color="red">*</font></label>
                            <div class="col-sm-8"><input type="text" class="form-control" name="order"
                                                         placeholder="请输入子任务名称"></div>
                        </div>

                        <div class="form-group">
                            <label for="lastname" class="col-sm-2 control-label">SQL脚本：<font
                                    color="red">*</font></label>
                            <div class="col-sm-8"><textarea class="form-control" rows="3" name="sql"
                                                            placeholder="请输入SQL脚本"></textarea></div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-3 col-sm-6">

                            <#--<button class="btn btn-info btn-xs pull-left2 add">+新增</button>-->
                            <#--<button class="btn btn-info btn-xs pull-left2 save">+保存</button>-->
                            <#--<button type="submit" class="btn btn-info btn-xs pull-left2 testsql">测试SQL</button>-->

                                <button class="btn btn-default add">+新增</button>
                                <button class="btn btn-default save">+保存</button>
                                <a type="submit" class="btn btn-default testsql">测试SQL</a>
                                <div class="col-sm-9" id="errMsg" style="display: none;"><font color="red">测试失败!</font>
                                </div>
                                <div class="col-sm-9" id="rigMsg" style="display: none;"><font
                                        color="green">测试成功!</font></div>
                                <input type="hidden" name="id">
                            </div>
                        </div>

                        <div class="form-group">
                            <table id="joblog_list" class="table table-bordered table-striped display" width="100%">
                                <thead>
                                <tr>
                                    <th name="id">序号</th>
                                    <th name="subtasks">子任务名称</th>
                                    <th name="operate">操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <#--<#assign sqllist="${group.sqlList}"?eval />-->
                                <#list sqllist.subtasks as item>
                                <#--<#assign var = 1 >-->
                                <tr>
                                    <td>${item_index+1}</td>
                                    <td>${item.subtask_name}</td>
                                <#--<td>${item.sql}</td>-->
                                    <td>
                                        <button class="btn btn-warning btn-xs updateSub"
                                                id="${item_index+1}"
                                                subtask_name="${item.subtask_name}"
                                                sql="${item.sql}">编辑
                                        </button>
                                        <button class="btn btn-danger btn-xs remove" id="${item.subtask_name}">删除
                                        </button>
                                        <button class="btn btn-warning btn-xs up" id="${item.subtask_name}">上移</button>
                                        <button class="btn btn-warning btn-xs down" id="${item.subtask_name}">下移
                                        </button>
                                    </td>
                                </tr>
                                </#list>
                                </tbody>
                            </table>
                        </div>
                </div>
                <div class="modal-footer">
                    <button type="button" id="rebut" class="btn btn-default" data-dismiss="modal">关闭
                    </button>

                </div>
            <#--<hr>-->

            </div>
            </form>
        </div>

    </div>

</div>
<!-- footer -->
<@netCommon.commonFooter />
</div>

<@netCommon.commonScript />
<!-- DataTables -->
<script src="${request.contextPath}/static/adminlte/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables/dataTables.bootstrap.min.js"></script>
<#-- jquery.validate -->
<script src="${request.contextPath}/static/plugins/jquery/jquery.validate.min.js"></script>
<#--<script src="${request.contextPath}/static/plugins/jquery/jquery-3.2.1.js"></script>-->
<#--<script src="${request.contextPath}/static/plugins/jquery/commons.js"></script>-->
<script src="${request.contextPath}/static/js/jobsql.index.1.js"></script>
</body>
</html>
