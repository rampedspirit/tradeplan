package com.tradeplan.mockfeed.persistence;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityWriter {

	@Autowired
	private SymbolRespository symbolRespository;

	@Autowired
	private UserRepository userEntityRepository;

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

	public void updateAllSymbols(List<SymbolEntity> symbols) {
		symbolRespository.saveAll(symbols);
	}
}