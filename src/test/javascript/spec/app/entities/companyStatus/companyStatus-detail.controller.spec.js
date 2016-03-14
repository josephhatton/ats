'use strict';

describe('Controller Tests', function() {

    describe('CompanyStatus Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCompanyStatus;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCompanyStatus = jasmine.createSpy('MockCompanyStatus');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'CompanyStatus': MockCompanyStatus
            };
            createController = function() {
                $injector.get('$controller')("CompanyStatusDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'atsApp:companyStatusUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
