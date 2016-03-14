'use strict';

angular.module('atsApp')
    .factory('HiringContactSearch', function ($resource) {
        return $resource('api/_search/hiringContacts/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
