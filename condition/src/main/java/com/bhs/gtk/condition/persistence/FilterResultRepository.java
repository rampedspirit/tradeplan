package com.bhs.gtk.condition.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilterResultRepository extends CrudRepository<FilterResultEntity, FilterResultId> {
}