'use strict';

angular.module('atsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('skillCategory', {
                parent: 'entity',
                url: '/skillCategorys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'SkillCategorys'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/skillCategory/skillCategorys.html',
                        controller: 'SkillCategoryController'
                    }
                },
                resolve: {
                }
            })
            .state('skillCategory.detail', {
                parent: 'entity',
                url: '/skillCategory/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'SkillCategory'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/skillCategory/skillCategory-detail.html',
                        controller: 'SkillCategoryDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'SkillCategory', function($stateParams, SkillCategory) {
                        return SkillCategory.get({id : $stateParams.id});
                    }]
                }
            })
            .state('skillCategory.new', {
                parent: 'skillCategory',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/skillCategory/skillCategory-dialog.html',
                        controller: 'SkillCategoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('skillCategory', null, { reload: true });
                    }, function() {
                        $state.go('skillCategory');
                    })
                }]
            })
            .state('skillCategory.edit', {
                parent: 'skillCategory',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/skillCategory/skillCategory-dialog.html',
                        controller: 'SkillCategoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['SkillCategory', function(SkillCategory) {
                                return SkillCategory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('skillCategory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('skillCategory.delete', {
                parent: 'skillCategory',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/skillCategory/skillCategory-delete-dialog.html',
                        controller: 'SkillCategoryDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['SkillCategory', function(SkillCategory) {
                                return SkillCategory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('skillCategory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
