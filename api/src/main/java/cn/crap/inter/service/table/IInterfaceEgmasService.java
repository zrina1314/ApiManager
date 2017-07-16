package cn.crap.inter.service.table;

import java.util.List;

import cn.crap.dto.InterfaceEgmasPDFDto;
import cn.crap.framework.JsonResult;
import cn.crap.framework.base.IBaseService;
import cn.crap.model.Interface;
import cn.crap.model.InterfaceEgmas;
import cn.crap.springbeans.Config;
import cn.crap.utils.Page;

public interface IInterfaceEgmasService extends IBaseService<InterfaceEgmas>{
	/**
	 * 根据模块id，url，接口名等分页查询接口列表
	 * 管理员能查看所有接口、普通用户只能查看自己创建的模块下的接口
	 * @param page 分页信息
	 * @param moduleIds 
	 * @param interFace 接口类，自动封装的接口查询信息
	 * @param currentPage 当前页码
	 * @return
	 */
	public List<InterfaceEgmas> getInterfaceList(Page page, List<String> moduleIds, InterfaceEgmas interFace, Integer currentPage);
	/**
	 * 根据模块id，url，接口名等分页查询接口列表
	 * 管理员能查看所有接口、普通用户只能查看自己创建的模块下的接口
	 * @param page 分页信息
	 * @param moduleIds 
	 * @param interFace 接口类，自动封装的接口查询信息
	 * @param currentPage 当前页码
	 * @return
	 */
	public JsonResult getInterfaceListJson(Page page, List<String> moduleIds, InterfaceEgmas interFace, Integer currentPage);
	
	/**
	 * 查询接口责任人
	 * @return
	 */
	public JsonResult getInterfaceResponsibilityListToJsonResult(InterfaceEgmas interFace);
	
	/**
	 * 查询接口责任人
	 * @return
	 */
	public List<InterfaceEgmas> getInterfaceResponsibilityList(InterfaceEgmas interFace);
	
	/**
	 * 根据接口名，参数，请求头组装请求示例
	 * @param interFaceInfo
	 * @return
	 */
	public void getInterFaceRequestExam(InterfaceEgmas interFaceInfo);
}