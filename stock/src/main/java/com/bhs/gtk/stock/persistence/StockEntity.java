package com.bhs.gtk.stock.persistence;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(StockId.class)
public class StockEntity {

	@Id
	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String exchange;

	@Id
	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String symbol;

	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String name;

	@Column(length = PersistenceConstants.SMALL_TEXT_LIMIT)
	private String series;

	@Column
	private Date dol;

	protected StockEntity() {
	}

	public StockEntity(String exchange, String symbol, String name) {
		this.exchange = exchange;
		this.symbol = symbol;
		this.name = name;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
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

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public void setDol(Date dol) {
		this.dol = dol;
	}

	public Date getDol() {
		return dol;
	}
}
