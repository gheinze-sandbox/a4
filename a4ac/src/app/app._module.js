(function() {

    "use strict";
    
    angular.module('a4', [
      'templates-app',
      'templates-common',
      'a4.messages',
      'a4.home',
      'a4.about',
      'ngSanitize', // required for dialog.main module
      'security',
      'ui.router'
    ]);

})();
