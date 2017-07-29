package cn.crap.framework.auth;

import java.net.InetAddress;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.crap.framework.MyException;
import cn.crap.inter.dao.ICacheDao;
import cn.crap.inter.service.table.ISettingService;
import cn.crap.inter.service.tool.ICacheService;
import cn.crap.model.Setting;
import cn.crap.springbeans.Config;
import cn.crap.springbeans.GetBeanByConfig;
import cn.crap.utils.Aes;
import cn.crap.utils.Const;
import cn.crap.utils.MyCookie;
import cn.crap.utils.MyString;
import cn.crap.utils.StringUtils;
import cn.crap.utils.Tools;

/**
 * 对登录状态进行拦截
 * 
 * @author
 *
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	private GetBeanByConfig getBEanByConfig;
	@Autowired
	private Config config;

	@Autowired
	private ICacheService cacheService;

	@Autowired
	private ISettingService settingService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
			AuthPassport authPassport = ((HandlerMethod) handler).getMethodAnnotation(AuthPassport.class);

			String serverName = request.getServerName();
			if (!StringUtils.isEmpty(serverName) && serverName.startsWith("10.118.")) {
				// 记录下当前内网IP（10.118开头的）
				String cacheServerIP = cacheService.getStr(Const.CACHE_SERVER_IP);
				if (!serverName.equals(cacheServerIP)) {
					//更新缓存
					cacheService.setStr(Const.CACHE_SERVER_IP, serverName, 60 * 60 * 24 * 100);
					
					//保存到数据库
					List<Setting> settings= settingService.findByMap(Tools.getMap("key",Const.SETTING_SERVER_IP),null,null);
					if(settings.size()>0){
						Setting setting = settings.get(0);
						setting.setValue(serverName);
						settingService.update(setting);
					}
				}
			}

			// 未登陆用户唯一识别
			String uuid = MyCookie.getCookie(Const.COOKIE_UUID, false, request);
			if (MyString.isEmpty(uuid)) {
				MyCookie.addCookie(Const.COOKIE_UUID, System.currentTimeMillis() + Tools.getChar(10), response);
			}

			try {
				String serviceIp = InetAddress.getLocalHost().getHostAddress();
				// 返回服务器ip
				response.setHeader("serviceIp", serviceIp);
			} catch (Exception e) {
				e.printStackTrace();
				response.setHeader("serviceIp", "服务器配置异常，无法获取服务器IP");
			}

			if (authPassport == null || authPassport.validate() == false)
				return true;

			String token = MyCookie.getCookie(Const.COOKIE_TOKEN, false, request);
			String uid = MyCookie.getCookie(Const.COOKIE_USERID, false, request);
			// 前端没有传递token，未登录
			if (MyString.isEmpty(token) || MyString.isEmpty(uid) || !Aes.desEncrypt(token).equals(uid)) {
				if (request.getRequestURI().endsWith("admin.do"))
					response.sendRedirect("loginOrRegister.do#/login");
				else
					throw new MyException("000021");
			}

			// 后端没登录信息：登录超时
			ICacheDao cacheDao = getBEanByConfig.getCacheDao();
			Object obj = cacheDao.getObj(Const.CACHE_USER + uid);
			if (obj == null) {
				// 删除cookie
				MyCookie.deleteCookie(Const.COOKIE_TOKEN, request, response);
				if (request.getRequestURI().endsWith("admin.do")) {
					response.sendRedirect("loginOrRegister.do#/login");
					return false;
				} else
					throw new MyException("000021");
			}

			// 每次访问，将用户登录有效信息延长
			cacheDao.setObj(Const.CACHE_USER + uid, obj, config.getLoginInforTime());

			if (!authPassport.authority().equals("")) {
				return Tools.hasAuth(authPassport.authority());
			} else {
				return true;
			}

		} else
			return true;
	}

}