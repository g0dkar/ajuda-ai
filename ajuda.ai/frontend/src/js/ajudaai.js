(function (angular) {
	var app = angular.module("ajuda-ai", ["ngAnimate", "ui.router"]);
	var debug = true;
	var html5mode = true;
	var apiEndpoint = "http://localhost/api";
	
	/* ***************************************** */
	/* Controllers                               */
	/* ***************************************** */
	
	app.controller("IndexController", ["$scope", "$http", "$interval", function ($scope, $http, $interval) {
		$scope.homeInstitutions = [];
		
		var update = function () {
			$http.get(apiEndpoint + "/institution/random-list").then(function (response) {
				if (angular.isArray(response.data)) {
					$scope.homeInstitutions = response.data;
				}
			});
		};
		
		update();
		
		// Atualiza a cada 5 minutos
		var updateInterval = $interval(update, 300000);
		
		$scope.$on("$destroy", function () {
			$interval.cancel(updateInterval);
		});
	}]);
	
	/* ***************************************** */
	/* Configurações e outras coisas             */
	/* ***************************************** */
	
	app.run(["$rootScope", "$location", "$state", function ($rootScope, $location, $state) {
		// When something explodes...
		$rootScope.$on("$stateChangeError", function (event, toState, toParams, fromState, fromParams, error) {
			console.log("[$stateChangeError] args =", arguments);
//			$state.go("main.index");
		});
		
		$rootScope.isActivePath = function (url) {
			return $location.path().indexOf(url) > -1;
		};
		
		var template = {};
		$rootScope.template = template;
		$rootScope.setTemplate = function (what, value) {
			template[what] = value;
		}
		
		var pageContentElement = angular.element(document.getElementById("page-content"));
		
		$rootScope.$on("$stateChangeSuccess", function () {
			if (window.ga) {
				ga("send", "pageview", { page: $location.path() });
			}
			
			pageContentElement.addClass("loaded");
		});
		
		$rootScope.$on("$stateChangeStart", function () {
			pageContentElement.removeClass("loaded");
		});
	}]);
	
	app.factory("errorInterceptor", ["$q", function ($q) {
		return {
			responseError: function (response) {
				console.log("[errorInterceptor] something went wrong", response);
				return $q.reject(response);
			}
		};
	}]);
	
//	app.factory("bearerInterceptor", ["$q", function ($q) {
//		return {
//			request: function (config) {
//				return $q(function (resolve, reject) {
//					keycloak.updateToken().success(function () {
//						if (!config.headers) { config.headers = {}; }
//						config.headers.Authorization = "Bearer " + keycloak.token;
//						resolve(config);
//					}).error(function() {
//						keycloak.login();
//						reject(config);
//					});
//				});
//			}
//		};
//	}]);
	
	// ROUTER
	app.config(["$httpProvider", "$locationProvider", "$compileProvider", "$urlRouterProvider", "$urlMatcherFactoryProvider", "$stateProvider", function ($httpProvider, $locationProvider, $compileProvider, $urlRouterProvider, $urlMatcherFactoryProvider, $stateProvider) {
//		$httpProvider.interceptors.push("bearerInterceptor");
		$httpProvider.interceptors.push("errorInterceptor");
		
		// Setting HTML 5 mode (bretty urls)
		$locationProvider.html5Mode(html5mode);
		
		// Show debug stuff?
		$compileProvider.debugInfoEnabled(debug);
		
		// A "404" will go to /
//		$urlRouterProvider.otherwise("/");
		
		// Trailling "/" are optional 
		$urlMatcherFactoryProvider.strictMode(false);
		
		// And now... routes.
		$stateProvider.state("main", {
			url: "",
			"abstract": true,
			template: "<ui-view/>"
		})
		.state("main.index", {
			url: "",
			templateUrl: "/fragments/main.index.html",
			controller: "IndexController"
		})
		.state("main.sobre", {
			url: "/sobre",
			templateUrl: "/fragments/main.sobre.html"
		})
		.state("main.contato", {
			url: "/contato",
			templateUrl: "/fragments/main.contato.html"
		})
//		.state("admin.quiz.create", {
//			url: "/create",
//			templateUrl: context + "/api/ng/quiz-create",
//			controller: "QuizCreateController",
//			resolve: {
//				groups: ["$q", "$http", "toastr", function ($q, $http, toastr) {
//					return requireHttp("qcgl", $q, $http, toastr, context + "/api/group/my-groups");
//				}]
//			}
//		})
//		.state("admin.quiz.view", {
//			url: "/view/{quiz_id:[a-z][a-z0-9\\-]*}",
//			"abstract": true,
//			templateUrl: context + "/api/ng/quiz-view",
//			controller: ["$scope", "quiz", "quizzes", function ($scope, quiz, quizzes) {
//				$scope.quiz = quiz;
//				$scope.quizzes = quizzes.quizzes;
//				$scope.setQuiz = function (q) { $scope.quiz = q; };
//				$scope.setPageClass("page-bg theme-" + quiz.options.theme + "-bg");
//			}],
//			resolve: {
//				quiz: ["$q", "$http", "$stateParams", "toastr", function ($q, $http, $stateParams, toastr) {
//					return requireHttp("qvq", $q, $http, toastr, context + "/api/quiz/get/" + $stateParams.quiz_id);
//				}],
//				quizzes: ["$q", "$http", "toastr", function ($q, $http, toastr) {
//					return requireHttp("qvql", $q, $http, toastr, context + "/api/quiz/my-quizzes");
//				}]
//			}
//		})
//		.state("admin.quiz.view.presentation", {
//			url: "",
//			templateUrl: context + "/api/ng/quiz-view-presentation",
//			controller: "QuizPresentationController"
//		})
//		.state("admin.quiz.view.questions", {
//			url: "/questions",
//			templateUrl: context + "/api/ng/quiz-view-questions",
//			controller: "QuizQuestionsController"
//		})
//		.state("admin.quiz.view.answers", {
//			url: "/answers",
//			templateUrl: context + "/api/ng/quiz-view-answers",
//			controller: "QuizAnswersController"
//		})
//		.state("admin.group", {
//			"abstract": true,
//			url: "/group",
//			templateUrl: context + "/api/ng/group-main"
//		})
//		.state("admin.group.dashboard", {
//			url: "",
//			templateUrl: context + "/api/ng/group-dashboard",
//			controller: "GroupDashboardController",
//			resolve: {
//				groups: ["$q", "$http", "toastr", function ($q, $http, toastr) {
//					return requireHttp("gdgl", $q, $http, toastr, context + "/api/group/my-groups");
//				}]
//			}
//		})
//		.state("admin.group.dashboardSingle", {
//			url: "/dashboard/{group_id:[a-z][a-z0-9\\-]*}",
//			templateUrl: context + "/api/ng/group-dashboard-single",
//			controller: "GroupDashboardSingleController",
//			resolve: {
//				group: ["$q", "$http", "toastr", "$stateParams", function ($q, $http, toastr, $stateParams) {
//					return requireHttp("gdsg", $q, $http, toastr, context + "/api/group/get/" + $stateParams.group_id);
//				}]
//			}
//		})
//		.state("admin.group.create", {
//			url: "/create",
//			templateUrl: context + "/api/ng/group-create",
//			controller: "GroupCreateController"
//		})
		;
	}]);
})(angular);