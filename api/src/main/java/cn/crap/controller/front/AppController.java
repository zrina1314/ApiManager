package cn.crap.controller.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.crap.framework.JsonResult;
import cn.crap.framework.base.BaseController;
import cn.crap.inter.service.table.IAppService;
import cn.crap.model.App;
import cn.crap.utils.Page;

@Controller("forntAppController")
@RequestMapping("/front/app")
public class AppController extends BaseController<App> {

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
	public JsonResult list(@ModelAttribute App source, @RequestParam(defaultValue = "1") int currentPage) {
		Page page = new Page(1000);
		page.setCurrentPage(currentPage);
		List<App> appList = appService.findByMap(null, page, null);
		String filterID = "ffff-1500376626351-1921681188-0002";
		int appListSize = appList.size();
		for (int i = 0; i < appListSize; i++) {
			if (filterID.equals(appList.get(i).getId())) {
				appList.remove(i);
				break;
			}
		}
		return new JsonResult(1, appList, page);
	}
}
