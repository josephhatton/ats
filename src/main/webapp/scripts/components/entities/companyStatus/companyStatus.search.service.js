'use strict';

angular.module('atsApp')
    .factory('CompanyStatusSearch', function ($resource) {
        return $resource('api/_search/companyStatuss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
