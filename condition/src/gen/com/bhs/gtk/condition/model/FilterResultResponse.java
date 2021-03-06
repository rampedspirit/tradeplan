package com.bhs.gtk.condition.model;

import java.util.Objects;
import com.bhs.gtk.condition.model.Location;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * FilterResultResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-03-03T22:03:47.873640300+05:30[Asia/Calcutta]")

public class FilterResultResponse   {
  @JsonProperty("filterId")
  private UUID filterId = null;

  @JsonProperty("location")
  @Valid
  private List<Location> location = new ArrayList<Location>();

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    QUEUED("QUEUED"),
    
    RUNNING("RUNNING"),
    
    PASS("PASS"),
    
    FAIL("FAIL"),
    
    ERROR("ERROR");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("status")
  private StatusEnum status = null;

  public FilterResultResponse filterId(UUID filterId) {
    this.filterId = filterId;
    return this;
  }

  /**
   * Get filterId
   * @return filterId
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public UUID getFilterId() {
    return filterId;
  }

  public void setFilterId(UUID filterId) {
    this.filterId = filterId;
  }

  public FilterResultResponse location(List<Location> location) {
    this.location = location;
    return this;
  }

  public FilterResultResponse addLocationItem(Location locationItem) {
    this.location.add(locationItem);
    return this;
  }

  /**
   * one filter is found in one or more locations in a condition.
   * @return location
  **/
  @ApiModelProperty(required = true, value = "one filter is found in one or more locations in a condition.")
  @NotNull

  @Valid

  public List<Location> getLocation() {
    return location;
  }

  public void setLocation(List<Location> location) {
    this.location = location;
  }

  public FilterResultResponse status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
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
        Objects.equals(this.location, filterResultResponse.location) &&
        Objects.equals(this.status, filterResultResponse.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(filterId, location, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FilterResultResponse {\n");
    
    sb.append("    filterId: ").append(toIndentedString(filterId)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

