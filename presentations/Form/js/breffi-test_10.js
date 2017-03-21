$(function() {
    FastClick.attach(document.body);

    $('.j-idea-1').focusout(function() {
        var self = $(this);

        if (self.val().length > 0) {
            $('.j-select-1').css('opacity', 1);
        } else {
            $('.j-select-1').css('opacity', 0);
        }
    });

    $('.j-idea-2').focusout(function() {
        var self = $(this);

        if (self.val().length > 0) {
            $('.j-select-2').css('opacity', 1);
        } else {
            $('.j-select-2').css('opacity', 0);
        }
    });

    window.storyCLMNavigation.onSwipeNext = function(direction) {
        if ($('.j-idea-1').val()) {
            StoryCLM.CustomEvents.Set('trebuet_prorabotki', $('.j-idea-1').val());
            console.log('trebuet_prorabotki: ' + $('.j-idea-1').val());
        }

        if ($('.j-idea-2').val()) {
            StoryCLM.CustomEvents.Set('hochu_v_sleduyshiy_raz', $('.j-idea-2').val());
            console.log('hochu_v_sleduyshiy_raz: ' + $('.j-idea-2').val());
        }

        if (direction) {
            window.location = direction;
        } else {
            return false;
        }
    }

});