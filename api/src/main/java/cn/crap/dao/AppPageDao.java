package cn.crap.dao;

import org.springframework.stereotype.Repository;

import cn.crap.framework.base.BaseDao;
import cn.crap.inter.dao.IAppPageDao;
import cn.crap.model.AppPage;

@Repository("appPageDao")
public class AppPageDao extends BaseDao<AppPage> implements IAppPageDao {

}