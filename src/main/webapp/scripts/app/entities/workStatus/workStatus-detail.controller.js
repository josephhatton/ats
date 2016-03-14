'use strict';

angular.module('atsApp')
    .controller('WorkStatusDetailController', function ($scope, $rootScope, $stateParams, entity, WorkStatus) {
        $scope.workStatus = entity;
        $scope.load = function (id) {
            WorkStatus.get({id: id}, function(result) {
                $scope.workStatus = result;
            });
        };
        var unsubscribe = $rootScope.$on('atsApp:workStatusUpdate', function(event, result) {
            $scope.workStatus = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
