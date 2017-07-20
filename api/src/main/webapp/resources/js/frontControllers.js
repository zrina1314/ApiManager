/**
 * 前端controller
 */
var webModule = angular.module("webModule", []);
//以html形式输出
webModule.filter("trustHtml",function($sce){
	 return function (input){ return $sce.trustAsHtml(input); } ;
});
webModule.filter("encodeURL",function($sce){
	 return function (input){ 
		 if(input){
		 input = replaceAll(input,"\\/", "CA_FXG");
		 input = replaceAll(input,"\\+", "CA_ADD");
		 input = replaceAll(input,"\\-", "CA_DES");
		 input = replaceAll(input,"\\&", "CA_AND");
		 input = replaceAll(input,"\\|", "CA_HZ");
		 input = replaceAll(input,"\\{", "CA_DKHS");
		 input = replaceAll(input,"\\}", "CA_DKHE");
		 input = replaceAll(input,"\\?", "CA_WH");
		 input = replaceAll(input,"\\*", "CA_XH");
		 input = replaceAll(input,"\\#", "CA_JH");
		 input = replaceAll(input,"\\:", "CA_MH");
		 input = replaceAll(input,"\\.", "CA_DH");
		 }
		 return input;
	} ;
});
webModule.filter("decodeURL",function($sce){
		 return function (input){
			 if(input){
			 input = replaceAll(input, "CA_FXG","\/");
			 input = replaceAll(input, "CA_ADD","\+");
			 input = replaceAll(input, "CA_DES","\-");
			 input = replaceAll(input, "CA_AND","\&");
			 input = replaceAll(input, "CA_HZ","\|");
			 input = replaceAll(input, "CA_DKHS","\{");
			 input = replaceAll(input, "CA_DKHE","\}");
			 input = replaceAll(input, "CA_WH","\?");
			 input = replaceAll(input, "CA_XH","\*");
			 input = replaceAll(input, "CA_JH","\#");
			 input = replaceAll(input, "CA_MH","\:");
			 input = replaceAll(input, "CA_DH","\.");
			}
		 return input;
	 } ;
});
webModule.filter("removeLast",function(){
	return function (value) {
		if(!value)
			return "";
		else{
			return value.substring(0,value.length-1);
		}
	}
});
// 数据字典flag转中文
webModule.filter("directoryFlagName",function(){
	return function (value) {
		if(value=='primary')
			return "主键";
		else if(value=='foreign')
			return "外键";
		else if(value=='associate')
			return "关联";
		else
			return "普通";
	}
});
// 背景色
webModule.filter("directoryBG",function(){
	return function (value) {
		if(value=='primary')
			return "bg-danger";
		else if(value=='foreign')
			return "bg-success";
		else if(value=='associate')
			return "bg-info";
		else
			return "";
	}
});
/***
 * 设置一个空的Controller，该Controller下的数据直接调用app.js中$rootScope 中的方法
 * 初始化不需要加载数据
 */
