var id = null;
var mId = null;
var URL = null;

$(function() {
    // windowサイズ更新時
    $(window).resize(function() {
        if(mId != null) mlShow();
        if(id != null) mlShow();
    });
    $(window).scroll(function(){
        if(mId != null) mlShow();
        if(id != null) popupResize();
    });

    init();
});

function init() {
    // 左クリック処理
    $('.leftClick').click(function() {
        var parameter = $(this).attr("parameter");
        if($('.popup').css('display') == 'block') {
            if(id == $(this).attr("id")) {
                $('.popup').remove();
                id = null;
            } else {
                $('.popup').remove();
                id = $(this).attr("id");
                api(parameter);
            }
        }
        else {
            id =  $(this).attr("id");
            api(parameter);
        }
    });

    // 右クリック処理
    $(".rightClick").bind('contextmenu', function() {
        var parameter = $(this).attr("parameter");
        if($('.popup').css('display') == 'block') {
            if(id == $(this).attr("id")) {
                $('.popup').remove();
                id = null;
            } else {
                $('.popup').remove();
                id = $(this).attr("id");
                api(parameter);
            }
        }
        else {
            id =  $(this).attr("id");
            api(parameter);
        }

        return false;
    });

    // リストボックス右クリック処理
    $(".selectRightClick").bind('contextmenu', function() {
        var parameter = $("#"+$(this).attr("id")+" option:selected").attr("parameter");
        if($('.popup').css('display') == 'block') {
            if(id == $(this).attr("id")) {
                $('.popup').remove();
                id = null;
            } else {
                $('.popup').remove();
                id = $(this).attr("id");
                api(parameter);
            }
        }
        else {
            id =  $(this).attr("id");
            api(parameter);
        }

        return false;
    });

    $(".selectRightClick").click(function() {
        $(".popup").remove();
        id = null;
    });
    $(".rightClick").click(function() {
        $(".popup").remove();
        id = null;
    });

    // 複数意味右クリック処理
    $(".multipleRightClick").bind('contextmenu', function() {
        var parameters = $(this).attr("parameters");
        if($('.multiple-list').css('display') == 'block') {
            if(mId == $(this).attr("id")) {
                $('.multiple-list').remove();
                mId = null;
            } else {
                $('.multiple-list').remove();
                mId = $(this).attr("id");
                show(parameters);
            }
        }
        else {
            mId =  $(this).attr("id");
            show(parameters);
        }
        return false;
    });
    $(".multipleRightClick").click(function() {
        $(".multiple-list").remove();
        mId = null;
    });
}

function show(parameters) {
    $("#"+mId).after('<ul class="multiple-list" />');
    var array = parameters.split('$');
    $.each(array, function(i, para) {
        initURL();
        URL += para;
        var url;
        $.ajaxSetup({ async: false });
        $.getJSON(URL, function(data){
            url = data.symbolGraphic.imageUrl;
            var img = "<li class='rightClickTest' id=" + "meanings" + i + " " +
                "parameter=" + para + " " + ">" +
                "<div class='mu-cell'><img class='mu-icon' src=" + url + " alt='foo'/>" +
                "<input class='disabled_checkbox' type='checkbox' checked='checked'/></div></li>";
            $(img).appendTo('.multiple-list');
        });
        $.ajaxSetup({ async: true });
    });

    // 右クリック処理
    $(".rightClickTest").bind('contextmenu', function() {
        var parameter = $(this).attr("parameter");
        if($('.popup').css('display') == 'block') {
            if(id == $(this).attr("id")) {
                $('.popup').remove();
                id = null;
            } else {
                $('.popup').remove();
                id = $(this).attr("id");
                api(parameter);
            }
        }
        else {
            id =  $(this).attr("id");
            api(parameter);
        }

        return false;
    });

    // チェックボックスのクリックを無効化します。
    $('.mu-cell .disabled_checkbox').click(function() {
        return false;
    });
    // 画像がクリックされた時の処理です。
    $('img.mu-icon').on('click', function() {
        if(!$(this).is('.checked')) {
            // チェックが入っていない画像をクリックした場合、チェックを入れます。
            $(this).addClass('checked');
        }else {
            // チェックが入っている画像をクリックした場合、チェックを外します。
            $(this).removeClass('checked')
        }
    });
    mlShow();
}

function initURL() {
    URL = "http://localhost:8080/Dictionary/api/dictionary/multiview_symbol/"
}

function api(parameter) {
    initURL();
    $("#"+id).after('<div class="popup scroll"></div>');
    $('.popup').empty();
    URL += parameter;
    $.getJSON(URL, function(data){
        if(parseInt(data.num) > 0)
            $.each(data.viewSymbols, function(i, viewSymbol){
                var img = "<div class='cell'><img src="+viewSymbol.imageUrl+"></div>";
                $(img).appendTo('.popup');
            });
        else $('<h1 id="no-hits" class="no-hits">No Hits</h1>').appendTo('.popup');
    });
    popupResize();
}

