(function() {

    "use strict";
    

    angular.module('a4.about')
        .controller('AboutController', About);


    function About() {

        // Using "controller as", $scope does not need to be injected
        var vm = this;
        
        vm.dropdownDemoItems = [
          "The first choice!",
          "And another choice for you.",
          "but wait! A third!"
        ];
        
    }


})();