webModule.controller('detailCtrl', function($scope, $http, $state, $stateParams,httpService) {
});
webModule.controller('frontSearchCtrl', function($rootScope,$scope, $http, $state, $stateParams,httpService) {
	$scope.getData = function(page) {
		if(!$stateParams.keyword)
			$stateParams.keyword ="";
		params = "iUrl=frontSearch.do|iPost=POST|iLoading=FLOAT|iParams=&keyword="+ $stateParams.keyword;
		$rootScope.getBaseData($scope,$http,params,page);
    };
    $scope.getData();
});
/**************************错误码列表****************************/
mainModule.controller('errorCtrl', function($rootScope,$scope, $http, $state, $stateParams,httpService) {
	$scope.getData = function(page,setPwd) {
		//setPwd不为空，表示用户输入了密码，需要记录至cookie中
		if(setPwd) setPassword();
		var params ="&password="+unescapeAndDecode('password');
		params +="&visitCode="+unescapeAndDecode('visitCode');
		params = "iUrl=front/error/list.do|iLoading=FLOAT|iPost=true|iParams=&projectId=" +
			$stateParams.projectId +"&errorMsg=" + $stateParams.errorMsg+"&errorCode=" + $stateParams.errorCode + params;
		$rootScope.getBaseData($scope,$http,params,page);
    };
    $scope.getData();
});
/*************************数据字典列表******************************/
mainModule.controller('frontDictCtrl', function($rootScope,$scope, $http, $state, $stateParams,httpService) {
	$scope.getData = function(page,setPwd) {
		var params = "iUrl=front/article/diclist.do|iLoading=FLOAT|iPost=POST|iParams=&moduleId="+$stateParams.moduleId+"&name="+$stateParams.search;
		//setPwd不为空，表示用户输入了密码，需要记录至cookie中
		if(setPwd) setPassword();
		params +="&password="+unescapeAndDecode('password');
		params +="&visitCode="+unescapeAndDecode('visitCode');
		$rootScope.getBaseData($scope,$http,params,page);
    };
    $scope.getData();
});
/**************************article列表****************************/
mainModule.controller('fontArticleCtrl', function($rootScope,$scope, $http, $state, $stateParams,httpService) {
	$scope.getData = function(page,setPwd) {
		var params = "iUrl=front/article/list.do|iLoading=FLOAT|iPost=POST|iParams=&type=" + $stateParams.type
		+"&moduleId="+$stateParams.moduleId+"&name="+$stateParams.name+"&category="+$stateParams.category;
		if(setPwd) setPassword();
		params +="&password="+unescapeAndDecode('password');
		params +="&visitCode="+unescapeAndDecode('visitCode');
		$rootScope.getBaseData($scope,$http,params,page);
    };
    $scope.getData();
});
/**
 * article详情（数据字典，网站页面，文章）
 */
webModule.controller('articleDetailCtrl', function($rootScope,$scope, $http, $state, $stateParams,httpService) {
	$scope.getData = function(page,setPwd) {
		//setPwd不为空，表示用户输入了密码，需要记录至cookie中
		if(setPwd) setPassword();
		var params = "iLoading=FLOAT|iUrl=front/article/detail.do?id="+$stateParams.id+"&currentPage="+page;
		params +="&password="+unescapeAndDecode('password');
		params +="&visitCode="+unescapeAndDecode('visitCode');
		httpService.callHttpMethod($http,params).success(function(result) {
			var isSuccess = httpSuccess(result,'iLoading=FLOAT');
			if(!isJson(result)||isSuccess.indexOf('[ERROR]') >= 0){
				 $rootScope.error = isSuccess.replace('[ERROR]', '');
				 $rootScope.dictionary = null;
				 $rootScope.webpage = null;
				 $rootScope.model = null;//初始化评论对象
			 }else{
				 $rootScope.webpage = result.data;
				 $rootScope.page = result.page;
				 $rootScope.model = result.others.comment;//初始化评论对象
				 $rootScope.comments = result.others.comments;//评论列表
				 $rootScope.commentCode = result.others.commentCode;//游客评论是否需要输入验证码
				 $rootScope.others = result.others;//导航路径
				 //如果是数据字典，则将内容转为json
				 if($rootScope.webpage.content!=''&&$rootScope.webpage.type=="DICTIONARY"){
					 $rootScope.dictionary = eval("("+$rootScope.webpage.content+")");
			     }
			 }
		});
    };
    $scope.getData(1);
});
/**
 * 资源列表
 */
webModule.controller('frontProjectCtrl', function($rootScope,$scope, $http, $state, $stateParams,httpService) {
	$scope.getData = function(page,setPwd) {
		var params = "iUrl=front/project/list.do|iLoading=FLOAT|iPost=true|iParams=&myself="+$stateParams.myself+"&name="+$stateParams.name;
		$rootScope.getBaseData($scope,$http,params,page);
    };
    $scope.getData();
});
/**
 * 模块列表
 */
