'use strict';

angular.module('atsApp')
    .factory('JobTypeSearch', function ($resource) {
        return $resource('api/_search/jobTypes/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
