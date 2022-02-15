package com.bhs.gtk.filter.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilterResultRepository extends CrudRepository<FilterResultEntity, FilterResultId> {
	List<FilterResultEntity> findByFilterId(UUID filterId);
}