webModule.controller('frontModuleCtrl', function($rootScope,$scope, $http, $state, $stateParams,$location,$http ,httpService) {
	$scope.getData = function(page,setPwd) {
		//setPwd不为空，表示用户输入了密码，需要记录至cookie中
		if(setPwd) setPassword();
		
		var url = $location.absUrl();
		var projectId = url.substr(url.indexOf("#")+2,url.length).split("/")[0];
		var params = "&projectId=" + projectId;
		params +="&password="+unescapeAndDecode('password');
		params +="&visitCode="+unescapeAndDecode('visitCode');
		params = "iUrl=front/module/list.do|iLoading=FLOAT|iParams="+params;
		httpService.callHttpMethod($http,params).success(function(result) {
			var isSuccess = httpSuccess(result,'iLoading=FLOAT');
			if(!isJson(result)||isSuccess.indexOf('[ERROR]') >= 0){
				 $rootScope.error = isSuccess.replace('[ERROR]', '');
				 $rootScope.moduleList = null;
			 }else{
				 $rootScope.moduleList = result.data;
				 $rootScope.others = result.others;
				 $rootScope.project = result.others.project;
			 }
		});
    };
    $scope.getData();
});
/**
 * 模块列表
 */
webModule.controller('frontModuleMenuCtrl', function($rootScope,$scope, $http, $state, $stateParams,$location,$http ,httpService) {
	$scope.getData = function(page,setPwd) {
		//setPwd不为空，表示用户输入了密码，需要记录至cookie中
		if(setPwd) setPassword();
		
		var url = $location.absUrl();
		var projectId = url.substr(url.indexOf("#")+2,url.length).split("/")[0];
		var params = "iUrl=front/module/menu.do|iLoading=FLOAT|iParams="+ "&projectId=" + projectId;
		httpService.callHttpMethod($http,params).success(function(result) {
			var isSuccess = httpSuccess(result,'iLoading=FLOAT');
			if(!isJson(result)||isSuccess.indexOf('[ERROR]') >= 0){
				 $rootScope.error = isSuccess.replace('[ERROR]', '');
				 $rootScope.projectMenu = null;
				 $rootScope.project = null;
			 }else{
				 $rootScope.projectMenu = result.data;
				 $rootScope.project = result.others.project;
			 }
		});
    };
    $scope.getData();
});


/**
 * 接口列表
 */
webModule.controller('frontInterfaceCtrl', function($rootScope,$scope, $http, $state, $stateParams,httpService) {
	$scope.getData = function(page,setPwd) {
		var params = "&interfaceName=" + $stateParams.interfaceName + "&url="+ $stateParams.url + "&moduleId="+ $stateParams.moduleId;
		//setPwd不为空，表示用户输入了密码，需要记录至cookie中
		if(setPwd) setPassword();
		params +="&password="+unescapeAndDecode('password');
		params +="&visitCode="+unescapeAndDecode('visitCode');
		params = "iUrl=front/interface/list.do|iLoading=FLOAT|iPost=true|iParams="+params;
		$rootScope.getBaseData($scope,$http,params,page);
    };
    $scope.getData();
});

/**
 * 接口详情
 * 不需要打开模态框，所以不能调用$rootScope中的getBaseData()
 */
webModule.controller('interfaceDetailCtrl', function($rootScope,$scope, $http, $state, $stateParams,httpService) {
	$scope.getData = function(page,setPwd) {
		//setPwd不为空，表示用户输入了密码，需要记录至cookie中
		if(setPwd) setPassword();
		var params = "iUrl=front/interface/detail.do|iLoading=FLOAT|iParams=&id="+$stateParams.id;
		params +="&password="+unescapeAndDecode('password');
		params +="&visitCode="+unescapeAndDecode('visitCode');
		httpService.callHttpMethod($http,params).success(function(result) {
			var isSuccess = httpSuccess(result,'iLoading=FLOAT');
			if(!isJson(result)||isSuccess.indexOf('[ERROR]') >= 0){
				 $rootScope.error = isSuccess.replace('[ERROR]', '');
				 $rootScope.model = null;
			 }else{
				 $rootScope.model = result.data;
				 $rootScope.model.fullUrl = $rootScope.model.moduleUrl +  $rootScope.model.url;
				 $rootScope.versions = result.others.versions;
				 $rootScope.errors = eval("("+result.data.errors+")");
				 
				 // 如果param以form=开头，表示为form表单参数
				 if(result.data.param.length>5 && result.data.param.substring(0,5)=="form="){
					 $rootScope.formParams = eval("("+result.data.param.substring(5)+")");
				 }else{
					 $rootScope.model.customParams = result.data.param;
					 $rootScope.formParams = null;
				 }
				 
				 $rootScope.headers = eval("("+result.data.header+")");
				 
				 $rootScope.responseParams = eval("("+result.data.responseParam+")");
				 $rootScope.paramRemarks = eval("("+result.data.paramRemark+")");
				 $rootScope.others = result.others;
				 if(result.data.method)// 调试页面默认显示method中第一个
					 $rootScope.model.debugMethod = result.data.method.split(",")[0];
			 }
		});
    };
    $scope.getDebugResult= function() {
    	$rootScope.model.headers = getParamFromTable("debugHeader");
		$rootScope.model.params =getParamFromTable("debugParams");
    	var params = "iUrl=front/interface/debug.do|iLoading=FLOAT|iPost=POST|iParams=&"+$.param($rootScope.model);
		httpService.callHttpMethod($http,params).success(function(result) {
			var isSuccess = httpSuccess(result,'iLoading=FLOAT');
			if(!isJson(result)||isSuccess.indexOf('[ERROR]') >= 0){
				 $rootScope.error = isSuccess.replace('[ERROR]', '');
				 $rootScope.model = null;
			 }else{
				 $rootScope.model.debugResult = result.data.debugResult;
				 
				 $rootScope.jsonformat("debugResult",false);
				 $rootScope.others = result.others;
			 }
		});
    };
    $scope.getData();
});

