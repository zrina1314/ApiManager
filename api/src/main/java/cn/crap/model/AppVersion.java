package cn.crap.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import cn.crap.framework.base.BaseModel;


@Entity
@Table(name="app_version")
@GenericGenerator(name="Generator", strategy="cn.crap.framework.IdGenerator")
public class AppVersion extends BaseModel implements Serializable{
	 /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	private String name;
	private String desc;
	private String code;
	private String force;
	private String appID;
	private String updateTime;
	private String updateUserID;
	private String apkUrl;

	
	
	public AppVersion(){};
	
	public AppVersion(String id, String name, String desc, String code, String force, String appID, String updateTime, String updateUserID,String apkUrl) {
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.code = code;
		this.force = force;
		this.appID = appID;
		this.updateTime = updateTime;
		this.updateUserID = updateUserID;
		this.apkUrl = apkUrl;
	}
	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "desc")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Column(name = "code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	@Column(name = "force")
	public String getForce() {
		return force;
	}

	public void setForce(String force) {
		this.force = force;
	}
	@Column(name = "app_id")
	public String getAppID() {
		return appID;
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}
	@Column(name = "update_time")
	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	@Column(name = "update_user_id")
	public String getUpdateUserID() {
		return updateUserID;
	}

	public void setUpdateUserID(String updateUserID) {
		this.updateUserID = updateUserID;
	}

	@Column(name = "api_url")
	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}	
}
