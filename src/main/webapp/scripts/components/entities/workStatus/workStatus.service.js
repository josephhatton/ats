'use strict';

angular.module('atsApp')
    .factory('WorkStatus', function ($resource, DateUtils) {
        return $resource('api/workStatuss/:id', {}, {
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
