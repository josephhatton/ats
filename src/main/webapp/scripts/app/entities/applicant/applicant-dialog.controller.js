'use strict';

angular.module('atsApp').controller('ApplicantDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Applicant',
        function($scope, $stateParams, $uibModalInstance, entity, Applicant) {

        $scope.applicant = entity;
        $scope.load = function(id) {
            Applicant.get({id : id}, function(result) {
                $scope.applicant = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('atsApp:applicantUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.applicant.id != null) {
                Applicant.update($scope.applicant, onSaveSuccess, onSaveError);
            } else {
                Applicant.save($scope.applicant, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
