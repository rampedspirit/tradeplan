package com.bhs.gtk.expression.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpressionRespository extends CrudRepository<ExpressionEntity, String> {

}
