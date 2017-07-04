$(function() {
	
/**	$('.column').hide();
	
	$('#searchBtn').on('click', function(){
	
		var tableName= document.getElementById("executorHandler").value;
		
			$.ajax({
				type : 'POST',
				url : base_url + '/jobmetadata/selectList',
				data : {"tableName":tableName},
				dataType : "json",
				success : function(data){
					
					//$('.column').hide();
					var tbody= $('#joblog_list').find('tbody');
						tbody.html('');	
					for ( var i = 0; i < data.length; i++) {
						var tr = $("<tr> "+
										"<td>"+data[i].ip+"</td>"+
                                       " <td>"+data[i].dcmOwner+"</td>"+
                                       " <td>"+data[i].tableName+"</td>"+
                                       "  <td>"+data[i].columnName+"</td>"+
                                       "   <td>"+data[i].columnClass+"</td>"+
                                      "  <td>"+data[i].columnSize+"</td>"+
                                      "   <td>"+data[i].columnExplain+"</td>"+
                                      "  <td>"+
                                      "      <button class='btn btn-warning btn-xs select' "+
                                          "          ip="+data[i].ip+" "+
                                       "             dcmOwner="+data[i].dcmOwner+""+
                                       "             tableName="+data[i].tableName+""+
                                       "             columnName="+data[i].columnName+" >查询历史变更</button>"+
										"</td>"+
									"</tr>");
						tbody.append(tr);
						}
					
					$(".select").on("click", function(){
						var ip= $(this).attr('ip');
						var dcmOwner= $(this).attr('dcmOwner');
						var tableName= $(this).attr('tableName');
						var columnName= $(this).attr('columnName');

						column(ip,dcmOwner,tableName,columnName);
					});
				},
			});
		

	});

		$(".select").on("click", function(){
			var ip= $(this).attr('ip');
			var dcmOwner= $(this).attr('dcmOwner');
			var tableName= $(this).attr('tableName');
			var columnName= $(this).attr('columnName');
			column(ip,dcmOwner,tableName,columnName);
		});
	
	function column(ip,dcmOwner,tableName,columnName){
		
		$.ajax({
			type : 'POST',
			url : base_url + '/jobmetadata/select',
			data : {"ip":ip,"dcmOwner":dcmOwner,"tableName":tableName,"columnName":columnName},
			dataType : "json",
			success : function(data){
				var column= $('#joblog_listcolumn').find('tbody');
				column.html('');	
			for ( var i = 0; i < data.length; i++) {
				var tr = $("<tr> "+
								"<td>"+data[i].ip+"</td>"+
                               " <td>"+data[i].dcmOwner+"</td>"+
                               " <td>"+data[i].tableName+"</td>"+
                               "  <td>"+data[i].columnName+"</td>"+
                               "   <td>"+data[i].columnClass+"</td>"+
                              "  <td>"+data[i].columnSize+"</td>"+
                              "   <td>"+data[i].columnExplain+"</td>"+
                              "  <td>"+data[i].dateTime +"</td>"+
							"</tr>");
				column.append(tr);
				}
			$('.column').show();
			$('.column').style.display="";
			
			},
		});	
		
		$("#close").on("click", function(){
			$('.column').hide();
		});
	}  */
//================================================================================================

	var jobTable = $("#job_list").dataTable({
		"deferRender": true,
		"processing" : true, 
	    "serverSide": true,
		"ajax": {
	        url: base_url + "/jobmetadata/pageList" ,
	        data : function ( d ) {
	        	var obj = {};
	        	obj.tableName = $('#executorHandler').val();
	        	obj.start = d.start;
	        	obj.length = d.length;
                return obj;
            }
	    },
	    "searching": false,
	    "ordering": false,
	    //"scrollX": false,
	    "columns": [
					 {"data" : "ip",  
	                        "sDefaultContent" : "",  
	                        "sClass" : "center"  
					 },
					 {"data" : "dcmOwner",  
	                        "sDefaultContent" : "",  
	                        "sClass" : "center"  
					 },
					 {"data" : "tableName",  
	                        "sDefaultContent" : "",  
	                        "sClass" : "center"  
					 },
					 {"data" : "columnName",  
	                        "sDefaultContent" : "",  
	                        "sClass" : "center"  
					 },
					 {"data" : "columnClass",  
	                        "sDefaultContent" : "",  
	                        "sClass" : "center"  
					 },
					 {"data" : "columnSize",  
	                        "sDefaultContent" : "",  
	                        "sClass" : "center"  
					 },
					 {"data" : "columnExplain",  
						 "render": function ( data, type, row ) {
							 return (data!=null)?'<span style="color: green">'+data+'</span>':'<span style="color: red">无</span>';
							}
					 },
					 
					 {"data" : "dateTime",  
	                        "sDefaultContent" : "",  
	                        "sClass" : "center"  
					 },
					 {"data": 'falg',
							"render": function ( data, type, row ) {
								return (data==1)?'<a class="columnID" href="javascript:;" ip='+row.ip+' dcmOwner='+row.dcmOwner+' tableName='+row.tableName+' columnName='+row.columnName+' >查看</a>':"无";
							} 
					 }
	            ],
		"language" : {
			"sProcessing" : "处理中...",
			"sLengthMenu" : "每页 _MENU_ 条记录",
			"sZeroRecords" : "没有匹配结果",
			"sInfo" : "第 _PAGE_ 页 ( 总共 _PAGES_ 页，_TOTAL_ 条记录 )",
			"sInfoEmpty" : "无记录",
			"sInfoFiltered" : "(由 _MAX_ 项结果过滤)",
			"sInfoPostFix" : "",
			"sSearch" : "搜索:",
			"sUrl" : "",
			"sEmptyTable" : "表中数据为空",
			"sLoadingRecords" : "载入中...",
			"sInfoThousands" : ",",
			"oPaginate" : {
				"sFirst" : "首页",
				"sPrevious" : "上页",
				"sNext" : "下页",
				"sLast" : "末页"
			},
			"oAria" : {
				"sSortAscending" : ": 以升序排列此列",
				"sSortDescending" : ": 以降序排列此列"
			}
		}
	});
	
	// 搜索按钮
		$('#searchBtn').on('click', function(){
			jobTable.fnDraw();
		});
	
		// 查看执行器详细执行日志
		$('#job_list').on('click', '.columnID', function(){
			var ip= $(this).attr('ip');
			var dcmOwner= $(this).attr('dcmOwner');
			var tableName= $(this).attr('tableName');
			var columnName= $(this).attr('columnName');
			$.ajax({
				type : 'POST',
				url : base_url + '/jobmetadata/select',
				data : {"ip":ip,"dcmOwner":dcmOwner,"tableName":tableName,"columnName":columnName},
				dataType : "json",
				success : function(data){
					var column= $('#joblog_listcolumn').find('tbody');
					column.html('');	
				for ( var i = 0; i < data.length; i++) {
					var tr = $("<tr> "+
									"<td>"+data[i].ip+"</td>"+
	                               " <td>"+data[i].dcmOwner+"</td>"+
	                               " <td>"+data[i].tableName+"</td>"+
	                               "  <td>"+data[i].columnName+"</td>"+
	                               "   <td>"+data[i].columnClass+"</td>"+
	                              "  <td>"+data[i].columnSize+"</td>"+
	                              "   <td>"+data[i].columnExplain+"</td>"+
	                              "  <td>"+data[i].dateTime +"</td>"+
								"</tr>");
					column.append(tr);
					}
				$('.column').show();
				$('.column').style.display="";
				
				},
			});	
			
			$("#close").on("click", function(){
				$('.column').hide();
			});
		});
});
