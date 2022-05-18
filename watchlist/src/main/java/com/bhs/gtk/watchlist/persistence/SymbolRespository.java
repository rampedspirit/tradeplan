package com.bhs.gtk.watchlist.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SymbolRespository extends CrudRepository<SymbolEntity, SymbolId> {
}
