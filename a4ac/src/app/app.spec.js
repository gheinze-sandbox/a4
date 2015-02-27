describe( 'AppController', function() {
    
  describe( 'isCurrentUrl', function() {
    var AppController, $location, $scope;

    beforeEach( module( 'a4' ) );

    beforeEach( inject( function( $controller, _$location_, $rootScope ) {
      $location = _$location_;
      $scope = $rootScope.$new();
      AppController = $controller( 'AppController', { $location: $location, $scope: $scope });
    }));

    // Note: a failure usually indicates a module dependency was not fulfilled
    it( 'should be created with all dependencies fulfilled', inject( function() {
      expect( AppController ).toBeTruthy();
    }));
    
  });
  
});
