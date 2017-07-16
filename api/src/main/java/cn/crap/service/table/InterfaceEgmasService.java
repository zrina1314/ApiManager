package cn.crap.service.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.crap.dto.ErrorDto;
import cn.crap.dto.InterfaceEgmasPDFDto;
import cn.crap.dto.InterfacePDFDto;
import cn.crap.dto.ParamDto;
import cn.crap.dto.ResponseParamDto;
import cn.crap.framework.JsonResult;
import cn.crap.framework.base.BaseService;
import cn.crap.framework.base.IBaseDao;
import cn.crap.inter.dao.IInterfaceDao;
import cn.crap.inter.dao.IInterfaceEgmasDao;
import cn.crap.inter.service.table.IInterfaceEgmasService;
import cn.crap.inter.service.table.IInterfaceService;
import cn.crap.inter.service.table.IModuleService;
import cn.crap.inter.service.tool.ICacheService;
import cn.crap.inter.service.tool.ILuceneService;
import cn.crap.model.AppPage;
import cn.crap.model.Interface;
import cn.crap.model.InterfaceEgmas;
import cn.crap.model.Module;
import cn.crap.springbeans.Config;
import cn.crap.utils.MyString;
import cn.crap.utils.Page;
import cn.crap.utils.Tools;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class InterfaceEgmasService extends BaseService<InterfaceEgmas> implements IInterfaceEgmasService {

	@Autowired
	private ICacheService cacheService;
	@Autowired
	private IModuleService moduleService;

	@Resource(name = "interfaceEgmasDao")
	public void setDao(IBaseDao<InterfaceEgmas> dao) {
		super.setDao(dao);
	}

	@Override
	@Transactional
	public List<InterfaceEgmas> getInterfaceList(Page page, List<String> moduleIds, InterfaceEgmas interFace, Integer currentPage) {
		if (page != null) {
			page.setCurrentPage(currentPage);
		}
		Map<String, Object> params = new HashMap<>();
		if (interFace != null) {
			params = Tools.getMap("moduleId", interFace.getModuleId(), "interfaceName|like",
					interFace.getInterfaceName(), "url|like",
					interFace.getUrl() == null ? "" : interFace.getUrl().trim(), "requestMethod|like",
					interFace.getRequestMethod(), "responsiblePerson",
					interFace.getResponsiblePerson() == null ? "" : interFace.getResponsiblePerson().trim(),
					"customModule", interFace.getCustomModule() == null ? "" : interFace.getCustomModule().trim());
			if (moduleIds != null) {
				moduleIds.add("NULL");// 防止长度为0，导致in查询报错
				params.put("moduleId|in", moduleIds);
			}
		}

		List<InterfaceEgmas> interfaces = findByMap(params,
				" new InterfaceEgmas(id,moduleId,interfaceName,version,createTime,updateBy,updateTime,remark,sequence,requestMethod,responsiblePerson,customModule)",
				page, " customModule desc,sequence desc, updateTime desc ");

		return interfaces;
	}
	
	
	@Override
	@Transactional
	public JsonResult getInterfaceListJson(Page page, List<String> moduleIds, InterfaceEgmas interFace, Integer currentPage) {
		if (page != null) {
			page.setCurrentPage(currentPage);
		}
		Map<String, Object> params = new HashMap<>();
		if (interFace != null) {
			params = Tools.getMap("moduleId", interFace.getModuleId(), "interfaceName|like",
					interFace.getInterfaceName(), "url|like",
					interFace.getUrl() == null ? "" : interFace.getUrl().trim(), "requestMethod|like",
					interFace.getRequestMethod(), "responsiblePerson",
					interFace.getResponsiblePerson() == null ? "" : interFace.getResponsiblePerson().trim(),
					"customModule", interFace.getCustomModule() == null ? "" : interFace.getCustomModule().trim());
			if (moduleIds != null) {
				moduleIds.add("NULL");// 防止长度为0，导致in查询报错
				params.put("moduleId|in", moduleIds);
			}
		}

		List<InterfaceEgmas> interfaces = findByMap(params,
				" new InterfaceEgmas(id,moduleId,interfaceName,version,createTime,updateBy,updateTime,remark,sequence,requestMethod,responsiblePerson,customModule)",
				page, " customModule desc,sequence desc, updateTime desc ");

		List<Module> modules = new ArrayList<Module>();
		params.clear();
		params.put("interfaces", interfaces);
		params.put("modules", modules);
		return new JsonResult(1, params, page,
				Tools.getMap("crumbs",
						Tools.getCrumbs("接口列表:" + cacheService.getModuleName(interFace.getModuleId()), "void"),
						"module", cacheService.getModule(interFace.getModuleId())));
		
//		return new JsonResult(1, interfaces, page,
//				Tools.getMap("crumbs", Tools.getCrumbs( module.getProjectName(), "#/"+module.getProjectId()+"/module/list", module.getName(), "void") ));
	}

	@Override
	@Transactional
	public JsonResult getInterfaceResponsibilityListToJsonResult(InterfaceEgmas interFace) {
		List<InterfaceEgmas> resultList = getInterfaceResponsibilityList(interFace);
		List<String[]> tempModuleList = queryInterfaceResponsibilityModuleList();
		Map<String, String> personMap = new HashMap<>(); // key: 模块名 value
		List<Map<String, Object>> personGroupList = new ArrayList<>();
		Map<String, Integer> personIndexMap = new HashMap<>(); // key: 模块名 value
		try {
			for (int i = 0; i < tempModuleList.size(); i++) {
				Object[] tempPersonMap = (Object[]) tempModuleList.get(i);
				String key = tempPersonMap[0].toString();
				String value = tempPersonMap[1].toString();
				int groupListIndex = 0;// groupList的下标

				if (!personIndexMap.containsKey(key)) {
					groupListIndex = personGroupList.size();
					personIndexMap.put(key, groupListIndex); // 不存在，记录下标
					HashMap<String, Object> groupItemMap = new HashMap<String, Object>();
					groupItemMap.put("personName", key);
					groupItemMap.put("modules", value);
					personGroupList.add(groupItemMap);
					continue;
				} else {
					groupListIndex = personIndexMap.get(key);
				}
				String oldValue = personGroupList.get(groupListIndex).get("modules").toString();
				if (!oldValue.contains(value)) {
					personGroupList.get(groupListIndex).put("modules", oldValue + " \\ " + value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

		Map<String, Object> params = new HashMap<String, Object>();
		List<Map<String, Object>> groupList = new ArrayList<>();
		Map<String, Integer> interfacesMap = new HashMap<>(); // key: 模块名 value
		try {
			if (resultList != null && !resultList.isEmpty()) {
				for (int i = 0; i < resultList.size(); i++) {
					String key = resultList.get(i).getCustomModule();
					InterfaceEgmas value = resultList.get(i);
					value.setSequence(i + 1);
					int groupListIndex = 0;// groupList的下标
					if (!interfacesMap.containsKey(key)) {
						groupListIndex = groupList.size();
						interfacesMap.put(key, groupListIndex); // 不存在，记录下标
						HashMap<String, Object> groupItemMap = new HashMap<String, Object>();
						groupItemMap.put("customModule", key);
						groupItemMap.put("resultList", new ArrayList<AppPage>());
						groupItemMap.put("resultListSize", 0);
						groupList.add(groupItemMap);
					} else {
						groupListIndex = interfacesMap.get(key);
					}
					((ArrayList<InterfaceEgmas>) (groupList.get(groupListIndex).get("resultList"))).add(value);
					groupList.get(groupListIndex).put("resultListSize",
							((ArrayList<InterfaceEgmas>) (groupList.get(groupListIndex).get("resultList"))).size());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		params.put("interfaces", groupList);
		params.put("personMap", personGroupList);

		return new JsonResult(1, params, null, Tools.getMap("crumbs",
				Tools.getCrumbs("接口列表:" + cacheService.getModuleName(interFace.getModuleId()), "void")));
	}

	@Override
	@Transactional
	public List<InterfaceEgmas> getInterfaceResponsibilityList(InterfaceEgmas interFace) {
		Map<String, Object> params = Tools.getMap("responsiblePerson",
				interFace.getResponsiblePerson() == null ? "" : interFace.getResponsiblePerson().trim(), "customModule",
				interFace.getCustomModule() == null ? "" : interFace.getCustomModule().trim());
		return findByMap(params,
				" new InterfaceEgmas(interfaceName,updateTime,requestMethod,responsiblePerson,customModule)", null,
				" customModule desc,sequence desc, updateTime desc ");
	}

	private List<String[]> queryInterfaceResponsibilityModuleList() {
		String hql = "SELECT responsiblePerson,customModule FROM InterfaceEgmas";
		List<String[]> result = (List<String[]>) queryByHql2(hql, null);
		return result;
	}

	@Override
	@Transactional
	public void getInterFaceRequestExam(InterfaceEgmas interFace) {
		interFace.setRequestExam("请求地址:" + interFace.getModuleUrl() + interFace.getUrl() + "\r\n");

		// 请求头
		JSONArray headers = JSONArray.fromObject(interFace.getHeader());
		StringBuilder strHeaders = new StringBuilder("请求头:\r\n");
		JSONObject obj = null;
		for (int i = 0; i < headers.size(); i++) {
			obj = (JSONObject) headers.get(i);
			strHeaders.append(
					"\t" + obj.getString("name") + "=" + (obj.containsKey("def") ? obj.getString("def") : "") + "\r\n");
		}

		// 请求参数
		StringBuilder strParams = new StringBuilder("请求参数:\r\n");
		if (!MyString.isEmpty(interFace.getParam())) {
			JSONArray params = null;
			if (interFace.getParam().startsWith("form=")) {
				params = JSONArray.fromObject(interFace.getParam().substring(5));
				for (int i = 0; i < params.size(); i++) {
					obj = (JSONObject) params.get(i);
					if (obj.containsKey("inUrl") && obj.getString("inUrl").equals("true")) {
						interFace.setRequestExam(interFace.getRequestExam().replace("{" + obj.getString("name") + "}",
								(obj.containsKey("def") ? obj.getString("def") : "")));
					} else {
						strParams.append("\t" + obj.getString("name") + "="
								+ (obj.containsKey("def") ? obj.getString("def") : "") + "\r\n");
					}
				}
			} else {
				strParams.append(interFace.getParam());
			}
		}
		interFace.setRequestExam(interFace.getRequestExam() + strHeaders.toString() + strParams.toString());
	}
}

