package com.bhs.gtk.mockfeed.persistence;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityWriter {

	@Autowired
	private StockEntityRespository stockEntityRespository;

	@Autowired
	private UserEntityRepository userEntityRepository;

	public void createUser(String name, String password) {
		UserEntity userEntity = new UserEntity();
		userEntity.setName(name);
		userEntity.setPassword(password);
		userEntity.setSubscriptionActive(true);
		userEntityRepository.save(userEntity);
	}
	
	public void save(UserEntity userEntity) {
		userEntityRepository.save(userEntity);
	}

	public void updateAllSymbols(List<StockEntity> stocks) {
		stockEntityRespository.saveAll(stocks);
	}
}
