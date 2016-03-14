'use strict';

angular.module('atsApp').controller('JobOrderDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'JobOrder',
        function($scope, $stateParams, $uibModalInstance, entity, JobOrder) {

        $scope.jobOrder = entity;
        $scope.load = function(id) {
            JobOrder.get({id : id}, function(result) {
                $scope.jobOrder = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('atsApp:jobOrderUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.jobOrder.id != null) {
                JobOrder.update($scope.jobOrder, onSaveSuccess, onSaveError);
            } else {
                JobOrder.save($scope.jobOrder, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
