package cn.crap.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import cn.crap.framework.base.BaseModel;

/**
 * 责任田
 * 
 * @date 2016-01-06
 */
@Entity
@Table(name = "apppage")
@GenericGenerator(name = "Generator", strategy = "cn.crap.framework.IdGenerator")
public class AppPage extends BaseModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String module; // 模块
	private String function; // 功能
	private String android="/"; // Android责任人
	private String ios="/"; // IOS责任人
	private String test="/"; // 测试责任人
	private String remark; // 备注
	private String updateTime; // 更新时间

	public AppPage() {
	};

	/**
	 * 责任田
	 * 
	 * @param id
	 *            责任ID
	 * @param module
	 *            模块
	 * @param function
	 *            功能
	 * @param android
	 *            android责任人
	 * @param ios
	 *            ios责任人
	 * @param test
	 *            测试责任人
	 * @param remark
	 *            备注
	 */
	public AppPage(String id, String module, String function, String android, String ios, String test, String remark, String updateTime, int sequence) {
		this.id = id;
		this.module = module;
		this.function = function;
		this.android = android;
		this.ios = ios;
		this.test = test;
		this.remark = remark;
		this.updateTime = updateTime;
		this.sequence = sequence;
	}

	@Column(name = "module")
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	@Column(name = "function")
	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "android")
	public String getAndroid() {
		return android;
	}

	public void setAndroid(String android) {
		this.android = android;
	}

	@Column(name = "ios")
	public String getIos() {
		return ios;
	}

	public void setIos(String ios) {
		this.ios = ios;
	}

	@Column(name = "test")
	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	@Column(name = "updateTime")
	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}