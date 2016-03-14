'use strict';

angular.module('atsApp').controller('WorkHistoryDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'WorkHistory',
        function($scope, $stateParams, $uibModalInstance, entity, WorkHistory) {

        $scope.workHistory = entity;
        $scope.load = function(id) {
            WorkHistory.get({id : id}, function(result) {
                $scope.workHistory = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('atsApp:workHistoryUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.workHistory.id != null) {
                WorkHistory.update($scope.workHistory, onSaveSuccess, onSaveError);
            } else {
                WorkHistory.save($scope.workHistory, onSaveSuccess, onSaveError);
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
        $scope.datePickerForEndDate = {};

        $scope.datePickerForEndDate.status = {
            opened: false
        };

        $scope.datePickerForEndDateOpen = function($event) {
            $scope.datePickerForEndDate.status.opened = true;
        };
}]);
