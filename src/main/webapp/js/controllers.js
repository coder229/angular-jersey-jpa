'use strict';

/* Controllers */

var controllers = angular.module('controllers', []);

controllers.controller('headerController', function ($scope, $http, $location) {
	$scope.isActive = function (viewLocation) { 
        return viewLocation === $location.path();
    };
});

/* To Do*/ 

controllers.controller('itemController', function ($scope, $http, $location) {
	console.log("item");
	$scope.refresh = function() {
		$http.get('rest/item').success(function(data, status, headers, config) {
			$scope.items = data;
		});
	};
	$scope.refresh();
	$scope.newItem = function() {
    	console.log("new item");
		$location.path('/detail/0');
    };
    $scope.removeItem = function(item) {
    	if ((item.id > 0) && confirm('Remove item ' + item.text + '?')) {
        	console.log("removing item");
	    	$http({method: 'DELETE', url: 'rest/item/' + item.id})
		    	.success(function(data, status, headers, config) {
					$scope.alerts = [
	                	{ type: 'success', msg: 'Item saved' }
					];				
					$scope.refresh();
		    	})
				.error(function(data, status, headers, config) {
					$scope.alerts = [
	                	{ type: 'danger', msg: data }
					];				
				});
    	}
    };
});

controllers.controller('itemDetailController', function ($scope, $routeParams, $http, $location) {
	console.log("item detail");
	$scope.item = { created: new Date() };

	if ($routeParams.itemId > 0) {
		$http.get('rest/item/' + $routeParams.itemId).success(function(data, status, headers, config) {
			$scope.item = data;
		});
	}
	$scope.save = function(item) {
    	console.log("save item");
		if (item.id) {
		  	$http.put('rest/item/' + item.id, item)
			  	.success(function(data, status, headers, config) {
			  		$scope.item = data;
			  		$location.path('/list');
				})
				.error(function(data, status, headers, config) {
					$scope.alerts = [
	                	{ type: 'danger', msg: data }
					];				
				});
		} else {
		  	$http.post('rest/item', item)
		  		.success(function(data, status, headers, config) {
		  			$scope.item = data;
			  		$location.path('/list');
				})
				.error(function(data, status, headers, config) {
					$scope.alerts = [
	                	{ type: 'danger', msg: data }
					];				
				});
		}
    };
	$scope.closeAlert = function(index) {
		  $scope.alerts.splice(index, 1);
	};
});
