'use strict';

angular.module('atsApp')
    .controller('CompanyStatusController', function ($scope, $state, CompanyStatus, CompanyStatusSearch) {

        $scope.companyStatuss = [];
        $scope.loadAll = function() {
            CompanyStatus.query(function(result) {
               $scope.companyStatuss = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            CompanyStatusSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.companyStatuss = result;
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
            $scope.companyStatus = {
                name: null,
                description: null,
                id: null
            };
        };
    });
