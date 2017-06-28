$(function () {

    // remove
    $('.remove').on('click', function () {
        var id = $(this).attr('id');

        layer.confirm('确认删除分组?', {icon: 3, title: '系统提示'}, function (index) {
            layer.close(index);

            $.ajax({
                type: 'POST',
                url: base_url + '/jobsql/remove',
                data: {"id": id},
                dataType: "json",
                success: function (data) {
                    if (data.code == 200) {
                        layer.open({
                            title: '系统提示',
                            content: '删除成功',
                            icon: '1',
                            end: function (layero, index) {
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

    // jquery.validate 自定义校验 “英文字母开头，只含有英文字母、数字和下划线”
    jQuery.validator.addMethod("myValid01", function (value, element) {
        var length = value.length;
        var valid = /^[a-z][a-zA-Z0-9-]*$/;
        return this.optional(element) || valid.test(value);
    }, "限制以小写字母开头，由小写字母、数字和下划线组成");

    $('.add').on('click', function () {
        $('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
    });
    var addModalValidate = $("#addModal .form").validate({
        errorElement: 'span',
        errorClass: 'help-block',
        focusInvalid: true,
        rules: {
            appName: {
                required: true,
                rangelength: [4, 64],
                myValid01: true
            },
            title: {
                required: true,
                rangelength: [4, 12]
            },
            order: {
                required: true,
                digits: true,
                range: [1, 1000]
            }
        },
        messages: {
            appName: {
                required: "请输入“AppName”",
                rangelength: "AppName长度限制为4~64",
                myValid01: "限制以小写字母开头，由小写字母、数字和中划线组成"
            },
            title: {
                required: "请输入“执行器名称”",
                rangelength: "长度限制为4~12"
            },
            order: {
                required: "请输入“排序”",
                digits: "请输入整数",
                range: "取值范围为1~1000"
            }
        },
        highlight: function (element) {
            $(element).closest('.form-group').addClass('has-error');
        },
        success: function (label) {
            label.closest('.form-group').removeClass('has-error');
            label.remove();
        },
        errorPlacement: function (error, element) {
            element.parent('div').append(error);
        },
        submitHandler: function (form) {
            $.post(base_url + "/jobsql/save", $("#addModal .form").serialize(), function (data, status) {
                if (data.code == "200") {
                    $('#addModal').modal('hide');
                    layer.open({
                        title: '系统提示',
                        content: '新增成功',
                        icon: '1',
                        end: function (layero, index) {
                            window.location.reload();
                        }
                    });
                } else {
                    layer.open({
                        title: '系统提示',
                        content: (data.msg || "新增失败"),
                        icon: '2'
                    });
                }
            });
        }
    });
    $("#addModal").on('hide.bs.modal', function () {
        $("#addModal .form")[0].reset();
        addModalValidate.resetForm();
        $("#addModal .form .form-group").removeClass("has-error");
    });

    // 注册方式，切换
    $("#addModal input[name=addressType], #updateModal input[name=addressType]").click(function () {
        var addressType = $(this).val();
        var $addressList = $(this).parents("form").find("input[name=addressList]");
        if (addressType == 0) {
            $addressList.val("");
            $addressList.attr("readonly", "readonly");
        } else {
            $addressList.removeAttr("readonly");
        }
    });

    // update
    $('.update').on('click', function () {
        $("#updateModal .form input[name='id']").val($(this).attr("id"));
        $("#updateModal .form input[name='appName']").val($(this).attr("appName"));
        $("#updateModal .form input[name='title']").val($(this).attr("title"));
        $("#updateModal .form input[name='order']").val($(this).attr("order"));

        // 注册方式
        var addressType = $(this).attr("addressType");
        $("#updateModal .form input[name='addressType']").removeAttr('checked');
        //$("#updateModal .form input[name='addressType'][value='"+ addressType +"']").attr('checked', 'true');
        $("#updateModal .form input[name='addressType'][value='" + addressType + "']").click();
        // 机器地址
        $("#updateModal .form input[name='addressList']").val($(this).attr("addressList"));

        $('#updateModal').modal({backdrop: false, keyboard: false}).modal('show');
    });
    var updateModalValidate = $("#updateModal .form").validate({
        errorElement: 'span',
        errorClass: 'help-block',
        focusInvalid: true,
        rules: {
            appName: {
                required: true,
                rangelength: [4, 64],
                myValid01: true
            },
            title: {
                required: true,
                rangelength: [4, 12]
            },
            order: {
                required: true,
                digits: true,
                range: [1, 1000]
            }
        },
        messages: {
            appName: {
                required: "请输入“AppName”",
                rangelength: "AppName长度限制为4~64",
                myValid01: "限制以小写字母开头，由小写字母、数字和中划线组成"
            },
            title: {
                required: "请输入“执行器名称”",
                rangelength: "长度限制为4~12"
            },
            order: {
                required: "请输入“排序”",
                digits: "请输入整数",
                range: "取值范围为1~1000"
            }
        },
        highlight: function (element) {
            $(element).closest('.form-group').addClass('has-error');
        },
        success: function (label) {
            label.closest('.form-group').removeClass('has-error');
            label.remove();
        },
        errorPlacement: function (error, element) {
            element.parent('div').append(error);
        },
        submitHandler: function (form) {
            $.post(base_url + "/jobsql/update", $("#updateModal .form").serialize(), function (data, status) {
                if (data.code == "200") {
                    $('#addModal').modal('hide');

                    layer.open({
                        title: '系统提示',
                        content: '更新成功',
                        icon: '1',
                        end: function (layero, index) {
                            window.location.reload();
                        }
                    });
                } else {
                    layer.open({
                        title: '系统提示',
                        content: (data.msg || "更新失败"),
                        icon: '2'
                    });
                }
            });
        }
    });
    $("#updateModal").on('hide.bs.modal', function () {
        $("#updateModal .form")[0].reset();
        addModalValidate.resetForm();
        $("#updateModal .form .form-group").removeClass("has-error");
    });

    // updateSub
    $('.updateSub').on('click', function () {
        $("#updateSubModal .form input[name='id']").val($(this).attr("id"));
        $("#updateSubModal .form input[name='appName']").val($(this).attr("appName"));
        $("#updateSubModal .form input[name='title']").val($(this).attr("title"));
        $("#updateSubModal .form input[name='order']").val($(this).attr("order"));

        // 注册方式
        var addressType = $(this).attr("addressType");
        $("#updateSubModal .form input[name='addressType']").removeAttr('checked');
        //$("#updateModal .form input[name='addressType'][value='"+ addressType +"']").attr('checked', 'true');
        $("#updateSubModal .form input[name='addressType'][value='" + addressType + "']").click();
        // 机器地址
        $("#updateSubModal .form input[name='addressList']").val($(this).attr("addressList"));

        $('#updateSubModal').modal({backdrop: false, keyboard: false}).modal('show');
    });
    var updateSubModalValidate = $("#updateSubModal .form").validate({
        errorElement: 'span',
        errorClass: 'help-block',
        focusInvalid: true,
        rules: {
            appName: {
                required: true,
                rangelength: [4, 64],
                myValid01: true
            },
            title: {
                required: true,
                rangelength: [4, 12]
            },
            order: {
                required: true,
                digits: true,
                range: [1, 1000]
            }
        },
        messages: {
            appName: {
                required: "请输入“AppName”",
                rangelength: "AppName长度限制为4~64",
                myValid01: "限制以小写字母开头，由小写字母、数字和中划线组成"
            },
            title: {
                required: "请输入“执行器名称”",
                rangelength: "长度限制为4~12"
            },
            order: {
                required: "请输入“排序”",
                digits: "请输入整数",
                range: "取值范围为1~1000"
            }
        },
        highlight: function (element) {
            $(element).closest('.form-group').addClass('has-error');
        },
        success: function (label) {
            label.closest('.form-group').removeClass('has-error');
            label.remove();
        },
        errorPlacement: function (error, element) {
            element.parent('div').append(error);
        },
        submitHandler: function (form) {
            $.post(base_url + "/jobsql/updateSub", $("#updateSubModal .form").serialize(), function (data, status) {
                if (data.code == "200") {
                    $('#addModal').modal('hide');

                    layer.open({
                        title: '系统提示',
                        content: '更新成功',
                        icon: '1',
                        end: function (layero, index) {
                            window.location.reload();
                        }
                    });
                } else {
                    layer.open({
                        title: '系统提示',
                        content: (data.msg || "更新失败"),
                        icon: '2'
                    });
                }
            });
        }
    });
    $('.testsql').on('click', function () {
    	  $.post(base_url + "/jobsql/testsql", $("#updateSubModal .form").serialize(), function (data, status) {
    		  if (data.code == "200") {
  				$("#eerMsg").hide();
  				$("#rigMsg").show();
  			} else {
  				$("#errMsg").show();
  				$("#rigMsg").hide();
  			}
          });
    });
    
    $("#rebut").on('click',function(){
       	$("#msg").hide();
       	$("#errMsg").hide();
        });
    
    $("#updateSubModal").on('hide.bs.modal', function () {
        $("#updateSubModal .form")[0].reset();
        addModalValidate.resetForm();
        $("#updateSubModal .form .form-group").removeClass("has-error");
    });


    //上移
    var $up = $(".up");
    $up.click(function () {
        var $tr = $(this).parents("tr");
        if ($tr.index() != 0) {
            $tr.fadeOut().fadeIn();
            $tr.prev().before($tr);
        }
    });
    //下移
    var $down = $(".down");
    var len = $down.length;
    $down.click(function () {
        var $tr = $(this).parents("tr");
        if ($tr.index() != len - 1) {
            $tr.fadeOut().fadeIn();
            $tr.next().after($tr);
        }
    });

   

});
