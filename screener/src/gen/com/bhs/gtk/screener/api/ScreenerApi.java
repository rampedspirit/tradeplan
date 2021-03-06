/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.0).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.bhs.gtk.screener.api;

import com.bhs.gtk.screener.model.Error;
import com.bhs.gtk.screener.model.ExecutableCreateRequest;
import com.bhs.gtk.screener.model.ScreenerCreateRequest;
import com.bhs.gtk.screener.model.ScreenerDetailedResponse;
import com.bhs.gtk.screener.model.ScreenerPatchData;
import com.bhs.gtk.screener.model.ScreenerResponse;
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
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-03-07T11:51:10.308924500+05:30[Asia/Calcutta]")

@Api(value = "Screener", description = "the Screener API")
public interface ScreenerApi {

    @ApiOperation(value = "create new screener", nickname = "createScreener", notes = "create new screener", response = ScreenerResponse.class, tags={ "screener", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "screener created successfully.", response = ScreenerResponse.class),
        @ApiResponse(code = 400, message = "Request is not understood.", response = Error.class) })
    @RequestMapping(value = "/v1/screener",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<ScreenerResponse> createScreener(@ApiParam(value = "payload to create screener" ,required=true )  @Valid @RequestBody ScreenerCreateRequest body);


    @ApiOperation(value = "delete", nickname = "deleteScreener", notes = "delete screener of given id", response = ScreenerResponse.class, tags={ "screener", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "screener with give id and all associated exectables of screener are deleted successfully.", response = ScreenerResponse.class),
        @ApiResponse(code = 404, message = "Requested screener not found.", response = Error.class) })
    @RequestMapping(value = "/v1/screener/{screenerId}",
        produces = { "application/json" }, 
        method = RequestMethod.DELETE)
    ResponseEntity<ScreenerResponse> deleteScreener(@ApiParam(value = "",required=true) @PathVariable("screenerId") UUID screenerId);


    @ApiOperation(value = "get all screener", nickname = "getAllScreeners", notes = "get screener", response = ScreenerResponse.class, responseContainer = "List", tags={ "screener", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Request sucessfully processed.", response = ScreenerResponse.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Request is not understood.", response = Error.class) })
    @RequestMapping(value = "/v1/screener",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<ScreenerResponse>> getAllScreeners();


    @ApiOperation(value = "get screener of given id", nickname = "getScreener", notes = "get screener", response = ScreenerDetailedResponse.class, tags={ "screener", })
    @ApiResponses(value = { 
        @ApiResponse(code = 206, message = "Request sucessfully processed.", response = ScreenerDetailedResponse.class),
        @ApiResponse(code = 400, message = "Request is not understood.", response = Error.class),
        @ApiResponse(code = 404, message = "Requested filter not found.", response = Error.class) })
    @RequestMapping(value = "/v1/screener/{screenerId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<ScreenerDetailedResponse> getScreener(@ApiParam(value = "",required=true) @PathVariable("screenerId") UUID screenerId);


    @ApiOperation(value = "run screener", nickname = "runScreener", notes = "run screener at given marketTime", response = ScreenerDetailedResponse.class, tags={ "screener", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "executable created successfully.", response = ScreenerDetailedResponse.class),
        @ApiResponse(code = 400, message = "Request is not understood.", response = Error.class) })
    @RequestMapping(value = "/v1/screener/{screenerId}/run",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<ScreenerDetailedResponse> runScreener(@ApiParam(value = "payload to create executable" ,required=true )  @Valid @RequestBody ExecutableCreateRequest body,@ApiParam(value = "",required=true) @PathVariable("screenerId") UUID screenerId);


    @ApiOperation(value = "update screener", nickname = "updateScreener", notes = "update screener of given id", response = ScreenerResponse.class, tags={ "screener", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Screener with give id is updated successfully.", response = ScreenerResponse.class),
        @ApiResponse(code = 400, message = "Request is not understood.", response = Error.class),
        @ApiResponse(code = 404, message = "Requested screener not found.", response = Error.class) })
    @RequestMapping(value = "/v1/screener/{screenerId}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PATCH)
    ResponseEntity<ScreenerResponse> updateScreener(@ApiParam(value = "Payload to change screener of given Id." ,required=true )  @Valid @RequestBody List<ScreenerPatchData> body,@ApiParam(value = "",required=true) @PathVariable("screenerId") UUID screenerId);

}
