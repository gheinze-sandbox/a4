/*
 * Assumes the constant "MESSAGES", an array, is defined for the app and will
 * find the associated message for the given key, replacing bind variables in
 * the message with the provided context.
 */
angular.module('services.localizedMessages', []).factory('localizedMessages', ['$interpolate', 'MESSAGES', function ($interpolate, messages) {

  var handleNotFound = function (msg, msgKey) {
    return msg || '?' + msgKey + '?';
  };

  return {
    get : function (msgKey, interpolateParams) {
      var msg =  messages[msgKey];
      if (msg) {
        return $interpolate(msg)(interpolateParams);
      } else {
        return handleNotFound(msg, msgKey);
      }
    }
  };
}]);