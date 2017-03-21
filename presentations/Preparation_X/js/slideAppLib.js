/* v2.6.0-2-807bdec - 2015-05-29 */
var CommunicateEmbedded =
/******/
(function(modules) { // webpackBootstrap
    /******/ // The module cache
    /******/
    var installedModules = {};

    /******/ // The require function
    /******/
    function __webpack_require__(moduleId) {

        /******/ // Check if module is in cache
        /******/
        if (installedModules[moduleId])
        /******/
            return installedModules[moduleId].exports;

        /******/ // Create a new module (and put it into the cache)
        /******/
        var module = installedModules[moduleId] = {
            /******/
            exports: {},
            /******/
            id: moduleId,
            /******/
            loaded: false
            /******/
        };

        /******/ // Execute the module function
        /******/
        modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);

        /******/ // Flag the module as loaded
        /******/
        module.loaded = true;

        /******/ // Return the exports of the module
        /******/
        return module.exports;
        /******/
    }


    /******/ // expose the modules object (__webpack_modules__)
    /******/
    __webpack_require__.m = modules;

    /******/ // expose the module cache
    /******/
    __webpack_require__.c = installedModules;

    /******/ // __webpack_public_path__
    /******/
    __webpack_require__.p = "";

    /******/ // Load entry module and return exports
    /******/
    return __webpack_require__(0);
    /******/
})
/************************************************************************/
/******/
([
    /* 0 */
    /***/
    function(module, exports, __webpack_require__) {

        /* jshint node: true */
        'use strict';
        var window = __webpack_require__(1);

        var CommunicateEmbedded = {
            callBackMethods: [],
            addCallBackMethod: function(cb) {
                console.log("callback added");
            },
            readyCallBacks: [],
            ready: function(cb) {
                window.addEventListener("load", function load(event) {
                    cb();
                    window.removeEventListener("load", load, false);
                }, false);
            },
            init: function() {
                console.log("init slide");
            },
            filterHistory: function(tags) {
                return [];
            },
            setData: function(key, value) {
                window.localStorage[key] = value;
            },
            getData: function(key) {
                return window.localStorage[key];
            },
            pressOnSlide: function(ev) {
                console.log("press on slide");
            },
            stopScroll: function(ev) {
                console.log("stop scroll");
            },
            callCallbackMethods: function() {
                console.log("callback called");
            },
            receiveSourceMessage: function(event) {
                console.log("receiveSourceMessage called");
            },
            openAttachment: function(alias) {
                console.log("openAttachment:" + alias);
            },
            navigated: function(alias) {
                console.log("navigated:" + alias);
            },
            navigate: function(alias) {
                console.log("navigate:" + alias);
            },
            fireEvent: function(alias) {
                console.log("fireEvent:" + alias);
            },
            fillQuestionary: function(alias) {
                console.log("fillQuestionary:" + alias);
            },
            enableNavigation: function() {
                console.log("navigation enabled");
            },
            suppressNavigation: function() {
                console.log("navigation suppressed");
            },

            navigateBackward: function(depth) {
                depth = depth || 1;
                console.log('navigateBackward:' + depth);
            },
            navigateForward: function(depth) {
                depth = depth || 1;
                console.log('navigateForward:' + depth);
            },
            keyPress: function(event) {
                if (event.keyCode < 37 || event.keyCode > 40) {
                    return;
                }
                switch (event.keyCode) {
                    case 39:
                        console.log('key right down');
                        break;
                    case 37:
                        console.log('key left down');
                        break;
                    case 38:
                        console.log('key up down');
                        break;
                    case 40:
                        console.log('key bottom down');
                        break;
                }
                event.preventDefault();
            }
        };

        window.addEventListener("message", CommunicateEmbedded.receiveSourceMessage, false);
        window.addEventListener("load", CommunicateEmbedded.init, false);

        module.exports = CommunicateEmbedded;


        /***/
    },
    /* 1 */
    /***/
    function(module, exports, __webpack_require__) {

        module.exports = window;

        /***/
    }
    /******/
]);