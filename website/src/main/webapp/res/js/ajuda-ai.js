(function (angular) {
	var app = angular.module("ajuda-ai", []);
	var institutionSlug = (function () { var nextSlash = location.pathname.indexOf("/", 1); return nextSlash > 0 ? location.pathname.substring(0, nextSlash) : location.pathname; })();
	
	app.controller("InstitutionController", ["$scope", "$http", "$interval", function ($scope, $http, $interval) {
		$scope.donations = null;
		$scope.loadingInfo = true;
		
		function updateDonations() {
			$scope.loadingInfo = true;
			
			$http.get(institutionSlug + "/api/info-doacoes").then(function (response) {
				$scope.donations = response.data;
				$scope.loadingInfo = false;
			}, function () { $scope.loadingInfo = false; });
		};
		
//		$scope.donate = function donate() {
//			$uibModal.open({
//				animation: true,
//				templateUrl: "donation-modal.html",
//				controller: "DonationModalController"
//			});
//		}
		
		updateDonations();
		$interval(updateDonations, 60000);	// 1 min
	}]);
	
	/*
	app.controller("DonationModalController", ["$scope", "$http", "$window", "$uibModal", "$uibModalInstance", function ($scope, $http, $window, $uibModal, $uibModalInstance) {
		$scope.remindData = { name: "", email: "", phone: "" };
		
		$scope.working = false;
		$scope.remindLater = function remindLater(form, evt) {
			if (form.$valid) {
				$scope.working = true;
				$http.post(institutionSlug + "/api/lembrar", $scope.remindData).then(function (response) {
					$scope.working = false;
					if (!angular.isArray(response.data)) {
						$uibModalInstance.close();
						$uibModal.open({
							animation: true,
							templateUrl: "donation-thanks.html",
							controller: "DonationThanksModalController"
						});
					}
					else {
						$window.alert("Não conseguimos efetuar seu pedido de lembrete de doação.\n\nDesenvolvedores: Detalhes do erro foram gravados no log.");
						if (console && console.log) { console.log("Erro na requisição de Doação", response); }
					}
				}, function (response) {
					$scope.working = false;
					$window.alert("Não conseguimos efetuar seu pedido de lembrete de doação. Por favor, aguarde alguns instantes e tente novamente.");
					if (console && console.log) { console.log("Erro na requisição de Doação", response); }
				});
			}
			else {
				$window.alert("Por favor, verifique se você preencheu todos os campos obrigatórios (nome e e-mail).");
			}
			
			evt.preventDefault();
			return false;
		};
		
		$scope.close = $uibModalInstance.close;
	}]);
	
	app.controller("DonationThanksModalController", ["$scope", "$uibModalInstance", function ($scope, $uibModalInstance) {
		$scope.close = $uibModalInstance.close;
	}]);
	*/
})(angular);