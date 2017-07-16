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
	private String desc;
	private String packageName;
	private String updateTime;
	private String updateUserId;
	private String tag;
	
    public App(){};
	
	public App( String name, String desc,String packageName,String updateTime,String updateUserId,String tag){
		this.name = name;
		this.desc = desc;
		this.packageName=packageName;
		this.updateTime = updateTime;
		this.updateUserId = updateUserId;
		this.tag = tag;
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
	@Column(name = "package_name")
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	@Column(name = "update_time")
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	@Column(name = "update_user_id")
	public String getUpdateUserId() {
		return updateUserId;
	}
	
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	@Column(name = "tag")
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	
}
