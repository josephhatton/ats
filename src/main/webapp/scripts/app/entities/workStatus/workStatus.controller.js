'use strict';

angular.module('atsApp')
    .controller('WorkStatusController', function ($scope, $state, WorkStatus, WorkStatusSearch) {

        $scope.workStatuss = [];
        $scope.loadAll = function() {
            WorkStatus.query(function(result) {
               $scope.workStatuss = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            WorkStatusSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.workStatuss = result;
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
            $scope.workStatus = {
                name: null,
                description: null,
                id: null
            };
        };
    });