webModule.controller('frontSourceDetailCtrl', function($rootScope,$scope, $http, $state, $stateParams,httpService) {
	$scope.getData = function(page,setPwd) {
		//setPwd不为空，表示用户输入了密码，需要记录至cookie中
		if(setPwd) setPassword();
		var params = "iLoading=FLOAT|iUrl=front/source/detail.do?id="+$stateParams.id;
		params +="&password="+unescapeAndDecode('password');
		params +="&visitCode="+unescapeAndDecode('visitCode');
		httpService.callHttpMethod($http,params).success(function(result) {
			var isSuccess = httpSuccess(result,'iLoading=FLOAT');
			if(!isJson(result)||isSuccess.indexOf('[ERROR]') >= 0){
				 $rootScope.error = isSuccess.replace('[ERROR]', '');
				 $rootScope.source = null;
			 }else{
				 $rootScope.source = result.data;
			 }
		});
    };
    $scope.getData();
});
/**
 * 资源列表
 */
webModule.controller('frontSourceCtrl', function($rootScope,$scope, $http, $state, $stateParams,httpService) {
	$scope.getData = function(page,setPwd) {
		//setPwd不为空，表示用户输入了密码，需要记录至cookie中
		if(setPwd) setPassword();
		var params = "iUrl=front/source/list.do|iLoading=FLOAT|iParams=&moduleId="+$stateParams.moduleId;
		params +="&password="+unescapeAndDecode('password');
		params +="&visitCode="+unescapeAndDecode('visitCode');
		$rootScope.getBaseData($scope,$http,params,page);
    };
    $scope.getData();
});



/***
 * 前端页面初始化，加载系统设置，菜单等
 */
webModule.controller('fontInit', function($rootScope,$scope, $http, $state, $stateParams,httpService) {
	$scope.getData = function(page,setPwd) {
		var params = "iUrl=front/init.do|iLoading=FLOAT"; //  表示查询所有
		httpService.callHttpMethod($http,params).success(function(result) {
			var isSuccess = httpSuccess(result,'iLoading=FLOAT');
			if(!isJson(result)||isSuccess.indexOf('[ERROR]') >= 0){
				alert("系统初始化异常："+isSuccess.replace('[ERROR]', ''));
			 }
			$rootScope.settings = result.data.settingMap;
			$rootScope.sessionAdminName = result.data.sessionAdminName;
			$rootScope.fontMenus = result.data.menuList;
		});
    };
    $scope.loginOut = function(){
		callAjaxByName("iUrl=back/loginOut.do|isHowMethod=updateDiv|iLoading=false|ishowMethod=doNothing|iAsync=false");
		location.reload();
	}
    $scope.getData();
});

/****************************************     以下为扩展功能    **************************************************/

/**
 * 接口列表
 */
