var isMobile = navigator.userAgent.match(/(iPhone|iPod|iPad|BlackBerry|Android)/i) != null;
var mdown = "mousedown";
var mmove = "mousemove";
var mup = "mouseup";
if (isMobile){
    mdown = "touchstart";
    mmove = "touchmove";
    mup = "touchend";
}
var drag_flag = false;

$(function() {
    $(".footer-but.but-c").on(mup, function(e){
        //$(".links").addClass("active");
        $(".links").fadeIn(300);
        e.preventDefault();
    });
    $(".footer-but.but-i").on(mup, function(e){
        //$(".links-i").addClass("active");
        $(".links-i").fadeIn(300);
        e.preventDefault();
    });
    $(".links").on(mup, function(e){
        //$(".links").removeClass("active");
        $(".links").fadeOut(300);
        e.preventDefault();
    });
    $(".links-i").on(mup, function(e){
        //$(".links-i").removeClass("active");
        $(".links-i").fadeOut(300);
        e.preventDefault();
    });
    $(".footer-but.but-m").on(clickEvent, function (e) {
        if (window.parent.PDFHelper) {
            if (window.parent.PDFHelper.OpenPDF)
                window.parent.PDFHelper.OpenPDF('media/ButtonM.pdf', window, true);
        }
        else {
            window.open('media/ButtonM.pdf');
        }
        e.preventDefault();
    });
    $('[data-pdf]').on(clickEvent, function (e) {
        if (window.parent.PDFHelper) {
            if (window.parent.PDFHelper.OpenPDF)
                window.parent.PDFHelper.OpenPDF('media/' + $(this).data('pdf'), window, true);
        }
        else {
            window.open('media/' + $(this).data('pdf'));
        }
        e.preventDefault();
    });
//SLIDE tanakan_2-4, tanakan_2-5, tanakan_2-5,
    if($('.path-bg').length > 0){
        setTimeout(function(){
            $('.spaceship').addClass('start');
            $('.path').addClass('start');
            var counter = 0;
            var no_ten = false;
            var stop_fly = 0;
            var start_fly = setInterval(function(){
                if(counter <= 11 && !no_ten){
                    counter++;
                }
                if(counter == 11){
                    no_ten = true;
                    stop_fly++;
                }
                if(no_ten){
                    counter--;
                }
                if (counter == 0){
                    no_ten = false;
                    if(stop_fly == 1){
                        clearInterval(start_fly);
                        $('.spaceship').addClass('cycled');
                        $('.spaceship').removeClass('start');
                        $('.path').removeClass('start');
                        $('.spaceship').draggable({
                            axis: "y",
                            containment: 'parent',
                            start: function(){
                                storyCLMNavigation.blockSwipe();
                                $('.spaceship').removeClass('cycled');
                            },
                            drag: function() {
                                storyCLMNavigation.blockSwipe();
                                path_pos = $('.spaceship').position().top;
                                path_h = 324-path_pos;
                                path_k = 294/50;
                                $('.path').height((path_h));
                                $('.moon-val').html(Math.ceil(50-path_pos/path_k));
                            },
                            stop: function () {
                                var space_cifr;
                                space_cifr = parseInt($('.moon-val').html());
                                localStorage.setItem('space_val',space_cifr);
                            }
                        });
                    }
                }
                $('.moon-val').html(counter);
            },100);
        },750);
    }
//SLIDE tanakan_3, tanakan_3-5, tanakan_3-6,
    if($('.path-hor-bg').length > 0){
        setTimeout(function(){
            $('.spaceship').addClass('start');
            $('.path-hor').addClass('start');
            var counter = 0;
            var no_ten = false;
            var stop_fly = 0;
            var space_num = localStorage.getItem('space_val');
            var space_num_int = parseInt(space_num);
            console.log(space_num_int);
            if (space_num != null) {
                $('.spaceship-path').css('width', 270 / 50 * space_num_int + 160 + 'px');
                $('.moon-val').html(space_num_int);
            } else if (space_num == null) {
                $('.spaceship-path').css('width','450px');
                $('.moon-val').html('50');
            }
            var start_fly = setInterval(function(){
                if(counter <= 11 && !no_ten){
                    counter++;
                }
                if(counter == 11){
                    no_ten = true;
                    stop_fly++;
                }
                if(no_ten){
                    counter--;
                }
                if (counter == 0){
                    no_ten = false;
                    if(stop_fly == 1){
                        clearInterval(start_fly);
                        $('.spaceship').addClass('cycled');
                        $('.spaceship').removeClass('start');
                        $('.path-hor').removeClass('start');

                        $('.spaceship').draggable({
                            axis: "x",
                            containment: 'parent',
                            start: function(){
                                $('.spaceship').removeClass('cycled');
                                storyCLMNavigation.blockSwipe();
                            },
                            drag: function() {
                                storyCLMNavigation.blockSwipe();
                                path_pos = $('.spaceship').position().left;
                                path_w_k = 430/270;
                                path_w = path_w_k * path_pos + 50;
                                path_k = 270/50;
                                $('.path-hor').width((path_w));
                                $('.earth-val').html(Math.ceil(path_pos/path_k));
                            }
                        });
                    }
                }
                $('.earth-val').html(counter);
            },100);
        },750);
    }
//tanakan_4
    if($('.s4').length > 0){
        setTimeout(function(){
            $('.line').addClass('active');
            setTimeout(function(){
                $('.percent').addClass('active');
            },1500)
        },500);
    }
//tanakan_5
    $('.button-1').on(mup, function(){
        $(this).addClass('active');
        $('.li').addClass('active');
    });
//tanakan_6
    if($('.s6').length > 0){
        setTimeout(function(){
            $('.line').addClass('active');
            setTimeout(function(){
                $('.percent, .col').addClass('active');
            },1500)
        },500);
    }
//tanakan_7
    if($('.s7').length > 0){
        setTimeout(function(){
            $('.graph-line').addClass('active');
            setTimeout(function(){
                $('.percent').addClass('active');
            },1500)
        },500);
    }
//tanakan_8
    if($('.s8').length > 0){
        setTimeout(function(){
            $('.graph-line').addClass('active');
            setTimeout(function(){
                $('.percent, .graph-diff').addClass('active');
            },1500)
        },500);
    }
//tanakan_9
    if($('.s9').length > 0){
        setTimeout(function(){
            $('.graph-line').addClass('active');
            setTimeout(function(){
                $('.percent').addClass('active');
            },3500)
        },500);
    }
//tanakan_16
    if($('.s16').length > 0){
        setTimeout(function(){
            $('.line').addClass('active');
            setTimeout(function(){
                $('.percent-1').addClass('active');
            },1000)
            setTimeout(function(){
                $('.percent-2, .col').addClass('active');
            },3000)
        },100);
    }
});



