package com.bhs.gtk.screener.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ConditionResult
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-09-25T06:41:13.746385900+05:30[Asia/Calcutta]")

public class ConditionResult   {
  @JsonProperty("screenerId")
  private UUID screenerId = null;

  @JsonProperty("executionId")
  private UUID executionId = null;

  @JsonProperty("conditionResultId")
  private UUID conditionResultId = null;

  @JsonProperty("scripName")
  private String scripName = null;

  /**
   * market time at which screener to be executed.
   */
  public enum ResultEnum {
    PASS("PASS"),
    
    FAIL("FAIL"),
    
    EVALUATING("EVALUATING"),
    
    INVALID("INVALID");

    private String value;

    ResultEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ResultEnum fromValue(String text) {
      for (ResultEnum b : ResultEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("result")
  private ResultEnum result = null;

  @JsonProperty("conditionCode")
  private String conditionCode = null;

  public ConditionResult screenerId(UUID screenerId) {
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

  public ConditionResult executionId(UUID executionId) {
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

  public ConditionResult conditionResultId(UUID conditionResultId) {
    this.conditionResultId = conditionResultId;
    return this;
  }

  /**
   * condition result  identifier.
   * @return conditionResultId
  **/
  @ApiModelProperty(required = true, value = "condition result  identifier.")
  @NotNull

  @Valid

  public UUID getConditionResultId() {
    return conditionResultId;
  }

  public void setConditionResultId(UUID conditionResultId) {
    this.conditionResultId = conditionResultId;
  }

  public ConditionResult scripName(String scripName) {
    this.scripName = scripName;
    return this;
  }

  /**
   * name of scrip.
   * @return scripName
  **/
  @ApiModelProperty(value = "name of scrip.")


  public String getScripName() {
    return scripName;
  }

  public void setScripName(String scripName) {
    this.scripName = scripName;
  }

  public ConditionResult result(ResultEnum result) {
    this.result = result;
    return this;
  }

  /**
   * market time at which screener to be executed.
   * @return result
  **/
  @ApiModelProperty(value = "market time at which screener to be executed.")


  public ResultEnum getResult() {
    return result;
  }

  public void setResult(ResultEnum result) {
    this.result = result;
  }

  public ConditionResult conditionCode(String conditionCode) {
    this.conditionCode = conditionCode;
    return this;
  }

  /**
   * condition code with filter details tagged.
   * @return conditionCode
  **/
  @ApiModelProperty(value = "condition code with filter details tagged.")


  public String getConditionCode() {
    return conditionCode;
  }

  public void setConditionCode(String conditionCode) {
    this.conditionCode = conditionCode;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConditionResult conditionResult = (ConditionResult) o;
    return Objects.equals(this.screenerId, conditionResult.screenerId) &&
        Objects.equals(this.executionId, conditionResult.executionId) &&
        Objects.equals(this.conditionResultId, conditionResult.conditionResultId) &&
        Objects.equals(this.scripName, conditionResult.scripName) &&
        Objects.equals(this.result, conditionResult.result) &&
        Objects.equals(this.conditionCode, conditionResult.conditionCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(screenerId, executionId, conditionResultId, scripName, result, conditionCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConditionResult {\n");
    
    sb.append("    screenerId: ").append(toIndentedString(screenerId)).append("\n");
    sb.append("    executionId: ").append(toIndentedString(executionId)).append("\n");
    sb.append("    conditionResultId: ").append(toIndentedString(conditionResultId)).append("\n");
    sb.append("    scripName: ").append(toIndentedString(scripName)).append("\n");
    sb.append("    result: ").append(toIndentedString(result)).append("\n");
    sb.append("    conditionCode: ").append(toIndentedString(conditionCode)).append("\n");
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

