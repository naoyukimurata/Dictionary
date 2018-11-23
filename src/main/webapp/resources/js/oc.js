$(function() {
    $('#pic-list').gridalicious({
        gutter: 30, //グリッドの間の余白を設定します
        width: 280, //グリッドの幅を設定します
        selector:'.item', //グリッド化する要素を指定出来ます。デフォルトは.itemです。
        animate: true, //アニメーションの有無を設定します。デフォルトはfalseです。
        animationOptions: { //アニメーションの諸々の設定を行います。
            speed: 100, //速度設定
            duration: 700, //アニメーションをどのくらいで実行するかを設定
        },
    });
});