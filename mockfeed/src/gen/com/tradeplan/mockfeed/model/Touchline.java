package com.tradeplan.mockfeed.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Touchline
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-06-09T14:14:08.618671700+05:30[Asia/Calcutta]")

public class Touchline   {
  @JsonProperty("success")
  private Boolean success = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("symbolsadded")
  private Integer symbolsadded = null;

  @JsonProperty("symbollist")
  @Valid
  private List<List<String>> symbollist = new ArrayList<List<String>>();

  @JsonProperty("totalsymbolsubscribed")
  private Integer totalsymbolsubscribed = null;

  public Touchline success(Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public Boolean isSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public Touchline message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Touchline symbolsadded(Integer symbolsadded) {
    this.symbolsadded = symbolsadded;
    return this;
  }

  /**
   * Get symbolsadded
   * @return symbolsadded
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public Integer getSymbolsadded() {
    return symbolsadded;
  }

  public void setSymbolsadded(Integer symbolsadded) {
    this.symbolsadded = symbolsadded;
  }

  public Touchline symbollist(List<List<String>> symbollist) {
    this.symbollist = symbollist;
    return this;
  }

  public Touchline addSymbollistItem(List<String> symbollistItem) {
    this.symbollist.add(symbollistItem);
    return this;
  }

  /**
   * Get symbollist
   * @return symbollist
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public List<List<String>> getSymbollist() {
    return symbollist;
  }

  public void setSymbollist(List<List<String>> symbollist) {
    this.symbollist = symbollist;
  }

  public Touchline totalsymbolsubscribed(Integer totalsymbolsubscribed) {
    this.totalsymbolsubscribed = totalsymbolsubscribed;
    return this;
  }

  /**
   * Get totalsymbolsubscribed
   * @return totalsymbolsubscribed
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public Integer getTotalsymbolsubscribed() {
    return totalsymbolsubscribed;
  }

  public void setTotalsymbolsubscribed(Integer totalsymbolsubscribed) {
    this.totalsymbolsubscribed = totalsymbolsubscribed;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Touchline touchline = (Touchline) o;
    return Objects.equals(this.success, touchline.success) &&
        Objects.equals(this.message, touchline.message) &&
        Objects.equals(this.symbolsadded, touchline.symbolsadded) &&
        Objects.equals(this.symbollist, touchline.symbollist) &&
        Objects.equals(this.totalsymbolsubscribed, touchline.totalsymbolsubscribed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, message, symbolsadded, symbollist, totalsymbolsubscribed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Touchline {\n");
    
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    symbolsadded: ").append(toIndentedString(symbolsadded)).append("\n");
    sb.append("    symbollist: ").append(toIndentedString(symbollist)).append("\n");
    sb.append("    totalsymbolsubscribed: ").append(toIndentedString(totalsymbolsubscribed)).append("\n");
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

