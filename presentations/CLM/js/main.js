;(function () {
    window.storyCLMNavigation = new StoryCLMNavigation();

    window.storyCLMNavigation.treshold = 200;

    /**
     * Обработка перехода на предыдущий слайд
    */
    window.storyCLMNavigation.onSwipePrev = function (direction) {
        window.location = direction;
    }

    /**
     * Обработка перехода на следующий слайд
    */
    window.storyCLMNavigation.onSwipeNext = function (direction) {
        window.location = direction;
    }
})();