(function (angular) {
	var app = angular.module("ajuda-ai", ["ngAnimate", "ui.router"]);
	var debug = true;
	var html5mode = false;
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
	
	app.controller("LoginController", ["$scope", "$http", "$window", "$state", "$stateParams", function ($scope, $http, $window, $state, $stateParams) {
		$scope.data = { username: "", password: "" };
		
		$scope.loggingIn = false;
		
		$scope.doLogin = function (evt) {
			evt.preventDefault();
			$scope.loggingIn = true;
			
			$http.post(apiEndpoint + "/login").then(function (response) {
				if (response.data && response.data.id) {
					$scope.setUser(response.data.user);
					
					if ($stateParams.nextState) {
						$state.go($stateParams.nextState, $stateParams.nextStateParams);
					}
					else {
						$state.go("admin.index");
					}
				}
				else {
					$scope.loggingIn = false;
					$window.alert("Usuário ou Senha incorretos. Por favor, tente novamente.");
				}
			}, function () {
				$scope.loggingIn = false;
				$window.alert("Falha na autenticação. Por favor, tente novamente.");
			});
		};
	}]);
	
	/* ***************************************** */
	/* Configurações e outras coisas             */
	/* ***************************************** */
	
	app.run(["$rootScope", "$location", "$state", function ($rootScope, $location, $state) {
		// When something explodes...
		$rootScope.$on("$stateChangeError", function (evt, toState, toParams, fromState, fromParams, error) {
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
		};
		
		var user = {};
		$rootScope.user = user;
		$rootScope.setUser = function (value) {
			user = value;
			$rootScope.user = user;
		};
		
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
		
		// Force login for all admin states
		$rootScope.$on("$stateChangeStart", function(evt, toState, toParams, fromState, fromParams) {
			if (toState.name.indexOf("admin") === -1 || $rootScope.user.id) {
				return;
			}
			
			evt.preventDefault(); // stop current execution
			$state.go("main.login", { nextState: toState, nextStateParams: toParams }); // go to login
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
	
	var _requireHttpTries = {};
	function requireHttp(id, $q, $http, url, opts) {
		if (_requireHttpTries[id] == undefined) {
			_requireHttpTries[id] = 0;
		}
		else if (_requireHttpTries[id] >= 10) {
			return $q(function (resolve) { resolve(null); });
		}
		
		return $q(function (resolve, reject) {
			console.log("[requireHttp] url = ", url);
			
			$http.get(url, opts).then(function (data) {
				console.log("[requireHttp.success] url = ", url, ", data = ", data.data);
				delete _requireHttpTries[id];
				resolve(data.data);
			},
			function (data) {
				console.log("[requireHttp.error] url = ", url, ", data = ", data.data);
				_requireHttpTries[id]++;
				reject(data.data);
			});
		});
	}
	
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
			templateUrl: "/fragments/main.contato.html",
			controller: "ContatoController"
		})
		.state("main.login", {
			url: "/login",
			templateUrl: "/fragments/main.login.html",
			controller: "LoginController"
		})
		
		.state("admin", {
			url: "/admin",
			"abstract": true,
			template: "<ui-view/>"
		})
		.state("admin.index", {
			url: "",
			templateUrl: "/fragments/admin.index.html",
			controller: "AdminIndexController"
		})
		
		.state("main.instituicao", {
			url: "/:slug",
			templateUrl: "/fragments/main.instituicao.html",
			controller: "InstitutionController",
			resolve: {
				institution: ["$q", "$http", "$stateParams", function ($q, $http, $stateParams) {
					return requireHttp("mainInst", $q, $http, apiEndpoint + "/institution/" + $stateParams.slug);
				}]
			}
		})
		//admin.index
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