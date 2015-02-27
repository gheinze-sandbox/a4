(function() {
    
    // @ngInject
    function SecurityService($http, $q, $location, securityRetryQueue, dialogs) {

        // Information about the current user
        var currentUser = null;
        
        var service = {};
        
        service.getLoginReason = getLoginReason;     // Get the first reason for needing a login
        service.showLogin = showLogin;               // Show the modal login dialog
        service.openLoginDialog = openLoginDialog;
        service.login= login;                        // Attempt to authenticate a user with the provided credentials
        service.cancelLogin = cancelLogin;           // Give up trying to login and clear the retry queue
        service.logout = logout;                     // Logout the current user and redirect
        service.requestCurrentUser = requestCurrentUser; // Ask the backend to see if a user is already authenticated - this may be from a previous session.
        service.currentUser = currentUser;           // Information about the current user
        service.isAuthenticated = isAuthenticated;   // Is the current user authenticated?
        service.isAdmin = isAdmin;                   // Is the current user an adminstrator?

        return service;
      
        
        // ------------------------------------------------
        // Private methods
        // ------------------------------------------------
        
        // Redirect to the given url (defaults to '/')
        function _redirect(url) {
            url = url || '/';
            $location.path(url);
        }


        // Register a handler for when an item is added to the retry queue
        securityRetryQueue.onItemAddedCallbacks.push(function (retryItem) {
            if (securityRetryQueue.hasMore()) {
                service.showLogin();
            }
        });




        // ------------------------------------------------
        // Public API: Login form dialog support
        // ------------------------------------------------
        
        var _loginDialog = null;
        
        
        function openLoginDialog() {
            if (_loginDialog) {
                throw new Error('Trying to open a dialog that is already open!');
            }
            _loginDialog = dialogs.create(
                    'security/login/loginForm.tpl.html',
                    'LoginFormController',
                    {},
                    {size: 'lg', keyboard: true, backdrop: false, windowClass: 'my-class'}
            );
            _loginDialog.result.then(onLoginDialogClose);
        }
        
        
        function closeLoginDialog(success) {
            if (_loginDialog) {
                _loginDialog.close(success);
            }
        }
        
        
        function onLoginDialogClose(success) {
            _loginDialog = null;
            if (success) {
                securityRetryQueue.retryAll();
            } else {
                securityRetryQueue.cancelAll();
                _redirect();
            }
        }


        // ------------------------------------------------
        // Public API functions
        // ------------------------------------------------
        
        
        // Get the first reason for needing a login
        function getLoginReason() {
            return securityRetryQueue.retryReason();
        }
        
        
        // Show the modal login dialog
        function showLogin() {
            openLoginDialog();
        }
         
        
        // Attempt to authenticate a user with the provided credentials
        function login(email, password) {
            var request = $http.post('/login', {email: email, password: password});
            return request.then(function (response) {
                service.currentUser = response.data.user;
                if (service.isAuthenticated()) {
                    closeLoginDialog(true);
                }
                return service.isAuthenticated();
            });
        }
         
        
            // Give up trying to login and clear the retry queue
        function cancelLogin() {
            closeLoginDialog(false);
            _redirect();
        }
         
        
        // Logout the current user and redirect
        function logout(redirectTo) {
            $http.post('/logout').then(function () {
                service.currentUser = null;
                _redirect(redirectTo);
            });
        }
            
            
        // Ask the backend to see if a user is already authenticated - this may be from a previous session.
        function requestCurrentUser() {
            if (service.isAuthenticated()) {
                return $q.when(service.currentUser);
            } else {
                return $http.get('/current-user').then(function (response) {
                    service.currentUser = response.data.user;
                    return service.currentUser;
                });
            }
        }
         
        
        
        // Is the current user authenticated?
        function isAuthenticated() {
            return !!service.currentUser;
        }

        
        // Is the current user an adminstrator?
        function isAdmin() {
            return !!(service.currentUser && service.currentUser.admin);
        }
            

    }

    
    // Define a service named "security"
    angular
        .module('security.service')
        .factory('security', SecurityService);


})();