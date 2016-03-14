'use strict';

angular.module('atsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('applicantStatus', {
                parent: 'entity',
                url: '/applicantStatuss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ApplicantStatuss'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/applicantStatus/applicantStatuss.html',
                        controller: 'ApplicantStatusController'
                    }
                },
                resolve: {
                }
            })
            .state('applicantStatus.detail', {
                parent: 'entity',
                url: '/applicantStatus/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ApplicantStatus'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/applicantStatus/applicantStatus-detail.html',
                        controller: 'ApplicantStatusDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'ApplicantStatus', function($stateParams, ApplicantStatus) {
                        return ApplicantStatus.get({id : $stateParams.id});
                    }]
                }
            })
            .state('applicantStatus.new', {
                parent: 'applicantStatus',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/applicantStatus/applicantStatus-dialog.html',
                        controller: 'ApplicantStatusDialogController',
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
                        $state.go('applicantStatus', null, { reload: true });
                    }, function() {
                        $state.go('applicantStatus');
                    })
                }]
            })
            .state('applicantStatus.edit', {
                parent: 'applicantStatus',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/applicantStatus/applicantStatus-dialog.html',
                        controller: 'ApplicantStatusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ApplicantStatus', function(ApplicantStatus) {
                                return ApplicantStatus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('applicantStatus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('applicantStatus.delete', {
                parent: 'applicantStatus',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/applicantStatus/applicantStatus-delete-dialog.html',
                        controller: 'ApplicantStatusDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ApplicantStatus', function(ApplicantStatus) {
                                return ApplicantStatus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('applicantStatus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
