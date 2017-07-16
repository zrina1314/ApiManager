package cn.crap.dao;

import org.springframework.stereotype.Repository;

import cn.crap.framework.base.BaseDao;
import cn.crap.inter.dao.IInterfaceEgmasDao;
import cn.crap.model.InterfaceEgmas;

@Repository("interfaceEgmasDao")
public class InterfaceEgmasDao extends BaseDao<InterfaceEgmas>
		implements IInterfaceEgmasDao {

}