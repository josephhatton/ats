'use strict';

angular.module('atsApp')
    .controller('ActivityActionController', function ($scope, $state, ActivityAction, ActivityActionSearch) {

        $scope.activityActions = [];
        $scope.loadAll = function() {
            ActivityAction.query(function(result) {
               $scope.activityActions = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            ActivityActionSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.activityActions = result;
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
            $scope.activityAction = {
                name: null,
                description: null,
                id: null
            };
        };
    });
