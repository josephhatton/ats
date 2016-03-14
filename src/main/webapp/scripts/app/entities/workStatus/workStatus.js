'use strict';

angular.module('atsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('workStatus', {
                parent: 'entity',
                url: '/workStatuss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'WorkStatuss'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/workStatus/workStatuss.html',
                        controller: 'WorkStatusController'
                    }
                },
                resolve: {
                }
            })
            .state('workStatus.detail', {
                parent: 'entity',
                url: '/workStatus/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'WorkStatus'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/workStatus/workStatus-detail.html',
                        controller: 'WorkStatusDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'WorkStatus', function($stateParams, WorkStatus) {
                        return WorkStatus.get({id : $stateParams.id});
                    }]
                }
            })
            .state('workStatus.new', {
                parent: 'workStatus',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/workStatus/workStatus-dialog.html',
                        controller: 'WorkStatusDialogController',
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
                        $state.go('workStatus', null, { reload: true });
                    }, function() {
                        $state.go('workStatus');
                    })
                }]
            })
            .state('workStatus.edit', {
                parent: 'workStatus',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/workStatus/workStatus-dialog.html',
                        controller: 'WorkStatusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['WorkStatus', function(WorkStatus) {
                                return WorkStatus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('workStatus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('workStatus.delete', {
                parent: 'workStatus',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/workStatus/workStatus-delete-dialog.html',
                        controller: 'WorkStatusDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['WorkStatus', function(WorkStatus) {
                                return WorkStatus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('workStatus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
