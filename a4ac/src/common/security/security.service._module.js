(function() {
    
    // Based loosely around work by Witold Szczerba - https://github.com/witoldsz/angular-http-auth
    angular.module('security.service', [
        'security.retryQueue', // Keeps track of failed requests that need to be retried once the user logs in
        'security.login',      // Contains the login form template and controller
        'dialogs.main'         // Used to display the login form as a modal dialog.
    ]);

})();