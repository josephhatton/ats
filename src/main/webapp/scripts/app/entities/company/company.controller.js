'use strict';

angular.module('atsApp')
    .controller('CompanyController', function ($scope, $state, Company, CompanySearch, ParseLinks) {

        $scope.companys = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            CompanySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.companys = result;
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
            $scope.company = {
                name: null,
                website: null,
                industry: null,
                revenue: null,
                employees: null,
                isDeleted: null,
                id: null
            };
        };
    });
