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
			$http.get("/admin/instituicao" + $scope.slug + "/api/dashboard-data").then(function (response) {
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
	
	app.controller("InstitutionDetailsController", ["$scope", "$http", function ($scope, $http) {
		var originalSlug = $institution.slug;
		
		$scope.institution = $institution;
		$scope.unavailableSlug = false;
		$scope.checkingSlug = false;
		
		$scope.$watch("institution.slug", function (newValue) {
			if (newValue) {
				console.log("institution.slug watcher: ", newValue);
				newValue = seoName(newValue);
				$scope.institution.slug = newValue;
				
				if (newValue != originalSlug) {
					$scope.checkingSlug = true;
					$http.get("/admin/instituicao/" + originalSlug + "/api/check-slug?s=" + newValue).then(function () {
						$scope.unavailableSlug = false;
						$scope.checkingSlug = false;
					}, function () {
						$scope.unavailableSlug = true;
						$scope.checkingSlug = false;
					});
				}
				else {
					$scope.unavailableSlug = false;
					$scope.checkingSlug = false;
				}
			}
		});
		
		function seoName(str) {
			if (typeof str === "string") {
				return str.replace(/\s+/g, "-").replace(/-{2,}/g, "-");
			}
		};
	}]);
	
	app.directive("markdown", function () {
		var timeout = null;
		
		return {
			require: "ngModel",
			element: "A",
			link: function (scope, element, attrs) {
				var el = document.getElementById(attrs.markdown);
				
				scope.$watch(attrs.ngModel, function (newValue) {
					el.innerHTML = newValue ? markdown.toHTML(newValue) : "<p>[ Previsão do Conteúdo de seu Post ficará aqui ]</p>";
				});
				
				if (!scope[attrs.ngModel]) {
					scope[attrs.ngModel] = element.val();
				}
			}
		}
	});
})(angular);