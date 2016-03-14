'use strict';

angular.module('atsApp')
	.controller('CompanyTypeDeleteController', function($scope, $uibModalInstance, entity, CompanyType) {

        $scope.companyType = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            CompanyType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
