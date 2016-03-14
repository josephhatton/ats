'use strict';

angular.module('atsApp')
    .controller('CompanyTypeDetailController', function ($scope, $rootScope, $stateParams, entity, CompanyType) {
        $scope.companyType = entity;
        $scope.load = function (id) {
            CompanyType.get({id: id}, function(result) {
                $scope.companyType = result;
            });
        };
        var unsubscribe = $rootScope.$on('atsApp:companyTypeUpdate', function(event, result) {
            $scope.companyType = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
