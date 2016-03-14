'use strict';

angular.module('atsApp')
    .controller('SkillCategoryController', function ($scope, $state, SkillCategory, SkillCategorySearch) {

        $scope.skillCategorys = [];
        $scope.loadAll = function() {
            SkillCategory.query(function(result) {
               $scope.skillCategorys = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            SkillCategorySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.skillCategorys = result;
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
            $scope.skillCategory = {
                name: null,
                id: null
            };
        };
    });
