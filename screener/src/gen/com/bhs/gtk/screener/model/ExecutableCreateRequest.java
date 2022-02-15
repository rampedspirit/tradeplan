package com.bhs.gtk.screener.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;
import org.threeten.bp.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * ExecutableCreateRequest
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-01-05T19:25:05.070620300+05:30[Asia/Calcutta]")

public class ExecutableCreateRequest {
	@JsonProperty("marketTime")
	private OffsetDateTime marketTime = null;

	@JsonProperty("note")
	private String note = null;

	@JsonProperty("scripNames")
	@Valid
	private List<String> scripNames = null;

	public ExecutableCreateRequest marketTime(OffsetDateTime marketTime) {
		this.marketTime = marketTime;
		return this;
	}

	/**
	 * market time at which screener to be executed.
	 * 
	 * @return marketTime
	 **/
	@ApiModelProperty(required = true, value = "market time at which screener to be executed.")
	@NotNull

	@Valid

	public OffsetDateTime getMarketTime() {
		return marketTime;
	}

	public void setMarketTime(OffsetDateTime marketTime) {
		this.marketTime = marketTime;
	}

	public ExecutableCreateRequest note(String note) {
		this.note = note;
		return this;
	}

	/**
	 * note related to execution
	 * 
	 * @return note
	 **/
	@ApiModelProperty(value = "note related to execution")

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public ExecutableCreateRequest scripNames(List<String> scripNames) {
		this.scripNames = scripNames;
		return this;
	}

	public ExecutableCreateRequest addScripNamesItem(String scripNamesItem) {
		if (this.scripNames == null) {
			this.scripNames = new ArrayList<String>();
		}
		this.scripNames.add(scripNamesItem);
		return this;
	}

	/**
	 * scrip names.
	 * 
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
		ExecutableCreateRequest executableCreateRequest = (ExecutableCreateRequest) o;
		return Objects.equals(this.marketTime, executableCreateRequest.marketTime)
				&& Objects.equals(this.note, executableCreateRequest.note)
				&& Objects.equals(this.scripNames, executableCreateRequest.scripNames);
	}

	@Override
	public int hashCode() {
		return Objects.hash(marketTime, note, scripNames);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ExecutableCreateRequest {\n");

		sb.append("    marketTime: ").append(toIndentedString(marketTime)).append("\n");
		sb.append("    note: ").append(toIndentedString(note)).append("\n");
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
