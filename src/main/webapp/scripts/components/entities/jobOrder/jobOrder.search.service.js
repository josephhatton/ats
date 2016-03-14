'use strict';

angular.module('atsApp')
    .factory('JobOrderSearch', function ($resource) {
        return $resource('api/_search/jobOrders/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
