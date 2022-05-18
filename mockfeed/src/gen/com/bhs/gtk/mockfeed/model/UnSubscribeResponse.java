package com.bhs.gtk.mockfeed.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * UnSubscribeResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-18T16:57:42.881383100+05:30[Asia/Calcutta]")

public class UnSubscribeResponse   {
  @JsonProperty("success")
  private Boolean success = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("symbolsremoved")
  private Integer symbolsremoved = null;

  public UnSubscribeResponse success(Boolean success) {
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

  public UnSubscribeResponse message(String message) {
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

  public UnSubscribeResponse symbolsremoved(Integer symbolsremoved) {
    this.symbolsremoved = symbolsremoved;
    return this;
  }

  /**
   * Get symbolsremoved
   * @return symbolsremoved
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public Integer getSymbolsremoved() {
    return symbolsremoved;
  }

  public void setSymbolsremoved(Integer symbolsremoved) {
    this.symbolsremoved = symbolsremoved;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UnSubscribeResponse unSubscribeResponse = (UnSubscribeResponse) o;
    return Objects.equals(this.success, unSubscribeResponse.success) &&
        Objects.equals(this.message, unSubscribeResponse.message) &&
        Objects.equals(this.symbolsremoved, unSubscribeResponse.symbolsremoved);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, message, symbolsremoved);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UnSubscribeResponse {\n");
    
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    symbolsremoved: ").append(toIndentedString(symbolsremoved)).append("\n");
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

