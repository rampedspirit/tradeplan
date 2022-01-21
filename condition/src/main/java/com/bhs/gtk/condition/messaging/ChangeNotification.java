package com.bhs.gtk.condition.messaging;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Component
public class ChangeNotification {
	private UUID id;
	private String status;
	
	
	protected ChangeNotification() {}
	
	public ChangeNotification(UUID id, ChangeStatusEnum status) {
		this.id = id;
		this.status = status.name();
	}
	
	  /**
	   * Gets or Sets status
	   */
	public enum ChangeStatusEnum {
		UPDATED("UPDATED"),
		DELETED("DELETED");

		private String value;

		ChangeStatusEnum(String value) {
			this.value = value;
		}

		@Override
		@JsonValue
		public String toString() {
			return String.valueOf(value);
		}

		@JsonCreator
		public static ChangeStatusEnum fromValue(String text) {
			for (ChangeStatusEnum b : ChangeStatusEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

}
