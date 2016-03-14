'use strict';

angular.module('atsApp')
    .factory('JobOrder', function ($resource, DateUtils) {
        return $resource('api/jobOrders/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
