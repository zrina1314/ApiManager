package cn.crap.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import cn.crap.framework.base.BaseModel;
import cn.crap.framework.base.BaseModelBase;

/**
 * @date 2016-12-29 EGMAS 验证码
 */
@Entity
@Table(name = "t_user_mobiles")
@GenericGenerator(name = "Generator", strategy = "cn.crap.framework.IdGenerator")
public class UserMobiles extends BaseModel implements Serializable {

	public UserMobiles() {
	}

	public UserMobiles(String mobile, String isValid, String validCode, Date createTm, String id, String userID, String validCodeImage, String type) {
		this.mobile = mobile;
		this.isValid = isValid;
		this.validCode = validCode;
		this.createTm = createTm;
		this.id = id;
		this.userID = userID;
		this.validCodeImage = validCodeImage;
		this.type = type;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mobile;
	private String isValid;
	private String validCode;
	private Date createTm;
	private String userID;
	private String validCodeImage;
	private String type;

	@Column(name = "mobile")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "is_valid")
	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String is_Valid) {
		this.isValid = is_Valid;
	}

	@Column(name = "valid_code")
	public String getValidCode() {
		return validCode;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}

	@Column(name = "create_tm")
	public Date getCreateTm() {
		return createTm;
	}

	public void setCreateTm(Date createTm) {
		this.createTm = createTm;
	}

	@Column(name = "user_id")
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	@Column(name = "valid_code_image")
	public String getValidCodeImage() {
		return validCodeImage;
	}

	public void setValidCodeImage(String validCodeImage) {
		this.validCodeImage = validCodeImage;
	}

	@Column(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Transient
	public String getCreateTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCreateTime(String createTime) {
		// TODO Auto-generated method stub
		
	}

	@Transient
	public int getSequence() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setSequence(int sequence) {
		// TODO Auto-generated method stub
		
	}
}
