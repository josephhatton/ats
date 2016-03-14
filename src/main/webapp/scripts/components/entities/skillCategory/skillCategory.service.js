'use strict';

angular.module('atsApp')
    .factory('SkillCategory', function ($resource, DateUtils) {
        return $resource('api/skillCategorys/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
