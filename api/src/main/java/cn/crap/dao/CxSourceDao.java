package cn.crap.dao;

import org.springframework.stereotype.Repository;

import cn.crap.framework.base.BaseDao;
import cn.crap.inter.dao.ICxSourceDao;
import cn.crap.model.CxSource;

@Repository("cxSourceDao")
public class CxSourceDao extends BaseDao<CxSource>
		implements ICxSourceDao {

}