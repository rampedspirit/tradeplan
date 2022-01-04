package com.bhs.gtk.screener.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.threeten.bp.DateTimeUtils;

import com.bhs.gtk.screener.model.ExecutableCreateRequest;
import com.bhs.gtk.screener.model.ScreenerCreateRequest;
import com.bhs.gtk.screener.model.ScripResult;

@Component
public class EntityCreator {
	
	@Autowired
	private ConditionResultRepository  conditionResultRepository;
	
	public ExecutableEntity createExecutableEntity(Date marketTime,String note, UUID watchlistId,UUID conditionId) {
		return  new ExecutableEntity(note, marketTime, watchlistId, conditionId);
	}
	
	public List<ConditionResultEntity> createConditionResultEntities(List<String> scripNames,Date marketTime, UUID conditionId) {
		List<ConditionResultEntity> resultEntities = new ArrayList<>();
		List<ConditionResultEntity> savedResultEntities = new ArrayList<>();
		for(String scrip: scripNames) {
			ConditionResultId resultId = new ConditionResultId(conditionId, marketTime, scrip);
			Optional<ConditionResultEntity> entityInContainer = conditionResultRepository.findById(resultId);
			if(entityInContainer.isPresent()) {
				savedResultEntities.add(entityInContainer.get());
			}else {
				resultEntities.add(new ConditionResultEntity(conditionId, marketTime, scrip,
						ScripResult.StatusEnum.QUEUED.name()));
			}
		}
		if(!resultEntities.isEmpty()) {
			Iterable<ConditionResultEntity> iterableSavedEntities = conditionResultRepository.saveAll(resultEntities);
			iterableSavedEntities.forEach(e -> savedResultEntities.add(e));
		}
		return  savedResultEntities;
	}
	
	public ScreenerEntity createScreenerEntity(ScreenerCreateRequest screenerRequest) {
		ScreenerEntity entity = new ScreenerEntity(screenerRequest.getName(), screenerRequest.getDescription(),
				screenerRequest.getWatchListId(), screenerRequest.getConditionId());
		return entity;
	}

}
