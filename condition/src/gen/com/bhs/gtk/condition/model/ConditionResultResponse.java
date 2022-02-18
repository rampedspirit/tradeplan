package com.bhs.gtk.condition.model;

import java.util.Objects;
import com.bhs.gtk.condition.model.FilterResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * ConditionResultResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-02-18T13:27:13.752501500+05:30[Asia/Calcutta]")

public class ConditionResultResponse   {
  @JsonProperty("id")
  private UUID id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("marketTime")
  private OffsetDateTime marketTime = null;

  @JsonProperty("scripName")
  private String scripName = null;

  /**
   * Gets or Sets conditionResult
   */
  public enum ConditionResultEnum {
    QUEUED("QUEUED"),
    
    RUNNING("RUNNING"),
    
    PASS("PASS"),
    
    FAIL("FAIL"),
    
    ERROR("ERROR");

    private String value;

    ConditionResultEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ConditionResultEnum fromValue(String text) {
      for (ConditionResultEnum b : ConditionResultEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("conditionResult")
  private ConditionResultEnum conditionResult = null;

  @JsonProperty("filtersResult")
  @Valid
  private List<FilterResult> filtersResult = null;

  public ConditionResultResponse id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * unique identifier of the condition.
   * @return id
  **/
  @ApiModelProperty(required = true, value = "unique identifier of the condition.")
  @NotNull

  @Valid

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public ConditionResultResponse name(String name) {
    this.name = name;
    return this;
  }

  /**
   * name of condition
   * @return name
  **/
  @ApiModelProperty(required = true, value = "name of condition")
  @NotNull


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ConditionResultResponse description(String description) {
    this.description = description;
    return this;
  }

  /**
   * description of condition
   * @return description
  **/
  @ApiModelProperty(value = "description of condition")


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ConditionResultResponse code(String code) {
    this.code = code;
    return this;
  }

  /**
   * text representing condition code
   * @return code
  **/
  @ApiModelProperty(value = "text representing condition code")


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public ConditionResultResponse marketTime(OffsetDateTime marketTime) {
    this.marketTime = marketTime;
    return this;
  }

  /**
   * market time at which screener to be executed.
   * @return marketTime
  **/
  @ApiModelProperty(value = "market time at which screener to be executed.")

  @Valid

  public OffsetDateTime getMarketTime() {
    return marketTime;
  }

  public void setMarketTime(OffsetDateTime marketTime) {
    this.marketTime = marketTime;
  }

  public ConditionResultResponse scripName(String scripName) {
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

  public ConditionResultResponse conditionResult(ConditionResultEnum conditionResult) {
    this.conditionResult = conditionResult;
    return this;
  }

  /**
   * Get conditionResult
   * @return conditionResult
  **/
  @ApiModelProperty(value = "")


  public ConditionResultEnum getConditionResult() {
    return conditionResult;
  }

  public void setConditionResult(ConditionResultEnum conditionResult) {
    this.conditionResult = conditionResult;
  }

  public ConditionResultResponse filtersResult(List<FilterResult> filtersResult) {
    this.filtersResult = filtersResult;
    return this;
  }

  public ConditionResultResponse addFiltersResultItem(FilterResult filtersResultItem) {
    if (this.filtersResult == null) {
      this.filtersResult = new ArrayList<FilterResult>();
    }
    this.filtersResult.add(filtersResultItem);
    return this;
  }

  /**
   * associated filters.
   * @return filtersResult
  **/
  @ApiModelProperty(value = "associated filters.")

  @Valid

  public List<FilterResult> getFiltersResult() {
    return filtersResult;
  }

  public void setFiltersResult(List<FilterResult> filtersResult) {
    this.filtersResult = filtersResult;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConditionResultResponse conditionResultResponse = (ConditionResultResponse) o;
    return Objects.equals(this.id, conditionResultResponse.id) &&
        Objects.equals(this.name, conditionResultResponse.name) &&
        Objects.equals(this.description, conditionResultResponse.description) &&
        Objects.equals(this.code, conditionResultResponse.code) &&
        Objects.equals(this.marketTime, conditionResultResponse.marketTime) &&
        Objects.equals(this.scripName, conditionResultResponse.scripName) &&
        Objects.equals(this.conditionResult, conditionResultResponse.conditionResult) &&
        Objects.equals(this.filtersResult, conditionResultResponse.filtersResult);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, code, marketTime, scripName, conditionResult, filtersResult);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConditionResultResponse {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    marketTime: ").append(toIndentedString(marketTime)).append("\n");
    sb.append("    scripName: ").append(toIndentedString(scripName)).append("\n");
    sb.append("    conditionResult: ").append(toIndentedString(conditionResult)).append("\n");
    sb.append("    filtersResult: ").append(toIndentedString(filtersResult)).append("\n");
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

