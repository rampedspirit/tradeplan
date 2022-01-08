package com.bhs.gtk.condition.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilterResultRepository extends CrudRepository<FilterResultEntity, FilterResultId> {
	List<ConditionResultEntity> findByFilterId(UUID filterId);
	List<ConditionResultEntity> findByStatus(String status);
}