/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.0).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.bhs.gtk.mockfeed.api;

import com.bhs.gtk.mockfeed.model.CreateUserRequest;
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
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-18T16:57:42.881383100+05:30[Asia/Calcutta]")

@Api(value = "Admin", description = "the Admin API")
public interface AdminApi {

    @ApiOperation(value = "", nickname = "createUser", notes = "", tags={ "admin", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Request sucessfully processed.") })
    @RequestMapping(value = "/createUser",
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<Void> createUser(@ApiParam(value = ""  )  @Valid @RequestBody CreateUserRequest body);


    @ApiOperation(value = "", nickname = "updateAllSymbols", notes = "", tags={ "admin", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Request sucessfully processed.") })
    @RequestMapping(value = "/updateAllSymbols",
        method = RequestMethod.GET)
    ResponseEntity<Void> updateAllSymbols(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "exchange", required = true) String exchange,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "url", required = true) String url);

}