'use strict';

angular.module('atsApp')
    .controller('ActivityController', function ($scope, $state, Activity, ActivitySearch) {

        $scope.activitys = [];
        $scope.loadAll = function() {
            Activity.query(function(result) {
               $scope.activitys = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            ActivitySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.activitys = result;
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
            $scope.activity = {
                priority: null,
                startDate: null,
                status: null,
                comments: null,
                id: null
            };
        };
    });
