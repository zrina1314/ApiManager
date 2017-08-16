package cn.crap.service.tool;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import cn.crap.dto.LoginInfoDto;
import cn.crap.dto.PickDto;
import cn.crap.enumeration.InterfaceStatus;
import cn.crap.enumeration.MonitorType;
import cn.crap.enumeration.PlatformType;
import cn.crap.enumeration.RequestMethod;
import cn.crap.enumeration.TrueOrFalse;
import cn.crap.framework.MyException;
import cn.crap.inter.service.table.IAppPageService;
import cn.crap.inter.service.table.IAppVersionService;
import cn.crap.inter.service.table.IArticleService;
import cn.crap.inter.service.table.ICxModuleService;
import cn.crap.inter.service.table.ICxSourceService;
import cn.crap.inter.service.table.IEgmasSourceService;
import cn.crap.inter.service.table.IErrorService;
import cn.crap.inter.service.table.IInterfaceEgmasService;
import cn.crap.inter.service.table.IMenuService;
import cn.crap.inter.service.table.IModuleService;
import cn.crap.inter.service.table.IProjectService;
import cn.crap.inter.service.table.IRoleService;
import cn.crap.inter.service.table.IUserService;
import cn.crap.inter.service.tool.ICacheService;
import cn.crap.inter.service.tool.IPickService;
import cn.crap.model.AppVersion;
import cn.crap.model.CxModule;
import cn.crap.model.CxSource;
import cn.crap.model.EgmasSource;
import cn.crap.model.Error;
import cn.crap.utils.Const;
import cn.crap.utils.Tools;

/**
 * 下拉选着
 * 
 * @author Ehsan
 *
 */
@Service("pickService")
public class PickService implements IPickService {
	@Autowired
	IMenuService menuService;
	@Autowired
	IInterfaceEgmasService interfaceEgmasService;
	@Autowired
	private ICacheService cacheService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IProjectService projectService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IModuleService moduleService;
	@Autowired
	private IAppPageService appPageService;
	@Autowired
	private IArticleService articleService;
	@Autowired
	private IErrorService errorService;
	@Autowired
	private IAppVersionService appVersionService;
	@Autowired
	private IEgmasSourceService egmasSourceService;
	@Autowired
	private ICxSourceService cxSourceService;
	@Autowired
	private ICxModuleService cxModuleService;
	
