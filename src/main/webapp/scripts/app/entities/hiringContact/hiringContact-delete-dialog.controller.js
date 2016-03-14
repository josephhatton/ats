'use strict';

angular.module('atsApp')
	.controller('HiringContactDeleteController', function($scope, $uibModalInstance, entity, HiringContact) {

        $scope.hiringContact = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            HiringContact.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
