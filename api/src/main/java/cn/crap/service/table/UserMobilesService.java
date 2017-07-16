package cn.crap.service.table;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.crap.framework.base.BaseService;
import cn.crap.framework.base.IBaseDao;
import cn.crap.inter.service.table.IUserMobilesService;
import cn.crap.model.UserMobiles;

@Service
public class UserMobilesService extends BaseService<UserMobiles> implements IUserMobilesService {

	@Resource(name = "userMobilesDao")
	public void setDao(IBaseDao<UserMobiles> dao) {
		super.setDao(dao);
	}
}
	