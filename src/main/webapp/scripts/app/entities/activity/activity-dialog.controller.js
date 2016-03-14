'use strict';

angular.module('atsApp').controller('ActivityDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Activity',
        function($scope, $stateParams, $uibModalInstance, entity, Activity) {

        $scope.activity = entity;
        $scope.load = function(id) {
            Activity.get({id : id}, function(result) {
                $scope.activity = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('atsApp:activityUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.activity.id != null) {
                Activity.update($scope.activity, onSaveSuccess, onSaveError);
            } else {
                Activity.save($scope.activity, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForStartDate = {};

        $scope.datePickerForStartDate.status = {
            opened: false
        };

        $scope.datePickerForStartDateOpen = function($event) {
            $scope.datePickerForStartDate.status.opened = true;
        };
}]);
