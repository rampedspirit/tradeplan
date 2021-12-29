package com.bhs.gtk.screener.model;

import java.util.Objects;
import com.bhs.gtk.screener.model.ScripResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.threeten.bp.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ExecutableDetailedResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-12-28T23:38:24.439962500+05:30[Asia/Calcutta]")

public class ExecutableDetailedResponse   {
  @JsonProperty("executionId")
  private UUID executionId = null;

  @JsonProperty("marketTime")
  private OffsetDateTime marketTime = null;

  @JsonProperty("note")
  private String note = null;

  @JsonProperty("status")
  private String status = null;

  @JsonProperty("numberOfScripForExecution")
  private BigDecimal numberOfScripForExecution = null;

  @JsonProperty("numberOfScripWithResultAvailable")
  private BigDecimal numberOfScripWithResultAvailable = null;

  @JsonProperty("result")
  @Valid
  private List<ScripResult> result = null;

  public ExecutableDetailedResponse executionId(UUID executionId) {
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

  public ExecutableDetailedResponse marketTime(OffsetDateTime marketTime) {
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

  public ExecutableDetailedResponse note(String note) {
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

  public ExecutableDetailedResponse status(String status) {
    this.status = status;
    return this;
  }

  /**
   * status of execution
   * @return status
  **/
  @ApiModelProperty(value = "status of execution")


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public ExecutableDetailedResponse numberOfScripForExecution(BigDecimal numberOfScripForExecution) {
    this.numberOfScripForExecution = numberOfScripForExecution;
    return this;
  }

  /**
   * total number of scrip attached to this executable for execution
   * @return numberOfScripForExecution
  **/
  @ApiModelProperty(value = "total number of scrip attached to this executable for execution")

  @Valid

  public BigDecimal getNumberOfScripForExecution() {
    return numberOfScripForExecution;
  }

  public void setNumberOfScripForExecution(BigDecimal numberOfScripForExecution) {
    this.numberOfScripForExecution = numberOfScripForExecution;
  }

  public ExecutableDetailedResponse numberOfScripWithResultAvailable(BigDecimal numberOfScripWithResultAvailable) {
    this.numberOfScripWithResultAvailable = numberOfScripWithResultAvailable;
    return this;
  }

  /**
   * number of scrip attached this executable which are executed and have execution result.
   * @return numberOfScripWithResultAvailable
  **/
  @ApiModelProperty(value = "number of scrip attached this executable which are executed and have execution result.")

  @Valid

  public BigDecimal getNumberOfScripWithResultAvailable() {
    return numberOfScripWithResultAvailable;
  }

  public void setNumberOfScripWithResultAvailable(BigDecimal numberOfScripWithResultAvailable) {
    this.numberOfScripWithResultAvailable = numberOfScripWithResultAvailable;
  }

  public ExecutableDetailedResponse result(List<ScripResult> result) {
    this.result = result;
    return this;
  }

  public ExecutableDetailedResponse addResultItem(ScripResult resultItem) {
    if (this.result == null) {
      this.result = new ArrayList<ScripResult>();
    }
    this.result.add(resultItem);
    return this;
  }

  /**
   * Get result
   * @return result
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<ScripResult> getResult() {
    return result;
  }

  public void setResult(List<ScripResult> result) {
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
    ExecutableDetailedResponse executableDetailedResponse = (ExecutableDetailedResponse) o;
    return Objects.equals(this.executionId, executableDetailedResponse.executionId) &&
        Objects.equals(this.marketTime, executableDetailedResponse.marketTime) &&
        Objects.equals(this.note, executableDetailedResponse.note) &&
        Objects.equals(this.status, executableDetailedResponse.status) &&
        Objects.equals(this.numberOfScripForExecution, executableDetailedResponse.numberOfScripForExecution) &&
        Objects.equals(this.numberOfScripWithResultAvailable, executableDetailedResponse.numberOfScripWithResultAvailable) &&
        Objects.equals(this.result, executableDetailedResponse.result);
  }

  @Override
  public int hashCode() {
    return Objects.hash(executionId, marketTime, note, status, numberOfScripForExecution, numberOfScripWithResultAvailable, result);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExecutableDetailedResponse {\n");
    
    sb.append("    executionId: ").append(toIndentedString(executionId)).append("\n");
    sb.append("    marketTime: ").append(toIndentedString(marketTime)).append("\n");
    sb.append("    note: ").append(toIndentedString(note)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    numberOfScripForExecution: ").append(toIndentedString(numberOfScripForExecution)).append("\n");
    sb.append("    numberOfScripWithResultAvailable: ").append(toIndentedString(numberOfScripWithResultAvailable)).append("\n");
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

