
/**
 * 配置路由。
 * 注意这里采用的是ui-router这个路由，而不是ng原生的路由。
 * ng原生的路由不能支持嵌套视图，所以这里必须使用ui-router。
 * @param  {[type]} $stateProvider
 * @param  {[type]} $urlRouterProvider
 * @return {[type]}
 */
app.config(function($stateProvider, $urlRouterProvider) {
	$urlRouterProvider.otherwise('/web/article/detail/web/PAGE/WELCOME');
	$stateProvider.state('frontSearchCtrl', {
		url : '/frontSearch/:keyword',
		views : {
			'main' :{
				templateUrl : function($stateParems){
					return 'resources/html/frontHtml/searchResult.tpl.html';
				}
			},
			'page@frontSearchCtrl' : {
				templateUrl : 'resources/html/frontHtml/page.tpl.html'
			}
		}
	}).state('frontError', {
		url : '/:projectId/error/list',
		views : {
			'main' : {
				templateUrl : 'resources/html/frontHtml/errorList.tpl.html'
			},
			'page@frontError' : {
				templateUrl : 'resources/html/frontHtml/page.tpl.html'
			}
		}
	}).state('frontModuleCtrl', {
		url : '/:projectId/module/list',
		views : {
			'main' : {
				templateUrl : 'resources/html/frontHtml/moduleList.tpl.html'
			}
		}
	}).state('frontInterfaceCtrl', {
		url : '/:projectId/interface/list/:moduleId',
		views : {
			'main' : {
				templateUrl : 'resources/html/frontHtml/interfaceList.tpl.html'
			},
			'page@frontInterfaceCtrl' : {
				templateUrl : 'resources/html/frontHtml/page.tpl.html'
			}
		}
	}).state('frontInterfaceDetailCtrl', {
		url : '/:projectId/front/interfaceDetail/:id',
		views : {
			'main' : {
				templateUrl : 'resources/html/frontHtml/interfaceDetail.tpl.html'
			}
		}
	}).state('frontInterfaceDebugCtrl', {
		url : '/:projectId/front/interface/debug/:id',
		views : {
			'main' :{
				templateUrl : function($stateParems){
					return 'resources/html/frontHtml/interfaceDebug.tpl.html';
				}
			}
		}
	}).state('frontInterfaceCxCtrl', {
		url : '/:projectId/interfaceCx/list/:moduleId',
		views : {
			'main' : {
				templateUrl : 'resources/html/frontHtml/interfaceCxList.tpl.html'
			},
			'page@frontInterfaceCtrl' : {
				templateUrl : 'resources/html/frontHtml/page.tpl.html'
			}
		}
	}).state('frontInterfaceCxDetailCtrl', {
		url : '/:projectId/front/interfaceCxDetail/:id',
		views : {
			'main' : {
				templateUrl : 'resources/html/frontHtml/interfaceCxDetail.tpl.html'
			}
		}
	}).state('frontInterfaceCxDebugCtrl', {
		url : '/:projectId/front/interfaceCx/debug/:id',
		views : {
			'main' :{
				templateUrl : function($stateParems){
					return 'resources/html/frontHtml/interfaceCxDebug.tpl.html';
				}
			}
		}
	}).state('frontInterfaceEgmasCtrl', {
		url : '/:projectId/interfaceEgmas/list/:moduleId',
		views : {
			'main' : {
				templateUrl : 'resources/html/frontHtml/interfaceEgmasList.tpl.html'
			},
			'page@frontInterfaceEgmasCtrl' : {
				templateUrl : 'resources/html/frontHtml/page.tpl.html'
			}
		}
	}).state('frontInterfaceEgmasDetailCtrl', {
		url : '/:projectId/front/interfaceEgmasDetail/:id',
		views : {
			'main' : {
				templateUrl : 'resources/html/frontHtml/interfaceEgmasDetail.tpl.html'
			}
		}
	}).state('frontInterfaceEgmasDebugCtrl', {
		url : '/:projectId/front/interfaceEgmas/debug/:id',
		views : {
			'main' :{
				templateUrl : function($stateParems){
					return 'resources/html/frontHtml/interfaceEgmasDebug.tpl.html';
				}
			}
		}
	}).state('frontSourceList', {
		url : '/:projectId/source/list/:moduleId',
		views : {
			'main' : {
				templateUrl : 'resources/html/frontHtml/sourceList.tpl.html'
			},
			'page@frontSourceList' : {
				templateUrl : 'resources/html/frontHtml/page.tpl.html'
			}
		}
	}).state('frontProjectList', {
		url : '/project/list/:myself/NULL',
		views : {
			'main' : {
				templateUrl : 'resources/html/frontHtml/projectList.tpl.html'
			},
			'page@frontProjectList' : {
				templateUrl : 'resources/html/frontHtml/page.tpl.html'
			}
		}
	}).state('frontToolsCtrl', {
		url : '/tools/captcha',
		views : {
			'main' : {
				templateUrl : 'resources/html/frontHtml/captcha.tpl.html'
			}
		}
	}).state('frontToolsEncryptCtrl', {
		url : '/tools/encrypt',
		views : {
			'main' : {
				templateUrl : 'resources/html/frontHtml/encrypt.tpl.html'
			}
		}
	}).state('frontAppPageCtrl', {
		url : '/responsibility/list',
		views : {
			'main' : {
				templateUrl : 'resources/html/frontHtml/appPage.tpl.html'
			}
		}
	}).state('frontResponsibilityFieldsEGMASCtrl', {
		url : '/responsibility/egmas/list',
		views : {
			'main' : {
				templateUrl : 'resources/html/frontHtml/responsibilityFieldsEgmas.tpl.html'
			}
		}
	})
	/*********************前端项目主页*******************/
});