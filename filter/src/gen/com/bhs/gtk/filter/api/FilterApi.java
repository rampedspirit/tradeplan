/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.0).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.bhs.gtk.filter.api;

import com.bhs.gtk.filter.model.Error;
import com.bhs.gtk.filter.model.FilterRequest;
import com.bhs.gtk.filter.model.FilterResponse;
import com.bhs.gtk.filter.model.FilterResultResponse;
import com.bhs.gtk.filter.model.PatchData;
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
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-02-06T11:02:32.848233600+05:30[Asia/Calcutta]")

@Api(value = "Filter", description = "the Filter API")
public interface FilterApi {

    @ApiOperation(value = "create new filter", nickname = "createFilter", notes = "create new filter", response = FilterResponse.class, tags={ "filter", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Filter created successfully.", response = FilterResponse.class),
        @ApiResponse(code = 400, message = "Request is not understood.", response = Error.class) })
    @RequestMapping(value = "/v1/filter",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<FilterResponse> createFilter(@ApiParam(value = "payload to create filter" ,required=true )  @Valid @RequestBody FilterRequest body);


    @ApiOperation(value = "delete", nickname = "deleteFilter", notes = "delete filter of given id", response = FilterResponse.class, tags={ "filter", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "filter with give id is deleted successfully.", response = FilterResponse.class),
        @ApiResponse(code = 404, message = "Requested filter not found.", response = Error.class) })
    @RequestMapping(value = "/v1/filter/{id}",
        produces = { "application/json" }, 
        method = RequestMethod.DELETE)
    ResponseEntity<FilterResponse> deleteFilter(@ApiParam(value = "",required=true) @PathVariable("id") UUID id);


    @ApiOperation(value = "get all filter", nickname = "getAllFilters", notes = "get filters", response = FilterResponse.class, responseContainer = "List", tags={ "filter", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Request sucessfully processed.", response = FilterResponse.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Request is not understood.", response = Error.class) })
    @RequestMapping(value = "/v1/filter",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<FilterResponse>> getAllFilters();


    @ApiOperation(value = "get filter of given id", nickname = "getFilter", notes = "get filter", response = FilterResponse.class, tags={ "filter", })
    @ApiResponses(value = { 
        @ApiResponse(code = 206, message = "Request sucessfully processed.", response = FilterResponse.class),
        @ApiResponse(code = 400, message = "Request is not understood.", response = Error.class),
        @ApiResponse(code = 404, message = "Requested filter not found.", response = Error.class) })
    @RequestMapping(value = "/v1/filter/{id}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<FilterResponse> getFilter(@ApiParam(value = "",required=true) @PathVariable("id") UUID id);


    @ApiOperation(value = "get filter result", nickname = "getFilterResult", notes = "get filter result", response = FilterResultResponse.class, tags={ "filter", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Request sucessfully processed.", response = FilterResultResponse.class),
        @ApiResponse(code = 400, message = "Request is not understood.", response = Error.class),
        @ApiResponse(code = 404, message = "Requested filter not found.", response = Error.class) })
    @RequestMapping(value = "/v1/filter/{filterId}/{marketTime}/{scripName}/result",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<FilterResultResponse> getFilterResult(@ApiParam(value = "",required=true) @PathVariable("filterId") UUID filterId,@ApiParam(value = "",required=true) @PathVariable("marketTime") String marketTime,@ApiParam(value = "",required=true) @PathVariable("scripName") String scripName);


    @ApiOperation(value = "update filter", nickname = "updateFilter", notes = "update filter of given id", response = FilterResponse.class, tags={ "filter", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Filter with give id is updated successfully.", response = FilterResponse.class),
        @ApiResponse(code = 400, message = "Request is not understood.", response = Error.class),
        @ApiResponse(code = 404, message = "Requested screener not found.", response = Error.class) })
    @RequestMapping(value = "/v1/filter/{id}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PATCH)
    ResponseEntity<FilterResponse> updateFilter(@ApiParam(value = "payload to create filter" ,required=true )  @Valid @RequestBody List<PatchData> body,@ApiParam(value = "",required=true) @PathVariable("id") UUID id);

}
