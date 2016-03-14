'use strict';

angular.module('atsApp')
    .controller('HiringContactController', function ($scope, $state, HiringContact, HiringContactSearch) {

        $scope.hiringContacts = [];
        $scope.loadAll = function() {
            HiringContact.query(function(result) {
               $scope.hiringContacts = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            HiringContactSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.hiringContacts = result;
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
            $scope.hiringContact = {
                firstName: null,
                lastName: null,
                nickName: null,
                phone1: null,
                phone2: null,
                email1: null,
                jobTitle: null,
                referralSource: null,
                contactType: null,
                email2: null,
                middleName: null,
                id: null
            };
        };
    });
