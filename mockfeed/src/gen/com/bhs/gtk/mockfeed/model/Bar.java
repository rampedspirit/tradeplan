package com.bhs.gtk.mockfeed.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.threeten.bp.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Bar
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-18T16:57:42.881383100+05:30[Asia/Calcutta]")

public class Bar   {
  @JsonProperty("time")
  private OffsetDateTime time = null;

  @JsonProperty("open")
  private Float open = null;

  @JsonProperty("high")
  private Float high = null;

  @JsonProperty("low")
  private Float low = null;

  @JsonProperty("close")
  private Float close = null;

  @JsonProperty("volume")
  private Float volume = null;

  @JsonProperty("oi")
  private Float oi = null;

  public Bar time(OffsetDateTime time) {
    this.time = time;
    return this;
  }

  /**
   * Get time
   * @return time
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public OffsetDateTime getTime() {
    return time;
  }

  public void setTime(OffsetDateTime time) {
    this.time = time;
  }

  public Bar open(Float open) {
    this.open = open;
    return this;
  }

  /**
   * Get open
   * @return open
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public Float getOpen() {
    return open;
  }

  public void setOpen(Float open) {
    this.open = open;
  }

  public Bar high(Float high) {
    this.high = high;
    return this;
  }

  /**
   * Get high
   * @return high
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public Float getHigh() {
    return high;
  }

  public void setHigh(Float high) {
    this.high = high;
  }

  public Bar low(Float low) {
    this.low = low;
    return this;
  }

  /**
   * Get low
   * @return low
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public Float getLow() {
    return low;
  }

  public void setLow(Float low) {
    this.low = low;
  }

  public Bar close(Float close) {
    this.close = close;
    return this;
  }

  /**
   * Get close
   * @return close
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public Float getClose() {
    return close;
  }

  public void setClose(Float close) {
    this.close = close;
  }

  public Bar volume(Float volume) {
    this.volume = volume;
    return this;
  }

  /**
   * Get volume
   * @return volume
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public Float getVolume() {
    return volume;
  }

  public void setVolume(Float volume) {
    this.volume = volume;
  }

  public Bar oi(Float oi) {
    this.oi = oi;
    return this;
  }

  /**
   * Get oi
   * @return oi
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public Float getOi() {
    return oi;
  }

  public void setOi(Float oi) {
    this.oi = oi;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Bar bar = (Bar) o;
    return Objects.equals(this.time, bar.time) &&
        Objects.equals(this.open, bar.open) &&
        Objects.equals(this.high, bar.high) &&
        Objects.equals(this.low, bar.low) &&
        Objects.equals(this.close, bar.close) &&
        Objects.equals(this.volume, bar.volume) &&
        Objects.equals(this.oi, bar.oi);
  }

  @Override
  public int hashCode() {
    return Objects.hash(time, open, high, low, close, volume, oi);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Bar {\n");
    
    sb.append("    time: ").append(toIndentedString(time)).append("\n");
    sb.append("    open: ").append(toIndentedString(open)).append("\n");
    sb.append("    high: ").append(toIndentedString(high)).append("\n");
    sb.append("    low: ").append(toIndentedString(low)).append("\n");
    sb.append("    close: ").append(toIndentedString(close)).append("\n");
    sb.append("    volume: ").append(toIndentedString(volume)).append("\n");
    sb.append("    oi: ").append(toIndentedString(oi)).append("\n");
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

