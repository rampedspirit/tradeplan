package com.bhs.gtk.stock.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRespository extends CrudRepository<StockEntity, StockId> {
}
