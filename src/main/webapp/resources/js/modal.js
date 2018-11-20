$(function() {
    $("#modal-overlay").hide();

    $("#modal-overlay,.modal-close").click(function() {
        //$("#pc-local").hide();
        //$("#app-local").hide();
        $(".mc").hide();
        $("#modal-overlay").hide();
    });

    $(window).resize(function() {
        centeringModalSyncer();
    });
});

//センタリング
function centeringModalSyncer() {
    //画面幅
    var w = $(window).width();
    //画面高さ
    var h = $(window).height();

    //#modal-content幅
    var cw = $(".mc").outerWidth(true);
    //#modal-content高さ
    var ch = $(".mc").outerHeight(true);

    var pxleft = ((w - cw)/2);
    var pxtop = ((h - ch)/2);

    $(".mc").css({"left": pxleft + "px"});
    $(".mc").css({"top": pxtop + "px"});
}

function showForm() {
    centeringModalSyncer();
    $("#modal-overlay").show();
    $("#modal-content").show();
}

function showUpdateForm() {
    centeringModalSyncer();
    $("#modal-overlay").show();
    $("#modal-content-f").show();
}

function showDeleteForm() {
    centeringModalSyncer();
    $("#modal-overlay").show();
    $("#modal-content-d").show();
}

function showAddForm() {
    centeringModalSyncer();
    $("#modal-overlay").show();
    $("#modal-content-add").show();
}

