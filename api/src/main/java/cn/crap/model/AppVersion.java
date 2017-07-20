package cn.crap.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import cn.crap.framework.base.BaseModel;

@Entity
@Table(name = "app_version")
@GenericGenerator(name = "Generator", strategy = "cn.crap.framework.IdGenerator")
public class AppVersion extends BaseModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private String versionDesc;
	private String code;
	private String versionForce;	//是否强制更新
	private String appID;
	private String createUserID;
	private String createUserName;
	private String filePathUat;
	private String filePathProduct;
	public AppVersion() {
	};

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "versionDesc")
	public String getVersionDesc() {
		return versionDesc;
	}

	public void setVersionDesc(String versionDesc) {
		this.versionDesc = versionDesc;
	}

	@Column(name = "code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "versionForce")
	public String getVersionForce() {
		return versionForce;
	}

	public void setVersionForce(String versionForce) {
		this.versionForce = versionForce;
	}

	@Column(name = "appID")
	public String getAppID() {
		return appID;
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}
	
	@Column(name = "createUserID")
	public String getCreateUserID() {
		return createUserID;
	}

	public void setCreateUserID(String createUserID) {
		this.createUserID = createUserID;
	}
	
	@Column(name = "createUserName")
	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	@Column(name = "filePathUat")
	public String getFilePathUat() {
		return filePathUat;
	}

	public void setFilePathUat(String filePathUat) {
		this.filePathUat = filePathUat;
	}

	@Column(name = "filePathProduct")
	public String getFilePathProduct() {
		return filePathProduct;
	}

	public void setFilePathProduct(String filePathProduct) {
		this.filePathProduct = filePathProduct;
	}
	
	
}
