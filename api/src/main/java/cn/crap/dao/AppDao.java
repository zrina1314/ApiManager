package cn.crap.dao;

import org.springframework.stereotype.Repository;

import cn.crap.framework.base.BaseDao;
import cn.crap.inter.dao.IAppDao;
import cn.crap.model.App;

@Repository("appDao")
public class AppDao extends BaseDao<App> implements IAppDao {

}