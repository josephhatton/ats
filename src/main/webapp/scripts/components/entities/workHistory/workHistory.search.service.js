'use strict';

angular.module('atsApp')
    .factory('WorkHistorySearch', function ($resource) {
        return $resource('api/_search/workHistorys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
