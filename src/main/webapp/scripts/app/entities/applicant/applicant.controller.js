'use strict';

angular.module('atsApp')
    .controller('ApplicantController', function ($scope, $state, Applicant, ApplicantSearch, ParseLinks) {

        $scope.applicants = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            ApplicantSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.applicants = result;
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
            $scope.applicant = {
                firstName: null,
                middleName: null,
                lastName: null,
                title: null,
                email1: null,
                homePhone: null,
                cellPhone: null,
                isDeleted: null,
                nickName: null,
                workPhone: null,
                email2: null,
                id: null
            };
        };
    });
