package com.bhs.gtk.filter.model;

import java.util.Objects;
import com.bhs.gtk.filter.model.ExpressionResultResponse;
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
 * FilterResultResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-03-03T22:39:08.045949100+05:30[Asia/Calcutta]")

public class FilterResultResponse   {
  @JsonProperty("filterId")
  private UUID filterId = null;

  @JsonProperty("marketTime")
  private OffsetDateTime marketTime = null;

  @JsonProperty("scripName")
  private String scripName = null;

  /**
   * Gets or Sets filterResult
   */
  public enum FilterResultEnum {
    QUEUED("QUEUED"),
    
    RUNNING("RUNNING"),
    
    PASS("PASS"),
    
    FAIL("FAIL"),
    
    ERROR("ERROR");

    private String value;

    FilterResultEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static FilterResultEnum fromValue(String text) {
      for (FilterResultEnum b : FilterResultEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("filterResult")
  private FilterResultEnum filterResult = null;

  @JsonProperty("expressionResults")
  @Valid
  private List<ExpressionResultResponse> expressionResults = null;

  public FilterResultResponse filterId(UUID filterId) {
    this.filterId = filterId;
    return this;
  }

  /**
   * Get filterId
   * @return filterId
  **/
  @ApiModelProperty(value = "")

  @Valid

  public UUID getFilterId() {
    return filterId;
  }

  public void setFilterId(UUID filterId) {
    this.filterId = filterId;
  }

  public FilterResultResponse marketTime(OffsetDateTime marketTime) {
    this.marketTime = marketTime;
    return this;
  }

  /**
   * Get marketTime
   * @return marketTime
  **/
  @ApiModelProperty(value = "")

  @Valid

  public OffsetDateTime getMarketTime() {
    return marketTime;
  }

  public void setMarketTime(OffsetDateTime marketTime) {
    this.marketTime = marketTime;
  }

  public FilterResultResponse scripName(String scripName) {
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

  public FilterResultResponse filterResult(FilterResultEnum filterResult) {
    this.filterResult = filterResult;
    return this;
  }

  /**
   * Get filterResult
   * @return filterResult
  **/
  @ApiModelProperty(value = "")


  public FilterResultEnum getFilterResult() {
    return filterResult;
  }

  public void setFilterResult(FilterResultEnum filterResult) {
    this.filterResult = filterResult;
  }

  public FilterResultResponse expressionResults(List<ExpressionResultResponse> expressionResults) {
    this.expressionResults = expressionResults;
    return this;
  }

  public FilterResultResponse addExpressionResultsItem(ExpressionResultResponse expressionResultsItem) {
    if (this.expressionResults == null) {
      this.expressionResults = new ArrayList<ExpressionResultResponse>();
    }
    this.expressionResults.add(expressionResultsItem);
    return this;
  }

  /**
   * Get expressionResults
   * @return expressionResults
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<ExpressionResultResponse> getExpressionResults() {
    return expressionResults;
  }

  public void setExpressionResults(List<ExpressionResultResponse> expressionResults) {
    this.expressionResults = expressionResults;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FilterResultResponse filterResultResponse = (FilterResultResponse) o;
    return Objects.equals(this.filterId, filterResultResponse.filterId) &&
        Objects.equals(this.marketTime, filterResultResponse.marketTime) &&
        Objects.equals(this.scripName, filterResultResponse.scripName) &&
        Objects.equals(this.filterResult, filterResultResponse.filterResult) &&
        Objects.equals(this.expressionResults, filterResultResponse.expressionResults);
  }

  @Override
  public int hashCode() {
    return Objects.hash(filterId, marketTime, scripName, filterResult, expressionResults);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FilterResultResponse {\n");
    
    sb.append("    filterId: ").append(toIndentedString(filterId)).append("\n");
    sb.append("    marketTime: ").append(toIndentedString(marketTime)).append("\n");
    sb.append("    scripName: ").append(toIndentedString(scripName)).append("\n");
    sb.append("    filterResult: ").append(toIndentedString(filterResult)).append("\n");
    sb.append("    expressionResults: ").append(toIndentedString(expressionResults)).append("\n");
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

