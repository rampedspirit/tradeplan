package com.bhs.gtk.filter.persistence;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilterRepository extends CrudRepository<FilterEntity, UUID>{

}
