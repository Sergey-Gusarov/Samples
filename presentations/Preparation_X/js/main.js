var clickEvent = ('ontouchstart' in window ? 'touchend' : 'click');
;(function () {
     window.storyCLMNavigation = new StoryCLMNavigation({
        threshold: 400,
        swipePointsCount: 1
    });


    /**
     * Обработка перехода на предыдущий слайд
    */
    window.storyCLMNavigation.onSwipePrev = function (direction) {
        if (direction) {
            window.location = direction;
        } else {
            return false;
        }
    }

    /**
     * Обработка перехода на следующий слайд
    */
    window.storyCLMNavigation.onSwipeNext = function (direction) {
        if (direction) {
            window.location = direction;
        } else {
            return false;
        }
    }

    //$('[data-pdf]').on(clickEvent, function (e) {
    //    StoryCLM.openMediaFile($(this).data('pdf'));
    //    e.preventDefault();
    //});
})();


(function () {
    try {
        var clickEvent = ('ontouchstart' in window ? 'touchend' : 'click');
        $("[data-route]").on(clickEvent, function (e) {
            var route = $(this).data('route');
            if (route)
                if (window.location) {  
                    window.location = route;
                }

            e.preventDefault();
        });
    }
    catch (ex) { }
})();


/*!
* StoryCLMStorage Library v0.4.0
* Copyright(c) 2016, Vladimir Klyuev, Breffi Inc. All rights reserved.
* License: Licensed under The MIT License.
*/

window.StoryCLMStoragePrefix = "cirkadin";

; (function (w) {

    w.StoryCLMStorage = (function () {

        var _presentationName = "SCLMS_" + w.StoryCLMStoragePrefix;
        var _state = {};

        function _getState() {
            var result = localStorage.getItem(_presentationName);
            if (result) {
                _state = JSON.parse(result).data || {};
            }
            return _state;
        }

        function _setState() {
            if (_state) {
                localStorage.setItem(_presentationName, JSON.stringify({ data: _state }));
            }
        }

        function _get() {

        }

        function _set() {

        }

        $(w).unload(function () {
            _setState();
        });

        w.PresentationState = _getState();
        console.log(w.PresentationState);

        return {
            Get: _get,
            Set: _set
        };
    })();

})(window);
