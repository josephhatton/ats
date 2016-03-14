'use strict';

angular.module('atsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('activityAction', {
                parent: 'entity',
                url: '/activityActions',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ActivityActions'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/activityAction/activityActions.html',
                        controller: 'ActivityActionController'
                    }
                },
                resolve: {
                }
            })
            .state('activityAction.detail', {
                parent: 'entity',
                url: '/activityAction/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ActivityAction'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/activityAction/activityAction-detail.html',
                        controller: 'ActivityActionDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'ActivityAction', function($stateParams, ActivityAction) {
                        return ActivityAction.get({id : $stateParams.id});
                    }]
                }
            })
            .state('activityAction.new', {
                parent: 'activityAction',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/activityAction/activityAction-dialog.html',
                        controller: 'ActivityActionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('activityAction', null, { reload: true });
                    }, function() {
                        $state.go('activityAction');
                    })
                }]
            })
            .state('activityAction.edit', {
                parent: 'activityAction',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/activityAction/activityAction-dialog.html',
                        controller: 'ActivityActionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ActivityAction', function(ActivityAction) {
                                return ActivityAction.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('activityAction', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('activityAction.delete', {
                parent: 'activityAction',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/activityAction/activityAction-delete-dialog.html',
                        controller: 'ActivityActionDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ActivityAction', function(ActivityAction) {
                                return ActivityAction.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('activityAction', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
