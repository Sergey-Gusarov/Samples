$(function() {
	$('.planet').on(mup, function() {
		var popupIndex = $(this).index();
		$('.popup').eq(popupIndex).toggleClass('active');
		$('.name').eq(popupIndex).toggleClass('active');
	});
	$('.popup').on(mup, function() {
		$(this).removeClass('active');
		var titleIndex = $(this).index();
		$('.name').eq(titleIndex).removeClass('active');
	});
});