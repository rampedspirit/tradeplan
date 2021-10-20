package com.bhs.gtk.screener.model;

import java.util.Objects;
import com.bhs.gtk.screener.model.ConditionScripResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.threeten.bp.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ScreenerResult
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-09-25T06:41:13.746385900+05:30[Asia/Calcutta]")

public class ScreenerResult   {
  @JsonProperty("screenerId")
  private UUID screenerId = null;

  @JsonProperty("executionId")
  private UUID executionId = null;

  @JsonProperty("marketTime")
  private OffsetDateTime marketTime = null;

  @JsonProperty("note")
  private String note = null;

  @JsonProperty("result")
  @Valid
  private List<ConditionScripResult> result = null;

  public ScreenerResult screenerId(UUID screenerId) {
    this.screenerId = screenerId;
    return this;
  }

  /**
   * screener identifier
   * @return screenerId
  **/
  @ApiModelProperty(value = "screener identifier")

  @Valid

  public UUID getScreenerId() {
    return screenerId;
  }

  public void setScreenerId(UUID screenerId) {
    this.screenerId = screenerId;
  }

  public ScreenerResult executionId(UUID executionId) {
    this.executionId = executionId;
    return this;
  }

  /**
   * unique identifier of the execution, using which result can be obtained
   * @return executionId
  **/
  @ApiModelProperty(value = "unique identifier of the execution, using which result can be obtained")

  @Valid

  public UUID getExecutionId() {
    return executionId;
  }

  public void setExecutionId(UUID executionId) {
    this.executionId = executionId;
  }

  public ScreenerResult marketTime(OffsetDateTime marketTime) {
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

  public ScreenerResult note(String note) {
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

  public ScreenerResult result(List<ConditionScripResult> result) {
    this.result = result;
    return this;
  }

  public ScreenerResult addResultItem(ConditionScripResult resultItem) {
    if (this.result == null) {
      this.result = new ArrayList<ConditionScripResult>();
    }
    this.result.add(resultItem);
    return this;
  }

  /**
   * screener result
   * @return result
  **/
  @ApiModelProperty(value = "screener result")

  @Valid

  public List<ConditionScripResult> getResult() {
    return result;
  }

  public void setResult(List<ConditionScripResult> result) {
    this.result = result;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScreenerResult screenerResult = (ScreenerResult) o;
    return Objects.equals(this.screenerId, screenerResult.screenerId) &&
        Objects.equals(this.executionId, screenerResult.executionId) &&
        Objects.equals(this.marketTime, screenerResult.marketTime) &&
        Objects.equals(this.note, screenerResult.note) &&
        Objects.equals(this.result, screenerResult.result);
  }

  @Override
  public int hashCode() {
    return Objects.hash(screenerId, executionId, marketTime, note, result);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScreenerResult {\n");
    
    sb.append("    screenerId: ").append(toIndentedString(screenerId)).append("\n");
    sb.append("    executionId: ").append(toIndentedString(executionId)).append("\n");
    sb.append("    marketTime: ").append(toIndentedString(marketTime)).append("\n");
    sb.append("    note: ").append(toIndentedString(note)).append("\n");
    sb.append("    result: ").append(toIndentedString(result)).append("\n");
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

