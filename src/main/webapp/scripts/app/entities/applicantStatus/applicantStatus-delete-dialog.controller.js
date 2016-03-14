'use strict';

angular.module('atsApp')
	.controller('ApplicantStatusDeleteController', function($scope, $uibModalInstance, entity, ApplicantStatus) {

        $scope.applicantStatus = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ApplicantStatus.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
