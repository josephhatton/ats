'use strict';

angular.module('atsApp').controller('CompanyStatusDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'CompanyStatus',
        function($scope, $stateParams, $uibModalInstance, entity, CompanyStatus) {

        $scope.companyStatus = entity;
        $scope.load = function(id) {
            CompanyStatus.get({id : id}, function(result) {
                $scope.companyStatus = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('atsApp:companyStatusUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.companyStatus.id != null) {
                CompanyStatus.update($scope.companyStatus, onSaveSuccess, onSaveError);
            } else {
                CompanyStatus.save($scope.companyStatus, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
