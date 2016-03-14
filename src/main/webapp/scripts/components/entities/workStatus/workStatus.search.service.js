'use strict';

angular.module('atsApp')
    .factory('WorkStatusSearch', function ($resource) {
        return $resource('api/_search/workStatuss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
