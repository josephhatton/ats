'use strict';

angular.module('atsApp')
    .controller('JobStatusController', function ($scope, $state, JobStatus, JobStatusSearch) {

        $scope.jobStatuss = [];
        $scope.loadAll = function() {
            JobStatus.query(function(result) {
               $scope.jobStatuss = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            JobStatusSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.jobStatuss = result;
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
            $scope.jobStatus = {
                name: null,
                description: null,
                id: null
            };
        };
    });
