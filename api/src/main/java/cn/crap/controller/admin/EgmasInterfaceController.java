package cn.crap.controller.admin;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.crap.dto.LoginInfoDto;
import cn.crap.dto.SearchDto;
import cn.crap.framework.JsonResult;
import cn.crap.framework.MyException;
import cn.crap.framework.auth.AuthPassport;
import cn.crap.framework.base.BaseController;
import cn.crap.inter.service.table.IErrorService;
import cn.crap.inter.service.table.IInterfaceEgmasService;
import cn.crap.inter.service.tool.ICacheService;
import cn.crap.model.Error;
import cn.crap.model.Interface;
import cn.crap.model.InterfaceEgmas;
import cn.crap.utils.Const;
import cn.crap.utils.DateFormartUtil;
import cn.crap.utils.MyCookie;
import cn.crap.utils.MyString;
import cn.crap.utils.Page;
import cn.crap.utils.Tools;

@Scope("prototype")
@Controller
@RequestMapping("/egmas/interface")
public class EgmasInterfaceController extends BaseController<Interface>{

	@Autowired
	private IInterfaceEgmasService interfaceEgmasService;
	@Autowired
	private IErrorService errorService;
	@Autowired
	private ICacheService cacheService;
	
	
	@RequestMapping("/list.do")
	@ResponseBody
	@AuthPassport
	public JsonResult list(@ModelAttribute InterfaceEgmas interFace,
			@RequestParam(defaultValue = "1") Integer currentPage){
		Page page= new Page(15);
		page.setCurrentPage(currentPage);
		return interfaceEgmasService.getInterfaceListJson(page, null, interFace, currentPage);
	}

	@RequestMapping("/detail.do")
	@ResponseBody
	public JsonResult detail(@ModelAttribute InterfaceEgmas interFace) throws MyException {
		InterfaceEgmas model;
		if(!interFace.getId().equals(Const.NULL_ID)){
			model= interfaceEgmasService.get(interFace.getId());
		}else{
			model = new InterfaceEgmas();
			model.setModuleId(interFace.getModuleId());
		}
		return new JsonResult(1, model);
	}
	
	/**
	 * @param interFace
	 * @return
	 * @throws MyException
	 * @throws IOException
	 */
	@RequestMapping("/copy.do")
	@ResponseBody
	public JsonResult copy(@ModelAttribute InterfaceEgmas interFace) throws MyException, IOException {
		
		if(interfaceEgmasService.getCount(Tools.getMap("url",interFace.getUrl()))>0){
			throw new MyException("000004");
		}
		interFace.setId(null);
		interfaceEgmasService.save(interFace);
		return new JsonResult(1, interFace);
	}
	
	/**
	 * 根据参数生成请求示例
	 * @param interFace
	 * @return
	 */
	@RequestMapping("/getRequestExam.do")
	@ResponseBody
	@AuthPassport
	public JsonResult getRequestExam(@ModelAttribute InterfaceEgmas interFace) {
		interfaceEgmasService.getInterFaceRequestExam(interFace);
		return new JsonResult(1, interFace);
	}

	@RequestMapping("/addOrUpdate.do")
	@ResponseBody
	public JsonResult addOrUpdate(@ModelAttribute InterfaceEgmas interFace) throws IOException, MyException {
		if (!MyString.isEmpty(interFace.getUrl()))
			interFace.setUrl(interFace.getUrl().trim());

		/**
		 * 根据选着的错误码id，组装json字符串
		 */
		String errorIds = interFace.getErrorList();
//		if (errorIds != null && !errorIds.equals("")) {
//			map = Tools.getMap("errorCode|in", Tools.getIdsFromField(errorIds));
//
//			DataCenter dc = dataCenterService.get(interFace.getModuleId());
//			while (!MyString.isEmpty(dc.getId()) && !dc.getParentId().equals("0") && !dc.getParentId().equals(Const.PRIVATE_MODULE)) {
//				dc = dataCenterService.get(dc.getParentId());
//			}
//			map.put("moduleId", dc.getId());
//			List<Error> errors = errorService.findByMap(map, null,
//					null);
//			interFace.setErrors(JSONArray.fromObject(errors).toString());
//		}else{
//			interFace.setErrors("[]");
//		}
		
		String token = MyCookie.getCookie(Const.COOKIE_TOKEN, false, request);
		LoginInfoDto user = (LoginInfoDto) cacheService.getObj(Const.CACHE_USER + token);
		if (user!=null) {
			//interFace.setUpdateBy("userName："+user.getUserName()+" | trueName："+ user.getTrueName());
			interFace.setUpdateBy(user.getTrueName());
		}
		interFace.setUpdateTime(DateFormartUtil.getDateByFormat(DateFormartUtil.YYYY_MM_DD_HH_mm));
		
		//请求示例为空，则自动添加
		if(MyString.isEmpty(interFace.getRequestExam())){
			interfaceEgmasService.getInterFaceRequestExam(interFace);
		}
		
		if (!MyString.isEmpty(interFace.getId())) {
			if ("EGMAS".equals(interFace.getMethod())) {
				// 头信息为空，则自动添加
				if (MyString.isEmpty(interFace.getHeader()) || "[]".equals(interFace.getHeader())) {
					interFace.initHeader();
				}
			} else {
				if (interfaceEgmasService.getCount(Tools.getMap("url", interFace.getUrl(), "id|!=", interFace.getId())) > 0) {
					throw new MyException("000004");
				}
			}
			interfaceEgmasService.update(interFace, "接口", "");			
		} else {
		
			interFace.setId(null);
			if ("EGMAS".equals(interFace.getMethod())) {
				// 头信息为空，则自动添加
				if (MyString.isEmpty(interFace.getHeader()) || "[]".equals(interFace.getHeader())) {
					interFace.initHeader();
				}
			} else {
				if(interfaceEgmasService.getCount(Tools.getMap("url",interFace.getUrl()))>0){
					throw new MyException("000004");
				}
			}
			interfaceEgmasService.save(interFace);
		}
		return new JsonResult(1, interFace);
	}

	@RequestMapping("/delete.do")
	@ResponseBody
	public JsonResult delete(@ModelAttribute InterfaceEgmas interFace) throws MyException, IOException {
		interFace = interfaceEgmasService.get(interFace.getId());
		interfaceEgmasService.delete(interFace, "接口", "");
		return new JsonResult(1, null);
	}

	@RequestMapping("/changeSequence.do")
	@ResponseBody
	public JsonResult changeSequence(@RequestParam String id,@RequestParam String changeId) throws MyException {
		InterfaceEgmas change = interfaceEgmasService.get(changeId);
		InterfaceEgmas model = interfaceEgmasService.get(id);
		
		int modelSequence = model.getSequence();
		
		model.setSequence(change.getSequence());
		change.setSequence(modelSequence);
		
		interfaceEgmasService.update(model);
		interfaceEgmasService.update(change);
		return new JsonResult(1, null);
	}
}
