package com.bhs.gtk.filter.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArithmeticExpressionResultRepository extends CrudRepository<ArithmeticExpressionResultEntity, ArithmeticExpressionResultId>{

}
