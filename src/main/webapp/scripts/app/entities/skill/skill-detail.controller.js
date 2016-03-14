'use strict';

angular.module('atsApp')
    .controller('SkillDetailController', function ($scope, $rootScope, $stateParams, entity, Skill) {
        $scope.skill = entity;
        $scope.load = function (id) {
            Skill.get({id: id}, function(result) {
                $scope.skill = result;
            });
        };
        var unsubscribe = $rootScope.$on('atsApp:skillUpdate', function(event, result) {
            $scope.skill = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
