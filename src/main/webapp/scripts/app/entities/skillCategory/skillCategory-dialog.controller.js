'use strict';

angular.module('atsApp').controller('SkillCategoryDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'SkillCategory',
        function($scope, $stateParams, $uibModalInstance, entity, SkillCategory) {

        $scope.skillCategory = entity;
        $scope.load = function(id) {
            SkillCategory.get({id : id}, function(result) {
                $scope.skillCategory = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('atsApp:skillCategoryUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.skillCategory.id != null) {
                SkillCategory.update($scope.skillCategory, onSaveSuccess, onSaveError);
            } else {
                SkillCategory.save($scope.skillCategory, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
