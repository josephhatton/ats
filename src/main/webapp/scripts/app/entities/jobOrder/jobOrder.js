'use strict';

angular.module('atsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('jobOrder', {
                parent: 'entity',
                url: '/jobOrders',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'JobOrders'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/jobOrder/jobOrders.html',
                        controller: 'JobOrderController'
                    }
                },
                resolve: {
                }
            })
            .state('jobOrder.detail', {
                parent: 'entity',
                url: '/jobOrder/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'JobOrder'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/jobOrder/jobOrder-detail.html',
                        controller: 'JobOrderDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'JobOrder', function($stateParams, JobOrder) {
                        return JobOrder.get({id : $stateParams.id});
                    }]
                }
            })
            .state('jobOrder.new', {
                parent: 'jobOrder',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/jobOrder/jobOrder-dialog.html',
                        controller: 'JobOrderDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    title: null,
                                    duration: null,
                                    description: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('jobOrder', null, { reload: true });
                    }, function() {
                        $state.go('jobOrder');
                    })
                }]
            })
            .state('jobOrder.edit', {
                parent: 'jobOrder',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/jobOrder/jobOrder-dialog.html',
                        controller: 'JobOrderDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['JobOrder', function(JobOrder) {
                                return JobOrder.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('jobOrder', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('jobOrder.delete', {
                parent: 'jobOrder',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/jobOrder/jobOrder-delete-dialog.html',
                        controller: 'JobOrderDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['JobOrder', function(JobOrder) {
                                return JobOrder.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('jobOrder', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
