var id;
var URL = "http://localhost:8080/Dictionary/api/dictionary/multiview_symbol/";

$(function() {
    // windowサイズ更新時
    $(window).resize(function() {
        popupResize(id);
    });

    // 左クリック処理
    $(".pop").click(function() {
        var parameter = $(this).attr("name");
        if($('.popup').css('display') == 'block') {
            if(id == $(this).attr("id")) {
                $('.popup').remove();
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
    $(".popupR").bind('contextmenu', function() {
        var parameter = $(this).val();
        if($('.popup').css('display') == 'block') {
            if(id == $(this).attr("id")) {
                $('.popup').remove();
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

    $(".popupR").click(function() {
        $(".popup").remove();
    });
});

function initURL() {
    URL = "http://localhost:8080/Dictionary/api/dictionary/multiview_symbol/"
}

function api(parameter) {
    initURL();
    $("#"+id).after('<div class="popup scroll"></div>');
    $('.popup').empty();
    URL += parameter;//"?key="+API_KEY+"&q="+encodeURIComponent('red roses');
    alert(URL);
    $.getJSON(URL, function(data){
        if(parseInt(data.num) > 0)
            $.each(data.viewSymbols, function(i, viewSymbol){
                var img = "<div class='cell'><img src="+viewSymbol.imageUrl+"></div>";
                $(img).appendTo('.popup');
            });
        else $('<h1>No Hits</h1>').appendTo('.popup');
    });
    popupResize();
}

// ポップアップリサイズ
function popupResize() {
    var pop = $("#"+id);
    var x = pop.offset().left+pop.outerWidth(true);
    var y = pop.offset().top;

    $(".popup").css({"left": x+10 + "px"});
    $(".popup").css({"top": y + "px"});
}


