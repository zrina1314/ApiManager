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
import cn.crap.inter.service.table.IEgmasSourceService;
import cn.crap.model.EgmasSource;
import cn.crap.utils.DateFormartUtil;
import cn.crap.utils.MyString;
import cn.crap.utils.Page;
import cn.crap.utils.Tools;

@Scope("prototype")
@Controller
@RequestMapping("egmasSource")
public class EgmasSourceController extends BaseController<EgmasSource> {

	@Autowired
	private IEgmasSourceService sourceService;

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
	public JsonResult list(@ModelAttribute EgmasSource source, @RequestParam(defaultValue = "1") int currentPage) {
		Page page = new Page(15);
		page.setCurrentPage(currentPage);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("sources", sourceService.findByMap(null, " new EgmasSource(id,createTime,status,sequence,name,url,updateTime) ", page, null));
		return new JsonResult(1, returnMap, page);
	}

	@RequestMapping("/detail.do")
	@ResponseBody
	@AuthPassport
	public JsonResult detail(@ModelAttribute EgmasSource source) {
		EgmasSource model;
		if (!MyString.isEmpty(source.getId())) {
			model = sourceService.get(source.getId());
		} else {
			model = new EgmasSource();
		}
		return new JsonResult(1, model);
	}

	@RequestMapping("/webDetail.do")
	@ResponseBody
	public JsonResult webDetail(@ModelAttribute EgmasSource source, String password, String visitCode) throws MyException {
		EgmasSource model;
		if (!MyString.isEmpty(source.getId())) {
			model = sourceService.get(source.getId());
		} else {
			throw new MyException("000020");
		}
		return new JsonResult(1, model);
	}

	@RequestMapping("/webList.do")
	@ResponseBody
	public JsonResult webList(@ModelAttribute EgmasSource source, @RequestParam(defaultValue = "1") int currentPage, String password, String visitCode, @RequestParam(defaultValue = "无") String directoryName) throws MyException {
		Page page = new Page(15);
		page.setCurrentPage(currentPage);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("sources", sourceService.findByMap(null, " new EgmasSource(id,createTime,status,sequence,name,url,updateTime) ", page, null));
		return new JsonResult(1, returnMap, page, Tools.getMap("crumbs", Tools.getCrumbs("目录列表:" + directoryName, "void")));
	}

	@RequestMapping("/addOrUpdate.do")
	@ResponseBody
	public JsonResult addOrUpdate(@ModelAttribute EgmasSource source) throws Exception {
		source.setUpdateTime(DateFormartUtil.getDateByFormat(DateFormartUtil.YYYY_MM_DD_HH_mm_ss));
		if (!MyString.isEmpty(source.getId())) {
			sourceService.update(source);
		} else {
			source.setId(null);
			sourceService.save(source);
		}
		return new JsonResult(1, source);
	}

	@RequestMapping("/delete.do")
	@ResponseBody
	public JsonResult delete(@ModelAttribute EgmasSource source) throws MyException, IOException {
		sourceService.delete(source);
		return new JsonResult(1, null);
	}
}
