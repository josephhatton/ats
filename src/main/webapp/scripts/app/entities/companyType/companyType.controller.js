'use strict';

angular.module('atsApp')
    .controller('CompanyTypeController', function ($scope, $state, CompanyType, CompanyTypeSearch) {

        $scope.companyTypes = [];
        $scope.loadAll = function() {
            CompanyType.query(function(result) {
               $scope.companyTypes = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            CompanyTypeSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.companyTypes = result;
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
            $scope.companyType = {
                name: null,
                description: null,
                id: null
            };
        };
    });
