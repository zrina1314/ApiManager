package cn.crap.controller.front;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.crap.dto.CrumbDto;
import cn.crap.dto.ErrorDto;
import cn.crap.dto.InterfaceCxPDFDto;
import cn.crap.dto.InterfacePDFDto;
import cn.crap.dto.ParamDto;
import cn.crap.dto.ResponseParamDto;
import cn.crap.framework.JsonResult;
import cn.crap.framework.MyException;
import cn.crap.framework.base.BaseController;
import cn.crap.inter.service.table.IInterfaceCxService;
import cn.crap.inter.service.table.IInterfaceService;
import cn.crap.inter.service.table.IModuleService;
import cn.crap.inter.service.tool.ICacheService;
import cn.crap.model.Interface;
import cn.crap.model.InterfaceCx;
import cn.crap.model.Module;
import cn.crap.model.Project;
import cn.crap.springbeans.Config;
import cn.crap.utils.Const;
import cn.crap.utils.Html2Pdf;
import cn.crap.utils.HttpPostGet;
import cn.crap.utils.MyString;
import cn.crap.utils.Page;
import cn.crap.utils.StringUtils;
import cn.crap.utils.Tools;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

@Controller("frontInterfaceCxController")
@RequestMapping("/front/interfaceCx")
public class InterfaceCxController extends BaseController<InterfaceCx> {

	@Autowired
	private IInterfaceCxService interfaceCxService;
	@Autowired
	private IModuleService moduleService;
	@Autowired
	private ICacheService cacheService;
	@Autowired
	private Config config;

	@RequestMapping("/detail/pdf.do")
	public String pdf(String id, String moduleId, @RequestParam String secretKey) throws Exception {
		try {

			if (!secretKey.equals(cacheService.getSetting(Const.SETTING_SECRETKEY).getValue())) {
				request.setAttribute("result", "秘钥不正确，非法请求！");
				return "/WEB-INF/views/result.jsp";
			}

			if (MyString.isEmpty(id) && MyString.isEmpty(moduleId)) {
				request.setAttribute("result", "参数不能为空，生成PDF失败！");
				return "/WEB-INF/views/result.jsp";
			}

			List<InterfaceCxPDFDto> interfaces = new ArrayList<InterfaceCxPDFDto>();
			InterfaceCx interFace = null;
			InterfaceCxPDFDto interDto = null;
			Module module = null;
			if (!MyString.isEmpty(id)) {
				interDto = new InterfaceCxPDFDto();
				interFace = interfaceCxService.get(id);
				if (MyString.isEmpty(interFace.getId())) {
					request.setAttribute("result", "接口id有误，生成PDF失败。请确认配置文件config.properties中的网站域名配置是否正确！");
					return "/WEB-INF/views/result.jsp";
				}
				interfaceCxService.getInterDto(config, interfaces, interFace, interDto);
			} else {
				module = moduleService.get(moduleId);
				if (MyString.isEmpty(module.getId())) {
					request.setAttribute("result", "模块id有误，生成PDF失败。请确认配置文件config.properties中的网站域名配置是否正确！");
					return "/WEB-INF/views/result.jsp";
				}
				for (InterfaceCx inter : interfaceCxService.findByMap(Tools.getMap("moduleId", moduleId), null, null)) {
					interDto = new InterfaceCxPDFDto();
					interfaceCxService.getInterDto(config, interfaces, inter, interDto);

				}
			}
			request.setAttribute("MAIN_COLOR", cacheService.getSetting("MAIN_COLOR").getValue());
			request.setAttribute("ADORN_COLOR", cacheService.getSetting("ADORN_COLOR").getValue());
			request.setAttribute("interfaces", interfaces);
			request.setAttribute("moduleName", interFace == null ? module.getName() : interFace.getModuleName());
			request.setAttribute("title", cacheService.getSetting("TITLE").getValue());
			return "/WEB-INF/views/interFacePdf.jsp";
		} catch (Exception e) {
			request.setAttribute("result", "接口数据有误，请修改接口后再试，错误信息：" + e.getMessage());
			return "/WEB-INF/views/result.jsp";
		}
	}

	@RequestMapping("/download/pdf.do")
	@ResponseBody
	public void download(String id, String moduleId, HttpServletRequest req, HttpServletResponse response) throws Exception {

		Module module = null;
		if (!MyString.isEmpty(moduleId)) {
			module = cacheService.getModule(moduleId);
		} else {
			module = cacheService.getModule(interfaceCxService.get(id).getModuleId());
		}
		Project project = cacheService.getProject(module.getProjectId());
		// 如果是私有项目，必须登录才能访问，公开项目需要查看是否需要密码
		// 使用缓存的密码，不需要验证码
		isPrivateProject("", "", project);

		// interFace = interfaceService.get(interFace.getId());
		String displayFilename = "CrapApi" + System.currentTimeMillis() + ".pdf";
		byte[] buf = new byte[1024 * 1024 * 10];
		int len = 0;
		ServletOutputStream ut = null;
		BufferedInputStream br = null;
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "must-revalidate, no-transform");
		response.setDateHeader("Expires", 0L);

