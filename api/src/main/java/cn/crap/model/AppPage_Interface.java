package cn.crap.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import cn.crap.framework.base.BaseModel;

/**
 * 前端页面与接口的关系表
 * 
 * @date 2016-01-06
 */
@Entity
@Table(name = "apppage_interface")
@GenericGenerator(name = "Generator", strategy = "cn.crap.framework.IdGenerator")
public class AppPage_Interface extends BaseModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String appPage_id; // 页面ID
	private String interface_id; // 接口ID
	
	public AppPage_Interface() {
	};

	/**
	 * 前端页面与接口的关系表
	 * 
	 * @param appPage_id
	 *            前端页面ID
	 * @param interface_id
	 *            接口ID
	 */
	public AppPage_Interface(String appPage_id, String interface_id) {
		this.appPage_id = appPage_id;
		this.interface_id = interface_id;
	}

	@Column(name = "appPage_id")
	public String getAppPageId() {
		return appPage_id;
	}

	public void setAppPageId(String appPage_id) {
		this.appPage_id = appPage_id;
	}

	@Column(name = "interface_id")
	public String getInterfaceId() {
		return interface_id;
	}

	public void setInterfaceId(String interface_id) {
		this.interface_id = interface_id;
	}
}