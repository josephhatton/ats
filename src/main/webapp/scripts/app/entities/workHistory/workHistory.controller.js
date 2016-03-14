'use strict';

angular.module('atsApp')
    .controller('WorkHistoryController', function ($scope, $state, WorkHistory, WorkHistorySearch) {

        $scope.workHistorys = [];
        $scope.loadAll = function() {
            WorkHistory.query(function(result) {
               $scope.workHistorys = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            WorkHistorySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.workHistorys = result;
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
            $scope.workHistory = {
                company: null,
                title: null,
                startDate: null,
                endDate: null,
                startingCompensation: null,
                endingCompensation: null,
                compensationType: null,
                supervisor: null,
                supervisorTitle: null,
                supervisorPhone: null,
                duties: null,
                reasonForLeaving: null,
                id: null
            };
        };
    });