		String userAgent = req.getHeader("User-Agent");
		boolean isIE = (userAgent != null) && (userAgent.toLowerCase().indexOf("msie") != -1);
		response.setContentType("application/x-download");
		if (isIE) {
			displayFilename = URLEncoder.encode(displayFilename, "UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + displayFilename + "\"");
		} else {
			displayFilename = new String(displayFilename.getBytes("UTF-8"), "ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename=" + displayFilename);
		}
		String secretKey = cacheService.getSetting(Const.SETTING_SECRETKEY).getValue();
		br = new BufferedInputStream(new FileInputStream(Html2Pdf.createPdf(req, config, id, moduleId, secretKey)));
		ut = response.getOutputStream();
		while ((len = br.read(buf)) != -1)
			ut.write(buf, 0, len);
		ut.flush();
		br.close();
	}

	@RequestMapping("/list.do")
	@ResponseBody
	public JsonResult webList(@RequestParam String moduleId, String interfaceName, String url, @RequestParam(defaultValue = "1") Integer currentPage, String password, String visitCode) throws MyException {
		List<CrumbDto> dCrumbDtos = null;

		if (!MyString.isEmpty(moduleId)) {
			Module module = moduleService.get(moduleId);
			Project project = cacheService.getProject(module.getProjectId());
			// 如果是私有项目，必须登录才能访问，公开项目需要查看是否需要密码
			isPrivateProject(password, visitCode, project);

			dCrumbDtos = Tools.getCrumbs(module.getProjectName(), "#/" + module.getProjectId() + "/module/list", module.getName(), "void");
		}

		Page page = new Page(15);
		page.setCurrentPage(currentPage);

		List<InterfaceCx> interfaces = interfaceCxService.findByMap(Tools.getMap("moduleId", moduleId, "interfaceName|like", interfaceName, "fullUrl|like", url), " new InterfaceCx(id,moduleId,interfaceName,version,createTime,updateBy,updateTime,remark,sequence,template,url)", page, null);

		if (dCrumbDtos != null) {
			return new JsonResult(1, interfaces, page, Tools.getMap("crumbs", dCrumbDtos));
		} else {
			return new JsonResult(1, interfaces, page, null);
		}
	}

	@RequestMapping("/detail.do")
	@ResponseBody
	public JsonResult webDetail(@ModelAttribute InterfaceCx interFace, String password, String visitCode) throws MyException {
		interFace = interfaceCxService.get(interFace.getId());
		if (interFace != null) {

			Module module = cacheService.getModule(interFace.getModuleId());
			Project project = cacheService.getProject(module.getProjectId());
			// 如果是私有项目，必须登录才能访问，公开项目需要查看是否需要密码
			isPrivateProject(password, visitCode, project);

			/**
			 * 查询相同模块下，相同接口名的其它版本号
			 */
			List<InterfaceCx> versions = interfaceCxService.findByMap(Tools.getMap("moduleId", interFace.getModuleId(), "interfaceName", interFace.getInterfaceName(), "version|<>", interFace.getVersion()), null, null);
			return new JsonResult(1, interFace, null, Tools.getMap("versions", versions, "crumbs",
					Tools.getCrumbs(project.getName(), "#/" + project.getId() + "/module/list", module.getName() + ":接口列表", "#/" + project.getId() + "/interface/list/" + module.getId(), interFace.getInterfaceName(), "void"), "module", cacheService.getModule(interFace.getModuleId())));
		} else {
			throw new MyException("000012");
		}
	}

	@RequestMapping("/debug.do")
	@ResponseBody
	public JsonResult debug(@RequestParam(defaultValue = "") String sourceUrl, @RequestParam String header, @RequestParam(defaultValue = "") String customParams, @RequestParam(defaultValue = "POST") String debugMethod, @RequestParam String fullUrl) throws Exception {
		fullUrl = sourceUrl + fullUrl;
		Map<String, String> httpHeaders = new HashMap<String, String>();
		try {
			if (header != null && "" != header) {
				if ("[".equals(header.substring(0, 1))) {
					JSONArray jsonHeaders = JSONArray.fromObject(header);
					for (int i = 0; i < jsonHeaders.size(); i++) {
						JSONObject param = jsonHeaders.getJSONObject(i);
						for (Object paramKey : param.keySet()) {
							httpHeaders.put(paramKey.toString(), param.getString(paramKey.toString()));
						}
					}
				} else {
					JSONObject headersJsObject = JSONObject.fromObject(header);
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
		try {
			String tempResult =  HttpPostGet.postBodyCx(fullUrl, customParams, httpHeaders);
			if (StringUtils.isEmpty(tempResult)) {
				tempResult = "服务器返回结果为空";
			}
			return new JsonResult(1, Tools.getMap("debugResult",tempResult));
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResult(1, Tools.getMap("debugResult", "调试出错\r\nmessage:" + e.getMessage()));
		}
	}

}
