package cn.crap.service.table;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.crap.framework.base.BaseService;
import cn.crap.framework.base.IBaseDao;
import cn.crap.inter.service.table.IEgmasSourceService;
import cn.crap.model.EgmasSource;

@Service
public class EgmasSourceService extends BaseService<EgmasSource> implements IEgmasSourceService {

	@Resource(name = "egmasSourceDao")
	public void setDao(IBaseDao<EgmasSource> dao) {
		super.setDao(dao);
	}
}
