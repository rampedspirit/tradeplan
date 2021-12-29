package com.bhs.gtk.screener.persistence;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutableRespository extends CrudRepository<ExecutableEntity, UUID> {

}
