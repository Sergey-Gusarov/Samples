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

    $('.j-idea-3').focusout(function() {
        var self = $(this);

        if (self.val().length > 0) {
            $('.j-select-3').css('opacity', 1);
        } else {
            $('.j-select-3').css('opacity', 0);
        }
    });

    window.storyCLMNavigation.onSwipeNext = function(direction) {

        if ($('.j-idea-1').val()) {
            StoryCLM.CustomEvents.Set('idea_1', $('.j-idea-1').val());
            console.log('idea_1: ' + $('.j-idea-1').val());
        }

        if ($('.j-idea-2').val()) {
            StoryCLM.CustomEvents.Set('idea_2', $('.j-idea-2').val());
            console.log('idea_2: ' + $('.j-idea-2').val());
        }

        if ($('.j-idea-3').val()) {
            StoryCLM.CustomEvents.Set('idea_3', $('.j-idea-3').val());
            console.log('idea_3: ' + $('.j-idea-3').val());
        }

        if (direction) {
            window.location = direction;
        } else {
            return false;
        }
    }

});