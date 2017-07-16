package cn.crap.service.table;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.crap.framework.base.BaseService;
import cn.crap.framework.base.IBaseDao;
import cn.crap.inter.service.table.IAppPageService;
import cn.crap.model.AppPage;

@Service
public class AppService extends BaseService<App>
		implements IAppService {

	@Resource(name="appDao")
	public void setDao(IBaseDao<App> dao ) {
		super.setDao(dao);
	}
}
