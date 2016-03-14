'use strict';

angular.module('atsApp')
	.controller('WorkStatusDeleteController', function($scope, $uibModalInstance, entity, WorkStatus) {

        $scope.workStatus = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            WorkStatus.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
