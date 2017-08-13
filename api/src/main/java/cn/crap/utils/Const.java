package cn.crap.utils;

public class Const {
	// public final static String SESSION_ADMIN = "sessionAdminName";
	// public final static String SESSION_IMGCODE_TIMES =
	// "sessionImgCodeTryTimes";
	// public final static String SESSION_ADMIN_AUTH = "sessionAdminAuthor";
	// public final static String SESSION_ADMIN_ID = "sessionAdminId";
	// public final static String SESSION_ADMIN_TRUENAME =
	// "sessionAdminTrueName";
	// public final static String SESSION_ADMIN_ROLEIDS = "sessionAdminRoleIds";
	// public final static String SESSION_IMG_CODE = "sessionImgCode";
	// public final static String SESSION_TEMP_PASSWORD = "sessionTempPassword";

	public static final String NULL = "_NULL";
	public final static String NULL_ID = "NULL"; // 当新增数据时，前段传递的id=NULL
	public static final String NOT_NULL = "NOT_NULL";
	public static final String BLANK = "_BLANK";
	public static final String ALL = "_ALL";
	public static final String DEF_MODULEID = "defaultModuleId";
	public static final String SUPER = "super";
	public static final String MODULEID = "MODULEID";
	public static final String PROJECTID = "PROJECTID";
	public static final String AUTH_USER = "USER";
	public static final String AUTH_MENU = "MENU";
	public static final String AUTH_SETTING = "SETTING";
	public static final String AUTH_COMMENT = "COMMENT";
	public static final String AUTH_ADMIN = "ADMIN";// 管理员
	public static final String AUTH_ROLE = "ROLE";
	public static final String AUTH_LOG = "LOG";
	public static final String AUTH_PROJECT = "PROJECT_";
	// 缓存
	public static final String CACHE_USER = ":user";// 用户登录信息
	public static final String CACHE_IMGCODE = ":imgCode";
	public static final String CACHE_IMGCODE_TIMES = ":imgCodeTimes";
	public static final String CACHE_TEMP_PWD = ":tempPwd";
	public static final String CACHE_SEARCH_WORDS = ":searchWords";
	public static final String CACHE_MODULEIDS = ":moduleIds:";
	public static final String CACHE_ALL_DATACENTER = ":all:datacenter:";
	public static final String CACHE_ERROR_TIP = ":errorTip:";
	public static final String CACHE_TUIJIAN_OPEN_MODULEIDS = ":openTuijianModuleIds:";
	public static final String CACHE_ARTICLE_CATEGORY = ":article:category:";
	public static final String CACHE_WEBPAGE = ":webPageDetail:";
	public static final String CACHE_COMMENTLIST = ":commentList:";
	public static final String CACHE_COMMENT_PAGE = ":commentList:page:";
	public static final String CACHE_AUTHORIZE = ":authorize:";
	public static final String CACHE_FINDPWD = ":findPwd:";
	public static final String CACHE_MONITOR_INTERFACES = ":monitor:interfaces";
	public static final String CACHE_MONITOR_INTERFACES_HAS_SEND_EMAIL = ":monitor:interfaces:hasSendEmail";// 是否已经发送了告警邮件
	public static final String CACHE_MONITOR_INTERFACES_EMAIL_TIMES = ":monitor:interfaces:emailTimes";// 发送邮件次数
	public static final String CACHE_PROJECT = ":project:";
	public static final String CACHE_MODULE = "cache:model:";
	public static final String CACHE_CX_MODULE = "cache:cx:model:";
	public static final String CACHE_SETTING = "cache:setting";
	public static final String CACHE_USER_MODEL = "cache:user:model";// 用户登录信息
	public static final String CACHE_SETTINGLIST = "cache:settingList";
	public static final String CACHE_SERVER_IP = "server_ip";

	// 常量
	public static final String COOKIE_USERID = "cookieUserId";
	public static final String COOKIE_USERNAME = "cookieUserName";
	public static final String COOKIE_PASSWORD = "cookiePassword";
	public static final String COOKIE_TOKEN = "token";
	public static final String COOKIE_UUID = "uuid";
	public static final String COOKIE_REMBER_PWD = "cookieRemberPwd";
	public static final String COOKIE_PROJECTID = "cookieProjectId"; // 访问的项目ID
	public static final String MODULE = "MODULE";
	public static final String DIRECTORY = "DIRECTORY";
	public static final String SOURCE = "SOURCE";
	public static final String SEPARATOR = "SEPARATOR";
	public static final String ADMIN_MODULE = "0";
	public static final String WEB_MODULE = "web";
	public static final String LEVEL_PRE = "- - ";
	public static final String REGISTER = "register";
	public static final String GITHUB = "gitHub:";
	public static final String DOMAIN = "DOMAIN";
	// url
	public static final String FRONT_ARTICLE_URL = "#%s/article/list/%s/ARTICLE/%s/NULL";
	public static final String FRONT_PROJECT_URL = "#/%s/module/list";

	// 系统设置
	public static final String SETTING_SECRETKEY = "SECRETKEY";
	public final static String SETTING_VERIFICATIONCODE = "VERIFICATIONCODE";
	public final static String SETTING_VISITCODE = "VISITCODE";
	public static final String SETTING_COMMENTCODE = "COMMENTCODE";
	public static final String SETTING_LUCENE_DIR = "LUCENE_DIR";
	public static final String SETTING_TITLE = "TITLE";
	public static final String SETTING_OPEN_REGISTER = "openRegister";
	public static final String SETTING_GITHUB_ID = "githubClientID";
	public static final String SETTING_BUDDY_PATH = "BuddyPath"; // 系统配置中的KEY
	public static final String SETTING_SERVER_IP = "SERVER_IP"; // 服务器IP

