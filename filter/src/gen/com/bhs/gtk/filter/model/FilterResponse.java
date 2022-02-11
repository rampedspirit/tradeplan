package com.bhs.gtk.filter.model;

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
 * FilterResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-02-06T11:02:32.848233600+05:30[Asia/Calcutta]")

public class FilterResponse   {
  @JsonProperty("id")
  private UUID id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("code")
  private String code = null;

  public FilterResponse id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * unique identifier of the filter. It is auto-generated by system, API-client need not provide it during creation of screener. If provided the value will be ignored.
   * @return id
  **/
  @ApiModelProperty(value = "unique identifier of the filter. It is auto-generated by system, API-client need not provide it during creation of screener. If provided the value will be ignored.")

  @Valid

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public FilterResponse name(String name) {
    this.name = name;
    return this;
  }

  /**
   * name of filter that is unqiue across the application
   * @return name
  **/
  @ApiModelProperty(value = "name of filter that is unqiue across the application")


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public FilterResponse description(String description) {
    this.description = description;
    return this;
  }

  /**
   * description of filter
   * @return description
  **/
  @ApiModelProperty(value = "description of filter")


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public FilterResponse code(String code) {
    this.code = code;
    return this;
  }

  /**
   * text representing filter code
   * @return code
  **/
  @ApiModelProperty(value = "text representing filter code")


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FilterResponse filterResponse = (FilterResponse) o;
    return Objects.equals(this.id, filterResponse.id) &&
        Objects.equals(this.name, filterResponse.name) &&
        Objects.equals(this.description, filterResponse.description) &&
        Objects.equals(this.code, filterResponse.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, code);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FilterResponse {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
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

