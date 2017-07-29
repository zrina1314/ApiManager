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
		// emailAddresses[0] = "80000807@sf-express.com";
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
		String content = "" + "<style>\n" + "* {\n" + "\tmargin: 0;\n" + "\tborder: 0;\n" + "\tpadding: 0;\n" + "\tfont-family: '微软雅黑'\n" + "}\n" + "table {\n" + "\tborder-collapse: collapse;\n" + "\twidth: 650px;\n" + "\tmargin: 5px\n" + "}\n" + "tr, td, th {\n" + "\tborder: 1px solid #FFFFFF;\n"
				+ "\theight: 30px;\n" + "\tpadding: 10px\n" + "}\n" + "th {\n" + "\tfont-size: 20px;\n" + "\tbackground: #1E90FF;\n" + "\tcolor: #FFFFFF\n" + "}\n" + "td a {\n" + "\ttext-decoration: none;\n" + "\tcolor: FFFFFF;\n" + "\ttext-align: center;\n" + "\tdisplay: block\n" + "}\n"
				+ ".c1 {\n" + "\tcolor:#FFFFFF;\n\tfont-weight: bold;\n" + "\ttext-align: center\n" + "}\n" + ".c2 {\n\tcolor:#FFFFFF;\n\n" + "\ttext-align: center\n" + "}\n" + ".c3 {\n\tcolor:#FFFFFF;\n\n" + "\ttext-align: center\n" + "}\n" + "</style>\n"
				+ "<table border='0' cellspacing='1' cellpadding='0'>\n" + "<tr>\n" + "  <td class='c1'>项目名称</td>\n" + "  <td colspan=\"2\" color=\"#FFFFFF\"> <font color=\"#FFFFFF\">" + JOB_DESCRIPTION + "</font></td>\n" + "</tr>\n" + "<tr>\n" + "  <td class='c1'>构建状态</td>\n"
				+ "  <td colspan=\"2\" color=\"#FFFFFF\"> <font color=\"#FFFFFF\">" + buildStatus + "</font></td>\n" + "</tr>\n" + "<tr>\n" + "  <td class='c1' rowspan=\"2\">二维码</td>\n" + "  <td colspan=\"1\" class=\"c1 c2\">测试环境</td>\n" + "  <td colspan=\"1\" class=\"c2 c3\">生产环境</td>\n"
				+ "</tr>\n" + "<tr>\n" + "  <td class=\"c2\"><img src='" + uatQrImg + "' width=\"220px;\" height=\"220px\"></td>\n" + "  <td class=\"c2\"><img src='" + productQrImg + "' width=\"220px;\" height=\"220px\"></td>\n" + "</tr>\n" + "<tr>\n" + "  <td class='c1'>下载地址</td>\n"
				+ "  <td class=\"c2\"><a color=\"#FFFFFF\" href=\"" + uatApk + "\">点击下载</a></td>\n" + "  <td class=\"c3\"><a href='" + productApk + "' color=\"#FFFFFF\">点击下载</a></td>\n" + "</tr>\n"
				+ "<!--<tr><td class='c1'>构建时间</td><td colspan=\"4\" style=\"text-align:center\">2016-09-26 19:07</td></tr>-->\n" + "<tr>\n" + "  <td class='c1'>构建日志</td>\n" + "  <td colspan=\"4\"><a color=\"#FFFFFF\" href=\"" + buildUrl + "\">点击查看</a></td>\n" + "</tr>\n" + "<table>\n" + "<br/>\n"
				+ "<p style='font-size:16px'>代码变更信息:</p>\n" + "<div> " + changesSinceLastSuccess + " </div>\n";
		return content;
	}

	private String getMtml(String title, String content) {
		return getMtml(getServerIP(), title, content);
	}

	private String getMtml(String serverIP, String title, String content) {
		String emailTemplate = "<!DOCTYPE html>\n" + "<html>\n" + "<head>\n" + "<meta charset=\" utf-8\">\n" + "<meta name=\"author\" content=\"http://" + serverIP + "/\" />\n" + "<title>EGMAS</title>\n" + "<style type=\"text/css\">\n" + "/**\n" + " * Frosted glass effect\n" + " */\n" + "\n" + "\n"
				+ "body {\n\tcolor:#FFFFFF;\n" + "\tmin-height: 100vh;\n" + "\tbox-sizing: border-box;\n" + "\tmargin: 0;\n" + "\tpadding-top: calc(50vh - 6em);\n" + "\tfont: 250%/1.6 Baskerville, Palatino, serif;\n" + "}\n" + "body, .main::before {\n" + "\tbackground: url(\"http://" + serverIP
				+ "/api/resources/upload/image/emailBj2.jpg\");  \n\tbackground-attachment: fixed;\n\tbackground-size: cover;\n" + "}\n" + ".main {\n" + "\tposition: relative;\n" + "\tmargin: 0 auto;\n" + "\tpadding: 1em;\n" + "\tmax-width: 23em;\n" + "\tbackground: hsla(0,0%,100%,.25) border-box;\n" + "\toverflow: hidden;\n"
				+ "\tborder-radius: .3em;\n" + "\tbox-shadow: 0 0 0 1px hsla(0,0%,100%,.3) inset,  0 .5em 1em rgba(0, 0, 0, 0.6);\n" + "\ttext-shadow: 0 1px 1px hsla(0,0%,100%,.3);\n" + "\twidth: 735px;\n" + "}\n" + ".main::before {\n" + "\tcontent: '';\n" + "\tposition: absolute;\n" + "\ttop: 0;\n"
				+ "\tright: 0;\n" + "\tbottom: 0;\n" + "\tleft: 0;\n" + "\tmargin: -30px;\n" + "\tz-index: -1;\n" + "\t-webkit-filter: blur(10px);\n" + "\tfilter: blur(10px);\n" + "}\n" + "blockquote {\n" + "\tfont-style: italic;\n" + "\tmargin: 0;\n" + "\tpadding: 0;\n" + "}\n"
				+ "blockquote cite {\n" + "\tfont-style: normal;\n" + "}\n" + ".container_title_bj {\n" + "\ttext-align: center;\n" + "\tfont-size: 25px;\n" + "}\n" + ".container_content_bj {\n" + "\twidth: auto;\n" + "\tmin-height: 260px;\n" + "\tfont-size: 20px;\n" + "}\n"
				+ ".container_footer_bj {\n" + "\twidth: auto;\n\ttext-align: right;\n\tfont-size: 16px;\n" + "}\n" + ".author_logo {\n" + "\twidth: 150px;\n" + "\theight: 150px;\n" + "\topacity: 0.8;\n" + "\tposition: fixed;\n" + "\tright: 0px;\n" + "\tbottom: 0px;\n" + "\tbackground: url(http://"
				+ serverIP + "/api/resources/upload/image/oneselfLogo_100x100.png) no-repeat;\n" + "}\n" + "</style>\n" + "</head>\n" + "<body>\n" + "\n" + "<div class=\"main\">\n" + "  <blockquote>\n" + "    <div class=\"container_title_bj\">\n" + "    \t" + title + "\n" + "    </div>\n"
				+ "    <hr>\n" + "    </em>\n" + "    <div class=\"container_content_bj\" > " + content + " </div>\n" + "    </em>\n" + "    <footer>\n" + "      <div class=\"container_footer_bj\"> <a href=\"http://" + serverIP + "\" color=\"#FFFFFF\">本网站由花心大萝卜提供技术与支持</a> </div>\n"
				+ "    </footer>\n" + "     <div class=\"author_logo\"> </div>\n" + "  </blockquote>\n" + " \n" + "</div>\n" + "</body>\n" + "</html>";
		logger.debug(emailTemplate);
		return emailTemplate;
	}
}
