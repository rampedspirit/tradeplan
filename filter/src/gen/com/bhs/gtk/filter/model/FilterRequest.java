package com.bhs.gtk.filter.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * FilterRequest
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-01-24T11:56:26.914547600+05:30[Asia/Calcutta]")

public class FilterRequest   {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("parseTree")
  private String parseTree = null;

  public FilterRequest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * name of filter that is unqiue across the application
   * @return name
  **/
  @ApiModelProperty(required = true, value = "name of filter that is unqiue across the application")
  @NotNull


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public FilterRequest description(String description) {
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

  public FilterRequest code(String code) {
    this.code = code;
    return this;
  }

  /**
   * text representing filter code
   * @return code
  **/
  @ApiModelProperty(required = true, value = "text representing filter code")
  @NotNull


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public FilterRequest parseTree(String parseTree) {
    this.parseTree = parseTree;
    return this;
  }

  /**
   * output after parsing grammar
   * @return parseTree
  **/
  @ApiModelProperty(required = true, value = "output after parsing grammar")
  @NotNull


  public String getParseTree() {
    return parseTree;
  }

  public void setParseTree(String parseTree) {
    this.parseTree = parseTree;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FilterRequest filterRequest = (FilterRequest) o;
    return Objects.equals(this.name, filterRequest.name) &&
        Objects.equals(this.description, filterRequest.description) &&
        Objects.equals(this.code, filterRequest.code) &&
        Objects.equals(this.parseTree, filterRequest.parseTree);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, code, parseTree);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FilterRequest {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    parseTree: ").append(toIndentedString(parseTree)).append("\n");
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

