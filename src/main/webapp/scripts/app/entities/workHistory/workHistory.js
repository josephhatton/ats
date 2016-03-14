'use strict';

angular.module('atsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('workHistory', {
                parent: 'entity',
                url: '/workHistorys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'WorkHistorys'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/workHistory/workHistorys.html',
                        controller: 'WorkHistoryController'
                    }
                },
                resolve: {
                }
            })
            .state('workHistory.detail', {
                parent: 'entity',
                url: '/workHistory/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'WorkHistory'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/workHistory/workHistory-detail.html',
                        controller: 'WorkHistoryDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'WorkHistory', function($stateParams, WorkHistory) {
                        return WorkHistory.get({id : $stateParams.id});
                    }]
                }
            })
            .state('workHistory.new', {
                parent: 'workHistory',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/workHistory/workHistory-dialog.html',
                        controller: 'WorkHistoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    company: null,
                                    title: null,
                                    startDate: null,
                                    endDate: null,
                                    startingCompensation: null,
                                    endingCompensation: null,
                                    compensationType: null,
                                    supervisor: null,
                                    supervisorTitle: null,
                                    supervisorPhone: null,
                                    duties: null,
                                    reasonForLeaving: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('workHistory', null, { reload: true });
                    }, function() {
                        $state.go('workHistory');
                    })
                }]
            })
            .state('workHistory.edit', {
                parent: 'workHistory',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/workHistory/workHistory-dialog.html',
                        controller: 'WorkHistoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['WorkHistory', function(WorkHistory) {
                                return WorkHistory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('workHistory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('workHistory.delete', {
                parent: 'workHistory',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/workHistory/workHistory-delete-dialog.html',
                        controller: 'WorkHistoryDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['WorkHistory', function(WorkHistory) {
                                return WorkHistory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('workHistory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
