package com.example.api.to;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Response from some customer operation", description = "Customer Response Data")
public class CustomerResponse {
    @ApiModelProperty(value = "Customer id", example = "1")
    private Long id;

    @ApiModelProperty(value = "Customer name", example = "John Doe")
    private String name;

    @ApiModelProperty(value = "Customer email", example = "xavier@vingadores.com")
    private String email;

    @ApiModelProperty(value="Gender of the customer", example="M")
    private String gender;

    @ApiModelProperty(value = "List of addresses of the customer")
    private List<AddressResponse> address;
}
