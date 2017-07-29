package cn.crap.service.tool;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.sf.email.SendEMail;

import cn.crap.dto.EmailInfoDTO;
import cn.crap.inter.service.table.IAppService;
import cn.crap.inter.service.table.ISettingService;
import cn.crap.inter.service.tool.ICacheService;
import cn.crap.inter.service.tool.ISfEmailService;
import cn.crap.model.App;
import cn.crap.model.Setting;
import cn.crap.springbeans.Config;
import cn.crap.utils.Const;
import cn.crap.utils.StringUtils;
import cn.crap.utils.Tools;

@Service
public class SfEmailService implements ISfEmailService {
	private Log logger = LogFactory.getLog(ISfEmailService.class);// 日志类

	@Autowired
	private IAppService appService;
	private static final String ARTICLE_CONTENT = "" + "<h2><a href=\"http://10.118.4.52\\ApiDoc\">速运APP-文档系统</a></h2>" + "<p><h3> {0}刚刚{3}</h3><h1><a href=\"{2}\">【 {1} 】</a></h1></P>" + "<P>地址：<a href=\"{2}\">{2}</a></P>" + "有兴趣的小伙伴可以去看看<br>" + "{4}<br>"
			+ "<p>速运APP-文档系统是一个开放新技术交流平台，在这里可以探讨一切的技术（Android、IOS、EGMAS、自动化测试、等等）<br>速运APP-文档系统文库中包含：代码规范、项目源码介绍、技术难点、以及遇到的一些坑</p><br>" + "<p>如网页打开有样式问题，请用火狐浏览器打开</p><br>";

	@Autowired
	private JavaMailSenderImpl mailSenderService;
	@Autowired
	private ICacheService cacheService;
	@Autowired
	private ISettingService settingService;
	@Autowired
	private Config config;

	@Override
	public void sendAddArticleEmail(String title, String author, String url, String content) {
		Object[] params = { author, title, url, "发布了", content };
		sendAddAndUpdateArticleEmail(title, params);
	}

	@Override
	public void sendUpdateArticleEmail(String title, String author, String url, String content) {
		Object[] params = { author, title, url, "更新了", content };
		sendAddAndUpdateArticleEmail(title, params);
	}

	private void sendAddAndUpdateArticleEmail(String title, Object[] params) {
		String[] emailAddresses = new String[1];
		emailAddresses[0] = "80000807@sf-express.com";
		// emailAddresses[0] = "WXZDYFZ@sf-express.com";
		EmailInfoDTO mailInfoDTO = new EmailInfoDTO();
		// ICacheService cacheService =
		// SpringContextHolder.getBean("cacheService", CacheService.class);
		// mailInfoDTO.setEmailAddressFrom(cacheService.getSetting(Const.EMAIL_SEND_USER_ADDRESS).getValue());
		// mailInfoDTO.setPassword(cacheService.getSetting(Const.baishidada).getValue());

		mailInfoDTO.setSubject(title);
		// String emailContent = new MessageFormat(ARTICLE_CONTENT,
		// Locale.getDefault()).format(params);
		String emailContent = getMtml("10.118.61.67", "这是标题", "这是内容");

		mailInfoDTO.setContent(emailContent);
		mailInfoDTO.setEmailAddresses(emailAddresses);
		sendEmail(mailInfoDTO);
	}

	@Override
	public void sendJenkinsEmail(String jobName, String buildStatus, String buildUrl, String uatQrImg, String uatApk, String productQrImg, String productApk, String changesSinceLastSuccess) {
		String tempJobName = jobName;
		// 先通过JobName获取到APP名称
		List<App> apps = appService.findByMap(Tools.getMap("tag", jobName), null, null);
		if (apps.size() > 0) {
			App app = apps.get(0);
			tempJobName = app.getAppDesc();
		}

		EmailInfoDTO mailInfoDTO = new EmailInfoDTO();
		String[] emailAddresses = new String[1];
//		emailAddresses[0] = "80000807@sf-express.com";
		 emailAddresses[0] = "WXZDYFZ@sf-express.com";
		String content = getJenkinsEmainContent(tempJobName, buildStatus, buildUrl, uatQrImg, uatApk, productQrImg, productApk, changesSinceLastSuccess);

		mailInfoDTO.setSubject(tempJobName + " - Jenkins打包结果");
		mailInfoDTO.setContent(getMtml(tempJobName + " - Jenkins打包结果", content));
		mailInfoDTO.setEmailAddresses(emailAddresses);
		sendEmail(mailInfoDTO);
	}

	private void sendEmail(EmailInfoDTO mailInfoDTO) {
		SendEMail sendEmail = new SendEMail(mailInfoDTO);
		Thread thread = new Thread(sendEmail);
		thread.start();
	}

	private String getServerIP() {
		String serverIP = cacheService.getStr(Const.CACHE_SERVER_IP);
		if (StringUtils.isEmpty(serverIP)) {
			// 第二步读取数据库中的IP
			List<Setting> settings = settingService.findByMap(Tools.getMap("key", Const.SETTING_SERVER_IP), null, null);
			if (settings.size() > 0) {
				Setting setting = settings.get(0);
				serverIP = setting.getValue();
			}
		}
		if (StringUtils.isEmpty(serverIP)) {
			serverIP = "192.168.1.187";
		}
		return serverIP;
	}

