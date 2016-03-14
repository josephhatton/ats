'use strict';

angular.module('atsApp')
    .factory('ApplicantStatus', function ($resource, DateUtils) {
        return $resource('api/applicantStatuss/:id', {}, {
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
