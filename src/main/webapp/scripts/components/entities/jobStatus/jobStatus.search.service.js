'use strict';

angular.module('atsApp')
    .factory('JobStatusSearch', function ($resource) {
        return $resource('api/_search/jobStatuss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
