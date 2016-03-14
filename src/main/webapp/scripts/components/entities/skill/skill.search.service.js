'use strict';

angular.module('atsApp')
    .factory('SkillSearch', function ($resource) {
        return $resource('api/_search/skills/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
