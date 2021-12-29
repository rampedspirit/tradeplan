package com.bhs.gtk.screener.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ScripResult
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-12-29T07:42:30.906286300+05:30[Asia/Calcutta]")

public class ScripResult   {
  @JsonProperty("scripName")
  private String scripName = null;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PASS("PASS"),
    
    FAIL("FAIL"),
    
    SCHEDULED("SCHEDULED"),
    
    RUNNING("RUNNING"),
    
    INVALID("INVALID");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("status")
  private StatusEnum status = null;

  public ScripResult scripName(String scripName) {
    this.scripName = scripName;
    return this;
  }

  /**
   * Get scripName
   * @return scripName
  **/
  @ApiModelProperty(value = "")


  public String getScripName() {
    return scripName;
  }

  public void setScripName(String scripName) {
    this.scripName = scripName;
  }

  public ScripResult status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
  **/
  @ApiModelProperty(value = "")


  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScripResult scripResult = (ScripResult) o;
    return Objects.equals(this.scripName, scripResult.scripName) &&
        Objects.equals(this.status, scripResult.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(scripName, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScripResult {\n");
    
    sb.append("    scripName: ").append(toIndentedString(scripName)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

