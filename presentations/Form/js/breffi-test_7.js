$(function() {
    FastClick.attach(document.body);

    $('.j-select').on(clickEvent, function() {
        var self = $(this);

        if (self.hasClass('active')) {
            self.css('opacity', '0').removeClass('active');
        } else {
            self.css('opacity', '1').addClass('active');
        }
    });

    window.storyCLMNavigation.onSwipeNext = function(direction) {

        var trener = [];

        $('.active').each(function(index, item) {
            trener.push($(this).data('trener'));
        });

        if (trener.join(', ')) {
            StoryCLM.CustomEvents.Set('trener', trener.join(', '));
            console.log('trener: ' + trener.join(', '));
        }

        if (direction) {
            window.location = direction;
        } else {
            return false;
        }
    }

});