'use strict';

angular.module('atsApp')
    .controller('AddressDetailController', function ($scope, $rootScope, $stateParams, entity, Address) {
        $scope.address = entity;
        $scope.load = function (id) {
            Address.get({id: id}, function(result) {
                $scope.address = result;
            });
        };
        var unsubscribe = $rootScope.$on('atsApp:addressUpdate', function(event, result) {
            $scope.address = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
