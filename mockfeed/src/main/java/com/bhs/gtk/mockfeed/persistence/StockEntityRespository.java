package com.bhs.gtk.mockfeed.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockEntityRespository extends CrudRepository<StockEntity, StockId> {
}
