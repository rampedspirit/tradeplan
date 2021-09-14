package com.bhs.gtk.condition.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * PatchData
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-09-13T22:14:17.965791400+05:30[Asia/Calcutta]")

public class PatchData   {
  /**
   * Action to be performed on given resource. 
   */
  public enum OperationEnum {
    REPLACE("REPLACE");

    private String value;

    OperationEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static OperationEnum fromValue(String text) {
      for (OperationEnum b : OperationEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("operation")
  private OperationEnum operation = null;

  /**
   * The property of the json attribute to be replaced.
   */
  public enum PropertyEnum {
    NAME("NAME"),
    
    DESCRIPTION("DESCRIPTION"),
    
    CODE("CODE"),
    
    PARSE_TREE("PARSE_TREE");

    private String value;

    PropertyEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static PropertyEnum fromValue(String text) {
      for (PropertyEnum b : PropertyEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("property")
  private PropertyEnum property = null;

  @JsonProperty("value")
  private String value = null;

  public PatchData operation(OperationEnum operation) {
    this.operation = operation;
    return this;
  }

  /**
   * Action to be performed on given resource. 
   * @return operation
  **/
  @ApiModelProperty(example = "REPLACE", required = true, value = "Action to be performed on given resource. ")
  @NotNull


  public OperationEnum getOperation() {
    return operation;
  }

  public void setOperation(OperationEnum operation) {
    this.operation = operation;
  }

  public PatchData property(PropertyEnum property) {
    this.property = property;
    return this;
  }

  /**
   * The property of the json attribute to be replaced.
   * @return property
  **/
  @ApiModelProperty(example = "NAME", required = true, value = "The property of the json attribute to be replaced.")
  @NotNull


  public PropertyEnum getProperty() {
    return property;
  }

  public void setProperty(PropertyEnum property) {
    this.property = property;
  }

  public PatchData value(String value) {
    this.value = value;
    return this;
  }

  /**
   * The new value of the json attribute to be patched.
   * @return value
  **/
  @ApiModelProperty(required = true, value = "The new value of the json attribute to be patched.")
  @NotNull


  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PatchData patchData = (PatchData) o;
    return Objects.equals(this.operation, patchData.operation) &&
        Objects.equals(this.property, patchData.property) &&
        Objects.equals(this.value, patchData.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(operation, property, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PatchData {\n");
    
    sb.append("    operation: ").append(toIndentedString(operation)).append("\n");
    sb.append("    property: ").append(toIndentedString(property)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

