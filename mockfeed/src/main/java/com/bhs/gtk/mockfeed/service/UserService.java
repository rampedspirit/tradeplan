package com.bhs.gtk.mockfeed.service;

import com.bhs.gtk.mockfeed.exceptions.TokenException;
import com.bhs.gtk.mockfeed.persistence.UserEntity;

public interface UserService {
	public void createUser(String name, String password);

	public UserEntity getUserByNameAndPassword(String name, String password);
	
	public UserEntity getUserByToken(String token);
	
	public String createOrGetToken(String name, String password) throws TokenException;
}
