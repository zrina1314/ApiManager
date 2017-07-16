package cn.crap.dao;

import org.springframework.stereotype.Repository;

import cn.crap.framework.base.BaseDao;
import cn.crap.inter.dao.IEgmasSourceDao;
import cn.crap.model.EgmasSource;

@Repository("egmasSourceDao")
public class EgmasSourceDao extends BaseDao<EgmasSource>
		implements IEgmasSourceDao {

}