(function (angular) {
	var app = angular.module("ajuda-ai", ["ngAnimate", "ngSanitize", "ui.bootstrap"]);
	
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
	
	app.controller("InstitutionController", ["$scope", "$http", "$interval", function ($scope, $http, $interval) {
		var nextSlash = location.pathname.indexOf("/", 1);
		var institutionSlug = nextSlash > 0 ? location.pathname.substring(0, nextSlash) : location.pathname;
		$scope.donations = null;
		$scope.loadingInfo = true;
		
		function updateDonations() {
			$scope.loadingInfo = true;
			
			$http.get(institutionSlug + "/api/info-doacoes").then(function (response) {
				$scope.donations = response.data;
				$scope.loadingInfo = false;
			}, function () { $scope.loadingInfo = false; });
		};
		
		updateDonations();
		$interval(updateDonations, 60000);	// 1 min
	}]);
})(angular);