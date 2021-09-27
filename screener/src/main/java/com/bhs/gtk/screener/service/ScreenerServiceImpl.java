package com.bhs.gtk.screener.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhs.gtk.screener.model.PatchModel;
import com.bhs.gtk.screener.model.ScreenerRequest;
import com.bhs.gtk.screener.model.ScreenerResponse;
import com.bhs.gtk.screener.persistence.ScreenerEntity;
import com.bhs.gtk.screener.persistence.ScreenerRepository;
import com.bhs.gtk.screener.util.Mapper;

@Service
public class ScreenerServiceImpl implements ScreernerService {

	@Autowired
	Mapper mapper;
	
	@Autowired
	ScreenerRepository screenerRepository;
	
	@Override
	public ScreenerResponse createScreener(ScreenerRequest screenerRequest) {
		ScreenerEntity entity = screenerRepository.save(mapper.getScreenerEntity(screenerRequest));
		return mapper.getScreenerResponse(entity);
	}

	@Override
	public List<ScreenerResponse> getAllScreeners() {
		Iterator<ScreenerEntity> iterator = screenerRepository.findAll().iterator();
		List<ScreenerResponse> screenerResponses = new ArrayList<>();
		while(iterator.hasNext()) {
			screenerResponses.add(mapper.getScreenerResponse(iterator.next()));
		}
		return screenerResponses;
	}

	@Override
	public ScreenerResponse getScreener(UUID screenerId) {
		if(screenerId == null) {
			//throw exception
			return null;
		}
		Optional<ScreenerEntity> screenerEntityContainer = screenerRepository.findById(screenerId);
		if(screenerEntityContainer.isPresent()) {
			return mapper.getScreenerResponse(screenerEntityContainer.get());
		}
		//throw exception screener not found
		return null;
	}

	@Override
	public ScreenerResponse deleteScreener(UUID screenerId) {
		if(screenerId == null) {
			//throw exception
			return null;
		}
		if(screenerRepository.existsById(screenerId)) {
			ScreenerEntity screenerEntity = screenerRepository.findById(screenerId).get();
			screenerRepository.delete(screenerEntity);
			return mapper.getScreenerResponse(screenerEntity);
		}
		//throw exception
		return null;
	}

	@Override
	public ScreenerResponse updateScreener(PatchModel patchModel, UUID screenerId) {
		// TODO Auto-generated method stub
		return null;
	}

}
