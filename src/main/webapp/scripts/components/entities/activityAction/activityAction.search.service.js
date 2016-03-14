'use strict';

angular.module('atsApp')
    .factory('ActivityActionSearch', function ($resource) {
        return $resource('api/_search/activityActions/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
