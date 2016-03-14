'use strict';

angular.module('atsApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


