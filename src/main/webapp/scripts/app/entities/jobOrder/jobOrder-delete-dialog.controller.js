'use strict';

angular.module('atsApp')
	.controller('JobOrderDeleteController', function($scope, $uibModalInstance, entity, JobOrder) {

        $scope.jobOrder = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            JobOrder.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
