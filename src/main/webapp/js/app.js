'use strict';

/* App Module */

var app = angular.module('app', ['ngRoute', 'controllers', 'ui.bootstrap']);

app.config(function($routeProvider) {
	$routeProvider
		.when('/list', {
			templateUrl : 'pages/list.html',
			controller  : 'itemController'
		})
		.when('/detail/:itemId', {
			templateUrl : 'pages/detail.html',
			controller  : 'itemDetailController'
		})
		.otherwise({
			redirectTo: '/list'
		});
});

/**
 * A generic confirmation for risky actions.
 * Usage: Add attributes:
 * * ng-confirm-message="Are you sure?"
 * * ng-confirm-click="takeAction()" function
 * * ng-confirm-condition="mustBeEvaluatedToTrueForTheConfirmBoxBeShown" expression
 */
angular.module('app').directive('ngConfirmClick', [function() {
	return {
		restrict: 'A',
		link: function(scope, element, attrs) {
			element.bind('click', function() {
				var condition = scope.$eval(attrs.ngConfirmCondition);
				if(condition){
					var message = attrs.ngConfirmMessage;
					if (message && confirm(message)) {
						scope.$apply(attrs.ngConfirmClick);
					}
				}
				else{
					scope.$apply(attrs.ngConfirmClick);
				}
			});
		}
	}
}]);