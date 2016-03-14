'use strict';

angular.module('atsApp')
    .controller('ActivityActionDetailController', function ($scope, $rootScope, $stateParams, entity, ActivityAction) {
        $scope.activityAction = entity;
        $scope.load = function (id) {
            ActivityAction.get({id: id}, function(result) {
                $scope.activityAction = result;
            });
        };
        var unsubscribe = $rootScope.$on('atsApp:activityActionUpdate', function(event, result) {
            $scope.activityAction = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
