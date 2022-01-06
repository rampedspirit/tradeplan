package com.bhs.gtk.expression.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpressionResultRepository extends CrudRepository<ExpressionResultEntity, Long> {

}
