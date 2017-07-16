package cn.crap.dao;

import org.springframework.stereotype.Repository;

import cn.crap.framework.base.BaseDao;
import cn.crap.inter.dao.IUserMobilesDao;
import cn.crap.model.UserMobiles;

@Repository("userMobilesDao")
public class UserMobilesDao extends BaseDao<UserMobiles> implements IUserMobilesDao {
		@Override
		protected String getDbSource() {
			return DB_EGMAS;
		}
}