'use strict';

angular.module('atsApp')
    .controller('SkillController', function ($scope, $state, Skill, SkillSearch) {

        $scope.skills = [];
        $scope.loadAll = function() {
            Skill.query(function(result) {
               $scope.skills = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            SkillSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.skills = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.skill = {
                name: null,
                id: null
            };
        };
    });
