package cn.crap.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import cn.crap.framework.SpringContextHolder;
import cn.crap.framework.base.BaseModel;
import cn.crap.inter.service.tool.ICacheService;
import cn.crap.service.tool.CacheService;


@Entity
@Table(name="cx_module")
@GenericGenerator(name="Generator", strategy="cn.crap.framework.IdGenerator")
public class CxModule extends BaseModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String url;
	private byte canDelete;
	private String remark;
	private String userId;
	private String templateId;
	private String templateName="";
	
	public CxModule(String id,String name, String url, String remark, String userId, String createTime,byte canDelete){
		this.id = id;
		this.name = name;
		this.url = url;
		this.remark = remark;
		this.userId = userId;
		this.createTime = createTime;
		this.canDelete = canDelete;
	}
	public CxModule(){};
	public CxModule(String name) {
		this.name = name;
		this.userId = "superAdmin";
	}

	@Column(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@Column(name="url")
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Column(name="canDelete")
	public byte getCanDelete() {
		return canDelete;
	}
	public void setCanDelete(byte canDelete) {
		this.canDelete = canDelete;
	}
	
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name="userId")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name="templateId")
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	
	@Transient
	public String getTemplateName(){
		return templateName;
	}
	public void setTemplateName(String templateName){
		this.templateName=templateName;
	}
	
	@Transient
	@Override
	public String getLogRemark() {
		return name;
	}
	
}
	