(function (angular) {
	var app = angular.module("ajuda-ai-admin", []);
	
	app.controller("DashboardController", ["$scope", "$http", "$timeout", function ($scope, $http, $timeout) {
		var updateTimeout = null;
		
		$scope.data = {};
		$scope.updating = false;
		function update() {
			if (updateTimeout) { $timeout.cancel(updateTimeout); }
			
			$scope.updating = true;
			$http.get("/admin/api/dashboard-data").then(function (response) {
				$scope.updating = false;
				$scope.data = response.data;
				updateTimeout = $timeout(update, 60000);
			}, function (response) {
				$scope.updating = false;
				updateTimeout = $timeout(update, 60000);
			});
		};
		
		$scope.update = update;
		update();
	}]);
	
	app.controller("InstitutionDashboardController", ["$scope", "$http", "$timeout", function ($scope, $http, $timeout) {
		var updateTimeout = null;
		
		$scope.data = {};
		$scope.updating = false;
		function update() {
			if (updateTimeout) { $timeout.cancel(updateTimeout); }
			
			$scope.updating = true;
			$http.get("/admin" + $scope.slug + "/api/dashboard-data").then(function (response) {
				$scope.updating = false;
				$scope.data = response.data;
				updateTimeout = $timeout(update, 60000);
			}, function (response) {
				$scope.updating = false;
				updateTimeout = $timeout(update, 60000);
			});
		};
		
		$scope.update = update;
		update();
	}]);
})(angular);