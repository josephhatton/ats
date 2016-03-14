'use strict';

angular.module('atsApp')
	.controller('WorkHistoryDeleteController', function($scope, $uibModalInstance, entity, WorkHistory) {

        $scope.workHistory = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            WorkHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
