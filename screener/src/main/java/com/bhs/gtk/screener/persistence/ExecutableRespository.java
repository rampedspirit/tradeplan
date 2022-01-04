package com.bhs.gtk.screener.persistence;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutableRespository extends CrudRepository<ExecutableEntity, UUID> {
	List<ExecutableEntity> findByConditionIdAndMarketTime(UUID conditionId, Date marketTime);
	ExecutableEntity findByConditionIdAndMarketTimeAndWatchlistId(UUID conditionId, Date marketTime,UUID watchlistId);
}
