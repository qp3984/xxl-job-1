$(function () {
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
    //置顶
    var $top = $(".top");
    $top.click(function () {
        var $tr = $(this).parents("tr");
        $tr.fadeOut().fadeIn();
        $(".table1").prepend($tr);
        $tr.css("color", "#f60");
    });
    //置底
    var $stick = $(".stick");
    $stick.click(function () {
        var $tr = $(this).parents("tr");
        $tr.fadeOut().fadeIn();
        $(".table1").append($tr);
        $tr.css("color", "#f60");
    });

    //保存
    $('.J_save').click(function () {

        var url = "www.cenrise.com";
        var forms = $("form").serialize();
        $.post(url, forms, function (res) {
            if (res > 0) {
                antd.message.success('保存成功！');
                location.href = 'www.cenrise.com';
            } else {
                antd.message.error('保存失败');
                return false;
            }
        });
    });


});