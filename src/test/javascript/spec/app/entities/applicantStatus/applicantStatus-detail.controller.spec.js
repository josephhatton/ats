'use strict';

describe('Controller Tests', function() {

    describe('ApplicantStatus Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockApplicantStatus;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockApplicantStatus = jasmine.createSpy('MockApplicantStatus');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ApplicantStatus': MockApplicantStatus
            };
            createController = function() {
                $injector.get('$controller')("ApplicantStatusDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'atsApp:applicantStatusUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
