'use strict';

angular.module('atsApp')
    .controller('WorkHistoryDetailController', function ($scope, $rootScope, $stateParams, entity, WorkHistory) {
        $scope.workHistory = entity;
        $scope.load = function (id) {
            WorkHistory.get({id: id}, function(result) {
                $scope.workHistory = result;
            });
        };
        var unsubscribe = $rootScope.$on('atsApp:workHistoryUpdate', function(event, result) {
            $scope.workHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
