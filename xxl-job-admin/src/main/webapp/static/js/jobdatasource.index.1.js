$(function() {

	// remove
	$('.remove').on('click', function(){
		var connectionName = $(this).attr('connectionName');

		layer.confirm('确认删除分组?', {icon: 3, title:'系统提示'}, function(index){
			layer.close(index);

			$.ajax({
				type : 'POST',
				url : base_url + '/jobdatasource/remove',
				data : {"connectionName":connectionName},
				dataType : "json",
				success : function(data){
					if (data.code == 200) {
						layer.open({
							title: '系统提示',
							content: '删除成功',
							icon: '1',
							end: function(layero, index){
								window.location.reload();
							}
						});
					} else {
						layer.open({
							title: '系统提示',
							content: (data.msg || "删除失败"),
							icon: '2'
						});
					}
				},
			});
		});
	});
	
	jQuery.validator.addMethod("myValid01", function(value, element) {
		var length = value.length;
		var valid = /^[\w\.]+$/;
		return this.optional(element) || valid.test(value);
	}, "由字母、数字和下划线组成");
	
	$('.add').on('click', function(){
		$('#baseModal').modal({backdrop: false, keyboard: false}).modal('show');
		$("#baseModal .form input[name='flag']").val("true");
		$("#baseModal .form input[name='type'][value='mysql']").click();
	});
	
	$('.test').on('click',function(){
		$.post(base_url + "/jobdatasource/test",  $("#baseModal .form").serialize(), function(data, status) {
			if (data.code == "200") {
				$("#errMsg").hide();
				$("#rigMsg").show();
			} else {
				$("#rigMsg").hide();
				$("#errMsg").show();
			}
		});
	});
	var modalValidate = $("#baseModal .form").validate({
		errorElement : 'span',
		errorClass : 'help-block',
		focusInvalid : true,
		rules : {
			username : {
				required : true,
				rangelength:[4,64]
			},
			password : {
				required : true,
				rangelength:[4, 12],
			},
			connectionName : {
				required : true,
				rangelength:[4,64],
				myValid01 : true
			},
			hostName : {
				required : true,
				rangelength:[4, 64]
			},
			code: {
				required : true,
				rangelength:[4, 12]
			},
			dbName: {
				required : true,
				rangelength:[4, 64]
			}
		},
		messages : {
			username : {
				required :"请输入“用户名”",
				rangelength:"用户名长度限制为4~64"
			},
			password : {
				required :"请输入“密码”",
				rangelength:"长度限制为4~12"
			},
			connectionName : {
				required :"请输入“连接名称”",
				rangelength:"用户名长度限制为4~64",
				myValid01: "由字母、数字和下划线组成"
			},
			hostName : {
				required :"请输入“主机名或IP”",
				rangelength:"长度限制为4~64"
			},
			code : {
				required :"请输入“端口号”",
				rangelength:"用户名长度限制为4~64"
			},
			dbName : {
				required :"请输入“数据库名”",
				rangelength:"长度限制为4~64"
			}
		},
		highlight : function(element) {
			$(element).closest('.form-group').addClass('has-error');
		},
		success : function(label) {
			label.closest('.form-group').removeClass('has-error');
			label.remove();
		},
		errorPlacement : function(error, element) {
			element.parent('div').append(error);
		},
		submitHandler : function(form) {
			var flag=$("#baseModal .form input[name='flag']").attr("flag");
			var url=0;
			if(flag){
				url=base_url+"/jobdatasource/add";
			}else{
				
				url=base_url+"/jobdatasource/update"
			}
			$.post(url,  $("#baseModal .form").serialize(), function(data, status) {
				if (data.code == "200") {
					$('#baseModal').modal('hide');
					layer.open({
						title: '系统提示',
						content: (data.msg),
						icon: '1',
						end: function(layero, index){
							window.location.reload();
						}
					});
				} else {
					layer.open({
						title: '系统提示',
						content: (data.msg),
						icon: '2'
					});
				}
			});
		}
	});
	$("#baseModal").on('hide.bs.modal', function () {
		$("#baseModal .form")[0].reset();
		modalValidate.resetForm();
		$("#baseModal .form .form-group").removeClass("has-error");
	});

    $("#misbut").on('click',function(){
   	$("#errMsg").hide();
   	$("#rigMsg").hide();
    });

	// update
	$('.update').on('click', function(){
		$('#baseModal').modal({backdrop: false, keyboard: false}).modal('show');
		$("#baseModal .form input[name='flag']").val("false");
		$("#baseModal .form input[name='oldName']").val($(this).attr("connectionName"));
		$("#baseModal .form input[name='connectionName']").val($(this).attr("connectionName"));
		$("#baseModal .form input[name='hostName']").val($(this).attr("hostName"));
		$("#baseModal .form input[name='dbName']").val($(this).attr("dbName"));
		$("#baseModal .form input[name='code']").val($(this).attr("code"));
		$("#baseModal .form input[name='username']").val($(this).attr("username"));
		$("#baseModal .form input[name='password']").val($(this).attr("password"));

		// 注册方式
		var addressType = $(this).attr("type");
		$("#baseModal .form input[name='type']").removeAttr('checked');
		//$("#updateModal .form input[name='addressType'][value='"+ addressType +"']").attr('checked', 'true');
		$("#baseModal .form input[name='type'][value='"+ addressType +"']").click();
	  });
	  
	
	
});