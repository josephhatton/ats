'use strict';

angular.module('atsApp')
    .controller('ActivityDetailController', function ($scope, $rootScope, $stateParams, entity, Activity) {
        $scope.activity = entity;
        $scope.load = function (id) {
            Activity.get({id: id}, function(result) {
                $scope.activity = result;
            });
        };
        var unsubscribe = $rootScope.$on('atsApp:activityUpdate', function(event, result) {
            $scope.activity = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
