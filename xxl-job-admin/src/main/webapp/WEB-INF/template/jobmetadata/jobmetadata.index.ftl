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
			<h1>元数据管理<small>任务调度中心</small></h1>
		</section>

		<!-- Main content -->
	    <section class="content">
			
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
			            <div class="box-header">
							<h3 class="box-title">数据字典列表</h3>&nbsp;&nbsp;
						</div>
						<div class="col-xs-4">
                    <div class="input-group">
                        <span class="input-group-addon">TableName</span>
                        <input type="text" class="form-control" id="executorHandler" autocomplete="on" >
                    </div>
                </div>
	            <div class="col-xs-2">
	            	<button class="btn btn-block btn-info" id="searchBtn">搜索</button>
	            </div>
			            <div class="box-body">
			              	<table id="job_list" class="table table-bordered table-striped display" width="100%" >
				                <thead>
					            	<tr>
                                        <th name="ip" >IP地址</th>
                                        <th name="dcmOwner" >库名</th>
                                        <th name="tableName" >表名</th>
                                        <th name="columnName" >字段名称</th>
                                        <th name="columnClass" >字段类型</th>
                                        <th name="columnSize" >字段长度</th>
                                        <th name="columnExplain" >注释</th>
                                        <th name="dataTime" >最近更新时间</th>
                                        <th name="flag" >查看更新</th>
                                      
					                </tr>
				                </thead>
                                <tbody>
								
								</tbody>
							</table>
						</div>
						
						
						 <div class="box-body column"  style="display:none; POSITION:absolute; left:40%; top:54%; width:800px; height:400px; margin-left:-300px; margin-top:-200px; border:1px solid #888; background-color:#edf; text-align:center">
								 <table id="joblog_listcolumn" class="table table-bordered table-striped display" width="100%" >
				                <thead>
					            	<tr>
                                        <th name="ip" >IP地址</th>
                                        <th name="dcmOwner" >库名</th>
                                        <th name="tableName" >表名</th>
                                        <th name="columnName" >字段名称</th>
                                        <th name="columnClass" >字段类型</th>
                                        <th name="columnSize" >字段长度</th>
                                        <th name="columnExplain" >注释</th>
                                        <th name="dataTime" >更新时间</th>
					                </tr>
				                </thead>
                                <tbody>
							
								</tbody>
								<div class="col-xs-2" style="left:85%">
	            					<button  id="close">关闭</button>
	           					 </div>
							</table>
						 </div>
					</div>
				</div>
			</div>
	    </section>
	</div>

    
	
	<!-- footer -->
	<@netCommon.commonFooter />
</div>

<@netCommon.commonScript />
<!-- DataTables -->
<script src="${request.contextPath}/static/adminlte/plugins/datatables/jquery.dataTables.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables/dataTables.bootstrap.min.js"></script>
<#-- jquery.validate -->
<script src="${request.contextPath}/static/plugins/jquery/jquery.validate.min.js"></script>
<script src="${request.contextPath}/static/js/jobmetadata.index.1.js"></script>

</body>
</html>
