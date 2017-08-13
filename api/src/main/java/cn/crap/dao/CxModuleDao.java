package cn.crap.dao;

import org.springframework.stereotype.Repository;

import cn.crap.framework.base.BaseDao;
import cn.crap.inter.dao.ICxModuleDao;
import cn.crap.model.CxModule;

@Repository("dataCxCenterDao")
public class CxModuleDao extends BaseDao<CxModule>
		implements ICxModuleDao {

}