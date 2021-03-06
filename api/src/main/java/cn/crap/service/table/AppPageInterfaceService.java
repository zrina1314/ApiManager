package cn.crap.service.table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import cn.crap.framework.base.BaseService;
import cn.crap.framework.base.IBaseDao;
import cn.crap.inter.service.table.IAppPageInterfaceService;
import cn.crap.model.AppPageInterface;
import cn.crap.utils.StringUtils;


@Service
public class AppPageInterfaceService extends BaseService<AppPageInterface>
		implements IAppPageInterfaceService {

	@Resource(name="appPageInterfaceDao")
	public void setDao(IBaseDao<AppPageInterface> dao ) {
		super.setDao(dao);
	}

	@Override
	public List<AppPageInterface> getListByAppPageID(String appPageId) {
		return null;
	}

	@Override
	public List<AppPageInterface> getListByInterfaceID(String interfaceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AppPageInterface> getList(String appPageId, String interfaceId) {
		Map<String, Object> params = new HashMap<>();
		
		if (StringUtils.isNotEmpty(appPageId)) {
			params.put("appPageId", appPageId);
		}
		if (StringUtils.isNotEmpty(interfaceId)) {
			params.put("interfaceId", interfaceId);
		}
		return findByMap(params," new AppPage_Interface(appPageId,interfaceId)",null," appPageId desc,interfaceId desc");
	}
}
