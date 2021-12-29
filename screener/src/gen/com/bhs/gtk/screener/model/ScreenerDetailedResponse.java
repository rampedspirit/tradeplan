package com.bhs.gtk.screener.model;

import java.util.Objects;
import com.bhs.gtk.screener.model.ExecutableResponse;
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
 * ScreenerDetailedResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-12-28T23:38:24.439962500+05:30[Asia/Calcutta]")

public class ScreenerDetailedResponse   {
  @JsonProperty("screenerId")
  private UUID screenerId = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("watchListId")
  private UUID watchListId = null;

  @JsonProperty("conditionId")
  private UUID conditionId = null;

  @JsonProperty("executable")
  @Valid
  private List<ExecutableResponse> executable = null;

  public ScreenerDetailedResponse screenerId(UUID screenerId) {
    this.screenerId = screenerId;
    return this;
  }

  /**
   * screener identifier
   * @return screenerId
  **/
  @ApiModelProperty(value = "screener identifier")

  @Valid

  public UUID getScreenerId() {
    return screenerId;
  }

  public void setScreenerId(UUID screenerId) {
    this.screenerId = screenerId;
  }

  public ScreenerDetailedResponse name(String name) {
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

  public ScreenerDetailedResponse description(String description) {
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

  public ScreenerDetailedResponse watchListId(UUID watchListId) {
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

  public ScreenerDetailedResponse conditionId(UUID conditionId) {
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

  public ScreenerDetailedResponse executable(List<ExecutableResponse> executable) {
    this.executable = executable;
    return this;
  }

  public ScreenerDetailedResponse addExecutableItem(ExecutableResponse executableItem) {
    if (this.executable == null) {
      this.executable = new ArrayList<ExecutableResponse>();
    }
    this.executable.add(executableItem);
    return this;
  }

  /**
   * all execuatbles which are attached to the screener with given watchlist and condition
   * @return executable
  **/
  @ApiModelProperty(value = "all execuatbles which are attached to the screener with given watchlist and condition")

  @Valid

  public List<ExecutableResponse> getExecutable() {
    return executable;
  }

  public void setExecutable(List<ExecutableResponse> executable) {
    this.executable = executable;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScreenerDetailedResponse screenerDetailedResponse = (ScreenerDetailedResponse) o;
    return Objects.equals(this.screenerId, screenerDetailedResponse.screenerId) &&
        Objects.equals(this.name, screenerDetailedResponse.name) &&
        Objects.equals(this.description, screenerDetailedResponse.description) &&
        Objects.equals(this.watchListId, screenerDetailedResponse.watchListId) &&
        Objects.equals(this.conditionId, screenerDetailedResponse.conditionId) &&
        Objects.equals(this.executable, screenerDetailedResponse.executable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(screenerId, name, description, watchListId, conditionId, executable);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScreenerDetailedResponse {\n");
    
    sb.append("    screenerId: ").append(toIndentedString(screenerId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    watchListId: ").append(toIndentedString(watchListId)).append("\n");
    sb.append("    conditionId: ").append(toIndentedString(conditionId)).append("\n");
    sb.append("    executable: ").append(toIndentedString(executable)).append("\n");
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

