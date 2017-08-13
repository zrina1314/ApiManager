package cn.crap.service.table;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.crap.dto.PickDto;
import cn.crap.framework.base.BaseService;
import cn.crap.framework.base.IBaseDao;
import cn.crap.inter.dao.ICxModuleDao;
import cn.crap.inter.service.table.ICxModuleService;
import cn.crap.model.CxModule;
import cn.crap.utils.Const;
import cn.crap.utils.MyString;
import cn.crap.utils.Page;
import cn.crap.utils.Tools;

@Service
public class CxModuleService extends BaseService<CxModule> implements ICxModuleService {

	@Autowired
	private ICxModuleDao dataCxCenterDao;

	@Resource(name = "dataCxCenterDao")
	public void setDao(IBaseDao<CxModule> dao) {
		super.setDao(dao);
	}

	@Override
	@Transactional
	public CxModule get(String id) {
		CxModule model = dataCxCenterDao.get(id);
		if (model == null)
			return new CxModule();
		return model;
	}

	@Override
	@Transactional
	public void getDataCenterPick(List<PickDto> picks, List<String> projectIds, String idPre, String value, String suffix) {
		if (MyString.isEmpty(suffix))
			suffix = "";
		PickDto pick = null;
		for (String projectId : projectIds) {
			List<CxModule> dcs = findByMap(null, null, null);
//			if (dcs.size() > 0) {
//				pick = new PickDto(Const.SEPARATOR, dcs.get(0).getProjectName());
//				picks.add(pick);
//			}

			for (CxModule dc : dcs) {
				if (MyString.isEmpty(value))
					pick = new PickDto(idPre + dc.getId(), Const.LEVEL_PRE + dc.getName() + suffix);
				else
					pick = new PickDto(idPre + dc.getId(), value.replace("moduleId", dc.getId()).replace("moduleName", dc.getName()).replace("projectId", projectId), Const.LEVEL_PRE + dc.getName() + suffix);
				picks.add(pick);
			}
		}
	}

	@Override
	@Transactional
	public List<String> getList(Byte status, String type, String userId) {
		List<Byte> statuss = null;
		if (status != null) {
			statuss = new ArrayList<Byte>();
			statuss.add(status);
		}
		return getListByStatuss(statuss, type, userId);
	}

	@Override
	@Transactional
	public List<String> getListByStatuss(List<Byte> statuss, String type, String userId) {
		Page page = new Page();
		page.setSize(2000);// 最多显示钱2000条
		List<String> ids = new ArrayList<String>();
		List<CxModule> dcs = findByMap(Tools.getMap("status|in", statuss, "type", type, "userId", userId), page, null);
		for (CxModule dc : dcs) {
			ids.add(dc.getId());
		}
		return ids;
	}

}
