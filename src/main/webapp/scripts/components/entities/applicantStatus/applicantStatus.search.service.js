'use strict';

angular.module('atsApp')
    .factory('ApplicantStatusSearch', function ($resource) {
        return $resource('api/_search/applicantStatuss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