	private String getJenkinsEmainContent(String JOB_DESCRIPTION, String buildStatus, String buildUrl, String uatQrImg, String uatApk, String productQrImg, String productApk, String changesSinceLastSuccess) {
		String content = "" + "<style>\n" + ".c1 {\n" + "\tfont-size: 14px;\n\tpadding:5px;\n" + "\ttext-align: center\n" + "}\n" + ".c2 {\n" + "\ttext-align: center\n" + "}\n" + ".c3 {\n" + "\ttext-align: center\n" + "}\n" + "</style>\n"
				+ "<table border='0' cellspacing='1' cellpadding='0'>\n" + "<tr>\n" + "  <td class='c1'>项目名称</td>\n" + "  <td colspan=\"2\" style=\"text-align:left;padding-left:5px;\"> " + JOB_DESCRIPTION + "</td>\n" + "</tr>\n" + "<tr>\n" + "  <td class='c1'>构建状态</td>\n" + "  <td colspan=\"2\" style=\"text-align:left;padding-left:5px;\">" + buildStatus + "</td>\n" + "</tr>\n"
				+ "<tr>\n" + "  <td class='c1' rowspan=\"2\">二维码</td>\n" + "  <td colspan=\"1\" class=\"c1 c2\">测试环境</td>\n" + "  <td colspan=\"1\" class=\"c1 c2 c3\">生产环境</td>\n" + "</tr>\n" + "<tr>\n" + "  <td class=\"c2\"><img src='" + uatQrImg + "' width=\"220px;\" height=\"220px\"></td>\n"
				+ "  <td class=\"c2\"><img src='" + productQrImg + "' width=\"220px;\" height=\"220px\"></td>\n" + "</tr>\n" + "<tr>\n" + "  <td class='c1'>下载地址</td>\n" + "  <td class=\"c2\"><a href=\"" + uatApk + "\">点击下载</a></td>\n" + "  <td class=\"c3\"><a href='" + productApk
				+ "' >点击下载</a></td>\n" + "</tr>\n" + "<!--<tr><td class='c1'>构建时间</td><td colspan=\"4\" style=\"text-align:center\">2016-09-26 19:07</td></tr>-->\n" + "<tr>\n" + "  <td class='c1'>构建日志</td>\n" + "  <td colspan=\"4\"><a  href=\"" + buildUrl + "\">点击查看</a></td>\n" + "</tr>\n"
				+ "<table>\n";
		return content;
	}

	private String getMtml(String title, String content) {
		return getMtml(getServerIP(), title, content);
	}

	private String getMtml(String serverIP, String title, String content) {
		String emailTemplate = "<!DOCTYPE html>\n" + "<html>\n" + "<head>\n" + "<meta charset=\" utf-8\">\n" + "<meta name=\"author\" content=\"http://www.softwhy.com/\" />\n" + "<title>" + title + "</title>\n" + "<style>\n" + "* {\n" + "\tmargin: 0;\n" + "\tborder: 0;\n" + "\tpadding: 0;\n"
				+ "\tfont-family: '微软雅黑'\n" + "}\n" +"body{	text-align:center;		}\n	.mainContainer{		margin:0 auto;		}"+ ".title {\n" + "\tfont-size: 24px;\n" + "\ttext-align: center;\n" + "\tbackground-color: #1E90FF;\n" + "\tcolor: #FFFFFF;\n" + "\tpadding: 10px\n" + "}\n" + ".welcome a {\n" + "\tfont-size: 12px;\n" + "\ttext-indent: 2em;\n"
				+ "\tpadding-top: 5px;\n" + "\ttext-align: left;\n" + "\tpadding-bottom: 5px;\n" + "\tpadding-left: 10px;\n" + "\tpadding-right: 10px;\n" + "\tpadding: 10px\n" + "}\n" + ".footer {\n" + "\twidth: auto;\n" + "\ttext-align: right;\n" + "\tfont-size: 12px;\n" + "\tpadding: 10px\n"
				+ "}\n" + ".footer a {\n" + "\twidth: auto;\n" + "\ttext-align: right;\n" + "\tpadding: 10px\n" + "\tfont-size: 12px;\n" + "}\n" + "table {\n" + "\tborder-collapse: collapse;\n" + "\twidth: 650px;\n" + "}\n" + "tr, td, th {\n" + "\tborder: 1px solid #1E90FF;\n"
				+ "\tminHeight: 10px;\n" + "\t\n" + "}\n" + "th {\n" + "\tfont-size: 20px;\n" + "\tbackground: #1E90FF;\n" + "\tcolor: #fff\n" + "}\n" + "td a {\n" + "\ttext-decoration: none;\n" + "\tcolor: blue;\n" + "\ttext-align: center;\n" + "\tdisplay: block\n" + "}\n" + "</style>\n"
				+ "</head>\n" + "<body>\n" + "<table width=\"200\" border=\"1\" class=\"mainContainer\">\n" + "  <tr>\n" + "    <td class=\"title\"><strong>" + title + "</strong></td>\n" + "  </tr>\n" + "  <tr>\n" + "    <td class=\"welcome\"><a target=\"_blank\" href=\"http://" + serverIP
				+ "\">亲爱的各位研发小伙伴，这是一个功能比较全的网站。包含EGMAS接口测试桩、加解密处理、短信验证码查询、速运通Apk打包管理、开发文档等技术文章文献。欢迎大家多多使用，多多交流</a></td>\n" + "  </tr>\n" + "  <tr>\n" + "    <td>" + content + "</td>\n" + "  </tr>\n" + "  <tr>\n" + "    <td class=\"footer\"><a href=\"http://" + serverIP
				+ "\" target=\"_blank\" style=\"color:#6f5499;\">本网站由花心大萝卜提供技术与支持</a></td>\n" + "  </tr>\n" + "</table>\n" + "</body>\n" + "</html>";
		logger.debug(emailTemplate);
		return emailTemplate;
	}
}
