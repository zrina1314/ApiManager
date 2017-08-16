package cn.crap.service.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.crap.dto.ErrorDto;
import cn.crap.dto.InterfaceCxPDFDto;
import cn.crap.dto.ParamDto;
import cn.crap.dto.ResponseParamDto;
import cn.crap.framework.JsonResult;
import cn.crap.framework.base.BaseService;
import cn.crap.framework.base.IBaseDao;
import cn.crap.inter.dao.IInterfaceCxDao;
import cn.crap.inter.service.table.IInterfaceCxService;
import cn.crap.inter.service.table.IModuleService;
import cn.crap.inter.service.tool.ICacheService;
import cn.crap.inter.service.tool.ILuceneService;
import cn.crap.model.InterfaceCx;
import cn.crap.model.Module;
import cn.crap.springbeans.Config;
import cn.crap.utils.MyString;
import cn.crap.utils.Page;
import cn.crap.utils.Tools;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class InterfaceCxService extends BaseService<InterfaceCx>
		implements IInterfaceCxService ,ILuceneService<InterfaceCx>{
	@Autowired
	private ICacheService cacheService;
	@Autowired
	private IModuleService moduleService;
	@Resource(name="interfaceCxDao")
	IInterfaceCxDao interfaceCxDao;
	
	@Resource(name="interfaceCxDao")
	public void setDao(IBaseDao<InterfaceCx> dao) {
		super.setDao(dao);
	}

	@Override
	public void getInterDto(Config config, List<InterfaceCxPDFDto> interfaces, InterfaceCx interFaceCx, InterfaceCxPDFDto interDto) {
		interDto.setModel(interFaceCx);
		if(interFaceCx.getParam().startsWith("form=")){
			interDto.setFormParams(JSONArray.toArray(JSONArray.fromObject(interFaceCx.getParam().substring(5)),ParamDto.class));
		}else{
			interDto.setCustomParams( interFaceCx.getParam());
		}
		interDto.setTrueMockUrl(config.getDomain()+"/mock/trueExam.do?id="+interFaceCx.getId());
		interDto.setFalseMockUrl(config.getDomain()+"/mock/falseExam.do?id="+interFaceCx.getId());

		interDto.setHeaders( JSONArray.toArray(JSONArray.fromObject(interFaceCx.getHeader()),ParamDto.class));
		interDto.setResponseParam( JSONArray.toArray(JSONArray.fromObject(interFaceCx.getResponseParam()),ResponseParamDto.class) );
		interDto.setParamRemarks( JSONArray.toArray(JSONArray.fromObject(interFaceCx.getParamRemark()), ResponseParamDto.class) );
		interDto.setErrors( JSONArray.toArray(JSONArray.fromObject(interFaceCx.getErrors()),ErrorDto.class) );
		interfaces.add(interDto);
	}
	
	@Override
	@Transactional
	public InterfaceCx get(String id){
		InterfaceCx model = interfaceCxDao.get(id);
		if(model == null)
			 return new InterfaceCx();
		return model;
	}
	
	@Override
	@Transactional
	public JsonResult getInterfaceList(Page page,List<String> moduleIds, InterfaceCx interFaceCx, Integer currentPage) {
		page.setCurrentPage(currentPage);
		
		Map<String, Object> params = Tools.getMap("moduleId", interFaceCx.getModuleId(),
				"interfaceName|like", interFaceCx.getInterfaceName(),"fullUrl|like", interFaceCx.getUrl()==null?"":interFaceCx.getUrl().trim());
		if(moduleIds != null){
			moduleIds.add("NULL");// 防止长度为0，导致in查询报错
			params.put("moduleId|in", moduleIds);
		}
			
		List<InterfaceCx> interfaces = findByMap(
				params, " new InterfaceCx(id,moduleId,interfaceName,version,createTime,updateBy,updateTime,remark,sequence)", page, null);
		
		List<Module> modules = new ArrayList<Module>();
		// 搜索接口时，modules为空
		if (interFaceCx.getModuleId() != null && MyString.isEmpty(interFaceCx.getInterfaceName()) && MyString.isEmpty(interFaceCx.getUrl()) ) {
			params = Tools.getMap("parentId", interFaceCx.getModuleId(), "type", "MODULE");
			if(moduleIds != null){
				moduleIds.add("NULL");// 防止长度为0，导致in查询报错
				params.put("id|in", moduleIds);
			}
			params.put("id|!=", "top");// 顶级目录不显示
			modules = moduleService.findByMap(params, null, null);
		}
		params.clear();
		params.put("interfaces", interfaces);
		params.put("modules", modules);
		return new JsonResult(1, params, page, 
				Tools.getMap("crumbs", Tools.getCrumbs("接口列表:"+cacheService.getModuleName(interFaceCx.getModuleId()),"void"),
						"module",cacheService.getModule(interFaceCx.getModuleId())));
	}
	
	@Override
	@Transactional
	public void getInterFaceRequestExam(InterfaceCx interFaceCx) {
			interFaceCx.setResponseParamRemark("请求地址:"+interFaceCx.getModuleUrl()+interFaceCx.getUrl()+"\r\n");
			
			// 请求头
			JSONArray headers = JSONArray.fromObject(interFaceCx.getHeader());
			StringBuilder strHeaders = new StringBuilder("请求头:\r\n");
			JSONObject obj = null;
			for(int i=0;i<headers.size();i++){  
				obj = (JSONObject) headers.get(i);
		        strHeaders.append("\t"+obj.getString("name") + "="+ (obj.containsKey("def")?obj.getString("def"):"")+"\r\n");
		    }  
			
			// 请求参数
			StringBuilder strParams = new StringBuilder("请求参数:\r\n");
			if(!MyString.isEmpty(interFaceCx.getParam())){
				JSONArray params = null;
				if(interFaceCx.getParam().startsWith("form=")){
					 params = JSONArray.fromObject(interFaceCx.getParam().substring(5));
					 for(int i=0;i<params.size();i++){  
							obj = (JSONObject) params.get(i);
							if(obj.containsKey("inUrl") && obj.getString("inUrl").equals("true")){
								interFaceCx.setResponseParamRemark(interFaceCx.getResponseParamRemark().replace("{"+obj.getString("name")+"}", (obj.containsKey("def")?obj.getString("def"):"")));
							}else{
								strParams.append("\t"+obj.getString("name") + "=" + (obj.containsKey("def")?obj.getString("def"):"")+"\r\n");
							}
					 }  
				}else{
					strParams.append(interFaceCx.getParam()); 
				}
			}
			interFaceCx.setResponseParamRemark(interFaceCx.getResponseParamRemark()+strHeaders.toString()+strParams.toString());
	}

	@Override
	@Transactional
	public List<InterfaceCx> getAll() {
		return interfaceCxDao.findByMap(null, null, null);
	}

	@Override
	public String getLuceneType() {
		return "接口";
	}
}
