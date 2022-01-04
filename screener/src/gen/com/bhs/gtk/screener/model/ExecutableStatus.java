package com.bhs.gtk.screener.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets executableStatus
 */
public enum ExecutableStatus {
  
  QUEUED("QUEUED"),
  
  RUNNING("RUNNING"),
  
  COMPLETED("COMPLETED");

  private String value;

  ExecutableStatus(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ExecutableStatus fromValue(String text) {
    for (ExecutableStatus b : ExecutableStatus.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

