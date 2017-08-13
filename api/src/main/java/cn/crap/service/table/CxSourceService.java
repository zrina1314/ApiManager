package cn.crap.service.table;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.crap.framework.base.BaseService;
import cn.crap.framework.base.IBaseDao;
import cn.crap.inter.service.table.ICxSourceService;
import cn.crap.model.CxSource;

@Service
public class CxSourceService extends BaseService<CxSource> implements ICxSourceService {

	@Resource(name = "cxSourceDao")
	public void setDao(IBaseDao<CxSource> dao) {
		super.setDao(dao);
	}
}
