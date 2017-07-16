package cn.crap.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import cn.crap.dto.ILuceneDto;
import cn.crap.dto.SearchDto;
import cn.crap.enumeration.MonitorType;
import cn.crap.enumeration.ProjectType;
import cn.crap.framework.SpringContextHolder;
import cn.crap.framework.base.BaseModel;
import cn.crap.inter.service.tool.ICacheService;
import cn.crap.service.tool.CacheService;
import cn.crap.utils.Const;
import cn.crap.utils.MyString;
import cn.crap.utils.Tools;

@Entity
@Table(name = "interfaceegmas")
@GenericGenerator(name = "Generator", strategy = "cn.crap.framework.IdGenerator")
public class InterfaceEgmas  extends BaseModel implements Serializable {
	private static final String DEFAULT_HEADER = "[{\"name\":\"deviceId\",\"necessary\":\"false\",\"type\":\"string\",\"def\":\"V5cn5ddlLPEDAADEXdVjpaTO\",\"remark\":\"\"}" + ",{\"name\":\"phoneId\",\"necessary\":\"false\",\"type\":\"string\",\"def\":\"V5cn5ddlLPEDAADEXdVjpaTO\",\"remark\":\"\"}"
			+ ",{\"name\":\"mediaCode\",\"necessary\":\"false\",\"type\":\"string\",\"def\":\"ANDROID.APP\",\"remark\":\"\"} " + ",{\"name\":\"systemCode\",\"necessary\":\"false\",\"type\":\"string\",\"def\":\"ANDROID.APP\",\"remark\":\"\"}"
			+ ",{\"name\":\"platform\",\"necessary\":\"false\",\"type\":\"string\",\"def\":\"Android\",\"remark\":\"\"}" + ",{\"name\":\"langCode\",\"necessary\":\"false\",\"type\":\"string\",\"def\":\"sc\",\"remark\":\"\"}"
			+ ",{\"name\":\"systemVersion\",\"necessary\":\"false\",\"type\":\"string\",\"def\":\"6.0.1\",\"remark\":\"\"}" + ",{\"name\":\"countryCode\",\"necessary\":\"false\",\"type\":\"string\",\"def\":\"CN\",\"remark\":\"\"}"
			+ ",{\"name\":\"Content-Type\",\"necessary\":\"false\",\"type\":\"string\",\"def\":\"application/x-www-form-urlencoded\",\"remark\":\"\"}" + ",{\"name\":\"client_ver\",\"necessary\":\"false\",\"type\":\"string\",\"def\":\"8.2.0\",\"remark\":\"\"}"
			+ ",{\"name\":\"screen_size\",\"necessary\":\"false\",\"type\":\"string\",\"def\":\"1440x2560\",\"remark\":\"\"}" + ",{\"name\":\"protocol_ver\",\"necessary\":\"false\",\"type\":\"string\",\"def\":\"334\",\"remark\":\"\"}"
			+ ",{\"name\":\"region_code\",\"necessary\":\"false\",\"type\":\"string\",\"def\":\"CN\",\"remark\":\"\"}" + ",{\"name\":\"language_code\",\"necessary\":\"false\",\"type\":\"string\",\"def\":\"zh_CN\",\"remark\":\"\"}"
			+ ",{\"name\":\"model\",\"necessary\":\"false\",\"type\":\"string\",\"def\":\"Le X820\",\"remark\":\"\"}]";
	// private static final String DEFAULT_URL =
	// "http://ezgo-isn.intsit.sfdc.com.cn/appSyt/";
	private static final String DEFAULT_METHOD = "EGMAS";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url;
	private String method = DEFAULT_METHOD;
	private String param;
	private String requestExam;
	private String responseParam;
	private String errorList;
	private String trueExam;
	private String falseExam;
	private String moduleId;
	private String interfaceName;
	private String updateBy;
	private String updateTime;
	private String remark;// 备注
	private String errors;
	private String version = "1";// 版本号
	private String header;// 请求头
	private String requestMethod;// EGMAS请求信息
	private String responsiblePerson;// 责任人
	private String customModule; // 自定义模块名，按功能划分

	public InterfaceEgmas() {
	}

	public InterfaceEgmas(String interfaceName, String updateTime, String requestMethod, String responsiblePerson, String customModule) {
		super();
		this.interfaceName = interfaceName;
		this.updateTime = updateTime;
		this.requestMethod = requestMethod;
		this.responsiblePerson = responsiblePerson;
		this.customModule = customModule;
	}

	public InterfaceEgmas(String id, String moduleId, String interfaceName, String version, String createTime, String updateBy, String updateTime, String remark, int sequence) {
		super();
		this.id = id;
		this.moduleId = moduleId;
		this.interfaceName = interfaceName;
		this.version = version;
		this.createTime = createTime;
		this.updateBy = updateBy;
		this.updateTime = updateTime;
		this.remark = remark;
		this.sequence = sequence;
	}

