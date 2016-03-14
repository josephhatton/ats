'use strict';

angular.module('atsApp').controller('JobStatusDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'JobStatus',
        function($scope, $stateParams, $uibModalInstance, entity, JobStatus) {

        $scope.jobStatus = entity;
        $scope.load = function(id) {
            JobStatus.get({id : id}, function(result) {
                $scope.jobStatus = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('atsApp:jobStatusUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.jobStatus.id != null) {
                JobStatus.update($scope.jobStatus, onSaveSuccess, onSaveError);
            } else {
                JobStatus.save($scope.jobStatus, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
