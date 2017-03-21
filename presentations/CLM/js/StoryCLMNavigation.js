/**
* @param treshold
* @param onSwipeNext = function (direction) {}
* @param onSwipePrev = function (direction) {}
*/

var StoryCLMNavigation = function () {
    if (this instanceof StoryCLMNavigation) {
        var self = this;

        this.isSwipeAllBlock = false;
        this.isSwipePrevBlock = false;
        this.isSwipeNextBlock = false;

        var next = document.querySelector('meta[name="clm-swipe-next"]').getAttribute('content');
        var previous = document.querySelector('meta[name="clm-swipe-previous"]').getAttribute('content');

        var swipeEl = document.body;
        var direction = 0;

        this.swObj = new Hammer(swipeEl);

        this.swObj.get('pan').set({
            threshold: this.treshold || 200,
            pointers: 1
        });

        this.swObj.on('panleft', function () {
            if (self.isSwipeAllBlock || self.isSwipeNextBlock || self.isEmptyMeta(next)) return false;
            direction = 1;
        });

        this.swObj.on('panright', function () {
            if (self.isSwipeAllBlock || self.isSwipePrevBlock || self.isEmptyMeta(previous)) return false;
            direction = 2;
        });

        this.swObj.on('panend', function () {
            if (direction == 1) {
                direction = 0;
                self.onSwipeNext(next);
            } else if (direction == 2) {
                direction = 0;
                self.onSwipePrev(previous);
            }
        });
    } else {
        return new StoryCLMNavigation(treshold);
    }
};

StoryCLMNavigation.prototype = {
    /**
     * Блокирует всю навигацию
     */
    block: function (bool) {
        var self = this;

        if (bool === true) {
            self.isSwipeAllBlock = bool;
        } else {
            //мини-таймаут, чтоб не сработал panend
            setTimeout(function () {
                self.isSwipeAllBlock = false;
            }, 50);
        }
    },

    /**
     * Блокирует навигацию вперед
     */
    blockNext: function (bool) {
        var self = this;
        console.log(12);
        if (bool === true) {
            console.log(12);
            self.isSwipeNextBlock = bool;
        } else {
            //мини-таймаут, чтоб не сработал panend
            setTimeout(function () {
                self.isSwipeNextBlock = false;
            }, 50);
        }
    },

    /**
     * Блокирует навигацию назад
     */
    blockPrev: function (bool) {
        var self = this;

        if (bool === true) {
            self.isSwipePrevBlock = bool;
        } else {
            //мини-таймаут, чтоб не сработал panend
            setTimeout(function () {
                self.isSwipePrevBlock = false;
            }, 50);
        }
    },

    /**
     * Одноразовый блок pan-ов
     */
    blockSwipe: function () {
        this.swObj.stop();
    },

    /**
     * Проверка на наличие следующего/предыдущего слайда
     */
    isEmptyMeta: function (direction) {
        return !direction.length;
    }
};