'use strict';

angular.module('atsApp')
    .controller('JobOrderController', function ($scope, $state, JobOrder, JobOrderSearch, ParseLinks) {

        $scope.jobOrders = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            JobOrderSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.jobOrders = result;
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
            $scope.jobOrder = {
                title: null,
                duration: null,
                description: null,
                id: null
            };
        };
    });
