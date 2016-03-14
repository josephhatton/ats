'use strict';

angular.module('atsApp')
    .factory('CompanyTypeSearch', function ($resource) {
        return $resource('api/_search/companyTypes/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