webModule.controller('frontInterfaceEgmasCtrl', function($rootScope,$scope, $http, $state, $stateParams,$http ,httpService) {
	$scope.getData = function(page,setPwd) {
		//setPwd不为空，表示用户输入了密码，需要记录至cookie中
		if(setPwd) setPassword();
		
		
		var params = "&interfaceName=" + $stateParams.interfaceName + "&requestMethod="+$stateParams.requestMethod+"&url="+ $stateParams.url + "&moduleId="+ $stateParams.moduleId;
		
		if($("#customModule").val()!=null&&$("#customModule").val()!=''){
			params += "&customModule=" + $("#customModule").val();
		}
		if($("#responsiblePerson").val()!=null&&$("#responsiblePerson").val()!=''){
			params += "&responsiblePerson=" + $("#responsiblePerson").val();
		}
		params +="&password="+unescapeAndDecode('password');
		params +="&visitCode="+unescapeAndDecode('visitCode');
		params = "iUrl=front/interfaceEgmas/list.do|iLoading=FLOAT|iParams="+params;
		$rootScope.getBaseData($scope,$http,params,page);
    };
    $scope.getData();
});

/**
 * 接口详情
 * 不需要打开模态框，所以不能调用$rootScope中的getBaseData()
 */

webModule.controller('interfaceEgmasDetailCtrl', function($rootScope,$scope, $http, $state, $stateParams,$http ,httpService) {
	$scope.getData = function(page,setPwd) {
		//setPwd不为空，表示用户输入了密码，需要记录至cookie中
		if(setPwd) setPassword();
		var params = "iUrl=front/interfaceEgmas/detail.do|iLoading=FLOAT|iParams=&id="+$stateParams.id;
		params +="&password="+unescapeAndDecode('password');
		params +="&visitCode="+unescapeAndDecode('visitCode');
		httpService.callHttpMethod($http,params).success(function(result) {
			var isSuccess = httpSuccess(result,'iLoading=FLOAT');
			if(!isJson(result)||isSuccess.indexOf('[ERROR]') >= 0){
				 $rootScope.error = isSuccess.replace('[ERROR]', '');
				 $rootScope.model = null;
			 }else{
				 $rootScope.error = null;
				 $rootScope.model = result.data;
				 $rootScope.versions = result.others.versions;
				 $rootScope.errors = eval("("+result.data.errors+")");
				 
				 // 如果param以form=开头，表示为form表单参数
				 if(result.data.param.length>5 && result.data.param.substring(0,5)=="form="){
					 $rootScope.formParams = eval("("+result.data.param.substring(5)+")");
				 }else{
					 $rootScope.model.customParams = result.data.param;
				 }
				 
				 $rootScope.headers = eval("("+result.data.header+")");
				 $rootScope.responseParams = eval("("+result.data.responseParam+")");
				 if(result.data.method=="EGMAS")
				 {
					 var headersJsVar = "";
					 var paramsJsVar = "";
					 try
					 { 
						 headersJsVar = disposeHeaderToJson($rootScope.headers);
						 //处理头信息 JS
						 $rootScope.model.headersJS = format(headersJsVar);
					 } catch (e)
					 {
						 $rootScope.model.headersJS = headersJsVar;
					 }
					 try
					 { 
						 if(result.data.param.length>5 && result.data.param.substring(0,5)=="form="){
							 paramsJsVar =disposeHeaderToJson($rootScope.formParams);
						 }else{
							 paramsJsVar =disposeHeaderToJson($rootScope.responseParams);
						 }
						 paramsJsVar = format(paramsJsVar);
						 $rootScope.model.paramsJS =paramsJsVar;
					 } catch (e)
					 {
						 $rootScope.model.paramsJS =paramsJsVar;
					 }
				 }
				 
				 $rootScope.others = result.others;
				 if(result.data.method)// 调试页面默认显示method中第一个
					 $rootScope.model.debugMethod = result.data.method.split(",")[0];
			 }
		});
    };
    $scope.getDebugResult= function() {
    	$rootScope.model.headers = getParamFromTable("debugHeader");
		$rootScope.model.params =getParamFromTable("debugParams");
    	if($rootScope.model.debugMethod=="EGMAS"){
    		$rootScope.model.requestSourceName = $("#egmasSource").val();
    		$rootScope.model.headers  = $rootScope.model.headersJS;
    		$rootScope.model.params  =  $rootScope.model.paramsJS;
    	}
    	var params = "iUrl=front/interfaceEgmas/debug.do|iLoading=FLOAT|iPost=POST|iParams=&"+$.param($rootScope.model);
    	httpService.callHttpMethod($http,params).success(function(result) {
			var isSuccess = httpSuccess(result,'iLoading=FLOAT');
			if(!isJson(result)||isSuccess.indexOf('[ERROR]') >= 0){
				 $rootScope.error = isSuccess.replace('[ERROR]', '');
				 $rootScope.model = null;
			 }else{
				 $rootScope.error = null;
				 try
				 { 
					 var debugResultObj=eval("("+result.data.debugResult+")");
					 if(isJson(debugResultObj))
						 $rootScope.model.debugResult = format(result.data.debugResult);
					 else
						 $rootScope.model.debugResult = result.data.debugResult;
				 }
				 catch (e)
				 {
					 $rootScope.model.debugResult = result.data.debugResult;
				 } 
				 $rootScope.others = result.others;
				 if(result.data.interval!=null)
					 $rootScope.model.interval = result.data.interval;
			 }
		});
    };
    $scope.getData();
});

