$(function() {
	$('.planet').on(mup, function() {
		var popupIndex = $(this).index();
		$('.popup').eq(popupIndex).toggleClass('active');
		$(this).children('.planet-title').toggleClass('active');
	});
	$('.popup').on(mup, function() {
		$(this).removeClass('active');
		var titleIndex = $(this).index();
		$('.planet-title').eq(titleIndex).removeClass('active');
	});
});