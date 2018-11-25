var id;

$(function() {
    $(window).resize(function() {
        popupResize(id);
    });

    $(".pop").click(function() {
        if($('.popup').css('display') == 'block') {
            if(id == $(this).attr("id")) {
                $('.popup').remove();
            } else {
                $('.popup').remove();
                id = $(this).attr("id");
                api();
            }
        }
        else {
            id =  $(this).attr("id");
            api();
        }
    });

    $(".popupR").bind('contextmenu', function() {
        var val = $('[name=alphabet]').val();
        alert(val);
        if($('.popup').css('display') == 'block') {
            if(id == $(this).attr("id")) {
                $('.popup').remove();
            } else {
                $('.popup').remove();
                id = $(this).attr("id");
                api();
            }
        }
        else {
            id =  $(this).attr("id");
            api();
        }

        return false;
    });

    $(".popupR").click(function() {
        $(".popup").remove();
    });
});

function api() {
    $("#"+id).after('<div class="popup scroll"></div>');
    $('.popup').empty();

    var URL = "http://localhost:8080/Dictionary/api/dictionary/multiview_symbol/beautiful/";//"?key="+API_KEY+"&q="+encodeURIComponent('red roses');
    $.getJSON(URL, function(data){
        if(parseInt(data.num) > 0)
            $.each(data.viewSymbols, function(i, viewSymbol){
                var img = "<div class='cell'><img src="+viewSymbol.imageUrl+"></div>";
                $(img).appendTo('.popup');
            });
        else alert('No hits');
    });
    popupResize();
}

// ポップアップのリサイズ
function popupResize() {
    var pop = $("#"+id);
    var x = pop.offset().left+pop.outerWidth(true);
    var y = pop.offset().top;

    $(".popup").css({"left": x+10 + "px"});
    $(".popup").css({"top": y + "px"});
}


