package com.sf.email;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import cn.crap.dto.EmailInfoDTO;
import cn.crap.utils.Const;
import cn.crap.utils.StringUtils;

public class SendEMail implements Runnable {
	private EmailInfoDTO mailInfoDTO;

	public SendEMail(EmailInfoDTO mailInfoDTO) {
		this.mailInfoDTO = mailInfoDTO;
	}

	@Override
	public synchronized void run() {
		sendEmail();
	}

	/***
	 * 发送邮件函数
	 * 
	 * @param toAddress
	 *            收件人（为空则从数据库读取所有联系人）
	 * @return
	 */
	public boolean sendEmail() {
		try {
			if (mailInfoDTO == null) {
				return false;
			}
			if (StringUtils.isEmpty(mailInfoDTO.getEmailAddressFrom())) {
				return false;
			}
			if (mailInfoDTO.getEmailAddresses() == null) {
				return false;
			}
			Properties props = System.getProperties();
			props.put("mail.smtp.host", StringUtils.isEmpty(mailInfoDTO.getEmailServerHost()) ? Const.EMAIL_SERVER_HOST : mailInfoDTO.getEmailServerHost().trim());
			props.put("mail.smtp.port", StringUtils.isEmpty(mailInfoDTO.getMailServerPort()) ? Const.EMAIL_SERVER_PORT : mailInfoDTO.getMailServerPort().trim());
			props.put("mail.smtp.auth", "true");
			final String userName = StringUtils.isEmpty(mailInfoDTO.getUserName()) ? Const.EMAIL_SEND_USER_NAME : mailInfoDTO.getUserName().trim();
			final String password = StringUtils.isEmpty(mailInfoDTO.getPassword()) ? Const.EMAIL_SEND_USER_PWD : mailInfoDTO.getPassword().trim();
			Session session = Session.getDefaultInstance(props, new Authenticator() {
				@Override
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(userName, password);
				}
			});
			MimeMessage mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(mailInfoDTO.getEmailAddressFrom().trim()));
			InternetAddress[] emailAddressTo = getInternetAddresses(mailInfoDTO.getEmailAddresses());
			mimeMessage.setSubject(mailInfoDTO.getSubject());
			mimeMessage.setRecipients(Message.RecipientType.TO, emailAddressTo);
			Multipart multipart = new MimeMultipart();
			MimeBodyPart mailContent = new MimeBodyPart();
			mailContent.setContent(mailInfoDTO.getContent(), "text/html;charset=utf-8");
			multipart.addBodyPart(mailContent);
			mimeMessage.setContent(multipart);
			mimeMessage.setSentDate(new Date());
			Transport.send(mimeMessage);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/** 地址转换 */
	private InternetAddress[] getInternetAddresses(String[] emailAddresses) throws AddressException {
		InternetAddress[] internetAddresses = new InternetAddress[emailAddresses.length];
		for (int i = 0; i < emailAddresses.length; i++) {
			internetAddresses[i] = new InternetAddress(emailAddresses[i].trim());
		}
		return internetAddresses;
	}
}
