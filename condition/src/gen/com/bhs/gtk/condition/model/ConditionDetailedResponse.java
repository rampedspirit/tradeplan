package com.bhs.gtk.condition.model;

import java.util.Objects;
import com.bhs.gtk.condition.model.Filter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ConditionDetailedResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-01-27T06:56:31.566079200+05:30[Asia/Calcutta]")

public class ConditionDetailedResponse   {
  @JsonProperty("id")
  private UUID id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("filters")
  @Valid
  private List<Filter> filters = null;

  public ConditionDetailedResponse id(UUID id) {
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

  public ConditionDetailedResponse name(String name) {
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

  public ConditionDetailedResponse description(String description) {
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

  public ConditionDetailedResponse code(String code) {
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

  public ConditionDetailedResponse filters(List<Filter> filters) {
    this.filters = filters;
    return this;
  }

  public ConditionDetailedResponse addFiltersItem(Filter filtersItem) {
    if (this.filters == null) {
      this.filters = new ArrayList<Filter>();
    }
    this.filters.add(filtersItem);
    return this;
  }

  /**
   * associated filters.
   * @return filters
  **/
  @ApiModelProperty(value = "associated filters.")

  @Valid

  public List<Filter> getFilters() {
    return filters;
  }

  public void setFilters(List<Filter> filters) {
    this.filters = filters;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConditionDetailedResponse conditionDetailedResponse = (ConditionDetailedResponse) o;
    return Objects.equals(this.id, conditionDetailedResponse.id) &&
        Objects.equals(this.name, conditionDetailedResponse.name) &&
        Objects.equals(this.description, conditionDetailedResponse.description) &&
        Objects.equals(this.code, conditionDetailedResponse.code) &&
        Objects.equals(this.filters, conditionDetailedResponse.filters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, code, filters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConditionDetailedResponse {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    filters: ").append(toIndentedString(filters)).append("\n");
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

