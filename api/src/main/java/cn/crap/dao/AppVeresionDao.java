package cn.crap.dao;

import org.springframework.stereotype.Repository;

import cn.crap.framework.base.BaseDao;
import cn.crap.inter.dao.IAppDao;
import cn.crap.inter.dao.IAppVersionDao;
import cn.crap.model.App;
import cn.crap.model.AppVersion;

@Repository("appVersionDao")
public class AppVeresionDao extends BaseDao<AppVersion> implements IAppVersionDao {

}