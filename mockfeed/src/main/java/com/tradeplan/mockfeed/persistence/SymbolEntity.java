package com.tradeplan.mockfeed.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SymbolEntity {

	@Id
	@Column
	private Long id;

	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String symbol;

	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String name;

	protected SymbolEntity() {
	}

	public SymbolEntity(Long id, String symbol, String name) {
		this.id = id;
		this.symbol = symbol;
		this.name = name;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
