$(function() {
	$('.planet').eq(0).on(mup, function() {
		$('.planet').removeClass('active tremor');
		$('.content').removeClass('active');
		$(this).addClass('active');
		$('.content').eq(0).addClass('active');
		setTimeout(function() {
			if(!$('.planet').eq(1).hasClass('active')) {
				$('.planet').eq(1).addClass('tremor');
			}
		}, 2000);
	});
	$('.planet').eq(1).on(mup, function() {
		$('.planet').removeClass('active tremor');
		$('.content').removeClass('active');
		$(this).addClass('active');
		$('.content').eq(1).addClass('active');
		setTimeout(function() {
			if(!$('.planet').eq(0).hasClass('active')) {
				$('.planet').eq(0).addClass('tremor');
			}
		}, 2000);
	});
});