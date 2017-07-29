package cn.crap.controller.front;

import java.util.ArrayList;
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
import cn.crap.inter.service.table.IAppPageService;
import cn.crap.model.AppPage;
import cn.crap.utils.Tools;

@Scope("prototype")
@Controller
@RequestMapping("/front/appPage")
public class AppPageController extends BaseController<AppPage> {

	@Autowired
	private IAppPageService appPageService;

	/**
	 * 前端错误码列表，只查询公开的顶级项目错误码（错误码定义在顶级项目中） 非公开的项目，错误码只能在项目主页中查看
	 * 
	 * @param module
	 * @param person
	 * @return
	 * @throws MyException
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public JsonResult list(@RequestParam String module, @RequestParam String person) throws MyException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, Object> map =  new HashMap<String, Object>();
		// 搜索条件
		if (null != person && !"".equals(person)) {
			if (person.length() > 1) {
				switch (person.charAt(0)) {
				case 'A':
					map = Tools.getMap("module|like", module, "android|like", person.substring(1));
					break;
				case 'I':
					map = Tools.getMap("module|like", module, "ios|like", person.substring(1));
					break;
				case 'T':
					map = Tools.getMap("module|like", module, "test|like", person.substring(1));
					break;
				default:
					map = Tools.getMap("module|like", module);
					break;
				}
			}
		} else {
			map = Tools.getMap("module|like", module);
		}

		List<AppPage> resultList = appPageService.findByMap(map, " new AppPage(id,module,function,android,ios,test,remark,updateTime,sequence) ", null, " module desc,sequence desc, updateTime desc ");
		List<Map<String, Object>> groupList = new ArrayList<>();
		Map<String, Integer> moduleMap = new HashMap<>(); // key: 模块名 value :

		try {
			if (resultList != null && !resultList.isEmpty()) {
				for (int i = 0; i < resultList.size(); i++) {
					String key = resultList.get(i).getModule();
					AppPage value = resultList.get(i);
					int groupListIndex = 0;// groupList的下标
					if (!moduleMap.containsKey(key)) {
						groupListIndex = groupList.size();
						moduleMap.put(key, groupListIndex); // 不存在，记录下标
						HashMap<String, Object> groupItemMap = new HashMap<String, Object>();
						groupItemMap.put("module", key);
						groupItemMap.put("resultList", new ArrayList<AppPage>());
						groupItemMap.put("resultListSize", 0);
						groupList.add(groupItemMap);
					}else{
						groupListIndex = moduleMap.get(key);
					}
					((ArrayList<AppPage>) (groupList.get(groupListIndex).get("resultList"))).add(value);
					groupList.get(groupListIndex).put("resultListSize", ((ArrayList<AppPage>) (groupList.get(groupListIndex).get("resultList"))).size());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		returnMap.put("sources", groupList);
		if (map != null)
			map.clear();
		return new JsonResult(1, returnMap);
	}
}
