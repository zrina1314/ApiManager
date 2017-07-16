package cn.crap.controller.front;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.crap.framework.JsonResult;
import cn.crap.framework.MyException;
import cn.crap.framework.base.BaseController;
import cn.crap.inter.service.table.IUserMobilesService;
import cn.crap.model.UserMobiles;
import cn.crap.utils.Tools;

@Scope("prototype")
@Controller
@RequestMapping("/front/tools")
public class ToolsController extends BaseController<UserMobiles> {

	@Autowired
	private IUserMobilesService userMobilesService;

	/**
	 * 前端错误码列表，只查询公开的顶级项目错误码（错误码定义在顶级项目中） 非公开的项目，错误码只能在项目主页中查看
	 * 
	 * @param module
	 * @param person
	 * @return
	 * @throws MyException
	 */
	@RequestMapping("/queryCaptcha.do")
	@ResponseBody
	public JsonResult queryCaptcha(@RequestParam String mobile) throws MyException {
		 Map<String, Object> returnMap   = new HashMap<String, Object>();
		 Map<String, Object> map = Tools.getMap("mobile", mobile);
		List<UserMobiles> resultList = userMobilesService.findByMap(map, " new UserMobiles(mobile,isValid,validCode,createTm,id,userID,validCodeImage,type) ", null, " createTm desc");
		returnMap.put("userMobiles", resultList);
		return new JsonResult(1, returnMap);
	}
}
