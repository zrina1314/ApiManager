package cn.crap.dao;

import org.springframework.stereotype.Repository;

import cn.crap.framework.base.BaseDao;
import cn.crap.inter.dao.IAppPageInterfaceDao;
import cn.crap.model.AppPage_Interface;

@Repository("appPageInterfaceDao")
public class AppPageInterfaceDao extends BaseDao<AppPage_Interface> implements IAppPageInterfaceDao {

}