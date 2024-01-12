package com.example.api.web.rest;

import com.example.api.to.CustomerCreateUpdateRequest;
import com.example.api.to.CustomerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;

@ApiResponses(value = {
        @ApiResponse(code = 400, message = "Something is wrong with the request"),
        @ApiResponse(code = 401, message = "Someone tried to access a restricted resource without authorization"),
        @ApiResponse(code = 403, message = "User does not have permission to do this action"),
        @ApiResponse(code = 404, message = "Someone tried to access a resource that does not exist"),
        @ApiResponse(code = 500, message = "Something went wrong on server side"),
})
@Api(value = "Customers", tags = "Customers", description = "Customer Resources")
public interface CustomerController {

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created a customer"),
    })
    @ApiOperation(value = "Create a customer")
    CustomerResponse save(@Valid CustomerCreateUpdateRequest customerCreateUpdateRequest);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Updated a customer"),
    })
    @ApiOperation(value = "Update a customer")
    CustomerResponse update(Long id, @Valid CustomerCreateUpdateRequest customerCreateUpdateRequest);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retrieve all customers"),
    })
    @ApiOperation(value = "Retrieve all customers")
    Page<CustomerResponse> findAll(Integer page, Integer size);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retrieve all customers by search"),
    })
    @ApiOperation(value = "Retrieve all customers by search")
    Page<CustomerResponse> findAllBySearch(
            @Pattern(regexp = "[a-zA-Z ]*")
            @RequestParam(required = false)
            String name,

            @Email
            @RequestParam(required = false)
            String email,

            @Size(min = 1, max = 1)
            @RequestParam(required = false)
            String gender,
            Integer page,
            Integer size
    );

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retrieve a customer by id"),
    })
    @ApiOperation(value = "Retrieve a customer by id")
    CustomerResponse findById(Long id);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deleted a customer by id"),
    })
    @ApiOperation(value = "Delete a customer by id")
    void deleteById(Long id);
}
