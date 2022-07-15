package com.tradeplan.datawriter.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandleRespository extends CrudRepository<CandleEntity, CandleId> {
}
