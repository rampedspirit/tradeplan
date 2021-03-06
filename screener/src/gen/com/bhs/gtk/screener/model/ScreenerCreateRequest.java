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
 * ScreenerCreateRequest
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-03-07T11:51:10.308924500+05:30[Asia/Calcutta]")

public class ScreenerCreateRequest   {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("watchListId")
  private UUID watchListId = null;

  @JsonProperty("conditionId")
  private UUID conditionId = null;

  public ScreenerCreateRequest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * name of screener
   * @return name
  **/
  @ApiModelProperty(required = true, value = "name of screener")
  @NotNull


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ScreenerCreateRequest description(String description) {
    this.description = description;
    return this;
  }

  /**
   * description of screener
   * @return description
  **/
  @ApiModelProperty(value = "description of screener")


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ScreenerCreateRequest watchListId(UUID watchListId) {
    this.watchListId = watchListId;
    return this;
  }

  /**
   * watch list identifier
   * @return watchListId
  **/
  @ApiModelProperty(value = "watch list identifier")

  @Valid

  public UUID getWatchListId() {
    return watchListId;
  }

  public void setWatchListId(UUID watchListId) {
    this.watchListId = watchListId;
  }

  public ScreenerCreateRequest conditionId(UUID conditionId) {
    this.conditionId = conditionId;
    return this;
  }

  /**
   * condition identifier.
   * @return conditionId
  **/
  @ApiModelProperty(value = "condition identifier.")

  @Valid

  public UUID getConditionId() {
    return conditionId;
  }

  public void setConditionId(UUID conditionId) {
    this.conditionId = conditionId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScreenerCreateRequest screenerCreateRequest = (ScreenerCreateRequest) o;
    return Objects.equals(this.name, screenerCreateRequest.name) &&
        Objects.equals(this.description, screenerCreateRequest.description) &&
        Objects.equals(this.watchListId, screenerCreateRequest.watchListId) &&
        Objects.equals(this.conditionId, screenerCreateRequest.conditionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, watchListId, conditionId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScreenerCreateRequest {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    watchListId: ").append(toIndentedString(watchListId)).append("\n");
    sb.append("    conditionId: ").append(toIndentedString(conditionId)).append("\n");
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

