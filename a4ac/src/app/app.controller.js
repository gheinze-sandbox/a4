(function() {

    "use strict";
    
    angular.module('a4')
        .controller('AppController', App);


    // @ngInject
    function App($scope) {

        // Using "controller as", $scope does not need to be injected
        var vm = this;

        $scope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
            if ( angular.isDefined( toState.data.pageTitle ) ) {
                vm.pageTitle = 'A4 ' + toState.data.pageTitle;
            }
        });
        
    }

})();

