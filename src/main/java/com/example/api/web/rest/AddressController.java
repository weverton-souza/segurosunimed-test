package com.example.api.web.rest;

import com.example.api.to.AddressCreateUpdateRequest;
import com.example.api.to.AddressResponse;
import com.example.api.to.CustomerResponse;
import com.example.api.to.ViaCepResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import org.springframework.data.domain.Page;

@ApiResponses(value = {
        @ApiResponse(code = 400, message = "Something is wrong with the request"),
        @ApiResponse(code = 401, message = "Someone tried to access a restricted resource without authorization"),
        @ApiResponse(code = 403, message = "User does not have permission to do this action"),
        @ApiResponse(code = 404, message = "Someone tried to access a resource that does not exist"),
        @ApiResponse(code = 500, message = "Something went wrong on server side"),
})
@Api(value = "Addresses", tags = "Addresses", description = "Address Resources")
public interface AddressController {

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created an address"),
    })
    @ApiOperation(value = "Create an address")
    AddressResponse save(@Valid AddressCreateUpdateRequest addressRequest);

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created a address"),
    })
    @ApiOperation(value = "Add one or more addresses to a customer")
    CustomerResponse save(Long customerId, @Valid AddressCreateUpdateRequest addressesRequest);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Updated an address"),
    })
    @ApiOperation(value = "Update an address")
    AddressResponse update(Long id, @Valid AddressCreateUpdateRequest addressRequest);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retrieve all addresses"),
    })
    @ApiOperation(value = "Retrieve all addresses")
    Page<AddressResponse> findAll(Integer page, Integer size);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retrieve an address by id"),
    })
    @ApiOperation(value = "Retrieve an address by id")
    AddressResponse findById(Long id);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retrieve an address by zipCode"),
    })
    @ApiOperation(value = "Retrieve an address by zipCode")
    ViaCepResponse findByZipCode(String zipCode);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deleted an address by id"),
    })
    @ApiOperation(value = "Delete an address by id")
    void deleteById(Long id);
}
