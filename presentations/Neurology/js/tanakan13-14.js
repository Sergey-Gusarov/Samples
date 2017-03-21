$(function() {
    $('.content').eq(0).addClass('active');
    setTimeout(function(){
        $('.content').eq(0).addClass('active1');
        setTimeout(function(){
            $('.content').eq(0).addClass('active2');
        }, 2000)
    }, 3000)



    $('.planet').eq(0).on(mup, function() {
		$('.planet').removeClass('active tremor');
		$('.content').removeClass('active active1 active2');
		$(this).addClass('active');
        $('.content').eq(0).addClass('active');
        setTimeout(function(){
            $('.content').eq(0).addClass('active1');
            setTimeout(function(){
                $('.content').eq(0).addClass('active2');
            }, 2000)
        }, 3000)
        $('.planet').eq(1).addClass('tremor');
	});
	$('.planet').eq(1).on(mup, function() {
		$('.planet').removeClass('active tremor');
		$('.content').removeClass('active active1 active2');
		$(this).addClass('active');
        $('.content').eq(1).addClass('active');
        setTimeout(function(){
            $('.content').eq(1).addClass('active1');
            setTimeout(function(){
                $('.content').eq(1).addClass('active2');
            }, 2000)
        }, 2000)
        $('.planet').eq(0).addClass('tremor');
	});
});