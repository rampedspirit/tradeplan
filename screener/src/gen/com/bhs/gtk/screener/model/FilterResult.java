package com.bhs.gtk.screener.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * FilterResult
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-09-25T06:41:13.746385900+05:30[Asia/Calcutta]")

public class FilterResult   {
  @JsonProperty("screenerId")
  private UUID screenerId = null;

  @JsonProperty("executionId")
  private UUID executionId = null;

  @JsonProperty("conditionResultId")
  private UUID conditionResultId = null;

  @JsonProperty("filterResultId")
  private UUID filterResultId = null;

  @JsonProperty("filterCode")
  private String filterCode = null;

  public FilterResult screenerId(UUID screenerId) {
    this.screenerId = screenerId;
    return this;
  }

  /**
   * unique identifier of the screener to which execution is associated.
   * @return screenerId
  **/
  @ApiModelProperty(value = "unique identifier of the screener to which execution is associated.")

  @Valid

  public UUID getScreenerId() {
    return screenerId;
  }

  public void setScreenerId(UUID screenerId) {
    this.screenerId = screenerId;
  }

  public FilterResult executionId(UUID executionId) {
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

  public FilterResult conditionResultId(UUID conditionResultId) {
    this.conditionResultId = conditionResultId;
    return this;
  }

  /**
   * condition result  identifier.      
   * @return conditionResultId
  **/
  @ApiModelProperty(value = "condition result  identifier.      ")

  @Valid

  public UUID getConditionResultId() {
    return conditionResultId;
  }

  public void setConditionResultId(UUID conditionResultId) {
    this.conditionResultId = conditionResultId;
  }

  public FilterResult filterResultId(UUID filterResultId) {
    this.filterResultId = filterResultId;
    return this;
  }

  /**
   * unique identifier of the filter result
   * @return filterResultId
  **/
  @ApiModelProperty(required = true, value = "unique identifier of the filter result")
  @NotNull

  @Valid

  public UUID getFilterResultId() {
    return filterResultId;
  }

  public void setFilterResultId(UUID filterResultId) {
    this.filterResultId = filterResultId;
  }

  public FilterResult filterCode(String filterCode) {
    this.filterCode = filterCode;
    return this;
  }

  /**
   * filter code with result of statements tagged.
   * @return filterCode
  **/
  @ApiModelProperty(value = "filter code with result of statements tagged.")


  public String getFilterCode() {
    return filterCode;
  }

  public void setFilterCode(String filterCode) {
    this.filterCode = filterCode;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FilterResult filterResult = (FilterResult) o;
    return Objects.equals(this.screenerId, filterResult.screenerId) &&
        Objects.equals(this.executionId, filterResult.executionId) &&
        Objects.equals(this.conditionResultId, filterResult.conditionResultId) &&
        Objects.equals(this.filterResultId, filterResult.filterResultId) &&
        Objects.equals(this.filterCode, filterResult.filterCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(screenerId, executionId, conditionResultId, filterResultId, filterCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FilterResult {\n");
    
    sb.append("    screenerId: ").append(toIndentedString(screenerId)).append("\n");
    sb.append("    executionId: ").append(toIndentedString(executionId)).append("\n");
    sb.append("    conditionResultId: ").append(toIndentedString(conditionResultId)).append("\n");
    sb.append("    filterResultId: ").append(toIndentedString(filterResultId)).append("\n");
    sb.append("    filterCode: ").append(toIndentedString(filterCode)).append("\n");
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

