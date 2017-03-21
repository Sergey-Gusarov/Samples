$(function() {
	$('.planet').on(mup, function(){
		$('.planet').removeClass('pretremor');
		$('.planet').removeClass('active');
		$('.planet').removeClass('tremor');
		$(this).addClass('active');
		var planetIndex = $(this).index();
		$('.content').removeClass('active');
		$('.content').eq(planetIndex).addClass('active');
		setTimeout(function() {
			$('.planet').addClass('tremor').eq(planetIndex).removeClass('tremor');
		}, 2000);
	});
});