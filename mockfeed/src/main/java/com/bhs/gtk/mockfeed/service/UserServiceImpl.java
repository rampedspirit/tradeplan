package com.bhs.gtk.mockfeed.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhs.gtk.mockfeed.exceptions.TokenException;
import com.bhs.gtk.mockfeed.persistence.EntityReader;
import com.bhs.gtk.mockfeed.persistence.EntityWriter;
import com.bhs.gtk.mockfeed.persistence.UserEntity;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private EntityReader entityReader;

	@Autowired
	private EntityWriter entityWriter;

	// 3Hours
	private long MAX_TOKEN_LIFE_IN_SECS = 10800;

	@Override
	public void createUser(String name, String password) {
		entityWriter.createUser(name, password);
	}

	@Override
	public UserEntity getUserByNameAndPassword(String name, String password) {
		return entityReader.getUserByNameAndPassword(name, password);
	}
	
	@Override
	public UserEntity getUserByToken(String token) {
		return entityReader.getUserByToken(token);
	}
	
	@Override
	public String createOrGetToken(String name, String password) throws TokenException {
		UserEntity user = entityReader.getUserByNameAndPassword(name, password);
		if (user != null) {
			if (!user.isSubscriptionActive()) {
				throw new TokenException("User subscription expired.");
			}

			String token = user.getToken();
			if (isTokenRegenerationRequired(user.getTokenGenerationTime())) {
				token = UUID.randomUUID().toString();
				user.setToken(token);
				user.setTokenGenerationTime(new Date());
				entityWriter.save(user);
			}
			return token;
		}
		throw new TokenException("The user name or password is incorrect.");
	}

	private boolean isTokenRegenerationRequired(Date tokenGenerationTime) {
		boolean isTokenRegenerationRequired = true;
		if (tokenGenerationTime != null) {
			Date currentTime = new Date();
			long diffInMillies = currentTime.getTime() - tokenGenerationTime.getTime();
			long diffInSeconds = diffInMillies / 1000;
			isTokenRegenerationRequired = diffInSeconds > MAX_TOKEN_LIFE_IN_SECS;
		}
		return isTokenRegenerationRequired;
	}
}
