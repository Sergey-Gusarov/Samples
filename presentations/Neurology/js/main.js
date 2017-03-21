;
(function () {
    window.clickEvent = ('ontouchstart' in window ? 'touchend' : 'click');

    window.storyCLMNavigation = new StoryCLMNavigation({
        threshold: 400,
        swipePointsCount: 1
    });


    /**
     * Обработка перехода на предыдущий слайд
     */
    window.storyCLMNavigation.onSwipePrev = function (direction) {
        if (direction && !drag_flag) {
            window.location = direction;
        } else {
            return false;
        }
    }

    /**
     * Обработка перехода на следующий слайд
     */
    window.storyCLMNavigation.onSwipeNext = function (direction) {
        if (direction && !drag_flag) {
            window.location = direction;
        } else {
            return false;
        }
    }
})();
(function () {
    try {
        var clickEvent = ('ontouchstart' in window ? 'touchend' : 'click');
        $("[data-route]").on(clickEvent, function (e) {
            console.log(this)
            var route = $(this).data('route');
            if (route)
                if (window.location)
                    window.location = route;

            e.preventDefault();
        });
    } catch (ex) {}
})();


function addToKPI(key, value) {
    // if (window.parent.addData) {
    //     window.parent.addData(key, value);
    // } else {
    //     console.log(key + ', ' + value);
    //     console.log(typeof value);
    // }
}