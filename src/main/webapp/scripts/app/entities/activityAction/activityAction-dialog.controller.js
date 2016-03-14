'use strict';

angular.module('atsApp').controller('ActivityActionDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ActivityAction',
        function($scope, $stateParams, $uibModalInstance, entity, ActivityAction) {

        $scope.activityAction = entity;
        $scope.load = function(id) {
            ActivityAction.get({id : id}, function(result) {
                $scope.activityAction = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('atsApp:activityActionUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.activityAction.id != null) {
                ActivityAction.update($scope.activityAction, onSaveSuccess, onSaveError);
            } else {
                ActivityAction.save($scope.activityAction, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
