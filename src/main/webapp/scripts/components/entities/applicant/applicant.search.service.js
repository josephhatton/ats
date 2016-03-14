'use strict';

angular.module('atsApp')
    .factory('ApplicantSearch', function ($resource) {
        return $resource('api/_search/applicants/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
