'use strict';

angular.module('atsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('jobType', {
                parent: 'entity',
                url: '/jobTypes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'JobTypes'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/jobType/jobTypes.html',
                        controller: 'JobTypeController'
                    }
                },
                resolve: {
                }
            })
            .state('jobType.detail', {
                parent: 'entity',
                url: '/jobType/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'JobType'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/jobType/jobType-detail.html',
                        controller: 'JobTypeDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'JobType', function($stateParams, JobType) {
                        return JobType.get({id : $stateParams.id});
                    }]
                }
            })
            .state('jobType.new', {
                parent: 'jobType',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/jobType/jobType-dialog.html',
                        controller: 'JobTypeDialogController',
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
                        $state.go('jobType', null, { reload: true });
                    }, function() {
                        $state.go('jobType');
                    })
                }]
            })
            .state('jobType.edit', {
                parent: 'jobType',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/jobType/jobType-dialog.html',
                        controller: 'JobTypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['JobType', function(JobType) {
                                return JobType.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('jobType', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('jobType.delete', {
                parent: 'jobType',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/jobType/jobType-delete-dialog.html',
                        controller: 'JobTypeDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['JobType', function(JobType) {
                                return JobType.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('jobType', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