	// SOLR
	public static final String SOLR_URL = "SOLR_URL";
	public static final String SOLR_QUEUESIZE = "SOLR_QUEUESIZE";
	public static final String SOLR_THREADCOUNT = "SOLR_THREADCOUNT";
	public static final String SEARCH_TYPE = "SEARCH_TYPE";

	// EGMAS常量
	public static final String METHOD = "method";
	public static final String REQUESTPARAMS = "requstParams";

	// 邮件模块
	public static final String EMAIL_SERVER_HOST = "hqmail.sf.com";
	public static final String EMAIL_SERVER_PORT = "25";
	public static final String EMAIL_SEND_ADDRESS = "80000807@sf-express.com";
	public static final String EMAIL_SEND_USER_NAME = "80000807";
	public static final String EMAIL_SEND_USER_PWD = "nimeide_777";
	public static final String EMAIL_SEND_USER_ADDRESS = "EMAIL_SEND_USER_NAME"; // 系统配置中的KEY

	public static final String EMAIL_SEND_PERSONAL_NAME = "花心大萝卜";
	public static final String EMAIL_RECEIVE_ALL = ""; // 所有的接收者

	public static final String EMAIL_TEMPLATE = "<!DOCTYPE html>\n" + "<html>\n" + "<head>\n" + "<meta charset=\" utf-8\">\n" + "<meta name=\"author\" content=\"http://{0}/\" />\n" + "<title>EGMAS</title>\n" + "<style type=\"text/css\">\n" + "/**\n" + " * Frosted glass effect\n" + " */\n"
			+ "\n" + "\n" + "body {\n" + "\tmin-height: 100vh;\n" + "\tbox-sizing: border-box;\n" + "\tmargin: 0;\n" + "\tpadding-top: calc(50vh - 6em);\n" + "\tfont: 250%/1.6 Baskerville, Palatino, serif;\n" + "}\n" + "body, .main::before {\n"
			+ "\tbackground: url(\"http://{0}/api/resources/upload/image/emailBj2.jpg\") 0 / cover fixed;\n" + "}\n" + ".main {\n" + "\tposition: relative;\n" + "\tmargin: 0 auto;\n" + "\tpadding: 1em;\n" + "\tmax-width: 23em;\n" + "\tbackground: hsla(0,0%,100%,.25) border-box;\n"
			+ "\toverflow: hidden;\n" + "\tborder-radius: .3em;\n" + "\tbox-shadow: 0 0 0 1px hsla(0,0%,100%,.3) inset,  0 .5em 1em rgba(0, 0, 0, 0.6);\n" + "\ttext-shadow: 0 1px 1px hsla(0,0%,100%,.3);\n" + "\twidth: 735px;\n" + "}\n" + ".main::before {\n" + "\tcontent: '';\n"
			+ "\tposition: absolute;\n" + "\ttop: 0;\n" + "\tright: 0;\n" + "\tbottom: 0;\n" + "\tleft: 0;\n" + "\tmargin: -30px;\n" + "\tz-index: -1;\n" + "\t-webkit-filter: blur(10px);\n" + "\tfilter: blur(10px);\n" + "}\n" + "blockquote {\n" + "\tfont-style: italic;\n" + "\tmargin: 0;\n"
			+ "\tpadding: 0;\n" + "}\n" + "blockquote cite {\n" + "\tfont-style: normal;\n" + "}\n" + ".container_title_bj {\n" + "\ttext-align: center;\n" + "\tfont-size: 25px;\n" + "}\n" + ".container_content_bj {\n" + "\twidth: auto;\n" + "\tmin-height: 260px;\n" + "\tfont-size: 20px;\n" + "}\n"
			+ ".container_footer_bj {\n" + "\twidth: auto;\n\ttext-align: right;\n\tfont-size: 16px;\n" + "}\n" + ".author_logo {\n" + "\twidth: 110px;\n" + "\theight: 110px;\n" + "\topacity: 0.8;\n" + "\tposition: fixed;\n" + "\tright: 0px;\n" + "\tbottom: 0px;\n"
			+ "\tbackground: url(http://{0}/api/resources/upload/image/oneselfLogo_100x100.png) no-repeat;\n" + "}\n" + "</style>\n" + "</head>\n" + "<body>\n" + "\n" + "<div class=\"main\">\n" + "  <blockquote>\n" + "    <div class=\"container_title_bj\">\n" + "    \t{1}\n"
			+ "    </div>\n" + "    <hr>\n" + "    </em>\n" + "    <div class=\"container_content_bj\" > {2} </div>\n" + "    </em>\n" + "    <footer>\n"
			+ "      <div class=\"container_footer_bj\"> <a style=\"color:#6f5499;\" href=\"http://api.crap.cn?sj=1501235277939\">本网站由花心大萝卜提供技术与支持</a> </div>\n" + "    </footer>\n" + "     <div class=\"author_logo\"> </div>\n" + "  </blockquote>\n" + " \n" + "</div>\n" + "</body>\n" + "</html>";

}
