var clickEvent = ('ontouchstart' in window ? 'touchend' : 'click');;
(function() {
    window.storyCLMNavigation = new StoryCLMNavigation({
        threshold: 200,
        swipePointsCount: 1
    });

    window.storyCLMNavigation.onSwipePrev = function(direction) {
        if (direction) {
            window.location = direction;
        } else {
            return false;
        }
    }

    window.storyCLMNavigation.onSwipeNext = function(direction) {
        if (direction) {
            window.location = direction;
        } else {
            return false;
        }
    }

    try {
        $("[data-route]").on(clickEvent, function(e) {
            var route = $(this).data('route');
            if (route)
                if (window.location)
                    window.location = route;

            e.preventDefault();
        });
    } catch (ex) {}

})();