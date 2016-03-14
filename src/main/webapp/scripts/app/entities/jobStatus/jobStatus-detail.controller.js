'use strict';

angular.module('atsApp')
    .controller('JobStatusDetailController', function ($scope, $rootScope, $stateParams, entity, JobStatus) {
        $scope.jobStatus = entity;
        $scope.load = function (id) {
            JobStatus.get({id: id}, function(result) {
                $scope.jobStatus = result;
            });
        };
        var unsubscribe = $rootScope.$on('atsApp:jobStatusUpdate', function(event, result) {
            $scope.jobStatus = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
