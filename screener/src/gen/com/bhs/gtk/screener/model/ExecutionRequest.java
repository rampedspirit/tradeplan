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
 * ExecutionRequest
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-09-25T06:41:13.746385900+05:30[Asia/Calcutta]")

public class ExecutionRequest   {
  @JsonProperty("marketTime")
  private OffsetDateTime marketTime = null;

  @JsonProperty("note")
  private String note = null;

  @JsonProperty("scrips")
  @Valid
  private List<String> scrips = null;

  public ExecutionRequest marketTime(OffsetDateTime marketTime) {
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

  public ExecutionRequest note(String note) {
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

  public ExecutionRequest scrips(List<String> scrips) {
    this.scrips = scrips;
    return this;
  }

  public ExecutionRequest addScripsItem(String scripsItem) {
    if (this.scrips == null) {
      this.scrips = new ArrayList<String>();
    }
    this.scrips.add(scripsItem);
    return this;
  }

  /**
   * scrip names
   * @return scrips
  **/
  @ApiModelProperty(value = "scrip names")


  public List<String> getScrips() {
    return scrips;
  }

  public void setScrips(List<String> scrips) {
    this.scrips = scrips;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExecutionRequest executionRequest = (ExecutionRequest) o;
    return Objects.equals(this.marketTime, executionRequest.marketTime) &&
        Objects.equals(this.note, executionRequest.note) &&
        Objects.equals(this.scrips, executionRequest.scrips);
  }

  @Override
  public int hashCode() {
    return Objects.hash(marketTime, note, scrips);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExecutionRequest {\n");
    
    sb.append("    marketTime: ").append(toIndentedString(marketTime)).append("\n");
    sb.append("    note: ").append(toIndentedString(note)).append("\n");
    sb.append("    scrips: ").append(toIndentedString(scrips)).append("\n");
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

