$(function() {
    FastClick.attach(document.body);

    $('.j-select').on(clickEvent, function() {
        $('.j-select').css('opacity', '0').removeClass('active');
        $(this).css('opacity', '1').addClass('active');

    });

    window.storyCLMNavigation.onSwipeNext = function(direction) {

        if ($('.j-select.active').data('select')) {
            StoryCLM.CustomEvents.Set("emotion_vovlechennost", String($('.j-select.active').data('select')));
            console.log('emotion_vovlechennost: ' + $('.j-select.active').data('select'));
        }

        if (direction) {
            window.location = direction;
        } else {
            return false;
        }
    }

});