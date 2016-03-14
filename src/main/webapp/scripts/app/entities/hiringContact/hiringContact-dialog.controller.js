'use strict';

angular.module('atsApp').controller('HiringContactDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'HiringContact',
        function($scope, $stateParams, $uibModalInstance, entity, HiringContact) {

        $scope.hiringContact = entity;
        $scope.load = function(id) {
            HiringContact.get({id : id}, function(result) {
                $scope.hiringContact = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('atsApp:hiringContactUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.hiringContact.id != null) {
                HiringContact.update($scope.hiringContact, onSaveSuccess, onSaveError);
            } else {
                HiringContact.save($scope.hiringContact, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
