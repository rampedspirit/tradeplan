package com.bhs.gtk.watchlist.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.threeten.bp.LocalDate;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * SymbolResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-18T20:00:55.380225700+05:30[Asia/Calcutta]")

public class SymbolResponse   {
  @JsonProperty("symbol")
  private String symbol = null;

  @JsonProperty("exchange")
  private String exchange = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("series")
  private String series = null;

  @JsonProperty("dol")
  private LocalDate dol = null;

  public SymbolResponse symbol(String symbol) {
    this.symbol = symbol;
    return this;
  }

  /**
   * symbol of stock
   * @return symbol
  **/
  @ApiModelProperty(required = true, value = "symbol of stock")
  @NotNull


  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public SymbolResponse exchange(String exchange) {
    this.exchange = exchange;
    return this;
  }

  /**
   * exchage that stock belongs to
   * @return exchange
  **/
  @ApiModelProperty(required = true, value = "exchage that stock belongs to")
  @NotNull


  public String getExchange() {
    return exchange;
  }

  public void setExchange(String exchange) {
    this.exchange = exchange;
  }

  public SymbolResponse name(String name) {
    this.name = name;
    return this;
  }

  /**
   * name of stock
   * @return name
  **/
  @ApiModelProperty(required = true, value = "name of stock")
  @NotNull


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public SymbolResponse series(String series) {
    this.series = series;
    return this;
  }

  /**
   * series of stock
   * @return series
  **/
  @ApiModelProperty(value = "series of stock")


  public String getSeries() {
    return series;
  }

  public void setSeries(String series) {
    this.series = series;
  }

  public SymbolResponse dol(LocalDate dol) {
    this.dol = dol;
    return this;
  }

  /**
   * date of listing
   * @return dol
  **/
  @ApiModelProperty(required = true, value = "date of listing")
  @NotNull

  @Valid

  public LocalDate getDol() {
    return dol;
  }

  public void setDol(LocalDate dol) {
    this.dol = dol;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SymbolResponse symbolResponse = (SymbolResponse) o;
    return Objects.equals(this.symbol, symbolResponse.symbol) &&
        Objects.equals(this.exchange, symbolResponse.exchange) &&
        Objects.equals(this.name, symbolResponse.name) &&
        Objects.equals(this.series, symbolResponse.series) &&
        Objects.equals(this.dol, symbolResponse.dol);
  }

  @Override
  public int hashCode() {
    return Objects.hash(symbol, exchange, name, series, dol);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SymbolResponse {\n");
    
    sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
    sb.append("    exchange: ").append(toIndentedString(exchange)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    series: ").append(toIndentedString(series)).append("\n");
    sb.append("    dol: ").append(toIndentedString(dol)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

