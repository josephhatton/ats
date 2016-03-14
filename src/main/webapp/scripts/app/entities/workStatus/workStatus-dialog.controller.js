'use strict';

angular.module('atsApp').controller('WorkStatusDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'WorkStatus',
        function($scope, $stateParams, $uibModalInstance, entity, WorkStatus) {

        $scope.workStatus = entity;
        $scope.load = function(id) {
            WorkStatus.get({id : id}, function(result) {
                $scope.workStatus = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('atsApp:workStatusUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.workStatus.id != null) {
                WorkStatus.update($scope.workStatus, onSaveSuccess, onSaveError);
            } else {
                WorkStatus.save($scope.workStatus, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
