'use strict';

angular.module('atsApp')
    .controller('CompanyStatusDetailController', function ($scope, $rootScope, $stateParams, entity, CompanyStatus) {
        $scope.companyStatus = entity;
        $scope.load = function (id) {
            CompanyStatus.get({id: id}, function(result) {
                $scope.companyStatus = result;
            });
        };
        var unsubscribe = $rootScope.$on('atsApp:companyStatusUpdate', function(event, result) {
            $scope.companyStatus = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
