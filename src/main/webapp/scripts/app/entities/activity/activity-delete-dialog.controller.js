'use strict';

angular.module('atsApp')
	.controller('ActivityDeleteController', function($scope, $uibModalInstance, entity, Activity) {

        $scope.activity = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Activity.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
