'use strict';

angular.module('atsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('activity', {
                parent: 'entity',
                url: '/activitys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Activitys'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/activity/activitys.html',
                        controller: 'ActivityController'
                    }
                },
                resolve: {
                }
            })
            .state('activity.detail', {
                parent: 'entity',
                url: '/activity/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Activity'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/activity/activity-detail.html',
                        controller: 'ActivityDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Activity', function($stateParams, Activity) {
                        return Activity.get({id : $stateParams.id});
                    }]
                }
            })
            .state('activity.new', {
                parent: 'activity',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/activity/activity-dialog.html',
                        controller: 'ActivityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    priority: null,
                                    startDate: null,
                                    status: null,
                                    comments: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('activity', null, { reload: true });
                    }, function() {
                        $state.go('activity');
                    })
                }]
            })
            .state('activity.edit', {
                parent: 'activity',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/activity/activity-dialog.html',
                        controller: 'ActivityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Activity', function(Activity) {
                                return Activity.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('activity', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('activity.delete', {
                parent: 'activity',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/activity/activity-delete-dialog.html',
                        controller: 'ActivityDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Activity', function(Activity) {
                                return Activity.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('activity', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
