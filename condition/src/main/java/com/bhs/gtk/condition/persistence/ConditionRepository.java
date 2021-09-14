package com.bhs.gtk.condition.persistence;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConditionRepository extends CrudRepository<ConditionEntity, UUID> {

}
