package com.bhs.gtk.filter.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ExpressionType {
	
    COMPARE_EXPRESSION("COMPARE_EXPRESSION"),
    ARITHEMETIC_EXPRESSION ("ARITHEMETIC_EXPRESSION");

    private String value;

    ExpressionType(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ExpressionType fromValue(String text) {
      for (ExpressionType b : ExpressionType.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

}
