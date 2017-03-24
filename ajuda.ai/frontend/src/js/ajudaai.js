(function (angular, window) {
	var app = angular.module("ajuda-ai", ["ngAnimate", "ui.router", "ng-showdown", "ezfb", "vcRecaptcha"]);
	var debug = true;
	var html5mode = false;
	var apiEndpoint = "http://localhost:8080/v1";
	
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
	
	app.controller("InstituicaoController", ["$scope", "$http", "$interval", "institution", function ($scope, $http, $interval, institution) {
		$scope.institution = institution;
		$scope.donationStats = { value: 0, count: 0 };
		
		var update = function () {
			$http.get(apiEndpoint + "/institution/" + institution.slug + "/donation-stats").then(function (response) {
				$scope.donationStats = response.data;
			});
		};
		
		update();
		
		// Atualiza a cada 1 minuto
		var updateInterval = $interval(update, 60000);
		
		$scope.$on("$destroy", function () {
			$interval.cancel(updateInterval);
		});
	}]);
	
	app.controller("InstituicaoDoarController", ["$scope", "$http", function ($scope, $http) {
		$scope.recaptchaKey = "6LcJsSMTAAAAALmEuGm_V1yzF05DGn540TLXd6HH";
		var donation = {
			value: 20,
			anonymous: false,
			addcosts: false,
			addcoststype: "0",
			email: $scope.user.email ? $scope.user.email : "",
			name: $scope.user.firstname ? ($scope.user.firstname + " " + $scope.user.lastname) : ""
		};
		
		$scope.donation = donation;
		
		$scope.calcCosts = function (type) {
			var value = donation.value;
			if (type == 0) {
				switch ($scope.institution.paymentService) {
					case "moip": return ((Math.ceil((100 * value + 65) / .9451) - 100 * value) / 100).toFixed(2);
					case "pagseguro": return ((Math.ceil((100 * value + 65) / .9451) - 100 * value) / 100).toFixed(2);
					default: return 0;
				}
			}
			else {
				switch ($scope.institution.paymentService) {
					case "moip": return ((Math.ceil((100 * value + 65) / .9651) - 100 * value) / 100).toFixed(2);
					case "pagseguro": return ((Math.ceil((100 * value + 65) / .9651) - 100 * value) / 100).toFixed(2);
					default: return 0;
				}
			}
		}
	}]);
	
	app.controller("LoginController", ["$scope", "$http", "$window", "$state", "$stateParams", function ($scope, $http, $window, $state, $stateParams) {
		$scope.data = { username: "", password: "" };
		
		$scope.loggingIn = false;
		
		$scope.doLogin = function (evt) {
			evt.preventDefault();
			$scope.loggingIn = true;
			
			$http.post(apiEndpoint + "/auth/login", $scope.data).then(function (response) {
				if (response.data && response.data.id) {
					$scope.setUser(response.data);
					
					if ($stateParams.nextState && $stateParams.nextState.name) {
						$state.go($stateParams.nextState.name, $stateParams.nextStateParams);
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
	
	app.controller("AdminIndexController", ["$scope", "$http", "$interval", function ($scope, $http, $interval) {
		$scope.loading = true;
		
		$scope.dashData = {
			donations: 0,
			institutions: 0,
			value: 0,
			meanValue: 0
		};
		
		$http.get(apiEndpoint + "/profile/dashboard-data").then(function (response) { 
			$scope.loading = false;
			
			if (angular.isObject(response.data)) {
				$scope.dashData = response.data;
			}
		}, function () {
			$scope.loading = false;
		});
	}]);
	
	app.controller("AdminInstituicoesController", ["$scope", "$http", "$state", function ($scope, $http, $state) {
		if (!$scope.user.isInstitution) {
			$state.go("admin.index");
			return;
		}
		
		$scope.loading = true;
		
		$scope.dashData = {
			value: 0,
			helpers: 0,
			maxValue: 0,
			meanValue: 0,
			donations: 0,
			institutionCount: 0,
			institutions: []
		};
		
		$scope.servicoPagamento = function (servico) {
			switch (servico) {
				case "moip": return "MoIP";
				case "pagseguro": return "PagSeguro";
				default: return servico;
			}
		};
		
		$http.get(apiEndpoint + "/institution/dashboard-data").then(function (response) { 
			$scope.loading = false;
			
			if (angular.isObject(response.data)) {
				$scope.dashData = response.data;
			}
		}, function () {
			$scope.loading = false;
		});
	}]);
	
	app.controller("AdminInstituicaoVerController", ["$scope", "institutionPosts", function ($scope, institutionPosts) {
		$scope.institutionPosts = institutionPosts;
	}]);
	
	app.controller("AdminInstituicaoPostController", ["$scope", "institution", "post", function ($scope, $state, institution, post) {
		$scope.post = post;
	}]);
	
	app.controller("AdminInstituicaoEditarController", ["$scope", "$http", "$state", "institution", function ($scope, $http, $state, institution) {
		var slug = institution.slug;
		$scope.institution = institution;
		$scope.submitting = false;
		
		$scope.doSubmit = function (evt) {
			evt.preventDefault();
			$scope.submitting = true;
			
			$http.post(apiEndpoint + "/institution/save", $scope.institution).then(function (response) { 
				$scope.submitting = false;
				
				if (!angular.isArray(response.data)) {
					$scope.institution = response.data;
					if ($scope.institution.slug != slug) {
						$state.go("admin.instituicaoEditar", { slug: $scope.institution.slug, institution: $scope.institution });
					}
				}
				else {
					var errors = response.data;
					for (var i = 0; i < errors.length; i++) {
						angular.element(document.getElementById("institution_" + errors[i].category)).addClass("has-error");
					}
				}
			}, function () {
				$scope.submitting = false;
			});
		};
	}]);
	
	app.controller("AdminProfileController", ["$scope", "$http", function ($scope, $http) {
		//$scope.user = $scope.getUser;
		$scope.submitting = false;
		
		var passwordCritique = {
			upper: false,
			lower: false,
			numbers: false,
			especial: false
		};
		
		$scope.$watch("user.newPassword", function (newValue) {
			if (newValue) {
				passwordCritique.upper = /[A-Z]/g.test(newValue);
				passwordCritique.lower = /[a-z]/g.test(newValue);
				passwordCritique.numbers = /[0-9]/g.test(newValue);
				passwordCritique.especial = /[\W_]/g.test(newValue);
			}
		});
		
		$scope.passwordCritique = passwordCritique;
		
		$scope.doSubmit = function (evt) {
			evt.preventDefault();
			$scope.submitting = true;
			
			$http.post(apiEndpoint + "/profile/save", $scope.user).then(function (response) { 
				$scope.submitting = false;
				
				if (!angular.isArray(response.data)) {
					$scope.setUser(response.data);
				}
				else {
					var errors = response.data;
					for (var i = 0; i < errors.length; i++) {
						angular.element(document.getElementById("user_" + errors[i].category)).addClass("has-error");
					}
				}
			}, function () {
				$scope.submitting = false;
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
				window.ga("send", "pageview", { page: $location.path() });
			}
			
			pageContentElement.addClass("loaded");
		});
		
		$rootScope.$on("$stateChangeStart", function () {
			pageContentElement.removeClass("loaded");
		});
		
		// Force login for all admin states
		$rootScope.$on("$stateChangeStart", function(evt, toState, toParams) {
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
	app.config(["$httpProvider", "$locationProvider", "$compileProvider", "$urlRouterProvider", "$urlMatcherFactoryProvider", "$stateProvider", "ezfbProvider", function ($httpProvider, $locationProvider, $compileProvider, $urlRouterProvider, $urlMatcherFactoryProvider, $stateProvider, ezfbProvider) {
		// Manda o JSESSIONID sempre
		$httpProvider.defaults.withCredentials = true;
		
		$httpProvider.interceptors.push("errorInterceptor");
		
		// Setting HTML 5 mode (bretty urls)
		$locationProvider.html5Mode(html5mode);
		
		// Show debug stuff?
		$compileProvider.debugInfoEnabled(debug);
		
		// A "404" will go to /
		$urlRouterProvider.otherwise("/");
		
		// Trailling "/" are optional 
		$urlMatcherFactoryProvider.strictMode(false);
		
		// Configura o ezfb
		ezfbProvider.setLocale("pt_BR");
		ezfbProvider.setInitParams({ appId: "1579957135597873" });
		
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
			controller: "LoginController",
			params: {
				nextState: { name: "admin.index" }, 
				nextStateParams: {}
			}
		})
		
		.state("admin", {
			url: "/admin",
			"abstract": true,
			templateUrl: "/fragments/admin.html",
			controller: ["$scope", "$http", "$state", "$interval", function ($scope, $http, $state, $interval) {
				var update = function () {
					$http.get(apiEndpoint + "/profile/me").then(function (response) {
						if (angular.isObject(response.data)) {
							$scope.setUser(response.data);
						}
						else {
							$state.go("admin.sair");
						}
					}, function () {
						$state.go("admin.sair");
					});
				};
				
				// Atualiza a cada 5 minutos
				var updateInterval = $interval(update, 300000);
				
				$scope.$on("$destroy", function () {
					$interval.cancel(updateInterval);
				});
			}]
		})
		.state("admin.index", {
			url: "",
			templateUrl: "/fragments/admin.index.html",
			controller: "AdminIndexController"
		})
		.state("admin.profile", {
			url: "/profile",
			templateUrl: "/fragments/admin.profile.html",
			controller: "AdminProfileController"
		})
		.state("admin.instituicoes", {
			url: "/instituicoes",
			templateUrl: "/fragments/admin.instituicoes.html",
			controller: "AdminInstituicoesController"
		})
		.state("admin.instituicao", {
			url: "/:slug",
			abstract: true,
			template: "<ui-view/>",
			controller: ["$scope", "$state", "institution", function ($scope, $state, institution) {
				if (!$scope.user.isInstitution) {
					$state.go("admin.index");
					return;
				}
				
				$scope.institution = institution;
				$scope.servicoPagamento = function (servico) {
					switch (servico) {
						case "moip": return "MoIP";
						case "pagseguro": return "PagSeguro";
						default: return servico;
					}
				};
			}],
			resolve: {
				institution: ["$q", "$http", "$stateParams", function ($q, $http, $stateParams) {
					if ($stateParams.institution) {
						return $stateParams.institution;
					}
					else {
						return requireHttp("adminInst", $q, $http, apiEndpoint + "/institution/" + $stateParams.slug);
					}
				}]
			},
			params: {
				slug: "",
				institution: null
			}
		})
		.state("admin.instituicao.ver", {
			url: "/view",
			templateUrl: "/fragments/admin.instituicao.ver.html",
			controller: "AdminInstituicaoVerController",
			resolve: {
				institutionPosts: ["$q", "$http", "$stateParams", function ($q, $http, $stateParams) {
					return requireHttp("adminInstVer", $q, $http, apiEndpoint + "/institution/" + $stateParams.slug + "/posts?sa=1");
				}]
			}
		})
		.state("admin.instituicao.editar", {
			url: "/edit",
			templateUrl: "/fragments/admin.instituicao.editar.html",
			controller: "AdminInstituicaoEditarController"
		})
		.state("admin.instituicao.post", {
			url: "/post/:postSlug",
			templateUrl: "/fragments/admin.instituicao.post.html",
			controller: "AdminInstituicaoPostController",
			resolve: {
				post: ["$q", "$http", "$stateParams", function ($q, $http, $stateParams) {
					if ($stateParams.post) {
						return $stateParams.post;
					}
					else {
						return requireHttp("adminInstPost", $q, $http, apiEndpoint + "/institution/" + $stateParams.slug);
					}
				}]
			},
			params: {
				postSlug: "",
				post: null
			}
		})
		.state("admin.sair", {
			url: "/sair",
			template: "",
			controller: ["$scope", "$http", "$state", function ($scope, $http, $state) {
				$http.get(apiEndpoint + "/auth/logout").then(function () {
					$scope.setUser({});
					$state.go("main.index");
				}, function () {
					$scope.setUser({});
					$state.go("main.index");
				});
			}]
		})
		
		.state("random", {
			url: "/random",
			template: "",
			controller: ["$state", "institution", function ($state, institution) {
				$state.go("main.instituicao.index", { slug: institution.slug, institution: institution });
			}],
			resolve: {
				institution: ["$q", "$http", function ($q, $http) {
					return requireHttp("randomInstitution", $q, $http, apiEndpoint + "/institution/random");
				}]
			}
		})
		
		.state("main.instituicao", {
			url: "/:slug",
			abstract: true,
			templateUrl: "/fragments/main.instituicao.html",
			controller: "InstituicaoController",
			resolve: {
				institution: ["$q", "$http", "$stateParams", function ($q, $http, $stateParams) {
					if ($stateParams.institution) {
						return $stateParams.institution;
					}
					else {
						return requireHttp("mainInst", $q, $http, apiEndpoint + "/institution/" + $stateParams.slug);
					}
				}]
			},
			params: {
				slug: "",
				institution: null
			}
		})
		
		.state("main.instituicao.index", {
			url: "",
			templateUrl: "/fragments/main.instituicao.index.html"
		})
		
		.state("main.instituicao.doar", {
			url: "/doar",
			templateUrl: "/fragments/main.instituicao.doar.html",
			controller: "InstituicaoDoarController"
		})
		
		.state("main.instituicao.post", {
			url: "/:postSlug",
			templateUrl: "/fragments/main.instituicao.post.html",
			controller: "InstitutionPostController",
			resolve: {
				post: ["$q", "$http", "$stateParams", function ($q, $http, $stateParams) {
					if ($stateParams.post) {
						return $stateParams.post;
					}
					else {
						return requireHttp("mainInstPost", $q, $http, apiEndpoint + "/institution/" + $stateParams.slug + "/" + $stateParams.postSlug);
					}
				}]
			},
			params: {
				postSlug: "",
				post: null
			}
		});
	}]);
})(angular, window);