package com.bhs.gtk.mockfeed.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * MethodRequest
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-18T16:57:42.881383100+05:30[Asia/Calcutta]")

public class MethodRequest   {
  /**
   * Gets or Sets method
   */
  public enum MethodEnum {
    ADDSYMBOL("addsymbol"),
    
    REMOVESYMBOL("removesymbol"),
    
    LOGOUT("logout");

    private String value;

    MethodEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static MethodEnum fromValue(String text) {
      for (MethodEnum b : MethodEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("method")
  private MethodEnum method = null;

  @JsonProperty("symbols")
  @Valid
  private List<String> symbols = new ArrayList<String>();

  public MethodRequest method(MethodEnum method) {
    this.method = method;
    return this;
  }

  /**
   * Get method
   * @return method
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public MethodEnum getMethod() {
    return method;
  }

  public void setMethod(MethodEnum method) {
    this.method = method;
  }

  public MethodRequest symbols(List<String> symbols) {
    this.symbols = symbols;
    return this;
  }

  public MethodRequest addSymbolsItem(String symbolsItem) {
    this.symbols.add(symbolsItem);
    return this;
  }

  /**
   * Get symbols
   * @return symbols
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public List<String> getSymbols() {
    return symbols;
  }

  public void setSymbols(List<String> symbols) {
    this.symbols = symbols;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MethodRequest methodRequest = (MethodRequest) o;
    return Objects.equals(this.method, methodRequest.method) &&
        Objects.equals(this.symbols, methodRequest.symbols);
  }

  @Override
  public int hashCode() {
    return Objects.hash(method, symbols);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MethodRequest {\n");
    
    sb.append("    method: ").append(toIndentedString(method)).append("\n");
    sb.append("    symbols: ").append(toIndentedString(symbols)).append("\n");
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

