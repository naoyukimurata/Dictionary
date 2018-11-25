$(function() {
    $(window).resize(function() {
        popupResize();
    });

    $(".pop").click(function() {
        if($('.popup').css('display') == 'block') {
            $(".popup").empty();
            $(".popup").hide();
        } else {
            popupResize();
            var URL = "http://localhost:8080/Dictionary/api/dictionary/multiview_symbol/beautiful/";
            //"?key="+API_KEY+"&q="+encodeURIComponent('red roses');
            $.getJSON(URL, function(data){
                if (parseInt(data.num) > 0)
                    $.each(data.viewSymbols, function(i, viewSymbol){
                        var img = "<div class='cell'><img src="+viewSymbol.imageUrl+"></div>";
                        $(img).appendTo(".popup");
                    });
                else
                    alert('No hits');
            });
            $(".popup").show();
        }

    });
});

function popupResize() {
    var pop = $(".pop");
    var x = pop.offset().left+pop.outerWidth(true);
    var y = pop.offset().top;

    $(".popup").css({"left": x+10 + "px"});
    $(".popup").css({"top": y + "px"});
}