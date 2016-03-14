'use strict';

angular.module('atsApp')
    .controller('JobTypeDetailController', function ($scope, $rootScope, $stateParams, entity, JobType) {
        $scope.jobType = entity;
        $scope.load = function (id) {
            JobType.get({id: id}, function(result) {
                $scope.jobType = result;
            });
        };
        var unsubscribe = $rootScope.$on('atsApp:jobTypeUpdate', function(event, result) {
            $scope.jobType = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
