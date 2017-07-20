package cn.crap.controller.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.crap.dto.LoginInfoDto;
import cn.crap.framework.JsonResult;
import cn.crap.framework.MyException;
import cn.crap.framework.auth.AuthPassport;
import cn.crap.framework.base.BaseController;
import cn.crap.inter.service.table.IAppService;
import cn.crap.inter.service.table.IAppVersionService;
import cn.crap.inter.service.table.IEgmasSourceService;
import cn.crap.model.App;
import cn.crap.model.AppVersion;
import cn.crap.model.EgmasSource;
import cn.crap.utils.Const;
import cn.crap.utils.DateFormartUtil;
import cn.crap.utils.MyCookie;
import cn.crap.utils.MyString;
import cn.crap.utils.Page;
import cn.crap.utils.Tools;

@Scope("prototype")
@Controller
@RequestMapping("appVersion")
public class AppVersionController extends BaseController<EgmasSource> {

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
	@AuthPassport
	public JsonResult list(@ModelAttribute AppVersion appVersion, @RequestParam(defaultValue = "1") int currentPage) {
		Page page = new Page(15);
		page.setCurrentPage(currentPage);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, Object> map = Tools.getMap("appId", appVersion.getAppID());
		returnMap.put("appVersionList", appVersionService.findByMap(map, page, null));
		return new JsonResult(1, returnMap, page);
	}

	@RequestMapping("/detail.do")
	@ResponseBody
	@AuthPassport
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

	@RequestMapping("/delete.do")
	@ResponseBody
	public JsonResult delete(@ModelAttribute AppVersion appVersion) throws MyException, IOException {
		appVersionService.delete(appVersion);
		return new JsonResult(1, null);
	}
}
