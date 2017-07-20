package cn.crap.controller.front;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.crap.dto.LoginInfoDto;
import cn.crap.framework.JsonResult;
import cn.crap.framework.base.BaseController;
import cn.crap.inter.service.table.IAppVersionService;
import cn.crap.model.AppVersion;
import cn.crap.utils.MyString;
import cn.crap.utils.Page;
import cn.crap.utils.StringUtils;
import cn.crap.utils.Tools;

@Controller("forntAppVersionController")
@RequestMapping("/front/appVersion")
public class AppVersionController extends BaseController<AppVersion> {

	@Autowired
	private IAppVersionService appVersionService;

	/**
	 * 
	 * @param source
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            每页显示多少条，-1表示查询全部
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public JsonResult list(@ModelAttribute AppVersion appVersion, @RequestParam(defaultValue = "1") int currentPage) {
		Page page = new Page(15);
		page.setCurrentPage(currentPage);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, Object> map = Tools.getMap("appId", appVersion.getAppID());
		List<AppVersion> appVersions = appVersionService.findByMap(map, page, null);
		try {
			String serviceIp = InetAddress.getLocalHost().getHostAddress();
			if (serviceIp != null && !StringUtils.isEmpty(serviceIp)) {
				if (appVersions != null && !appVersions.isEmpty()) {
					int appVersionSize = appVersions.size();
					AppVersion appVersionItem;
					for (int i = 0; i < appVersionSize; i++) {
						appVersionItem = appVersions.get(i);
						appVersionItem.setFilePathUat("http://" + serviceIp + "/" + appVersionItem.getFilePathUat());
						appVersionItem.setFilePathProduct("http://" + serviceIp + "/" + appVersionItem.getFilePathProduct());
					}
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		returnMap.put("appVersionList", appVersions);
		return new JsonResult(1, returnMap, page);
	}

	/**
	 * 
	 * @param source
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            每页显示多少条，-1表示查询全部
	 * @return
	 */
	@RequestMapping("/getVersionCodeGroup.do")
	@ResponseBody
	public JsonResult getVersionCodeGroup(@ModelAttribute AppVersion appVersion) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String where = StringUtils.isEmpty(appVersion.getAppID()) ? " 1=1 " : "appID = '" + appVersion.getAppID()+ "'";
		String hql = "select code from AppVersion where " + where + " group by code";
		List<String> codeList = (List<String>) appVersionService.queryByHql(hql, null);
		returnMap.put("codeList", codeList);
		return new JsonResult(1, returnMap, null);
	}

	@RequestMapping("/detail.do")
	@ResponseBody
	public JsonResult detail(@ModelAttribute AppVersion appVersion) {
		AppVersion model;
		if (!MyString.isEmpty(appVersion.getId())) {
			model = appVersionService.get(appVersion.getId());
		} else {
			model = new AppVersion();
			model.setAppID(appVersion.getAppID());
		}
		return new JsonResult(1, model);
	}
	
	@RequestMapping("/checkVersion.do")
	@ResponseBody
	public JsonResult checkVersion(@ModelAttribute AppVersion appVersion,@RequestParam(defaultValue="UAT") String operate) {
		AppVersion model;
		if (!MyString.isEmpty(appVersion.getId())) {
			model = appVersionService.get(appVersion.getId());
		} else {
			return new JsonResult(0, "","000002","查不到相关的版本");
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("appVersion", model);
		returnMap.put("operate", operate);
		return new JsonResult(1, returnMap);
	}

	@RequestMapping("/addOrUpdate.do")
	@ResponseBody
	public JsonResult addOrUpdate(@ModelAttribute AppVersion appVersion) throws Exception {
		LoginInfoDto user = Tools.getUser();
		if (user != null) {
			appVersion.setCreateUserID(user.getId());
			appVersion.setCreateUserName(user.getTrueName());
		}
		if (!MyString.isEmpty(appVersion.getId())) {
			appVersionService.update(appVersion);
		} else {
			appVersion.setId(null);
			appVersionService.save(appVersion);
		}
		return new JsonResult(1, appVersion);
	}
	
	@RequestMapping("/add.do")
	@ResponseBody
	public JsonResult add(@ModelAttribute AppVersion appVersion) throws Exception {
		LoginInfoDto user = Tools.getUser();
		if (user != null) {
			appVersion.setCreateUserID(user.getId());
			appVersion.setCreateUserName(user.getTrueName());
		}else{
			appVersion.setCreateUserID("admin");
			appVersion.setCreateUserName("超级管理员");
		}
		appVersion.setId(null);
		appVersionService.save(appVersion);
		return new JsonResult(1, appVersion);
	}
}
