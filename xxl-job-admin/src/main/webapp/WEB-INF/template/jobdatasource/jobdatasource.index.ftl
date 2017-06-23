<!DOCTYPE html>
<html>
<head>
  	<title>任务调度中心</title>
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
	<@netCommon.commonLeft "jobgroup" />
	
	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>数据源管理<small>数据源中心</small></h1>
		</section>

		<!-- Main content -->
	    <section class="content">
			
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
			            <div class="box-header">
							<h3 class="box-title">数据源列表</h3>&nbsp;&nbsp;
                            <button class="btn btn-info btn-xs pull-left2 add" >+新增数据源</button>
                            
						</div>
			            <div class="box-body">
			              	<table id="joblog_list" class="table table-bordered table-striped display" width="100%" >
				                <thead>
					            	<tr>
                                        <th>数据源名称</th>
                                        <th>连接类型</th>
                                        <th>操作</th>
					                </tr>
				                </thead>
                                <tbody>
								<#if list?exists && list?size gt 0>
								<#list list as sql>
									<tr>
                                        <td>${sql.connectionName}</td>
                                        <td>${sql.type}</td>
										<td>
                                            <button class="btn btn-warning btn-xs update"
                                                    
                                                    connectionName="${sql.connectionName}"
                                                    type="${sql.type}"
                                                    hostName="${sql.hostName}"
                                                    dbName="${sql.dbName}"
                                                    code="${sql.code}" 
                                                    username="${sql.username}" 
                                                    password="${sql.password}" >编辑</button>
                                            <button class="btn btn-danger btn-xs remove" connectionName="${sql.connectionName}" >删除</button>
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
    <div class="modal fade" id="baseModal" tabindex="-1" role="dialog"  aria-hidden="true">
    <input type="hidden" name="flag" value="" />
        <div class="modal-dialog ">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" >新增-更新数据源</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal form" role="form" >
                        <div class="form-group">
                            <label for="lastname" class="col-sm-3 control-label">连接名称<font color="red">*</font></label>
                            <div class="col-sm-9"><input type="text" class="form-control" name="connectionName" placeholder="请输入“连接名称”" maxlength="64" ></div>
                        </div>
                        <div class="form-group">
                            <label for="lastname" class="col-sm-3 control-label">主机名或IP<font color="red">*</font></label>
                            <div class="col-sm-9"><input type="text" class="form-control" name="hostName" placeholder="请输入“主机名或IP”" maxlength="50" ></div>
                        </div>
                        <div class="form-group">
                            <label for="lastname" class="col-sm-3 control-label">数据源类型<font color="red">*</font></label>
                            <div class="col-sm-9">
                                <input type="radio" name="type" value="mysql" checked />mysql
                                &nbsp;&nbsp;&nbsp;&nbsp;
                                <input type="radio" name="type" value="oracle" />oracle
                            </div>
                        </div>
                      <div class="form-group">
                            <label for="lastname" class="col-sm-3 control-label">数据库名称<font color="red">*</font></label>
                            <div class="col-sm-9"><input type="text" class="form-control" name="dbName" placeholder="数据库名称" maxlength="12" ></div>
                        </div>
                      <div class="form-group">
                            <label for="lastname" class="col-sm-3 control-label">端口号<font color="red">*</font></label>
                            <div class="col-sm-9"><input type="text" class="form-control" name="code" placeholder="端口号" maxlength="12" ></div>
                        </div>
                      <div class="form-group">
                            <label for="lastname" class="col-sm-3 control-label">用户名<font color="red">*</font></label>
                            <div class="col-sm-9"><input type="text" class="form-control" name="username" placeholder="用户名" maxlength="12" ></div>
                        </div>
                      <div class="form-group">
                            <label for="lastname" class="col-sm-3 control-label">密码<font color="red">*</font></label>
                            <div class="col-sm-9"><input type="text" class="form-control" name="password" placeholder="密码" maxlength="12" ></div>
                        </div>
                        <hr>
                         <a class="btn btn-info btn-xs col-sm-2 test" >测试链接</a> <div class="col-sm-9" id="msg" style="display: none;"><font  color="red">连接失败!</font></div><div class="col-sm-9" id="errMsg" style="display: none;"><font  color="green">连接成功!</font></div>
                        <div class="form-group">
                            <div class="col-sm-offset-5 col-sm-6">
                                <button type="submit" class="btn btn-primary"  >保存</button>
                                <button type="button" id="misbut" class="btn btn-default" data-dismiss="modal">取消</button>
                                
                            </div>
                        </div>
                    </form>
                </div>
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
<script src="${request.contextPath}/static/js/jobdatasource.index.1.js"></script>
</body>
</html>
