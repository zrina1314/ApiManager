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
import cn.crap.utils.DateFormartUtil;
import cn.crap.utils.MyString;
import cn.crap.utils.Page;
import cn.crap.utils.Tools;

@Scope("prototype")
@Controller
@RequestMapping("app")
public class AppController extends BaseController<EgmasSource> {

	@Autowired
	private IAppService appService;
	

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
	public JsonResult list(@ModelAttribute App source, @RequestParam(defaultValue = "1") int currentPage) {
		Page page = new Page(15);
		page.setCurrentPage(currentPage);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("appList", appService.findByMap(null, " new App(id,createTime,status,sequence,name,url,updateTime) ", page, null));
		return new JsonResult(1, returnMap, page);
	}
	
	@RequestMapping("/detail.do")
	@ResponseBody
	@AuthPassport
	public JsonResult detail(@ModelAttribute App app) {
		App model;
		if (!MyString.isEmpty(app.getId())) {
			model = appService.get(app.getId());
		} else {
			model = new App();
		}
		return new JsonResult(1, model);
	}

	@RequestMapping("/addOrUpdate.do")
	@ResponseBody
	public JsonResult addOrUpdate(@ModelAttribute App app) throws Exception {
		app.setUpdateTime(DateFormartUtil.getDateByFormat(DateFormartUtil.YYYY_MM_DD_HH_mm_ss));
		if (!MyString.isEmpty(app.getId())) {
			appService.update(app);
		} else {
			app.setId(null);
			appService.save(app);
		}
		return new JsonResult(1, app);
	}

	@RequestMapping("/delete.do")
	@ResponseBody
	public JsonResult delete(@ModelAttribute App app) throws MyException, IOException {
		appService.delete(app);
		return new JsonResult(1, null);
	}
}
