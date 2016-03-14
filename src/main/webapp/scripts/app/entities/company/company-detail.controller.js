'use strict';

angular.module('atsApp')
    .controller('CompanyDetailController', function ($scope, $rootScope, $stateParams, entity, Company) {
        $scope.company = entity;
        $scope.load = function (id) {
            Company.get({id: id}, function(result) {
                $scope.company = result;
            });
        };
        var unsubscribe = $rootScope.$on('atsApp:companyUpdate', function(event, result) {
            $scope.company = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
