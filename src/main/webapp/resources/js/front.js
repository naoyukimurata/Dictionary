var id = null;
var URL = null;

$(function() {
    // windowサイズ更新時
    $(window).resize(function() {
        if(id != null) popupResize();
    });
    $(window).scroll(function(){
        if(id != null) popupResize();
    });

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
});

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


// ポップアップリサイズ
function popupResize() {
    var contents = $("#"+id);
    var direction;
    var w = window.innerWidth;
    var h = window.innerHeight;
    var contentsW = contents.outerWidth(true);
    var contentsH = contents.outerHeight(true);
    var mU = parseInt($(contents).css('margin-top'), 10);
    var mD = parseInt($(contents).css('margin-bottom'), 10);
    var mL = parseInt($(contents).css('margin-left'), 10);
    var mR = parseInt($(contents).css('margin-right'), 10);
    var aH = screen.availHeight;
    var sT = $(window).scrollTop();
    var up = contents.offset().top;

    var contentsTop = contents.offset().top - $(window).scrollTop();
    var contentsDown = h-contentsTop-contentsH;
    var left = contents.offset().left;
    var right = w-left-contentsW;

    if(contentsTop >= contentsDown && contentsTop >= right && contentsTop >= left) direction = 0;
    else if(right >= contentsTop && right >= left && right >= contentsDown) direction = 1;
    else if(contentsDown >= contentsTop && contentsDown >= right && contentsDown >= left) direction = 2;
    else direction = 3;

    /* 上 */
    if(direction == 0) {
        $(".popup").css({"left": left + "px"});
        $(".popup").css({"top": 20 + "px"});
        $(".popup").width(w-left-20);
        $(".popup").height(aH - sT - (aH - up - contentsH) - 20 - contentsH);
    }
    /* 右 */
    else if(direction == 1) {
        if(up > contentsDown) {
            $(".popup").css({"left": left+contentsW-mR + "px"});
            $(".popup").css({"top": 20 + "px"});
            $(".popup").width(w-left-contentsW-30);
            $(".popup").height(aH - sT - (aH - up - contentsH));
        } else {
            $(".popup").css({"left": left+contentsW + "px"});
            $(".popup").css({"top": contentsTop + "px"});
            $(".popup").width(w-left-contentsW-30);
            $(".popup").height(contentsDown-20);
        }
    }
    /* 下 */
    else if(direction == 2) {
        $(".popup").css({"left": left + "px"});
        $(".popup").css({"top": contentsTop+contentsH-mU-mD + "px"});
        $(".popup").width(right+contentsW-20);
        $(".popup").height(contentsDown-20-contentsH);
    }
    /* 左 */
    else {
        if(up > contentsDown) {
            $(".popup").css({"left": 20 + "px"});
            $(".popup").css({"top": 20 + "px"});
            $(".popup").width(w-right-contentsW-30);
            $(".popup").height(aH - sT - (aH - up - contentsH));
        } else {
            $(".popup").css({"left": 20 + "px"});
            $(".popup").css({"top": contentsTop + "px"});
            $(".popup").width(w-right-contentsW-30);
            $(".popup").height(contentsDown-20);
        }
    }
}


