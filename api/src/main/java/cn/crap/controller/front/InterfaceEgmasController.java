package cn.crap.controller.front;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.crap.framework.JsonResult;
import cn.crap.framework.MyException;
import cn.crap.framework.base.BaseController;
import cn.crap.inter.service.table.IEgmasSourceService;
import cn.crap.inter.service.table.IInterfaceEgmasService;
import cn.crap.inter.service.table.IModuleService;
import cn.crap.inter.service.tool.ICacheService;
import cn.crap.model.EgmasSource;
import cn.crap.model.InterfaceEgmas;
import cn.crap.model.Module;
import cn.crap.utils.HttpPostGet;
import cn.crap.utils.HttpPostGetEgmas;
import cn.crap.utils.MyString;
import cn.crap.utils.Page;
import cn.crap.utils.Tools;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller("frontInterfaceEgmasController")
@RequestMapping("/front/interfaceEgmas")
public class InterfaceEgmasController extends BaseController<InterfaceEgmas> {

	@Autowired
	private IInterfaceEgmasService interfaceEgmasService;
	@Autowired
	private IModuleService moduleService;
	@Autowired
	private ICacheService cacheService;
	@Autowired
	private IEgmasSourceService egmasSourceService;

	@RequestMapping("/list.do")
	@ResponseBody
	public JsonResult webList(@ModelAttribute InterfaceEgmas interFace, @RequestParam(defaultValue = "1") Integer currentPage, String password, String visitCode) throws MyException {
		Page page = new Page(15);
		page.setCurrentPage(currentPage);

		return interfaceEgmasService.getInterfaceListJson(page, null, interFace, currentPage);
	}

	@RequestMapping("/responsibilityList.do")
	@ResponseBody
	public JsonResult webResponsibilityList(@ModelAttribute InterfaceEgmas interFace) throws MyException {
		return interfaceEgmasService.getInterfaceResponsibilityListToJsonResult(interFace);
	}

	@RequestMapping("/detail.do")
	@ResponseBody
	public JsonResult webDetail(@ModelAttribute InterfaceEgmas interFace, String password, String visitCode) throws MyException {
		interFace = interfaceEgmasService.get(interFace.getId());
		if (interFace != null) {

			/**
			 * 查询相同模块下，相同接口名的其它版本号
			 */
			List<InterfaceEgmas> versions = interfaceEgmasService.findByMap(Tools.getMap("moduleId", interFace.getModuleId(), "interfaceName", interFace.getInterfaceName(), "version|<>", interFace.getVersion()), null, null);
			Module module = cacheService.getModule(interFace.getModuleId());
			if (module != null) {
				module = moduleService.get(interFace.getModuleId());
			}
			return new JsonResult(1, interFace, null, Tools.getMap("versions", versions, "crumbs",
					Tools.getCrumbs(cacheService.getModuleName(interFace.getModuleId()), "#/" + interFace.getProjectId() + "/interface/list/" + interFace.getModuleId() + "/" + cacheService.getModuleName(interFace.getModuleId()), interFace.getInterfaceName(), "void"), "module", module));
		} else {
			throw new MyException("000012");
		}
	}

