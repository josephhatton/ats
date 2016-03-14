'use strict';

describe('Controller Tests', function() {

    describe('HiringContact Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockHiringContact;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockHiringContact = jasmine.createSpy('MockHiringContact');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'HiringContact': MockHiringContact
            };
            createController = function() {
                $injector.get('$controller')("HiringContactDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'atsApp:hiringContactUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
