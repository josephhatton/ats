'use strict';

describe('Controller Tests', function() {

    describe('ActivityAction Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockActivityAction;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockActivityAction = jasmine.createSpy('MockActivityAction');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ActivityAction': MockActivityAction
            };
            createController = function() {
                $injector.get('$controller')("ActivityActionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'atsApp:activityActionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
