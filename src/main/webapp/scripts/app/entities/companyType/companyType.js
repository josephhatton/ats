'use strict';

angular.module('atsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('companyType', {
                parent: 'entity',
                url: '/companyTypes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'CompanyTypes'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/companyType/companyTypes.html',
                        controller: 'CompanyTypeController'
                    }
                },
                resolve: {
                }
            })
            .state('companyType.detail', {
                parent: 'entity',
                url: '/companyType/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'CompanyType'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/companyType/companyType-detail.html',
                        controller: 'CompanyTypeDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'CompanyType', function($stateParams, CompanyType) {
                        return CompanyType.get({id : $stateParams.id});
                    }]
                }
            })
            .state('companyType.new', {
                parent: 'companyType',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/companyType/companyType-dialog.html',
                        controller: 'CompanyTypeDialogController',
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
                        $state.go('companyType', null, { reload: true });
                    }, function() {
                        $state.go('companyType');
                    })
                }]
            })
            .state('companyType.edit', {
                parent: 'companyType',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/companyType/companyType-dialog.html',
                        controller: 'CompanyTypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CompanyType', function(CompanyType) {
                                return CompanyType.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('companyType', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('companyType.delete', {
                parent: 'companyType',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/companyType/companyType-delete-dialog.html',
                        controller: 'CompanyTypeDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['CompanyType', function(CompanyType) {
                                return CompanyType.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('companyType', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
