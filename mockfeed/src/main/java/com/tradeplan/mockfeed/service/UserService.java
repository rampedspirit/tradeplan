package com.tradeplan.mockfeed.service;

import com.tradeplan.mockfeed.exceptions.TokenException;
import com.tradeplan.mockfeed.persistence.UserEntity;

public interface UserService {
	public void createUser(String name, String password);

	public UserEntity getUserByNameAndPassword(String name, String password);
	
	public UserEntity getUserByToken(String token);
	
	public String createOrGetToken(String name, String password) throws TokenException;
}
