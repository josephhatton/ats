'use strict';

angular.module('atsApp')
    .controller('ApplicantStatusDetailController', function ($scope, $rootScope, $stateParams, entity, ApplicantStatus) {
        $scope.applicantStatus = entity;
        $scope.load = function (id) {
            ApplicantStatus.get({id: id}, function(result) {
                $scope.applicantStatus = result;
            });
        };
        var unsubscribe = $rootScope.$on('atsApp:applicantStatusUpdate', function(event, result) {
            $scope.applicantStatus = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
