/*$(function() {
    $("#modal-overlay,.modal-close").click(function() {
        $("#pc-local").hide();
        $("#app-local").hide();
        $(".mc").hide();
        $("#modal-overlay").hide();
    });
});*/


//センタリング
function centering() {
    //画面幅
    var w = $(window).width();
    //画面高さ
    var h = $(window).height();

    //#modal-content幅
    var cw = $(".mcc").outerWidth(true);
    //#modal-content高さ
    var ch = $(".mcc").outerHeight(true);

    var pxleft = ((w - cw)/2);
    var pxtop = ((h - ch)/2);

    $(".mcc").css({"left": pxleft + "px"});
    $(".mcc").css({"top": pxtop + "px"});
}

function openGroup(id) {
    var objs=document.getElementsByClassName(id);
    for(var i = 0; i < objs.length; i++) {
        objs[i].style.display=(objs[i].style.display=='none')?'block':'none';
    }
}


$(document).ready(function() {
    $(".images").ZoomPic();

    $(".tag").tooltip({
        direction: "bottom"
    });
});

$(function() {
    $(".images").ZoomPic();

    $(".tag").tooltip({
        direction: "bottom"
    });
});

function message() {
    setTimeout(function(){
        $(".message").hide("slow");
    },10000);
}

function local() {
    centeringModalSyncer();
    $("#modal-overlay").show();
    $("#modal-content-pc").show();

}
function app() {
    centeringModalSyncer();
    $("#modal-overlay").show();
    $("#modal-content").show();
    $("#modal-content-app").show();
}
