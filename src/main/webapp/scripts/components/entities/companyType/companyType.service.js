'use strict';

angular.module('atsApp')
    .factory('CompanyType', function ($resource, DateUtils) {
        return $resource('api/companyTypes/:id', {}, {
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
