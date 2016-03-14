'use strict';

angular.module('atsApp').controller('JobTypeDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'JobType',
        function($scope, $stateParams, $uibModalInstance, entity, JobType) {

        $scope.jobType = entity;
        $scope.load = function(id) {
            JobType.get({id : id}, function(result) {
                $scope.jobType = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('atsApp:jobTypeUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.jobType.id != null) {
                JobType.update($scope.jobType, onSaveSuccess, onSaveError);
            } else {
                JobType.save($scope.jobType, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