	@Override
	public void getPickList(List<PickDto> picks, String code, String key, LoginInfoDto user) throws MyException {
		PickDto pick = null;
		switch (code) {
		// case "RECOMMENDPROJECT": // 推荐的模块
		// for (Project p : projectService.findByMap(Tools.getMap("type",
		// ProjectType.RECOMMEND.getType() ), null, null)) {
		// pick = new PickDto(p.getId(), p.getName());
		// picks.add(pick);
		// }
		// return;
		case "REQUESTMETHOD": // 枚举 请求方式 post get
			for (RequestMethod status : RequestMethod.values()) {
				pick = new PickDto(status.name(), status.getName(), status.getName());
				picks.add(pick);
			}
			return;
		// 枚举 接口状态
		case "INTERFACESTATUS":
			for (InterfaceStatus status : InterfaceStatus.values()) {
				pick = new PickDto(status.getName(), status.name());
				picks.add(pick);
			}
			return;
		case "TRUEORFALSE":// 枚举true or false
			for (TrueOrFalse status : TrueOrFalse.values()) {
				pick = new PickDto(status.getName(), status.name());
				picks.add(pick);
			}
			return;
		case "MONITORTYPE":// 监控类型
			for (MonitorType monitorType : MonitorType.values()) {
				pick = new PickDto(monitorType.name(), monitorType.getValue() + "", monitorType.getName());
				picks.add(pick);
			}
			return;
		case "ERRORCODE":// 错误码
			for (Error error : errorService.findByMap(Tools.getMap("moduleId", key), null, "errorCode asc")) {
				pick = new PickDto(error.getErrorCode(), error.getErrorCode() + "--" + error.getErrorMsg());
				picks.add(pick);
			}
			return;
			
		//********************************  EGMAS 扩展  ********************************************/
		case "CXMODULES":
			// 查看某个项目下的模块
				for(CxModule m : cxModuleService.findByMap(null, null, null)){
					pick = new PickDto(m.getId(), m.getName()+"</br>("+m.getUrl()+")");
					picks.add(pick);
				}
			return;
		case "EGMASSOURCE": // 枚举 EGMAS数据源 post get
			// 先去数据库查询
			List<EgmasSource> egmasSourceList = egmasSourceService.findByMap(null, " new EgmasSource(id,createTime,status,sequence,name,url,updateTime) ", null, null);
			for (EgmasSource egmasSource : egmasSourceList) {
				pick = new PickDto(egmasSource.getId(), egmasSource.getName(), egmasSource.getName());
				picks.add(pick);
			}
			return;
		case "CXSOURCE": // 枚举 CX数据源 post get
			// 先去数据库查询
			List<CxSource> cxSourceList = cxSourceService.findByMap(null, " new CxSource(id,createTime,status,sequence,name,url,updateTime) ", null, null);
			for (CxSource egmasSource : cxSourceList) {
				pick = new PickDto(egmasSource.getId(), egmasSource.getId(), egmasSource.getName());
				picks.add(pick);
			}
			return;
		case "PLATFORMTYPE": // 平台类型
			for (PlatformType status : PlatformType.values()) {
				pick = new PickDto(status.getName(), status.name());
				picks.add(pick);
			}
			return;
		case "APP_VERSION_NAME":		//APP版本名称集合
			for (AppVersion error : appVersionService.findByMap(Tools.getMap("appID", key), null,null)) {
				pick = new PickDto(error.getId(), error.getName());
				picks.add(pick);
			}
			return;
		case "APP_VERSION_CODE": 		//APP版本CODE集合
			for (AppVersion error : appVersionService.findByMap(Tools.getMap("appID", key), null,null)) {
				pick = new PickDto(error.getId(), error.getCode());
				picks.add(pick);
			}
			return;
		case "INTERFACE_FUNCTION_MODULE": // 接口功能模块
			int f = 0;
			@SuppressWarnings("unchecked")
			List<String> functionModuleList = (List<String>) interfaceEgmasService.queryByHql("select customModule from InterfaceEgmas group by customModule", null);
			for (String w : functionModuleList) {
				if (w == null)
					continue;
				f++;
				pick = new PickDto("fun_" + f, w, w);
				picks.add(pick);
			}
			return;
		case "RESPONSIBILITY_MODULE": // 责任田 模块名
			int i = 0;
			@SuppressWarnings("unchecked")
			List<String> categorys = (List<String>) appPageService.queryByHql("select module from AppPage group by module", null);
			for (String w : categorys) {
				if (w == null)
					continue;
				i++;
				pick = new PickDto("cat_" + i, w, w);
				picks.add(pick);
			}
			return;
		case "RESPONSIBILITY_PERSON": // 责任人 模块名
			int j = 0;
			List<String> androidList = (List<String>) appPageService.queryByHql("select android from AppPage group by android", null);
			List<String> iosList = (List<String>) appPageService.queryByHql("select ios from AppPage group by ios", null);
			List<String> testList = (List<String>) appPageService.queryByHql("select test from AppPage group by test", null);

			if (androidList != null && androidList.size() >= 1) {
				pick = new PickDto(Const.SEPARATOR, "Android");
				picks.add(pick);
				for (String w : androidList) {
					if (w == null || "".equals(w))
						continue;
					j++;
					pick = new PickDto("cat_android_" + j, "A"+w, "A"+w);
					picks.add(pick);
				}
			}
			if (iosList != null && iosList.size() >= 1) {
				pick = new PickDto(Const.SEPARATOR, "IOS");
				picks.add(pick);
				j = 0;
				for (String w : iosList) {
					if (w == null || "".equals(w))
						continue;
					j++;
					pick = new PickDto("cat_ios_" + j, "I"+w, "I"+w);
					picks.add(pick);
				}
			}
			if (testList != null && testList.size() >= 1) {
				pick = new PickDto(Const.SEPARATOR, "测试");
				picks.add(pick);
				j = 0;
				for (String w : testList) {
					if (w == null || "".equals(w))
						continue;
					j++;
					pick = new PickDto("cat_test" + j, "T"+w, "T"+w);
					picks.add(pick);
				}
			}
			return;
		case "INTERFACE_RESPONSIBILITY_PERSON": // 接口责任人 
			int n = 0;
			List<String> interfaceList = (List<String>) interfaceEgmasService.queryByHql("SELECT  responsiblePerson 	FROM InterfaceEgmas group by responsiblePerson", null);
			if (interfaceList != null && interfaceList.size() >= 1) {
				for (String w : interfaceList) {
					if (w == null || "".equals(w))
						continue;
					n++;
					pick = new PickDto("person_" + n, w, w);
					picks.add(pick);
				}
			}
			return;
		}
	}
}
