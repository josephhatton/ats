'use strict';

angular.module('atsApp').controller('ApplicantStatusDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ApplicantStatus',
        function($scope, $stateParams, $uibModalInstance, entity, ApplicantStatus) {

        $scope.applicantStatus = entity;
        $scope.load = function(id) {
            ApplicantStatus.get({id : id}, function(result) {
                $scope.applicantStatus = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('atsApp:applicantStatusUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.applicantStatus.id != null) {
                ApplicantStatus.update($scope.applicantStatus, onSaveSuccess, onSaveError);
            } else {
                ApplicantStatus.save($scope.applicantStatus, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
