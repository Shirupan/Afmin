/**   
 * 文件名：FacebookEntity.java</br>
 * 描述： </br>
 * 开发人员：杜逸平 </br>
 * 创建时间： 2014-9-23
 */

package com.gigifun.gp.model;

/**
 *  开发人员： 谁抢了我的飞宇 </br>
 * QQ：460543600</br>
 *  创建时间： 2014-9-23
 */

public class FacebookEntity {
	private String clientId;
	private String clientSecret;
	private String id;
	private String email;
	private String firstName;
	private String gender;
	private String lastName;
	private String link;
	private String locale;
	private String name;
	private String timezone;
	private String verified;
	private String updatedTime;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getVerified() {
		return verified;
	}

	public void setVerified(String verified) {
		this.verified = verified;
	}

	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * 开发人员：杜逸平
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer buffer=new StringBuffer();
		buffer.append("clientId").append("----").append(clientId).append("\n")
		.append("clientSecret").append("----").append(clientSecret).append("\n")
		.append("id").append("----").append(id).append("\n")
		.append("email").append("----").append(email).append("\n")
		.append("firstName").append("----").append(firstName).append("\n")
		.append("gender").append("----").append(gender).append("\n")
		.append("lastName").append("----").append(lastName).append("\n")
		.append("link").append("----").append(link).append("\n")
		.append("locale").append("----").append(locale).append("\n")
		.append("name").append("----").append(name).append("\n")
		.append("timezone").append("----").append(timezone).append("\n")
		.append("verified").append("----").append(verified).append("\n")
		.append("updatedTime").append("----").append(updatedTime);
		return buffer.toString();
	}
}
