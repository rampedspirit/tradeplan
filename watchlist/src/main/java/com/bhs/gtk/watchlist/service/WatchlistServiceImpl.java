package com.bhs.gtk.watchlist.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhs.gtk.watchlist.messaging.ChangeNotification.ChangeStatusEnum;
import com.bhs.gtk.watchlist.messaging.MessageProducer;
import com.bhs.gtk.watchlist.model.WatchlistCreateRequest;
import com.bhs.gtk.watchlist.model.WatchlistPatchData;
import com.bhs.gtk.watchlist.model.WatchlistPatchData.PropertyEnum;
import com.bhs.gtk.watchlist.model.WatchlistResponse;
import com.bhs.gtk.watchlist.persistence.EntityReader;
import com.bhs.gtk.watchlist.persistence.EntityWriter;
import com.bhs.gtk.watchlist.persistence.WatchlistEntity;
import com.bhs.gtk.watchlist.util.Mapper;

@Service
public class WatchlistServiceImpl implements WatchlistService {

	@Autowired
	private EntityWriter entityWriter;

	@Autowired
	private EntityReader entityReader;

	@Autowired
	private Mapper mapper;

	@Autowired
	private MessageProducer messageProducer;

	@Override
	public WatchlistResponse createWatchlist(WatchlistCreateRequest watchlist) {
		WatchlistEntity watchlistEntity = entityWriter.createWatchlistEntity(watchlist);
		return mapper.getWatchlistResponse(watchlistEntity);
	}

	@Override
	public List<WatchlistResponse> getAllWatchlists() {
		List<WatchlistEntity> filters = entityReader.getAllWatchlistEntites();
		return mapper.getAllWatchlistResponses(filters);
	}

	@Override
	public WatchlistResponse getWatchlist(UUID watchlistId) {
		WatchlistEntity watchlistEntity = entityReader.getWatchlistEntity(watchlistId);
		return mapper.getWatchlistResponse(watchlistEntity);
	}

	@Override
	public WatchlistResponse deleteWatchlist(UUID id) {
		WatchlistEntity watchlistEntity = entityReader.getWatchlistEntity(id);
		if (watchlistEntity == null) {
			return null;
		}
		if (messageProducer.sendChangeNotification(watchlistEntity.getId(), ChangeStatusEnum.DELETED)) {
			entityWriter.deleteWatchlistEntity(watchlistEntity);
			return mapper.getWatchlistResponse(watchlistEntity);
		}
		return null;
	}

	@Override
	public WatchlistResponse updateWatchlist(List<WatchlistPatchData> patchData, UUID watchlistId) {
		if (patchData == null || watchlistId == null) {
			// throw exception if validation fails
			return null;
		}
		if (!isValidatePatchData(patchData)) {
			return null;
		}

		WatchlistEntity filterEntity = entityReader.getWatchlistEntity(watchlistId);
		boolean scripNamesChanged = false;

		for (WatchlistPatchData pd : patchData) {
			@NotNull
			String value = pd.getValue();
			switch (pd.getProperty()) {
			case NAME:
				filterEntity.setName(value);
				break;
			case DESCRIPTION:
				filterEntity.setDescription(value);
				break;
			case SCRIP_NAMES:
				scripNamesChanged = true;
				String[] scripNames = value.split(",");
				List<String> scrips = new ArrayList<>();
				for (String scripName : scripNames) {
					scrips.add(scripName);
				}
				filterEntity.setScripNames(scrips);
				break;
			}
		}

		if (scripNamesChanged) {
			if (!messageProducer.sendChangeNotification(filterEntity.getId(), ChangeStatusEnum.UPDATED)) {
				// not able to notify change in logic and hence logic change is aborted.
				return null;
			}
		}

		WatchlistEntity changedFilterEntity = entityWriter.saveWatchlistEntity(filterEntity);
		return mapper.getWatchlistResponse(changedFilterEntity);
	}

	private boolean isValidatePatchData(List<WatchlistPatchData> patchData) {
		if (patchData == null || patchData.isEmpty()) {
			return false;
		}
		List<@NotNull PropertyEnum> properties = patchData.stream().map(p -> p.getProperty())
				.collect(Collectors.toList());
		// ^ is XOR operation.
		return !(properties.contains(PropertyEnum.NAME) ^ properties.contains(PropertyEnum.DESCRIPTION));
	}
}
