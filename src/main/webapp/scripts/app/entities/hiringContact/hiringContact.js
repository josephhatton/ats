'use strict';

angular.module('atsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('hiringContact', {
                parent: 'entity',
                url: '/hiringContacts',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'HiringContacts'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hiringContact/hiringContacts.html',
                        controller: 'HiringContactController'
                    }
                },
                resolve: {
                }
            })
            .state('hiringContact.detail', {
                parent: 'entity',
                url: '/hiringContact/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'HiringContact'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hiringContact/hiringContact-detail.html',
                        controller: 'HiringContactDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'HiringContact', function($stateParams, HiringContact) {
                        return HiringContact.get({id : $stateParams.id});
                    }]
                }
            })
            .state('hiringContact.new', {
                parent: 'hiringContact',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/hiringContact/hiringContact-dialog.html',
                        controller: 'HiringContactDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    firstName: null,
                                    lastName: null,
                                    nickName: null,
                                    phone1: null,
                                    phone2: null,
                                    email1: null,
                                    jobTitle: null,
                                    referralSource: null,
                                    contactType: null,
                                    email2: null,
                                    middleName: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('hiringContact', null, { reload: true });
                    }, function() {
                        $state.go('hiringContact');
                    })
                }]
            })
            .state('hiringContact.edit', {
                parent: 'hiringContact',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/hiringContact/hiringContact-dialog.html',
                        controller: 'HiringContactDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['HiringContact', function(HiringContact) {
                                return HiringContact.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('hiringContact', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('hiringContact.delete', {
                parent: 'hiringContact',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/hiringContact/hiringContact-delete-dialog.html',
                        controller: 'HiringContactDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['HiringContact', function(HiringContact) {
                                return HiringContact.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('hiringContact', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
