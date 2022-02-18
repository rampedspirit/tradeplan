package com.bhs.gtk.condition.model;

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
 * ConditionResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-02-18T13:27:13.752501500+05:30[Asia/Calcutta]")

public class ConditionResponse   {
  @JsonProperty("id")
  private UUID id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

  public ConditionResponse id(UUID id) {
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

  public ConditionResponse name(String name) {
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

  public ConditionResponse description(String description) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConditionResponse conditionResponse = (ConditionResponse) o;
    return Objects.equals(this.id, conditionResponse.id) &&
        Objects.equals(this.name, conditionResponse.name) &&
        Objects.equals(this.description, conditionResponse.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConditionResponse {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