function mlShow() {
    var contents = $("#"+mId);
    var mL = $(".multiple-list");
    var wW = window.innerWidth;
    var wH = window.innerHeight;
    var contentsW = contents.outerWidth(true);
    var contentsH = contents.outerHeight(true);
    var contentsTop = contents.offset().top - $(window).scrollTop();
    var contentsDown = wH - contentsH - contentsTop;
    var left = contents.offset().left;
    var right = wW-left-contentsW;

    // 表示するarea
    var topRightArea = contentsTop * (right+contentsW);
    var topLeftArea = contentsTop * (left+contentsW);
    var downRightArea = contentsDown * (right+contentsW);
    var downLeftArea = contentsDown * (left+contentsW);
    var rightTopArea = (contentsTop+contentsH)*right;
    var rightDownArea = right * (contentsH+contentsDown);
    var leftTopArea = (contentsTop+contentsH)*left;
    var leftDownArea = left * (contentsH+contentsDown);
    var arr = [topRightArea, topLeftArea, downRightArea, downLeftArea,
        rightTopArea, rightDownArea, leftTopArea, leftDownArea];
    var direction = arr.indexOf(Math.max.apply(null,arr));

    /* Top */
    if(direction == 0) {
        //alert(0);
        $(".multiple-list").css({"left": left + "px"});
        $(".multiple-list").css({"top": contentsTop-mL.outerHeight(true) + "px"});
    } else if(direction == 1) {
        //alert(1);
        $(".multiple-list").css({"left": left-40 + "px"});
        $(".multiple-list").css({"top": contentsTop-mL.outerHeight(true) + "px"});
    }
    /* Down */
    else if(direction == 2) {
        //alert(2);
        $(".multiple-list").css({"left": left + "px"});
        $(".multiple-list").css({"top": contentsTop+contentsH + "px"});
    } else if(direction == 3) {
        //alert(3);
        $(".multiple-list").css({"left": left-40 + "px"});
        $(".multiple-list").css({"top": contentsTop+contentsH + "px"});
    }
    /* Right */
    else if(direction == 4) {
        //alert(4);
        $(".multiple-list").css({"left": left+contentsW + "px"});
        $(".multiple-list").css({"top": contentsTop + "px"});
    } else if(direction == 5) {
        //alert(5);
        $(".multiple-list").css({"left": left+contentsW + "px"});
        $(".multiple-list").css({"top": contentsTop + "px"});
    }
    /* Left */
    else if(direction == 6) {
        //alert(6);
        $(".multiple-list").css({"left": contentsTop-mL.outerHeight(true) + "px"});
        $(".multiple-list").css({"top": left - mL.outerWidth(true) + "px"});
    } else {
        //alert(7);
        $(".multiple-list").css({"left": contentsTop-mL.outerHeight(true) + "px"});
        $(".multiple-list").css({"top": left - mL.outerWidth(true) + "px"});
    }
}

// ポップアップリサイズ
function popupResize() {
    var contents = $("#"+id);
    var wW = window.innerWidth;
    var wH = window.innerHeight;
    var contentsW = contents.outerWidth(true);
    var contentsH = contents.outerHeight(true);
    var contentsTop = contents.offset().top - $(window).scrollTop();
    var contentsDown = wH - contentsH - contentsTop;
    var left = contents.offset().left;
    var right = wW-left-contentsW;

    // 表示するarea
    var topRightArea = contentsTop * (right+contentsW);
    var topLeftArea = contentsTop * (left+contentsW);
    var downRightArea = contentsDown * (right+contentsW);
    var downLeftArea = contentsDown * (left+contentsW);
    var rightTopArea = (contentsTop+contentsH)*right;
    var rightDownArea = right * (contentsH+contentsDown);
    var leftTopArea = (contentsTop+contentsH)*left;
    var leftDownArea = left * (contentsH+contentsDown);
    var arr = [topRightArea, topLeftArea, downRightArea, downLeftArea,
                rightTopArea, rightDownArea, leftTopArea, leftDownArea];
    var direction = arr.indexOf(Math.max.apply(null,arr));

    /* Top */
    if(direction == 0) {
        $(".popup").css({"left": left + "px"});
        $(".popup").css({"top": 20 + "px"});
        $(".popup").width(right+contentsW-20);
        $(".popup").height(contentsTop-20);
    } else if(direction == 1) {
        $(".popup").css({"left": 20 + "px"});
        $(".popup").css({"top": 20 + "px"});
        $(".popup").width(left+contentsW);
        $(".popup").height(contentsTop-20);
    }
    /* Down */
    else if(direction == 2) {
        $(".popup").css({"left": left + "px"});
        $(".popup").css({"top": contentsTop+contentsH + "px"});
        $(".popup").width(right+contentsW-20);
        $(".popup").height(contentsDown-20);
    } else if(direction == 3) {
        $(".popup").css({"left": 20 + "px"});
        $(".popup").css({"top": contentsTop+contentsH + "px"});
        $(".popup").width(left+contentsW-20);
        $(".popup").height(contentsDown-20);
    }
    /* Right */
    else if(direction == 4) {
        $(".popup").css({"left": left+contentsW + "px"});
        $(".popup").css({"top": 20 + "px"});
        $(".popup").width(right-20);
        $(".popup").height(contentsH+contentsTop-20);
    } else if(direction == 5) {
        $(".popup").css({"left": left+contentsW + "px"});
        $(".popup").css({"top": contentsTop + "px"});
        $(".popup").width(right-20);
        $(".popup").height(contentsH+contentsDown-20);
    }
    /* Left */
    else if(direction == 6) {
        $(".popup").css({"left": 20 + "px"});
        $(".popup").css({"top": 20 + "px"});
        $(".popup").width(left-20);
        $(".popup").height(contentsH+contentsTop-20);
    } else {
        $(".popup").css({"left": 20 + "px"});
        $(".popup").css({"top": contentsTop + "px"});
        $(".popup").width(left-20);
        $(".popup").height(contentsH+contentsDown-20);
    }
}


