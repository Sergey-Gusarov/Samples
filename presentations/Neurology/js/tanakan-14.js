$(function() {
	$('.button').on(mup, function() {
		$(this).removeClass('tremor');
		var buttonIndex = $(this).index() + 1;
		$('.item:nth-child(' + buttonIndex + ')').addClass('active');
	});
});