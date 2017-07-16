package cn.crap.service.table;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.crap.framework.base.BaseService;
import cn.crap.framework.base.IBaseDao;
import cn.crap.inter.service.table.IAppPageService;
import cn.crap.model.AppPage;
import cn.crap.model.AppVersion;

@Service
public class AppVersionService extends BaseService<AppVersion>		implements IAppVersionService {

	@Resource(name="appVersionDao")
	public void setDao(IBaseDao<AppVersion> dao ) {
		super.setDao(dao);
	}
}
