package cn.crap.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import cn.crap.framework.base.BaseModel;

@Entity
@Table(name="app")
@GenericGenerator(name="Generator", strategy="cn.crap.framework.IdGenerator")
public class App extends BaseModel implements Serializable{
	
	 /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	private String name;
	private String appDesc;
	private String packageName;
	private String createUserID;
	private String createUserName;
	private String tag;
	
    public App(){};
	
	@Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "appDesc")
	public String getAppDesc() {
		return appDesc;
	}
	public void setAppDesc(String appDesc) {
		this.appDesc = appDesc;
	}
	@Column(name = "packageName")
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
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

	@Column(name = "tag")
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	
}
