(function (angular) {
	var app = angular.module("ajuda-ai", ["ngAnimate", "ngSanitize", "ui.bootstrap"]);
	var institutionSlug = (function () { var nextSlash = location.pathname.indexOf("/", 1); return nextSlash > 0 ? location.pathname.substring(0, nextSlash) : location.pathname; })();
	
	app.controller("MainController", ["$scope", function ($scope) {
		$scope.slides = [{
			id: 0,
			image: "/res/img/index-img-01.jpg",
			caption: "Juntamos quem <strong>quer ajudar</strong> a quem <strong>precisa de ajuda</strong>.",
			href: "/ama",
			button: "Ajude a AMA - Associação dos Amigos dos Autistas"
		}, {
			id: 1,
			image: "/res/img/index-img-02.jpg",
			caption: "Curtiu nosso projeto? <strong>Conheça</strong> o que é o Ajuda.Ai!",
			href: "/sobre",
			button: "Sobre o Ajuda.Ai"
		}, {
			id: 2,
			image: "/res/img/index-img-03.jpg",
			caption: "Saiba quem são <strong>os rostos</strong> (e dedos) por trás deste Projeto",
			href: "/sobre#desenvolvedores",
			button: "Conheça a gente :)"
		}];
		
		$scope.active = 0;
		$scope.currentSlideInterval = function () {
			if ($scope.active === 0) { return 20000; }
			else { return 5000; }
		}
	}]);
	
	app.controller("InstitutionController", ["$scope", "$http", "$interval", "$uibModal", function ($scope, $http, $interval, $uibModal) {
		$scope.donations = null;
		$scope.loadingInfo = true;
		
		function updateDonations() {
			$scope.loadingInfo = true;
			
			$http.get(institutionSlug + "/api/info-doacoes").then(function (response) {
				$scope.donations = response.data;
				$scope.loadingInfo = false;
			}, function () { $scope.loadingInfo = false; });
		};
		
		$scope.donate = function donate() {
			$uibModal.open({
				animation: true,
				templateUrl: "donation-modal.html",
				controller: "DonationModalController"
			});
		}
		
		updateDonations();
		$interval(updateDonations, 60000);	// 1 min
	}]);
	
	app.controller("DonationModalController", ["$scope", "$http", "$window", "$uibModal", "$uibModalInstance", function ($scope, $http, $window, $uibModal, $uibModalInstance) {
		$scope.remindLater = { name: "", email: "", phone: "" };
		
		$scope.working = false;
		$scope.remindLater = function remindLater(form, evt) {
			if (form.$valid) {
				$scope.working = true;
				$http.post(institutionSlug + "/api/lembrar", $scope.remindLater).then(function (response) {
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
	
	app.directive("currencyInput", function() {
		return {
			restrict : "A",
			scope : {
				field : "="
			},
			replace : true,
			template : "<span><input type=\"text\" ng-model=\"field\"></input>{{field}}</span>",
			link : function(scope, element, attrs) {
				
				$(element).bind("keyup", function(e) {
					var input = element.find("input");
					var inputVal = input.val();
					
					//clearing left side zeros
					while (scope.field.charAt(0) == "0") {
						scope.field = scope.field.substr(1);
					}
					
					scope.field = scope.field.replace(/[^\d.\","]/g, "");
					
					var point = scope.field.indexOf(".");
					if (point >= 0) {
						scope.field = scope.field.slice(0, point + 3);
					}
					
					var decimalSplit = scope.field.split(".");
					var intPart = decimalSplit[0];
					var decPart = decimalSplit[1];
					
					intPart = intPart.replace(/[^\d]/g, "");
					if (intPart.length > 3) {
						var intDiv = Math.floor(intPart.length / 3);
						while (intDiv > 0) {
							var lastComma = intPart.indexOf(",");
							if (lastComma < 0) {
								lastComma = intPart.length;
							}
							
							if (lastComma - 3 > 0) {
								intPart = intPart.splice(lastComma - 3, 0, ",");
							}
							intDiv--;
						}
					}
					
					if (decPart === undefined) {
						decPart = "";
					} else {
						decPart = "." + decPart;
					}
					var res = intPart + decPart;
					
					scope.$apply(function() {
						scope.field = res
					});
					
				});
			}
		};
	});
})(angular);