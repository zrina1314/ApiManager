package cn.crap.framework.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class BaseModelBase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String id;

	@Id
	@GeneratedValue(generator = "Generator")
	@Column(name = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Transient
	public String getLogRemark() {
		return "";
	}
}
