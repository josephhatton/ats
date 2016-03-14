'use strict';

angular.module('atsApp')
	.controller('AddressDeleteController', function($scope, $uibModalInstance, entity, Address) {

        $scope.address = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Address.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
