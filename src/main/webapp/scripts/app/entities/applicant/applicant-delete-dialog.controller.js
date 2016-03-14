'use strict';

angular.module('atsApp')
	.controller('ApplicantDeleteController', function($scope, $uibModalInstance, entity, Applicant) {

        $scope.applicant = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Applicant.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
