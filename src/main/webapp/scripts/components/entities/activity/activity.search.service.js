'use strict';

angular.module('atsApp')
    .factory('ActivitySearch', function ($resource) {
        return $resource('api/_search/activitys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
