'use strict';

angular.module('atsApp')
    .controller('JobOrderDetailController', function ($scope, $rootScope, $stateParams, entity, JobOrder) {
        $scope.jobOrder = entity;
        $scope.load = function (id) {
            JobOrder.get({id: id}, function(result) {
                $scope.jobOrder = result;
            });
        };
        var unsubscribe = $rootScope.$on('atsApp:jobOrderUpdate', function(event, result) {
            $scope.jobOrder = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
