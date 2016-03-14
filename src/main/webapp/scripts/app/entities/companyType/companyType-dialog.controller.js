'use strict';

angular.module('atsApp').controller('CompanyTypeDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'CompanyType',
        function($scope, $stateParams, $uibModalInstance, entity, CompanyType) {

        $scope.companyType = entity;
        $scope.load = function(id) {
            CompanyType.get({id : id}, function(result) {
                $scope.companyType = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('atsApp:companyTypeUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.companyType.id != null) {
                CompanyType.update($scope.companyType, onSaveSuccess, onSaveError);
            } else {
                CompanyType.save($scope.companyType, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
