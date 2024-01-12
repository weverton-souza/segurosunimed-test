package com.example.api.to;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Request to create or update an address", description = "Address Request Data")
public class AddressResponse {
    @ApiModelProperty(value = "Address id", example = "1")
    private Long id;

    @ApiModelProperty(value = "Street of a customer", example = "123 Main St")
    private String street;

    @ApiModelProperty(value = "Neighborhood of a customer", example = "Downtown")
    private String neighborhood;

    @ApiModelProperty(value = "City of a customer", example = "New York")
    private String city;

    @ApiModelProperty(value = "State of a customer", example = "NY")
    private String state;

    @ApiModelProperty(value = "Zip code of a customer", example = "12345")
    private String zip;

    @ApiModelProperty(value = "Type of address", example = "HOME")
    private String type;
}
