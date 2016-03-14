'use strict';

angular.module('atsApp')
	.controller('JobStatusDeleteController', function($scope, $uibModalInstance, entity, JobStatus) {

        $scope.jobStatus = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            JobStatus.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
