'use strict';

angular.module('atsApp')
    .controller('HiringContactDetailController', function ($scope, $rootScope, $stateParams, entity, HiringContact) {
        $scope.hiringContact = entity;
        $scope.load = function (id) {
            HiringContact.get({id: id}, function(result) {
                $scope.hiringContact = result;
            });
        };
        var unsubscribe = $rootScope.$on('atsApp:hiringContactUpdate', function(event, result) {
            $scope.hiringContact = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
