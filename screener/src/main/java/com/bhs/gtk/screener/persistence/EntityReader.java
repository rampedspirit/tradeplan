package com.bhs.gtk.screener.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityReader {

	
	@Autowired
	private ScreenerRepository screenerRepository;
	
	@Autowired
	private ExecutableRespository executableRespository;
	
	public List<ScreenerEntity> getAllScreenerEntities() {
		Iterator<ScreenerEntity> iterator = screenerRepository.findAll().iterator();
		List<ScreenerEntity> screenerEntities = new ArrayList<>();
		while(iterator.hasNext()) {
			screenerEntities.add(iterator.next());
		}
		return screenerEntities;
	}
	
	public ScreenerEntity getScreenerEntity(UUID screenerId) {
		if (screenerId != null) {
			Optional<ScreenerEntity> screenerEntityContainer = screenerRepository.findById(screenerId);
			if (screenerEntityContainer.isPresent()) {
				return screenerEntityContainer.get();
			}
		}
		return null;
	}
	
	public List<ExecutableEntity>  getExecutableEntitites(UUID conditionId, Date marketTime) {
		return executableRespository.findByConditionIdAndMarketTime(conditionId, marketTime);
	}
}