	@RequestMapping("/debug.do")
	@ResponseBody
	public JsonResult debug(@RequestParam String params, @RequestParam String headers, @RequestParam(defaultValue = "") String customParams, @RequestParam String debugMethod, @RequestParam String url, String moduleUrl, @RequestParam(defaultValue = "") String requestMethod,
			@RequestParam(defaultValue = "") String requestSourceName) throws Exception {
		if (!MyString.isEmpty(moduleUrl)) {
			if ("EGMAS".equals(debugMethod) && null != url && !"/".equals(url) && !"".equals(url)) {
			} else {
				url = moduleUrl + url;
			}
		}
		Map<String, String> httpParams = new HashMap<String, String>();
		Map<String, String> httpHeaders = new HashMap<String, String>();

		try {
			// 参数处理
			if (params != null && "" != params) {
				if ("[".equals(params.substring(0, 1))) {
					JSONArray jsonParams = JSONArray.fromObject(params);
					for (int i = 0; i < jsonParams.size(); i++) {
						JSONObject param = jsonParams.getJSONObject(i);
						for (Object paramKey : param.keySet()) {
							if (url.contains("{" + paramKey.toString() + "}")) {
								url = url.replace("{" + paramKey.toString() + "}", param.getString(paramKey.toString()));
							} else {
								httpParams.put(paramKey.toString(), param.getString(paramKey.toString()));
							}
						}
					}
				} else {
					JSONObject paramsJsObject = JSONObject.fromObject(params.toString());
					for (Object paramKey : paramsJsObject.keySet()) {
						if (url.contains("{" + paramKey.toString() + "}")) {
							url = url.replace("{" + paramKey.toString() + "}", paramsJsObject.getString(paramKey.toString()));
						} else {
							httpParams.put(paramKey.toString(), paramsJsObject.getString(paramKey.toString()));
						}
					}
				}
			}
		} catch (Exception e) {
			return new JsonResult(1, Tools.getMap("debugResult", "调试出错\r\n:参数错误，正确的参数必须是JSON格式\r\nmessage:" + e.getMessage()));
		}

		try {
			if (headers != null && "" != headers) {
				if ("[".equals(headers.substring(0, 1))) {
					JSONArray jsonHeaders = JSONArray.fromObject(headers);
					for (int i = 0; i < jsonHeaders.size(); i++) {
						JSONObject param = jsonHeaders.getJSONObject(i);
						for (Object paramKey : param.keySet()) {
							httpHeaders.put(paramKey.toString(), param.getString(paramKey.toString()));
						}
					}
				} else {
					JSONObject headersJsObject = JSONObject.fromObject(headers);
					for (Object paramKey : headersJsObject.keySet()) {
						httpHeaders.put(paramKey.toString(), headersJsObject.getString(paramKey.toString()));
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		// 如果自定义参数不为空，则表示需要使用post发送自定义包体
		if (!MyString.isEmpty(customParams) && !"EGMAS".equals(debugMethod)) {
			return new JsonResult(1, Tools.getMap("debugResult", HttpPostGet.postBody(url, customParams, httpHeaders)));
		} else if (!MyString.isEmpty(customParams) && "EGMAS".equals(debugMethod)) {
			JSONObject customParamsJsObject = JSONObject.fromObject(customParams);
			for (Object paramKey : customParamsJsObject.keySet()) {
				httpParams.put(paramKey.toString(), customParamsJsObject.getString(paramKey.toString()));
			}
		}

		try {
			switch (debugMethod) {
			case "POST":
				return new JsonResult(1, Tools.getMap("debugResult", HttpPostGet.post(url, httpParams, httpHeaders)));
			case "POST_CX":
				org.json.JSONObject jsonObject = new org.json.JSONObject(httpParams);
				httpHeaders.put("Content-Type", "application/json");
				httpHeaders.put("charset", "UTF-8");
				return new JsonResult(1, Tools.getMap("debugResult", HttpPostGet.postBody(url, jsonObject.toString(), httpHeaders)));
			case "GET":
				return new JsonResult(1, Tools.getMap("debugResult", HttpPostGet.get(url, httpParams, httpHeaders)));
			case "PUT":
				return new JsonResult(1, Tools.getMap("debugResult", HttpPostGet.put(url, httpParams, httpHeaders)));
			case "DELETE":
				return new JsonResult(1, Tools.getMap("debugResult", HttpPostGet.delete(url, httpParams, httpHeaders)));
			case "HEAD":
				return new JsonResult(1, Tools.getMap("debugResult", HttpPostGet.head(url, httpParams, httpHeaders)));
			case "OPTIONS":
				return new JsonResult(1, Tools.getMap("debugResult", HttpPostGet.options(url, httpParams, httpHeaders)));
			case "TRACE":
				return new JsonResult(1, Tools.getMap("debugResult", HttpPostGet.trace(url, httpParams, httpHeaders)));
			case "EGMAS":
				if (null == requestSourceName || "".equals(requestSourceName))
					return new JsonResult(1, Tools.getMap("debugResult", "调试出错\r\nmessage:" + "EGMAS接口必须选择数据源"));
				Map<String, Object> sourceQueryMap = new HashMap<>();
				sourceQueryMap.put("name", requestSourceName);
				List<EgmasSource> sources = egmasSourceService.findByMap(sourceQueryMap, null, null);
				if (sources == null || sources.size() == 0)
					return new JsonResult(1, Tools.getMap("debugResult", "调试出错\r\nmessage:" + "无法查询到对应的数据源"));
				if (url == null)
					return new JsonResult(1, Tools.getMap("debugResult", "调试出错\r\nmessage:" + "URL为空"));
				// 重新处理URL
				String newUrl = sources.get(0).getUrl() + "/";
				if (url.contains("/")) {
					newUrl = newUrl + url.substring(url.lastIndexOf("/") + 1);
				} else {
					newUrl = newUrl + url;
				}
				Date beginDate = new Date();
				String postResult = HttpPostGetEgmas.egmasPost(newUrl, httpParams, httpHeaders, requestMethod);
				Date endDate = new Date();
				long interval = endDate.getTime() - beginDate.getTime(); // 时间差，毫秒数
				Map<String, Object> resultMap = Tools.getMap("debugResult", postResult, "interval", String.valueOf(interval) + "毫秒");
				return new JsonResult(1, resultMap);
			default:
				return new JsonResult(1, Tools.getMap("debugResult", "不支持的请求方法：" + debugMethod));
			}
		} catch (Exception e) {
			return new JsonResult(1, Tools.getMap("debugResult", "调试出错\r\nmessage:" + e.getMessage()));
		}
	}
}
