package cn.crap.dao;

import org.springframework.stereotype.Repository;

import cn.crap.framework.base.BaseDao;
import cn.crap.inter.dao.IInterfaceCxDao;
import cn.crap.model.InterfaceCx;

@Repository("interfaceCxDao")
public class InterfaceCxDao extends BaseDao<InterfaceCx>
		implements IInterfaceCxDao {

}