package anyframe.example.monitoring.domain;

import java.io.Serializable;

import javax.persistence.Id;

import anyframe.core.generic.model.BaseObject;

public class Users extends BaseObject implements Serializable {

	private static final long serialVersionUID = -2181114344703204906L;

	private String userId;
	private String userName;
	private String password;
	private Integer enabled;
	private Long age;
	private String cellPhone;
	private String addr;
	private String email;
	private String createDate;
	private String modifyDate;

	@Id
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public Long getAge() {
		return this.age;
	}

	public void setAge(Long age) {
		this.age = age;
	}

	public String getCellPhone() {
		return this.cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getAddr() {
		return this.addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if ((o == null) || (getClass() != o.getClass())) {
			return false;
		}

		Users pojo = (Users) o;

		if ((userName != null) ? (!userName.equals(pojo.userName))
				: (pojo.userName != null)) {
			return false;
		}

		if ((password != null) ? (!password.equals(pojo.password))
				: (pojo.password != null)) {
			return false;
		}

		if ((enabled != null) ? (!enabled.equals(pojo.enabled))
				: (pojo.enabled != null)) {
			return false;
		}

		if ((age != null) ? (!age.equals(pojo.age)) : (pojo.age != null)) {
			return false;
		}

		if ((cellPhone != null) ? (!cellPhone.equals(pojo.cellPhone))
				: (pojo.cellPhone != null)) {
			return false;
		}

		if ((addr != null) ? (!addr.equals(pojo.addr)) : (pojo.addr != null)) {
			return false;
		}

		if ((email != null) ? (!email.equals(pojo.email))
				: (pojo.email != null)) {
			return false;
		}

		if ((createDate != null) ? (!createDate.equals(pojo.createDate))
				: (pojo.createDate != null)) {
			return false;
		}

		if ((modifyDate != null) ? (!modifyDate.equals(pojo.modifyDate))
				: (pojo.modifyDate != null)) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		int result = 0;
		result = ((userName != null) ? userName.hashCode() : 0);
		result = (31 * result) + ((password != null) ? password.hashCode() : 0);
		result = (31 * result) + ((enabled != null) ? enabled.hashCode() : 0);
		result = (31 * result) + ((age != null) ? age.hashCode() : 0);
		result = (31 * result)
				+ ((cellPhone != null) ? cellPhone.hashCode() : 0);
		result = (31 * result) + ((addr != null) ? addr.hashCode() : 0);
		result = (31 * result) + ((email != null) ? email.hashCode() : 0);
		result = (31 * result)
				+ ((createDate != null) ? createDate.hashCode() : 0);
		result = (31 * result)
				+ ((modifyDate != null) ? modifyDate.hashCode() : 0);

		return result;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer(getClass().getSimpleName());

		sb.append(" [");
		sb.append("userId").append("='").append(getUserId()).append("', ");
		sb.append("userName").append("='").append(getUserName()).append("', ");
		sb.append("password").append("='").append(getPassword()).append("', ");
		sb.append("enabled").append("='").append(getEnabled()).append("', ");
		sb.append("age").append("='").append(getAge()).append("', ");
		sb.append("cellPhone").append("='").append(getCellPhone())
				.append("', ");
		sb.append("addr").append("='").append(getAddr()).append("', ");
		sb.append("email").append("='").append(getEmail()).append("', ");
		sb.append("createDate").append("='").append(getCreateDate()).append(
				"', ");
		sb.append("modifyDate").append("='").append(getModifyDate()).append(
				"', ");
		sb.append("]");

		return sb.toString();
	}

}
