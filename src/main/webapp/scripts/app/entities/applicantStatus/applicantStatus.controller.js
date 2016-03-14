'use strict';

angular.module('atsApp')
    .controller('ApplicantStatusController', function ($scope, $state, ApplicantStatus, ApplicantStatusSearch) {

        $scope.applicantStatuss = [];
        $scope.loadAll = function() {
            ApplicantStatus.query(function(result) {
               $scope.applicantStatuss = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            ApplicantStatusSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.applicantStatuss = result;
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
            $scope.applicantStatus = {
                name: null,
                description: null,
                id: null
            };
        };
    });
