'use strict';

angular.module('atsApp')
	.controller('ActivityActionDeleteController', function($scope, $uibModalInstance, entity, ActivityAction) {

        $scope.activityAction = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ActivityAction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
