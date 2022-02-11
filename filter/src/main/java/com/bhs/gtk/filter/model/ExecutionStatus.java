package com.bhs.gtk.filter.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ExecutionStatus {
	
    QUEUED("QUEUED"),
    
    RUNNING("RUNNING"),
    
    PASS("PASS"),
    
    FAIL("FAIL"),
    
    ERROR("ERROR");

    private String value;

    ExecutionStatus(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ExecutionStatus fromValue(String text) {
      for (ExecutionStatus b : ExecutionStatus.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

}
