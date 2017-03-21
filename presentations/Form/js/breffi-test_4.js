$(function() {
    var options = {
        obj: [".round", ".round"],
        deltaX: [-290, 290],
        dragInterval: 2450,
        delay: 100,
        duration: 2450,
        objectOpacity: .6
    };
    var ut = new UTManager(options);
    ut.drag();

    $('.round').draggable({
        containment: "#slide_breffi-test_4_layer_36",
        axis: 'x',
        start: function(event, ui) {
            storyCLMNavigation.blockSwipe();
        },
        stop: function() {
            sliderPosition = $('.round').position().left;
            $('.round').animate({
                left: createEndPosition(sliderPosition)
            }, 200);
        }
    });

    $("#slide_breffi-test_4_layer_36").on(clickEvent, function(e) {
        var touch = e.originalEvent.touches[0] || e.originalEvent.changedTouches[0];
        var p = touch.clientX - 100;
        $('.round').animate({
            left: createEndPosition(p)
        }, 500);
    });

    window.storyCLMNavigation.onSwipeNext = function(direction) {

        StoryCLM.CustomEvents.Set('uroven_znaniy', String(sliderValue));
        console.log('uroven_znaniy: ' + String(sliderValue));


        if (direction) {
            window.location = direction;
        } else {
            return false;
        }
    }

});

var sliderPosition;
var sliderEndPosition;
var sliderValue = "0";

function createEndPosition(startPosition) {
    var pos;
    $(".num").css("color", "#A0A2A3");
    if (startPosition < 183) {
        sliderValue = "-3";
        pos = 133;
        $(".n1").css("color", "#23B0D4");
    } else if (startPosition >= 183 && startPosition < 280) {
        sliderValue = "-2";
        pos = 232;
        $(".n2").css("color", "#23B0D4");
    } else if (startPosition >= 280 && startPosition < 377) {
        sliderValue = "-1";
        pos = 329;
        $(".n3").css("color", "#23B0D4");
    } else if (startPosition >= 377 && startPosition < 475) {
        sliderValue = "0";
        pos = 426;
        $(".n4").css("color", "#23B0D4");
    } else if (startPosition >= 475 && startPosition < 575) {
        sliderValue = "1";
        pos = 524;
        $(".n5").css("color", "#23B0D4");
    } else if (startPosition >= 575 && startPosition < 670) {
        sliderValue = "2";
        pos = 622;
        $(".n6").css("color", "#23B0D4");
    } else if (startPosition >= 670) {
        sliderValue = "3";
        pos = 719;
        $(".n7").css("color", "#23B0D4");
    }
    return pos;
}