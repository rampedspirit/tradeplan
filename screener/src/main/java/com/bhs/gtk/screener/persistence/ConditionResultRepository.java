package com.bhs.gtk.screener.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConditionResultRepository extends CrudRepository<ConditionResultEntity, ConditionResultId> {
	List<ConditionResultEntity> findByConditionId(UUID conditionId);
	List<ConditionResultEntity> findByStatus(String status);
}
