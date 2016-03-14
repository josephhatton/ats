'use strict';

angular.module('atsApp')
    .factory('SkillCategorySearch', function ($resource) {
        return $resource('api/_search/skillCategorys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
