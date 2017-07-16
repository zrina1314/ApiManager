package cn.crap.inter.service.table;

import java.util.List;

import cn.crap.framework.base.IBaseService;
import cn.crap.model.AppPage;
import cn.crap.model.AppPage_Interface;
import cn.crap.model.Interface;
import cn.crap.utils.Page;

public interface IAppPageInterfaceService extends IBaseService<AppPage_Interface>{
	/**
	 * 根据模块id，url，接口名等分页查询接口列表
	 * 管理员能查看所有接口、普通用户只能查看自己创建的模块下的接口
	 * @param page 分页信息
	 * @param moduleIds 
	 * @param interFace 接口类，自动封装的接口查询信息
	 * @param currentPage 当前页码
	 * @return
	 */
	public List<AppPage_Interface> getListByAppPageID(String appPageId);
	
	public List<AppPage_Interface> getListByInterfaceID(String interfaceId);
	
	public List<AppPage_Interface> getList(String appPageId,String interfaceId);
}
