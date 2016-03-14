'use strict';

angular.module('atsApp')
    .factory('Applicant', function ($resource, DateUtils) {
        return $resource('api/applicants/:id', {}, {
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
