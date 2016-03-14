'use strict';

angular.module('atsApp')
    .controller('SkillCategoryDetailController', function ($scope, $rootScope, $stateParams, entity, SkillCategory) {
        $scope.skillCategory = entity;
        $scope.load = function (id) {
            SkillCategory.get({id: id}, function(result) {
                $scope.skillCategory = result;
            });
        };
        var unsubscribe = $rootScope.$on('atsApp:skillCategoryUpdate', function(event, result) {
            $scope.skillCategory = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
