package com.bhs.gtk.screener.service;

import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.bhs.gtk.screener.model.ExecutableDetailedResponse;
import com.bhs.gtk.screener.model.ExecutablePatchData;
import com.bhs.gtk.screener.model.ExecutableResponse;
import com.bhs.gtk.screener.persistence.ExecutableEntity;
import com.bhs.gtk.screener.persistence.ExecutableRespository;
import com.bhs.gtk.screener.util.Mapper;

public class ExecutableServiceImpl implements ExecutableService{

	
	@Autowired
	private Mapper mapper;
	
	@Autowired
	private ExecutableRespository executableRespository;
	
	@Override
	public ExecutableResponse getExecutable(UUID executableId) {
		ExecutableEntity entity = getExecutableEntity(executableId);
		if(entity != null) {
			return mapper.getExecutableResponse(entity);
		}
		//thow exeception
		return null;
	}

	@Override
	public ExecutableDetailedResponse getResult(UUID executableId) {
		ExecutableEntity entity = getExecutableEntity(executableId);
		if(entity != null) {
			return mapper.getExecutableDetailedResponse(entity);
		}
		//thow exeception
		return null;
	}

	@Override
	public ExecutableResponse updateExecutable(ExecutablePatchData executablePatchData, UUID executableId) {
		ExecutableEntity entity = getExecutableEntity(executableId);
		if (entity != null) {
			if (StringUtils.equals(executablePatchData.getProperty().name(),
					ExecutablePatchData.PropertyEnum.NOTE.name())) {
				entity.setNote(executablePatchData.getValue());
				ExecutableEntity savedEntity = executableRespository.save(entity);
				return mapper.getExecutableResponse(savedEntity);
			}
		}
		return null;
	}
	
	private ExecutableEntity getExecutableEntity(UUID executableId) {
		if (executableId != null) {
			Optional<ExecutableEntity> executableInContainer = executableRespository.findById(executableId);
			if (executableInContainer.isPresent()) {
				return executableInContainer.get();
			}
		}
		return null;
	}

}
