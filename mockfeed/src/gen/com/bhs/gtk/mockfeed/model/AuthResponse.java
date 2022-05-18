package com.bhs.gtk.mockfeed.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.threeten.bp.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * AuthResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-18T16:57:42.881383100+05:30[Asia/Calcutta]")

public class AuthResponse   {
  @JsonProperty("success")
  private Boolean success = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("segments")
  @Valid
  private List<String> segments = new ArrayList<String>();

  @JsonProperty("maxsymbols")
  private Integer maxsymbols = null;

  /**
   * Gets or Sets subscription
   */
  public enum SubscriptionEnum {
    TICK("tick"),
    
    _1MIN("1min"),
    
    TICK_1MIN("tick+1min"),
    
    TICK_5MIN("tick+5min"),
    
    TICK_1MIN_5MIN("tick+1min+5min");

    private String value;

    SubscriptionEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static SubscriptionEnum fromValue(String text) {
      for (SubscriptionEnum b : SubscriptionEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("subscription")
  private SubscriptionEnum subscription = null;

  @JsonProperty("validity")
  private OffsetDateTime validity = null;

  public AuthResponse success(Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public Boolean isSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public AuthResponse message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public AuthResponse segments(List<String> segments) {
    this.segments = segments;
    return this;
  }

  public AuthResponse addSegmentsItem(String segmentsItem) {
    this.segments.add(segmentsItem);
    return this;
  }

  /**
   * Get segments
   * @return segments
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public List<String> getSegments() {
    return segments;
  }

  public void setSegments(List<String> segments) {
    this.segments = segments;
  }

  public AuthResponse maxsymbols(Integer maxsymbols) {
    this.maxsymbols = maxsymbols;
    return this;
  }

  /**
   * Get maxsymbols
   * @return maxsymbols
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public Integer getMaxsymbols() {
    return maxsymbols;
  }

  public void setMaxsymbols(Integer maxsymbols) {
    this.maxsymbols = maxsymbols;
  }

  public AuthResponse subscription(SubscriptionEnum subscription) {
    this.subscription = subscription;
    return this;
  }

  /**
   * Get subscription
   * @return subscription
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public SubscriptionEnum getSubscription() {
    return subscription;
  }

  public void setSubscription(SubscriptionEnum subscription) {
    this.subscription = subscription;
  }

  public AuthResponse validity(OffsetDateTime validity) {
    this.validity = validity;
    return this;
  }

  /**
   * Get validity
   * @return validity
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public OffsetDateTime getValidity() {
    return validity;
  }

  public void setValidity(OffsetDateTime validity) {
    this.validity = validity;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuthResponse authResponse = (AuthResponse) o;
    return Objects.equals(this.success, authResponse.success) &&
        Objects.equals(this.message, authResponse.message) &&
        Objects.equals(this.segments, authResponse.segments) &&
        Objects.equals(this.maxsymbols, authResponse.maxsymbols) &&
        Objects.equals(this.subscription, authResponse.subscription) &&
        Objects.equals(this.validity, authResponse.validity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, message, segments, maxsymbols, subscription, validity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuthResponse {\n");
    
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    segments: ").append(toIndentedString(segments)).append("\n");
    sb.append("    maxsymbols: ").append(toIndentedString(maxsymbols)).append("\n");
    sb.append("    subscription: ").append(toIndentedString(subscription)).append("\n");
    sb.append("    validity: ").append(toIndentedString(validity)).append("\n");
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

