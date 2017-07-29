package cn.crap.dto;

import java.io.Serializable;

import cn.crap.utils.Const;

public class EmailInfoDTO implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String emailAddressFrom;
	private String emailServerHost;
	private String userName;
	private String password;
	private String subject;
	private String mailServerPort;
	/** 收件人 */
	private String[] emailAddresses;
	private String content;

	public EmailInfoDTO() {
		emailServerHost = Const.EMAIL_SERVER_HOST;
		mailServerPort = Const.EMAIL_SERVER_PORT;
		userName = Const.EMAIL_SEND_USER_NAME;
		password = Const.EMAIL_SEND_USER_PWD;
		emailAddressFrom = Const.EMAIL_SEND_ADDRESS;
	}

	/** 获取收件人邮箱列表 */
	public String[] getEmailAddresses() {
		return emailAddresses;
	}

	/** 设置收件人邮箱列表 */
	public void setEmailAddresses(String[] emailAddresses) {
		this.emailAddresses = emailAddresses;
	}

	/** 获取邮件正文内容 */
	public String getContent() {
		return content;
	}

	/** 设置邮件正文内容 */
	public void setContent(String content) {
		this.content = content;
	}

	public String getEmailAddressFrom() {
		return emailAddressFrom;
	}

	public void setEmailAddressFrom(String emailAddressFrom) {
		this.emailAddressFrom = emailAddressFrom;
	}

	public String getEmailServerHost() {
		return emailServerHost;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMailServerPort() {
		return mailServerPort;
	}

	public void setMailServerPort(String mailServerPort) {
		this.mailServerPort = mailServerPort;
	}

	public void setEmailServerHost(String emailServerHost) {
		this.emailServerHost = emailServerHost;
	}

}
