package com.bhs.gtk.condition.model;

import java.util.Objects;
import com.bhs.gtk.condition.model.PatchData;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * All patch operations follow the json patch api standard. The patch object is provided by the user in the patch operation for the particular resource.
 */
@ApiModel(description = "All patch operations follow the json patch api standard. The patch object is provided by the user in the patch operation for the particular resource.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-09-24T15:34:38.557240300+05:30[Asia/Calcutta]")

public class PatchModel   {
  @JsonProperty("patchData")
  @Valid
  private List<PatchData> patchData = null;

  public PatchModel patchData(List<PatchData> patchData) {
    this.patchData = patchData;
    return this;
  }

  public PatchModel addPatchDataItem(PatchData patchDataItem) {
    if (this.patchData == null) {
      this.patchData = new ArrayList<PatchData>();
    }
    this.patchData.add(patchDataItem);
    return this;
  }

  /**
   * attributes to be updated.
   * @return patchData
  **/
  @ApiModelProperty(value = "attributes to be updated.")

  @Valid

  public List<PatchData> getPatchData() {
    return patchData;
  }

  public void setPatchData(List<PatchData> patchData) {
    this.patchData = patchData;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PatchModel patchModel = (PatchModel) o;
    return Objects.equals(this.patchData, patchModel.patchData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(patchData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PatchModel {\n");
    
    sb.append("    patchData: ").append(toIndentedString(patchData)).append("\n");
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