/**
 * 责任田列表
 */
webModule.controller('frontAppPageCtrl', function($rootScope,$scope, $http, $state, $stateParams,$http ,httpService) {    
    $scope.getData = function(page) {
		var params = "iUrl=front/appPage/list.do|iLoading=FLOAT|iParams=&module="+$stateParams.module+"&person="+$stateParams.person;
		$rootScope.getBaseData($scope,$http,params,page);
    };
    $scope.getData();
});

/**
 * EGMAS责任田列表
 */
webModule.controller('frontResponsibilityFieldsEgmasCtrl', function($rootScope,$scope, $http, $state, $stateParams,$http ,httpService) {    
	$scope.getData = function(page,setPwd) {
		var params = "&responsiblePerson="+$stateParams.responsiblePerson+ "&customModule="+ $stateParams.customModule;
		params = "iUrl=front/interfaceEgmas/responsibilityList.do|iLoading=FLOAT|iParams="+params;
		$rootScope.getBaseData($scope,$http,params,page);
    };
    $scope.downloadExcel= function() {
    	alert("待优化……");
    };
    $scope.getData();
});

/**
 * 验证码查询
 */
webModule.controller('frontToolsCtrl', function($rootScope,$scope, $http, $state, $stateParams,$http ,httpService) {
	$scope.queryCaptch = function() {
		var queryMobileNo = $stateParams.queryMobileNo;
		if(queryMobileNo==null || "" == queryMobileNo.trim()){
			alert("手机号不能为空");
			return ;
		}
		queryMobileNo = parseInt(queryMobileNo);
		if(!angular.isNumber(queryMobileNo) || queryMobileNo.toString() =="NaN"){
			alert("手机号格式不正确");
			return ;
		}
		var params = "iLoading=FLOAT|iUrl=front/tools/queryCaptcha.do?mobile="+ $stateParams.queryMobileNo;
		httpService.callHttpMethod($http,params).success(function(result) {
			var isSuccess = httpSuccess(result,'iLoading=FLOAT');
			if(!isJson(result)||isSuccess.indexOf('[ERROR]') >= 0){
				 $rootScope.error = isSuccess.replace('[ERROR]', '');
				 $rootScope.userMobiles = null;
			 }else{
				 $rootScope.error = null;
				 $rootScope.userMobiles = result.data.userMobiles;
			 }
		});
    };
    $scope.getData();
});

/**
 * EGMAS 加解密
 */
webModule.controller('frontToolsEncryptCtrl', function($rootScope,$scope, $http, $state, $stateParams,$http ,httpService,encryptionService) {
	$stateParams.encryptText="";
	$stateParams.decryptText="";
	$scope.encrypt = function() {
		$stateParams.decryptText = encryptionService.encrypt($stateParams.encryptText);
    };
    
	$scope.decrypt = function() {
		$stateParams.decryptText = encryptionService.decrypt($stateParams.encryptText);
    };
    
	$scope.clear = function() {
		$stateParams.decryptText = "";
    };
    
	$scope.interchange = function() {
		var tempEncryptText = $stateParams.encryptText;
		$stateParams.encryptText = $stateParams.decryptText;
		$stateParams.decryptText = tempEncryptText;
    };
});



