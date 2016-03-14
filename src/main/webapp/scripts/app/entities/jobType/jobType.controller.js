'use strict';

angular.module('atsApp')
    .controller('JobTypeController', function ($scope, $state, JobType, JobTypeSearch) {

        $scope.jobTypes = [];
        $scope.loadAll = function() {
            JobType.query(function(result) {
               $scope.jobTypes = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            JobTypeSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.jobTypes = result;
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
            $scope.jobType = {
                name: null,
                description: null,
                id: null
            };
        };
    });
