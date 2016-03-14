'use strict';

angular.module('atsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('jobStatus', {
                parent: 'entity',
                url: '/jobStatuss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'JobStatuss'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/jobStatus/jobStatuss.html',
                        controller: 'JobStatusController'
                    }
                },
                resolve: {
                }
            })
            .state('jobStatus.detail', {
                parent: 'entity',
                url: '/jobStatus/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'JobStatus'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/jobStatus/jobStatus-detail.html',
                        controller: 'JobStatusDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'JobStatus', function($stateParams, JobStatus) {
                        return JobStatus.get({id : $stateParams.id});
                    }]
                }
            })
            .state('jobStatus.new', {
                parent: 'jobStatus',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/jobStatus/jobStatus-dialog.html',
                        controller: 'JobStatusDialogController',
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
                        $state.go('jobStatus', null, { reload: true });
                    }, function() {
                        $state.go('jobStatus');
                    })
                }]
            })
            .state('jobStatus.edit', {
                parent: 'jobStatus',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/jobStatus/jobStatus-dialog.html',
                        controller: 'JobStatusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['JobStatus', function(JobStatus) {
                                return JobStatus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('jobStatus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('jobStatus.delete', {
                parent: 'jobStatus',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/jobStatus/jobStatus-delete-dialog.html',
                        controller: 'JobStatusDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['JobStatus', function(JobStatus) {
                                return JobStatus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('jobStatus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
