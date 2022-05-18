package com.bhs.gtk.mockfeed.persistence;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class UserEntity {

	@Id
	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String name;
	
	@Column(length = PersistenceConstants.LARGE_TEXT_LIMIT)
	private String password;
	
	@Column(length = PersistenceConstants.LARGE_TEXT_LIMIT)
	private String token;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date tokenGenerationTime;
	
	@Column
	private boolean isSubscriptionActive;
	
	protected UserEntity() {		
	}
	
	public UserEntity(String name, String password) {
		this.name = name;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getTokenGenerationTime() {
		return tokenGenerationTime;
	}

	public void setTokenGenerationTime(Date tokenGenerationTime) {
		this.tokenGenerationTime = tokenGenerationTime;
	}

	public boolean isSubscriptionActive() {
		return isSubscriptionActive;
	}

	public void setSubscriptionActive(boolean isSubscriptionActive) {
		this.isSubscriptionActive = isSubscriptionActive;
	}
}
