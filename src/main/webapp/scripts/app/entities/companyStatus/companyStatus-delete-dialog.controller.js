'use strict';

angular.module('atsApp')
	.controller('CompanyStatusDeleteController', function($scope, $uibModalInstance, entity, CompanyStatus) {

        $scope.companyStatus = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            CompanyStatus.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
