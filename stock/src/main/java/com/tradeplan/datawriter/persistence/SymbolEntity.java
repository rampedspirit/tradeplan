package com.tradeplan.datawriter.persistence;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class SymbolEntity {
	
	@Id
	@Column
	private Long id;

	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String symbol;

	@Column(length = PersistenceConstants.LARGE_TEXT_LIMIT)
	private String name;

	protected SymbolEntity() {
	}

	public SymbolEntity(Long id, String symbol, String name) {
		this.id = id;
		this.name = name;
		this.symbol = symbol;
	}

	public Long getId() {
		return id;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getName() {
		return name;
	}
}
