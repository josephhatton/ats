'use strict';

angular.module('atsApp')
    .factory('JobType', function ($resource, DateUtils) {
        return $resource('api/jobTypes/:id', {}, {
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
