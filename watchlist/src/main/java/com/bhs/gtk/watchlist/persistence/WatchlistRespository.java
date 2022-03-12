package com.bhs.gtk.watchlist.persistence;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchlistRespository extends CrudRepository<WatchlistEntity, UUID> {
}
