(function() {

    "use strict";
    
    angular.module('a4')
        .config(config);

    // @ngInject
    function config($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise( '/home' );
    }


})();

