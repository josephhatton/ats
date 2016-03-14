'use strict';

angular.module('atsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('companyStatus', {
                parent: 'entity',
                url: '/companyStatuss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'CompanyStatuss'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/companyStatus/companyStatuss.html',
                        controller: 'CompanyStatusController'
                    }
                },
                resolve: {
                }
            })
            .state('companyStatus.detail', {
                parent: 'entity',
                url: '/companyStatus/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'CompanyStatus'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/companyStatus/companyStatus-detail.html',
                        controller: 'CompanyStatusDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'CompanyStatus', function($stateParams, CompanyStatus) {
                        return CompanyStatus.get({id : $stateParams.id});
                    }]
                }
            })
            .state('companyStatus.new', {
                parent: 'companyStatus',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/companyStatus/companyStatus-dialog.html',
                        controller: 'CompanyStatusDialogController',
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
                        $state.go('companyStatus', null, { reload: true });
                    }, function() {
                        $state.go('companyStatus');
                    })
                }]
            })
            .state('companyStatus.edit', {
                parent: 'companyStatus',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/companyStatus/companyStatus-dialog.html',
                        controller: 'CompanyStatusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CompanyStatus', function(CompanyStatus) {
                                return CompanyStatus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('companyStatus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('companyStatus.delete', {
                parent: 'companyStatus',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/companyStatus/companyStatus-delete-dialog.html',
                        controller: 'CompanyStatusDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['CompanyStatus', function(CompanyStatus) {
                                return CompanyStatus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('companyStatus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
