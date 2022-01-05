package com.bhs.gtk.condition.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Error
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-09-24T15:34:38.557240300+05:30[Asia/Calcutta]")

public class Error   {
  @JsonProperty("message")
  private String message = null;

  @JsonProperty("resolution")
  private String resolution = null;

  @JsonProperty("errorCode")
  private String errorCode = null;

  public Error message(String message) {
    this.message = message;
    return this;
  }

  /**
   * brief information about error
   * @return message
  **/
  @ApiModelProperty(value = "brief information about error")


  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Error resolution(String resolution) {
    this.resolution = resolution;
    return this;
  }

  /**
   * possibly hints to solve the error
   * @return resolution
  **/
  @ApiModelProperty(value = "possibly hints to solve the error")


  public String getResolution() {
    return resolution;
  }

  public void setResolution(String resolution) {
    this.resolution = resolution;
  }

  public Error errorCode(String errorCode) {
    this.errorCode = errorCode;
    return this;
  }

  /**
   * pattern to unqiuely identify error root cause and origin.
   * @return errorCode
  **/
  @ApiModelProperty(value = "pattern to unqiuely identify error root cause and origin.")


  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Error error = (Error) o;
    return Objects.equals(this.message, error.message) &&
        Objects.equals(this.resolution, error.resolution) &&
        Objects.equals(this.errorCode, error.errorCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, resolution, errorCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Error {\n");
    
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    resolution: ").append(toIndentedString(resolution)).append("\n");
    sb.append("    errorCode: ").append(toIndentedString(errorCode)).append("\n");
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

