package com.bhs.gtk.condition.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ConditionRequest
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-01-23T17:42:05.341043500+05:30[Asia/Calcutta]")

public class ConditionRequest   {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("parseTree")
  private String parseTree = null;

  public ConditionRequest name(String name) {
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

  public ConditionRequest description(String description) {
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

  public ConditionRequest code(String code) {
    this.code = code;
    return this;
  }

  /**
   * text representing condition code
   * @return code
  **/
  @ApiModelProperty(required = true, value = "text representing condition code")
  @NotNull


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public ConditionRequest parseTree(String parseTree) {
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
    ConditionRequest conditionRequest = (ConditionRequest) o;
    return Objects.equals(this.name, conditionRequest.name) &&
        Objects.equals(this.description, conditionRequest.description) &&
        Objects.equals(this.code, conditionRequest.code) &&
        Objects.equals(this.parseTree, conditionRequest.parseTree);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, code, parseTree);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConditionRequest {\n");
    
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

