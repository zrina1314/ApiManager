package cn.crap.framework.base;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.springframework.orm.hibernate4.HibernateTemplate;

import cn.crap.utils.DateFormartUtil;
import cn.crap.utils.MyString;
import cn.crap.utils.Page;
import cn.crap.utils.Tools;

@SuppressWarnings("unchecked")
public class BaseDao<T extends BaseModelBase> implements IBaseDao<T> {
	@Resource
	protected HibernateTemplate hibernateTemplate;

	public IBaseDao<T> genericDao;
	
	Class<T> entity;

	String entityName;	
	
	/** API SQL 数据源 */
	public static final String DB_API = "DB_API";

	/** EGMAS SQL 数据源 */
	public static final String DB_EGMAS = "DB_EGMAS";

	/**
	 * 返回数据库数据源
	 * 
	 * @return
	 */
	protected String getDbSource() {
		return DB_API;
	}

	@Resource
	protected HibernateTemplate egmasHibernateTemplate;

	public BaseDao() {
		this.entity = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		this.entityName = entity.getName();
	}

	@Override
	public T save(T t) {
		
		if (t instanceof BaseModel) {
			if(MyString.isEmpty(((BaseModel)t).getCreateTime()))
				((BaseModel)t).setCreateTime(DateFormartUtil.getDateByFormat(DateFormartUtil.YYYY_MM_DD_HH_mm_ss));
			// 如果没有设置排序，则自动取数据库中最大排序+1； 由于多个用户排序互相影响，故存在bug，但是要解决该问题，需要去掉用户输入排序字段的功能
			if(((BaseModel)t).getSequence() == 0){
				// 查询当前最大排序号
				String hql = "select max(sequence) from " + entity.getSimpleName();
				Query query = gethibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
				Object sequence = query.uniqueResult();
				if( sequence != null ){
					// 允许的最大排序不能超过10000
					int maxSequence = Integer.parseInt(sequence.toString());
					((BaseModel)t).setSequence( maxSequence >= 10000 ? 10000 : maxSequence + 1);
				}
			}
		}
		gethibernateTemplate().save(entityName, t);
		return t;
	}


	@Override
	public void delete(T t) {
		gethibernateTemplate().delete(entityName, t);
	}

	@Override
	public T get(String m) {
		return (T) gethibernateTemplate().get(entity, m);
	}

	@Override
	public List<T> findByExample(T t) {
		return gethibernateTemplate().findByExample(entityName, t);
	}

	@Override
	public List<T> loadAll(T t) {
		return gethibernateTemplate().loadAll(entity);
	}

	@Override
	public void update(T t) {
		gethibernateTemplate().merge(t);
	}
	
	@Override
	public int update(String hql, Map<String, Object> map) {
		Query query = gethibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);  
		Tools.setQuery(map, query);
		return query.executeUpdate();  
	}
	
	@Override
	public List<T> queryByHql(String hql, Map<String, Object> map) {
		return queryByHql(hql, map, null);
	}
	
	@Override
	public List<T> queryByHql(String hql, Map<String, Object> map, Page pageBean) {
		Query query = gethibernateTemplate().getSessionFactory().getCurrentSession()
				.createQuery(hql);
		if(pageBean!=null){
			pageBean.setAllRow(  getCount(map, hql.toUpperCase().indexOf(" WHERE ")>0? hql.substring(  hql.toUpperCase().indexOf(" WHERE")  ) : "")  );
			if(pageBean.getCurrentPage()>pageBean.getTotalPage())
				pageBean.setCurrentPage(pageBean.getTotalPage());
		}
		Tools.setPage(query, pageBean);
		Tools.setQuery(map, query);
		return query.list();
	}
	
	@Override
	public int getCount(Map<String, Object> map, String conditions) {
		String hql = "select count(*) from " + entity.getSimpleName() + conditions;
		
		Query query = gethibernateTemplate().getSessionFactory().getCurrentSession()
				.createQuery(hql);
		Tools.setQuery(map, query);
		return Integer.parseInt(query.uniqueResult().toString());
	}

	/**
	 * @param constructed: 构造函数 如 new A(aa,bb)
	 */
	@Override
	public List<T> findByMap(String construct,Map<String, Object> map,
			Page pageBean, String order) {
		String conditions = Tools.getHql(map);
		String hql = "select " + construct +" from "+entity.getSimpleName() + conditions + (MyString.isEmpty(order) ? " order by sequence desc, createTime desc" : " order by " + order);
		if (DB_EGMAS.equals(getDbSource())) {
			hql = hql.replace("status>-1", "1=1");
		}

		System.out.println("hql = " + hql);
		Query query = gethibernateTemplate().getSessionFactory().getCurrentSession()
				.createQuery(hql);
		if(pageBean!=null){
			pageBean.setAllRow(getCount(map, conditions));
			if(pageBean.getCurrentPage()>pageBean.getTotalPage())
				pageBean.setCurrentPage(pageBean.getTotalPage());
		}
		Tools.setPage(query, pageBean);
		Tools.setQuery(map, query);
		return query.list();
	}
	
	@Override
	public List<T> findByMap(Map<String, Object> map,
			Page pageBean, String order) {
		String conditions = Tools.getHql(map);
		String hql = "from "+entity.getSimpleName() + conditions + (MyString.isEmpty(order) ? " order by sequence desc, createTime desc" : " order by " + order);
		if (DB_EGMAS.equals(getDbSource())) {
			hql = hql.replace("status>-1", "1=1");
		}

		System.out.println("hql = " + hql);
		Query query = gethibernateTemplate().getSessionFactory().getCurrentSession()
				.createQuery(hql);
		if(pageBean!=null){
			pageBean.setAllRow(getCount(map, conditions));
			if(pageBean.getCurrentPage()>pageBean.getTotalPage())
				pageBean.setCurrentPage(pageBean.getTotalPage());
		}
		Tools.setPage(query, pageBean);
		Tools.setQuery(map, query);
		return query.list();
	}
	
	@Override
	public HibernateTemplate gethibernateTemplate() {
		return DB_API.equals(getDbSource()) ? hibernateTemplate : egmasHibernateTemplate;
	}
}
