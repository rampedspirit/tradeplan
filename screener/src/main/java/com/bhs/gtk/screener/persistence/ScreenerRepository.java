package com.bhs.gtk.screener.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreenerRepository extends CrudRepository<ScreenerEntity, UUID>{
	List<ScreenerEntity> findByConditionId(UUID conditionId);
}
