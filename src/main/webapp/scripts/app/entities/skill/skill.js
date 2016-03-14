'use strict';

angular.module('atsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('skill', {
                parent: 'entity',
                url: '/skills',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Skills'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/skill/skills.html',
                        controller: 'SkillController'
                    }
                },
                resolve: {
                }
            })
            .state('skill.detail', {
                parent: 'entity',
                url: '/skill/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Skill'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/skill/skill-detail.html',
                        controller: 'SkillDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Skill', function($stateParams, Skill) {
                        return Skill.get({id : $stateParams.id});
                    }]
                }
            })
            .state('skill.new', {
                parent: 'skill',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/skill/skill-dialog.html',
                        controller: 'SkillDialogController',
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
                        $state.go('skill', null, { reload: true });
                    }, function() {
                        $state.go('skill');
                    })
                }]
            })
            .state('skill.edit', {
                parent: 'skill',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/skill/skill-dialog.html',
                        controller: 'SkillDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Skill', function(Skill) {
                                return Skill.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('skill', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('skill.delete', {
                parent: 'skill',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/skill/skill-delete-dialog.html',
                        controller: 'SkillDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Skill', function(Skill) {
                                return Skill.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('skill', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
