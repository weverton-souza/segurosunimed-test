package com.example.api.to;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Request to create or update an address", description = "Address Request Data")
public class AddressCreateUpdateRequest {
    @ApiModelProperty(value = "Street of a customer", example = "123 Main St")
    private String street;

    @Size(min = 1, max = 100, message = "The neighborhood must be between 1 and 100 characters")
    @ApiModelProperty(value = "Neighborhood of a customer", example = "Downtown")
    private String neighborhood;

    @Size(min = 1, max = 100, message = "The city must be between 1 and 100 characters")
    @ApiModelProperty(value = "City of a customer", example = "New York")
    private String city;

    @Size(min = 2, max = 2, message = "The state must be 2 characters")
    @ApiModelProperty(value = "State of a customer", example = "NY")
    private String state;

    @Pattern(regexp = "\\d{8}", message = "The zip code must be only numbers and have 8 digits")
    @ApiModelProperty(value = "Zip code of a customer", example = "12345")
    private String zip;

    @Pattern(regexp = "[A-Z]*", message = "The type must be only letters")
    @ApiModelProperty(value = "Type of address", example = "HOME")
    private String type;
}
