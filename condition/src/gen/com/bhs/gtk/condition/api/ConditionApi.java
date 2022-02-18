/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.0).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.bhs.gtk.condition.api;

import com.bhs.gtk.condition.model.ConditionDetailedResponse;
import com.bhs.gtk.condition.model.ConditionRequest;
import com.bhs.gtk.condition.model.ConditionResponse;
import com.bhs.gtk.condition.model.Error;
import com.bhs.gtk.condition.model.PatchData;
import java.util.UUID;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-02-18T13:27:13.752501500+05:30[Asia/Calcutta]")

@Api(value = "Condition", description = "the Condition API")
public interface ConditionApi {

    @ApiOperation(value = "create new condition", nickname = "createCondition", notes = "create new condition", response = ConditionDetailedResponse.class, tags={ "condition", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Condition created successfully.", response = ConditionDetailedResponse.class),
        @ApiResponse(code = 400, message = "Request is not understood.", response = Error.class) })
    @RequestMapping(value = "/v1/condition",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<ConditionDetailedResponse> createCondition(@ApiParam(value = "payload to create condition" ,required=true )  @Valid @RequestBody ConditionRequest body);


    @ApiOperation(value = "delete", nickname = "deleteCondition", notes = "delete condition of given id", response = ConditionDetailedResponse.class, tags={ "condition", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "condition with give id is deleted successfully.", response = ConditionDetailedResponse.class),
        @ApiResponse(code = 404, message = "Requested condition not found.", response = Error.class) })
    @RequestMapping(value = "/v1/condition/{id}",
        produces = { "application/json" }, 
        method = RequestMethod.DELETE)
    ResponseEntity<ConditionDetailedResponse> deleteCondition(@ApiParam(value = "",required=true) @PathVariable("id") UUID id);


    @ApiOperation(value = "get all conditions", nickname = "getAllConditions", notes = "get conditions", response = ConditionResponse.class, responseContainer = "List", tags={ "condition", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Request sucessfully processed.", response = ConditionResponse.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Request is not understood.", response = Error.class) })
    @RequestMapping(value = "/v1/condition",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<ConditionResponse>> getAllConditions();


    @ApiOperation(value = "get condition of given id", nickname = "getCondition", notes = "get condition", response = ConditionDetailedResponse.class, tags={ "condition", })
    @ApiResponses(value = { 
        @ApiResponse(code = 206, message = "Request sucessfully processed.", response = ConditionDetailedResponse.class),
        @ApiResponse(code = 400, message = "Request is not understood.", response = Error.class),
        @ApiResponse(code = 404, message = "Requested condition not found.", response = Error.class) })
    @RequestMapping(value = "/v1/condition/{id}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<ConditionDetailedResponse> getCondition(@ApiParam(value = "",required=true) @PathVariable("id") UUID id);


    @ApiOperation(value = "update condition", nickname = "updateCondition", notes = "update condition of given id", response = ConditionDetailedResponse.class, tags={ "condition", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "condition with give id is updated successfully.", response = ConditionDetailedResponse.class),
        @ApiResponse(code = 400, message = "Request is not understood.", response = Error.class),
        @ApiResponse(code = 404, message = "Requested screener not found.", response = Error.class) })
    @RequestMapping(value = "/v1/condition/{id}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PATCH)
    ResponseEntity<ConditionDetailedResponse> updateCondition(@ApiParam(value = "payload to create Condition" ,required=true )  @Valid @RequestBody List<PatchData> body,@ApiParam(value = "",required=true) @PathVariable("id") UUID id);

}
