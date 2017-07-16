package cn.crap.dao;

import org.springframework.stereotype.Repository;

import cn.crap.framework.base.BaseDao;
import cn.crap.inter.dao.IAppPageInterfaceDao;
import cn.crap.model.AppPageInterface;

@Repository("appPageInterfaceDao")
public class AppPageInterfaceDao extends BaseDao<AppPageInterface> implements IAppPageInterfaceDao {

}