package com.bhs.gtk.condition.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilterRespository extends CrudRepository<FilterEntity, UUID> {
	List<FilterEntity> findByConditionsId(UUID id);
}
