package cn.crap.controller.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.crap.framework.JsonResult;
import cn.crap.framework.base.BaseController;
import cn.crap.inter.service.table.ISettingService;
import cn.crap.inter.service.tool.ICacheService;
import cn.crap.model.Setting;
import cn.crap.utils.Const;
import cn.crap.utils.StringUtils;
import cn.crap.utils.Tools;

@Controller("forntSettingController")
@RequestMapping("/front/setting")
public class SettingController extends BaseController<Setting> {

	@Autowired
	private ISettingService settingService;
	@Autowired
	private ICacheService cacheService;

	/**
	 * 获取当前服务器IP地址
	 * 
	 * @return
	 */
	@RequestMapping("/getServerIP.do")
	@ResponseBody
	public JsonResult getServerIP() {
		String serverIP = getServerIPStr();
		return new JsonResult(1, serverIP, null);
	}

	/**
	 * 获取当前服务器IP地址
	 * 
	 * @return
	 */
	@RequestMapping("/getServerIPStr.do")
	@ResponseBody
	public String getServerIPStr() {
		String serverIP = "";
		// 第一步读取缓存中的IP
		String cacheServerIP = cacheService.getStr(Const.CACHE_SERVER_IP);
		if (!StringUtils.isEmpty(cacheServerIP)) {
			serverIP = cacheServerIP;
		} else {
			// 第二步读取数据库中的IP
			List<Setting> settings = settingService.findByMap(Tools.getMap("key", Const.SETTING_SERVER_IP), null, null);
			if (settings.size() > 0) {
				Setting setting = settings.get(0);
				serverIP = setting.getValue();
			}
		}
		return serverIP;
	}
}
