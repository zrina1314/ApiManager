var app = angular.module('app', [ 'ui.router', 'mainModule','webModule','interfaceMethods','textAngular']);
/**
 * 由于整个应用都会和路由打交道，所以这里把$state和$stateParams这两个对象放到$rootScope上，方便其它地方引用和注入。
 * 这里的run方法只会在angular启动的时候运行一次。
 * 
 * @param {[type]}
 *            $rootScope
 * @param {[type]}
 *            $state
 * @param {[type]}
 *            $stateParams
 * @return {[type]}
 */
app.run(function($rootScope, $state, $stateParams, $http, $timeout,httpService) {
	$rootScope.$state = $state;
	$rootScope.$stateParams = $stateParams;
	$rootScope.pick = [];
	
	$rootScope.loadPickByName = function loadPick(params,event,iCallBack,iCallBackParam) { 
		var iwidth = getValue(params,'iwidth');
		var iheight = getValue(params,'iheight');
		var radio = getValue(params,'radio');
		var tag = getValue(params,'tag');
		var code = getValue(params,'code');
		var type = getValue(params,'type');
		var iparams = getValue(params,'params');
		var showType = getValue(params,'showType');
		var def = getValue(params,'def');
		var tagName = getValue(params,'tagName');
		var iUrl = getValue(params,'iUrl');
		$rootScope.loadPick(event,iwidth,iheight,radio,tag,code,type,def,iparams,showType,iCallBack,iCallBackParam,tagName,iUrl);
	}
	$rootScope.loadPick = function loadPick(event,iwidth,iheight,radio,tag,code,type,def,params,showType,iCallBack,iCallBackParam,tagName,iUrl) { 
		/** *********加载选择对话框******************* */
		if(!iUrl)
			iUrl = "pick.do";
		if(!params)
			params='';
		if(!tagName)
			tagName='';
		if(showType!='0'){
			if(!showType||showType=='')
				showType=5;
		}
		$("#pickContent").html(loadText);
		// 事件，宽度，高度，是否为单选，html元素id，查询的code，查询的type，默认值，其他参数，回调函数，回调参数
		callAjaxByName("iUrl="+iUrl+"|isHowMethod=updateDiv|iParams=&type="
				+type+"&radio="+radio+"&code="+code+"&tag="+tag+"&tagName="+tagName+"&def="+def+params,iCallBack,iCallBackParam);
		if(tagName)
			lookUp('lookUp', event, iheight, iwidth ,showType,tagName);
		else
			lookUp('lookUp', event, iheight, iwidth ,showType,tag);
		showMessage('lookUp','false',false,-1);
	}
	$rootScope.getBaseData = function($scope,$http,params,page) {
		if(page) $scope.currentPage = page;
		if($scope.currentPage) params += "&currentPage="+$scope.currentPage;
		httpService.callHttpMethod($http,params).success(function(result) {
			var isSuccess = httpSuccess(result,'iLoading=FLOAT','0');
			if(!isJson(result)||isSuccess.indexOf('[ERROR]') >= 0){
				 $rootScope.error = isSuccess.replace('[ERROR]', '');
				 $rootScope.list = null;
			 }else{
				 $rootScope.error = null;
				 $rootScope.list = result.data;
				 $rootScope.page = result.page;
				 $rootScope.others=result.others;
				 $rootScope.deleteIds = ",";
			 }
		}).error(function(result) {
			lookUp('lookUp','',100,300,3);
			closeTip('[ERROR]未知异常，请联系开发人员查看日志', 'iLoading=PROPUP_FLOAT', 3);
			$rootScope.error = result;
			 
		});;
    };
	$rootScope.detail = function(title,iwidth,iurl,iParams,callBack) {
			// 打开编辑对话框
			openMyDialog(title,iwidth);
			var params = "iUrl="+iurl+"|iLoading=FLOAT";
			if(iParams)
				params += "|iParams="+iParams;
			httpService.callHttpMethod($http,params).success(function(result) {
				var isSuccess = httpSuccess(result,'iLoading=FLOAT');
				if(!isJson(result)||isSuccess.indexOf('[ERROR]') >= 0){
					 $rootScope.error = isSuccess.replace('[ERROR]', '');
					 $rootScope.model = null;
				 }else{
					 $rootScope.model = result.data;
					 $rootScope.error = null;
					 $rootScope.deleteIds = ",";
					 if(callBack)
						 callBack();
				 }
			}).error(function(result) {
				lookUp('lookUp','',100,300,3);
				closeTip('[ERROR]未知异常，请联系开发人员查看日志', 'iLoading=PROPUP_FLOAT', 3);
				$rootScope.error = result;
				 
			});;
	};
	$rootScope.detailCxInterface = function(title,iwidth,iurl,iParams,callBack) {
		// 打开编辑对话框
		openMyDialog(title,iwidth);
		var params = "iUrl="+iurl+"|iLoading=FLOAT";
		if(iParams)
			params += "|iParams="+iParams;
		httpService.callHttpMethod($http,params).success(function(result) {
			var isSuccess = httpSuccess(result,'iLoading=FLOAT');
			if(!isJson(result)||isSuccess.indexOf('[ERROR]') >= 0){
				 $rootScope.error = isSuccess.replace('[ERROR]', '');
				 $rootScope.model = null;
			 }else{
				 $rootScope.model = result.data;
				 $rootScope.model.paramRemarks =eval("(" +$rootScope.model.paramRemark + ")"); 
				 $rootScope.model.responseParamRemarks =eval("(" +$rootScope.model.responseParamRemark + ")");
				 $rootScope.error = null;
				 $rootScope.deleteIds = ",";
				 if(callBack)
					 callBack();
			 }
		}).error(function(result) {
			lookUp('lookUp','',100,300,3);
			closeTip('[ERROR]未知异常，请联系开发人员查看日志', 'iLoading=PROPUP_FLOAT', 3);
			$rootScope.error = result;
			 
		});;
};
	// 点击详情回调，清除编辑缓存页面的table
	$rootScope.initEditInterFace = function (){
		changeDisplay('interFaceDetail','copyInterFace');
		$("#eparam").addClass('none');
		$("#param").removeClass('none');
		$("#eheader").addClass('none');
		$("#header").removeClass('none');
		$("#responseEparam").addClass('none');
		$("#responseParam").removeClass('none');
	}
	// 点击拷贝接口详情回调
	$rootScope.copyInterface = function() {
		changeDisplay('copyInterFace','interFaceDetail');
	};
	$rootScope.changeDisplay = function(id1, id2) {
		changeDisplay(id1,id2);
	}
	$rootScope.del = function(iUrl,id,title){
		title = title? title:"确认要删除【"+id+"】？";
		if (confirm(title)) {
			var params = "iUrl="+iUrl+"|iLoading=TIP";
			httpService.callHttpMethod($http,params).success(function(result) {
				var isSuccess = httpSuccess(result,'iLoading=TIP')
				if(!isJson(result)||isSuccess.indexOf('[ERROR]') >= 0){
					 $rootScope.error = isSuccess.replace('[ERROR]', '');
				 }else{
					 /**
						 * 回调刷新当前页面数据
						 */
					 $rootScope.error = null;
					 $timeout(function() {
						 $("#refresh").click();
	                 })
				 }
			}).error(function(result) {
				closeTip('[ERROR]未知异常，请联系开发人员查看日志', 'iLoading=PROPUP', 3);
				$rootScope.error = result;
				 
			});;
	    }
	};
	// 选中某个选项
	$rootScope.checkboxSelect = function(checkValues,value){
		if( $rootScope[checkValues].indexOf(","+value+",")>=0 ){
			$rootScope[checkValues] = $rootScope[checkValues].replace(value+",","");
		}else{
			$rootScope[checkValues] = $rootScope[checkValues]+value+","
		}
	}
	// 全选，不选
	$rootScope.selectAll = function(id,name,list){
		selectAll(id, name);
		if($("#"+id).prop("checked")==true){ 
			$rootScope[name] = ",";
			for (var i=0;i<list.length;i++){
				$rootScope[name] = $rootScope[name] + list[i].id + "," ;
			}
		}else{
			$rootScope[name] = ",";
		}
	}
	
	$rootScope.submitForm = function(iurl,callBack,myLoading){
		/**
		 * 回调刷新当前页面数据
		 */
		if(callBack){
			callBack();
		}
		iLoading = "TIPFLOAT";
		if(myLoading){
			iLoading = myLoading;
		}
		var params = "iUrl="+iurl+"|iLoading="+iLoading+"|iPost=POST|iParams=&"+$.param($rootScope.model);
		httpService.callHttpMethod($http,params).success(function(result) {
			var isSuccess = httpSuccess(result,'iLoading='+iLoading);
			if(!isJson(result)||isSuccess.indexOf('[ERROR]') >= 0){
				 $rootScope.error = isSuccess.replace('[ERROR]', '');
			 }else if(result.success==1){
				 $rootScope.error = null;
				 $rootScope.model = result.data;
				 // 关闭编辑对话框
				 closeMyDialog('myDialog');
				 $timeout(function() {
					 $("#refresh").click();
                 })
			 }
		}).error(function(result) {
			closeTip('[ERROR]未知异常，请联系开发人员查看日志', 'iLoading='+iLoading, 3);
			$rootScope.error = result;
			 
		});
	}
	
	$rootScope.submitCxInterfaceForm = function(iurl,callBack,myLoading){
		/**
		 * 回调刷新当前页面数据
		 */
		if(callBack){
			callBack();
		}
		iLoading = "TIPFLOAT";
		if(myLoading){
			iLoading = myLoading;
		}
		var json = getParamFromTable('eparamRemarkTable');
		try {
			eval("(" + json + ")");
		} catch (e) {
			alert("输入有误，json解析出错：" + e);
			return;
		}
		$rootScope.model.paramRemark = json;
		
		var responseJson = getParamFromTable('eResponseParamRemarkTable');
		try {
			eval("(" + responseJson + ")");
		} catch (e) {
			alert("输入有误，json解析出错：" + e);
			return;
		}
		$rootScope.model.responseParamRemark = responseJson;
		
		var params = "iUrl="+iurl+"|iLoading="+iLoading+"|iPost=POST|iParams=&"+$.param($rootScope.model);
		httpService.callHttpMethod($http,params).success(function(result) {
			var isSuccess = httpSuccess(result,'iLoading='+iLoading);
			if(!isJson(result)||isSuccess.indexOf('[ERROR]') >= 0){
				 $rootScope.error = isSuccess.replace('[ERROR]', '');
			 }else if(result.success==1){
				 $rootScope.error = null;
				 $rootScope.model = result.data;
				 // 关闭编辑对话框
				 closeMyDialog('myDialog');
				 $timeout(function() {
					 $("#refresh").click();
                 })
			 }
		}).error(function(result) {
			closeTip('[ERROR]未知异常，请联系开发人员查看日志', 'iLoading='+iLoading, 3);
			$rootScope.error = result;
			 
		});
	}
	
	
	
	$rootScope.changeSequence = function(url,id,changeId){
		var params = "iUrl="+url+"|iLoading=FLOAT|iPost=POST|iParams=&id="+id+"&changeId="+changeId;
		httpService.callHttpMethod($http,params).success(function(result) {
			var isSuccess = httpSuccess(result,'iLoading=FLOAT')
			if(!isJson(result)||isSuccess.indexOf('[ERROR]') >= 0){
				 $rootScope.error = isSuccess.replace('[ERROR]', '');
			 }else if(result.success==1){
				 $rootScope.error = null;
				 // 关闭编辑对话框
				 $timeout(function() {
					 $("#refresh").click();
                 })
			 }
		}).error(function(result) {
			lookUp('lookUp','',100,300,3);
			closeTip('[ERROR]未知异常，请联系开发人员查看日志', 'iLoading=PROPUP_FLOAT', 3);
			$rootScope.error = result;
			 
		});
	}
	/**
	 * 发送请求工具方法
	 */
	$rootScope.sendRequest = function(url,myLoading){
		var iLoading = "FLOATTIP";
		if(myLoading){
			iLoading = myLoading;
		}
		var params = "iUrl="+url+"|iLoading="+iLoading+"|iPost=POST";
		httpService.callHttpMethod($http,params).success(function(result) {
			var isSuccess = httpSuccess(result,'iLoading='+iLoading)
			if(!isJson(result)||isSuccess.indexOf('[ERROR]') >= 0){
				 $rootScope.error = isSuccess.replace('[ERROR]', '');
			 }else if(result.success==1){
				 $rootScope.error = null;
				 $timeout(function() {
					 $("#refresh").click();
                 })
			 }
		}).error(function(result) {
			lookUp('lookUp','',100,300,3);
			closeTip('[ERROR]未知异常，请联系开发人员查看日志', 'iLoading=PROPUP_FLOAT', 3);
			$rootScope.error = result;
			 
		});
	}
	/** *********************是否显示操作按钮*********** */
	
	$rootScope.hasError = function(error,id){
		if(error && error!=''){
			$("#"+id).removeClass("ndis");
			return true;
		}else{
			if(!$("#"+id).hasClass("ndis"))
				$("#"+id).addClass("ndis");
			return false;
		}
	}
	$rootScope.showOperation = function(dataType,moduleId){
		var userRole = $("#sessionRoleIds").val();
		if((","+userRole+",").indexOf(",super,")>=0){
			return true;
		}
		var needAuth = dataType;
		if(moduleId)
			needAuth = needAuth+"_"+moduleId;
		var sessionAuth = $("#sessionAuth").val();
		if((","+sessionAuth+",").indexOf(","+needAuth+",")>=0){
			return true;
		}
		return false;
	}
	
	$rootScope.getDate = function(str){
		if(str && str.indexOf(".")>0)
			return new Date(str.split(".")[0].replace("-", "/").replace("-", "/"));
	}
	/**
	 * 提交数据字典时回调将表格数据转换为json
	 */
	$rootScope.preAddDictionary = function(){
		var content = getParamFromTable("content");
		$rootScope.model.content = content;
	}
	/**
	 * 查看日志详情回调，格式化数据
	 */
	$rootScope.logDetailFormat = function(){
		$rootScope.model.content  = format($rootScope.model.content);
	}
	/**
	 * 数据字典、文章编辑回调
	 */
	$rootScope.getFields = function() {
    		// 切换为默认编辑器
    		changeDisplay('defEditor','kindEditor');
	    	var content = "";
	    	if($rootScope.model.content!=''){
	    		// 如果是文章，eval会报错
	    		try{
	    			content = eval("("+$rootScope.model.content+")");
	    		}catch(e){}
	    	}
	    	$("#content").find("tbody").find("tr").remove();
	    	if(content!=null&&content!=""){
		    	var i=0;
		    	$.each(content, function (n, value) {
		    		i++;
		    		addOneField(value.name, value.type, value.notNull,value.flag, value.def, value.remark, value.rowNum);
		        });  
	    	}
	};
	$rootScope.jsonformat = function(id,tiperror){
		var result = format($rootScope.model[id],tiperror);
		if(result){
			$rootScope.model[id] = result;
		}
	}
	$rootScope.callAjaxByName = function(iurl){
		callAjaxByName(iurl);
	}
	/** ************markdown************ */
	$rootScope.markdownEtitor = function(href){
		$("#markdownDialog").css('display','block'); 
		document.getElementById("markdownFrame").src=href;
	}
	 $rootScope.iClose = function(id) {
	    	iClose(id);
	 };
	 /** ****静态化*************** */
	 $rootScope.staticize= function (id){
			callAjaxByName('iUrl=user/staticize/staticize.do?projectId='+id+'|iLoading=TIPFLOAT静态化中，请稍后...|ishowMethod=updateDivWithImg|iFormId=staticize-form');
	 }
	 $rootScope.downloadStaticize= function (id){
		 var params = "iUrl=user/staticize/downloadStaticize.do?projectId="+id+"|iLoading=TIPFLOAT静态化中，请稍后...|iPost=POST";
			httpService.callHttpMethod($http,params).success(function(result) {
				var isSuccess = httpSuccess(result,'iLoading=TIPFLOAT')
				if(!isJson(result)||isSuccess.indexOf('[ERROR]') >= 0){
					 $rootScope.error = isSuccess.replace('[ERROR]', '');
				 }else if(result.success==1){
					 $("#downloadUrl").html("操作成功，将自动下载，3s后若无反应，请点击：<a href='"+ result.data +"' target='_blank'>下载</a> 手动下载");
					 window.open(result.data);
				 }
			}).error(function(result) {
				lookUp('lookUp','',100,300,3);
				closeTip('[ERROR]未知异常，请联系开发人员查看日志', 'iLoading=TIPFLOAT', 3);
				$rootScope.error = result;
			});
	 }
});