	public InterfaceEgmas(String id, String moduleId, String interfaceName, String version, String createTime, String updateBy, String updateTime, String remark, int sequence, String requestMethod) {
		this(id, moduleId, interfaceName, version, createTime, updateBy, updateTime, remark, sequence);
		this.requestMethod = requestMethod;
	}

	public InterfaceEgmas(String id, String moduleId, String interfaceName, String version, String createTime, String updateBy, String updateTime, String remark, int sequence, String requestMethod, String responsiblePerson) {
		this(id, moduleId, interfaceName, version, createTime, updateBy, updateTime, remark, sequence, requestMethod);
		this.responsiblePerson = responsiblePerson;
	}

	public InterfaceEgmas(String id, String moduleId, String interfaceName, String version, String createTime, String updateBy, String updateTime, String remark, int sequence, String requestMethod, String responsiblePerson, String customModule) {
		this(id, moduleId, interfaceName, version, createTime, updateBy, updateTime, remark, sequence, requestMethod, responsiblePerson);
		this.customModule = customModule;
	}

	@Transient
	public SearchDto toSearchDto() {
		SearchDto dto = new SearchDto();
		dto.setId(id);
		dto.setCreateTime(createTime);
		dto.setContent(remark + responseParam + param + requestMethod);
		dto.setModuleName(getModuleName());
		dto.setTitle(interfaceName);
		dto.setType(Interface.class.getSimpleName());
		dto.setUrl("#/font/interfaceEgmasDetail/" + id);
		dto.setVersion(version);
		dto.setRequestMethod(requestMethod);
		return dto;

	}

	@Transient
	@Override
	public String getLogRemark() {
		return interfaceName;
	}

	@Transient
	public String getModuleName() {
		if (!MyString.isEmpty(moduleId)) {
			ICacheService cacheService = SpringContextHolder.getBean("cacheService", CacheService.class);
			Module module = cacheService.getModule(moduleId);
			if (module != null)
				return module.getName();
		}
		return "";
	}

	@Transient
	public String getModuleUrl() {
		if (!MyString.isEmpty(moduleId)) {
			ICacheService cacheService = SpringContextHolder.getBean("cacheService", CacheService.class);
			Module module = cacheService.getModule(moduleId);
			if (module != null)
				return MyString.isEmpty(module.getUrl()) ? "" : module.getUrl();
		}
		return "";
	}

	@Column(name = "errors")
	public String getErrors() {
		return errors;
	}

	public void setErrors(String errors) {
		this.errors = errors;
	}

	@Column(name = "url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "method")
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Column(name = "param")
	public String getParam() {
		if (MyString.isEmpty(param))
			return "form=[]";
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	@Column(name = "requestExam")
	public String getRequestExam() {
		return requestExam;
	}

	public void setRequestExam(String requestExam) {
		this.requestExam = requestExam;
	}

	@Column(name = "responseParam")
	public String getResponseParam() {
		if (MyString.isEmpty(responseParam))
			return "[]";
		return responseParam;
	}

	public void setResponseParam(String responseParam) {
		this.responseParam = responseParam;
	}

	@Column(name = "errorList")
	public String getErrorList() {
		return errorList;
	}

	public void setErrorList(String errorList) {
		this.errorList = errorList;
	}

	@Column(name = "trueExam")
	public String getTrueExam() {
		return trueExam;
	}

	public void setTrueExam(String trueExam) {
		this.trueExam = trueExam;
	}

	@Column(name = "falseExam")
	public String getFalseExam() {
		return falseExam;
	}

	public void setFalseExam(String falseExam) {
		this.falseExam = falseExam;
	}

	@Column(name = "moduleId")
	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	@Column(name = "interfaceName")
	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "updateBy")
	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	@Column(name = "updateTime")
	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Column(name = "header")
	public String getHeader() {
		if (MyString.isEmpty(header))
			return "[]";
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	/** 初始化头信息 */
	public void initHeader() {
		this.header = DEFAULT_HEADER;
	}

	@Column(name = "requestMethod")
	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	@Column(name = "responsiblePerson")
	public String getResponsiblePerson() {
		return responsiblePerson;
	}

	public void setResponsiblePerson(String responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}

	@Column(name = "customModule")
	public String getCustomModule() {
		return customModule;
	}

	public void setCustomModule(String customModule) {
		this.customModule = customModule;
	}

	// 所在项目
	@Transient
	public String getProjectId() {
		if (!MyString.isEmpty(moduleId)) {
			ICacheService cacheService = SpringContextHolder.getBean("cacheService", CacheService.class);
			Module module = cacheService.getModule(moduleId);
			if (module != null)
				return module.getProjectId();
		}
		return "";
	}

}