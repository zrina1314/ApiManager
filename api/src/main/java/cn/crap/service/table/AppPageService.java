package cn.crap.service.table;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.crap.framework.base.BaseService;
import cn.crap.framework.base.IBaseDao;
import cn.crap.inter.service.table.IAppPageService;
import cn.crap.model.AppPage;

@Service
public class AppPageService extends BaseService<AppPage>
		implements IAppPageService {

	@Resource(name="appPageDao")
	public void setDao(IBaseDao<AppPage> dao ) {
		super.setDao(dao);
	}
}
