'use strict';

angular.module('atsApp')
	.controller('JobTypeDeleteController', function($scope, $uibModalInstance, entity, JobType) {

        $scope.jobType = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            JobType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
