(function () {

    "use strict";


    angular.module('a4.about')
            .config(config);


    // @ngInject
    function config($stateProvider) {

        $stateProvider.state('about', {
            url: '/about',
            views: {
                "main": {
                    controller:   'AboutController',
                    controllerAs: 'vm',
                    templateUrl:  'about/about.tpl.html'
                }
            },
            data: {pageTitle: 'What is It?'}
        });

    }


})();
