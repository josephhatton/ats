'use strict';

angular.module('atsApp')
    .controller('ApplicantDetailController', function ($scope, $rootScope, $stateParams, entity, Applicant) {
        $scope.applicant = entity;
        $scope.load = function (id) {
            Applicant.get({id: id}, function(result) {
                $scope.applicant = result;
            });
        };
        var unsubscribe = $rootScope.$on('atsApp:applicantUpdate', function(event, result) {
            $scope.applicant = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
