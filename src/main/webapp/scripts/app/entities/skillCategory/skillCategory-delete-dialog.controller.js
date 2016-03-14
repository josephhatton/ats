'use strict';

angular.module('atsApp')
	.controller('SkillCategoryDeleteController', function($scope, $uibModalInstance, entity, SkillCategory) {

        $scope.skillCategory = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            SkillCategory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
