package com.tradeplan.mockfeed.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityReader {

	@Autowired
	private UserRepository userEntityRepository;

	@Autowired
	private SymbolRespository symbolRespository;

	public UserEntity getUserByNameAndPassword(String name, String password) {
		Optional<UserEntity> user = userEntityRepository.findById(name);
		if (user.isPresent() && user.get().getPassword().equals(password)) {
			return user.get();
		}
		return null;
	}

	public List<SymbolEntity> getAllSymbols(String exchange) {
		Iterable<SymbolEntity> iterable = symbolRespository.findAll();
		return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
	}

	public long getAllSymbolsCount() {
		Iterable<SymbolEntity> iterable = symbolRespository.findAll();
		return StreamSupport.stream(iterable.spliterator(), false).count();
	}

	public UserEntity getUserByToken(String token) {
		Iterable<UserEntity> iterable = userEntityRepository.findAll();
		return StreamSupport.stream(iterable.spliterator(), false)
				.filter(user -> StringUtils.equals(user.getToken(), token)).findFirst().orElse(null);
	}
}
