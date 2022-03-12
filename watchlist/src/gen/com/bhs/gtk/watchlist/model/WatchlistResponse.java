package com.bhs.gtk.watchlist.model;

import java.util.Objects;
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
 * WatchlistResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-03-11T13:49:03.033972200+05:30[Asia/Calcutta]")

public class WatchlistResponse   {
  @JsonProperty("watchlistId")
  private UUID watchlistId = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("scripNames")
  @Valid
  private List<String> scripNames = null;

  public WatchlistResponse watchlistId(UUID watchlistId) {
    this.watchlistId = watchlistId;
    return this;
  }

  /**
   * watchlist identifier
   * @return watchlistId
  **/
  @ApiModelProperty(value = "watchlist identifier")

  @Valid

  public UUID getWatchlistId() {
    return watchlistId;
  }

  public void setWatchlistId(UUID watchlistId) {
    this.watchlistId = watchlistId;
  }

  public WatchlistResponse name(String name) {
    this.name = name;
    return this;
  }

  /**
   * name of watchlist
   * @return name
  **/
  @ApiModelProperty(required = true, value = "name of watchlist")
  @NotNull


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public WatchlistResponse description(String description) {
    this.description = description;
    return this;
  }

  /**
   * description of watchlist
   * @return description
  **/
  @ApiModelProperty(value = "description of watchlist")


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public WatchlistResponse scripNames(List<String> scripNames) {
    this.scripNames = scripNames;
    return this;
  }

  public WatchlistResponse addScripNamesItem(String scripNamesItem) {
    if (this.scripNames == null) {
      this.scripNames = new ArrayList<String>();
    }
    this.scripNames.add(scripNamesItem);
    return this;
  }

  /**
   * scrip names.
   * @return scripNames
  **/
  @ApiModelProperty(value = "scrip names.")


  public List<String> getScripNames() {
    return scripNames;
  }

  public void setScripNames(List<String> scripNames) {
    this.scripNames = scripNames;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WatchlistResponse watchlistResponse = (WatchlistResponse) o;
    return Objects.equals(this.watchlistId, watchlistResponse.watchlistId) &&
        Objects.equals(this.name, watchlistResponse.name) &&
        Objects.equals(this.description, watchlistResponse.description) &&
        Objects.equals(this.scripNames, watchlistResponse.scripNames);
  }

  @Override
  public int hashCode() {
    return Objects.hash(watchlistId, name, description, scripNames);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WatchlistResponse {\n");
    
    sb.append("    watchlistId: ").append(toIndentedString(watchlistId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    scripNames: ").append(toIndentedString(scripNames)).append("\n");
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

