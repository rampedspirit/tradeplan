package com.bhs.gtk.screener.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.threeten.bp.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ExecutableCreateRequest
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-12-28T23:38:24.439962500+05:30[Asia/Calcutta]")

public class ExecutableCreateRequest   {
  @JsonProperty("marketTime")
  private OffsetDateTime marketTime = null;

  @JsonProperty("note")
  private String note = null;

  @JsonProperty("ScripNames")
  @Valid
  private List<String> scripNames = null;

  public ExecutableCreateRequest marketTime(OffsetDateTime marketTime) {
    this.marketTime = marketTime;
    return this;
  }

  /**
   * market time at which screener to be executed.
   * @return marketTime
  **/
  @ApiModelProperty(required = true, value = "market time at which screener to be executed.")
  @NotNull

  @Valid

  public OffsetDateTime getMarketTime() {
    return marketTime;
  }

  public void setMarketTime(OffsetDateTime marketTime) {
    this.marketTime = marketTime;
  }

  public ExecutableCreateRequest note(String note) {
    this.note = note;
    return this;
  }

  /**
   * note related to execution
   * @return note
  **/
  @ApiModelProperty(value = "note related to execution")


  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public ExecutableCreateRequest scripNames(List<String> scripNames) {
    this.scripNames = scripNames;
    return this;
  }

  public ExecutableCreateRequest addScripNamesItem(String scripNamesItem) {
    if (this.scripNames == null) {
      this.scripNames = new ArrayList<String>();
    }
    this.scripNames.add(scripNamesItem);
    return this;
  }

  /**
   * scrip names.
   * @return scripNames
  **/
  @ApiModelProperty(value = "scrip names.")


  public List<String> getScripNames() {
    return scripNames;
  }

  public void setScripNames(List<String> scripNames) {
    this.scripNames = scripNames;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExecutableCreateRequest executableCreateRequest = (ExecutableCreateRequest) o;
    return Objects.equals(this.marketTime, executableCreateRequest.marketTime) &&
        Objects.equals(this.note, executableCreateRequest.note) &&
        Objects.equals(this.scripNames, executableCreateRequest.scripNames);
  }

  @Override
  public int hashCode() {
    return Objects.hash(marketTime, note, scripNames);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExecutableCreateRequest {\n");
    
    sb.append("    marketTime: ").append(toIndentedString(marketTime)).append("\n");
    sb.append("    note: ").append(toIndentedString(note)).append("\n");
    sb.append("    scripNames: ").append(toIndentedString(scripNames)).append("\n");
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

