package cn.crap.controller.admin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.crap.dto.LoginInfoDto;
import cn.crap.enumeration.ArticleType;
import cn.crap.framework.JsonResult;
import cn.crap.framework.MyException;
import cn.crap.framework.auth.AuthPassport;
import cn.crap.framework.base.BaseController;
import cn.crap.inter.service.table.IArticleService;
import cn.crap.inter.service.table.ICxModuleService;
import cn.crap.inter.service.table.IInterfaceService;
import cn.crap.inter.service.table.IModuleService;
import cn.crap.inter.service.table.IProjectService;
import cn.crap.inter.service.table.IProjectUserService;
import cn.crap.inter.service.table.IRoleService;
import cn.crap.inter.service.table.ISourceService;
import cn.crap.inter.service.table.IUserService;
import cn.crap.inter.service.tool.ICacheService;
import cn.crap.model.CxModule;
import cn.crap.model.Interface;
import cn.crap.model.Module;
import cn.crap.springbeans.Config;
import cn.crap.utils.Const;
import cn.crap.utils.MyString;
import cn.crap.utils.Page;
import cn.crap.utils.Tools;

@Controller
@RequestMapping("/userCx/module")
public class CxModuleController extends BaseController<CxModule> {

	@Autowired
	private ICxModuleService cxModuleService;
	@Autowired
	private ICacheService cacheService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IArticleService articleService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IInterfaceService interfaceService;
	@Autowired
	private IProjectService projectService;
	@Autowired
	private IProjectUserService projectUserService;
	@Autowired
	private ISourceService sourceService;
	@Autowired
	private Config config;

	@RequestMapping("/list.do")
	@ResponseBody
	public JsonResult list(@ModelAttribute Module module, @RequestParam(defaultValue = "1") int currentPage) throws MyException {
		Page page = new Page(15);
		page.setCurrentPage(currentPage);

		return new JsonResult(1, cxModuleService.findByMap(null, page, null), page);
	}

	@RequestMapping("/detail.do")
	@ResponseBody
	public JsonResult detail(@ModelAttribute CxModule module) throws MyException {
		CxModule cxModule;
		if (!module.getId().equals(Const.NULL_ID)) {
			cxModule = cxModuleService.get(module.getId());
			if (!MyString.isEmpty(cxModule.getTemplateId())) {
				Interface inter = interfaceService.get(cxModule.getTemplateId());
				if (inter != null)
					cxModule.setTemplateName(inter.getInterfaceName());
			}
		} else {
			cxModule = new CxModule();
			cxModule.setStatus(Byte.valueOf("1"));
		}
		return new JsonResult(1, cxModule);
	}

	@RequestMapping("/addOrUpdate.do")
	@ResponseBody
	public JsonResult addOrUpdate(@ModelAttribute CxModule cxModule) throws Exception {
		// 系统数据，不允许删除
		if (cxModule.getId().equals("web"))
			throw new MyException("000009");

		if (!MyString.isEmpty(cxModule.getId())) {
			// 更新该模块下的所有接口的fullUrl
			interfaceService.update("update Interface set fullUrl=CONCAT('" + (cxModule.getUrl() == null ? "" : cxModule.getUrl()) + "',url) where moduleId ='" + cxModule.getId() + "'", null);
			cxModuleService.update(cxModule, "模块", "");
		} else {
			cxModule.setUserId(Tools.getUser().getId());
			cxModuleService.save(cxModule);
		}
		cacheService.delObj(Const.CACHE_MODULE + cxModule.getId());

		/**
		 * 刷新用户权限
		 */
		LoginInfoDto user = Tools.getUser();
		// 将用户信息存入缓存
		cacheService.setObj(Const.CACHE_USER + user.getId(), new LoginInfoDto(userService.get(user.getId()), roleService, projectService, projectUserService), config.getLoginInforTime());
		return new JsonResult(1, cxModule);
	}

	/**
	 * 设置模块接口
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/setTemplate.do")
	@ResponseBody
	public JsonResult setTemplate(String id) throws Exception {
		Interface inter = interfaceService.get(id);

		CxModule cxModule = cacheService.getCxModule(inter.getModuleId());

		cxModule.setTemplateId(inter.isTemplate() ? null : inter.getId());
		cxModuleService.update(cxModule);

		interfaceService.update("update Interface set isTemplate=0 where moduleId ='" + cxModule.getId() + "'", null);
		if (!inter.isTemplate()) {
			inter.setTemplate(true);
			;
			interfaceService.update(inter);
		}

		cacheService.delObj(Const.CACHE_MODULE + cxModule.getId());
		return new JsonResult(1, cxModule);
	}

	@RequestMapping("/delete.do")
	@ResponseBody
	public JsonResult delete(@ModelAttribute CxModule cxModule) throws Exception {
		// 系统数据，不允许删除
		if (cxModule.getId().equals("web"))
			throw new MyException("000009");

		CxModule oldDataCenter = cacheService.getCxModule(cxModule.getId());

		if (interfaceService.getCount(Tools.getMap("moduleId", oldDataCenter.getId())) > 0) {
			throw new MyException("000024");
		}

		if (articleService.getCount(Tools.getMap("moduleId", oldDataCenter.getId(), "type", ArticleType.ARTICLE.name())) > 0) {
			throw new MyException("000034");
		}

		if (sourceService.getCount(Tools.getMap("moduleId", oldDataCenter.getId())) > 0) {
			throw new MyException("000035");
		}

		if (articleService.getCount(Tools.getMap("moduleId", oldDataCenter.getId(), "type", ArticleType.DICTIONARY.name())) > 0) {
			throw new MyException("000036");
		}

		cacheService.delObj(Const.CACHE_MODULE + cxModule.getId());
		cxModuleService.delete(cxModule, "模块", "");
		return new JsonResult(1, null);
	}

	@RequestMapping("/changeSequence.do")
	@ResponseBody
	@AuthPassport
	public JsonResult changeSequence(@RequestParam String id, @RequestParam String changeId) throws MyException {
		CxModule change = cxModuleService.get(changeId);
		CxModule model = cxModuleService.get(id);

		int modelSequence = model.getSequence();
		model.setSequence(change.getSequence());
		change.setSequence(modelSequence);

		cxModuleService.update(model);
		cxModuleService.update(change);

		return new JsonResult(1, null);
	}

}
