package com.example.api.to;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Request to create or update a customer", description = "Customer Request Data")
public class CustomerCreateUpdateRequest {
    @ApiModelProperty(value = "Customer id", example = "1", hidden = true)
    private Long id;

    @Size(min = 3, max = 255, message = "The name must be between 3 and 255 characters")
    @Pattern(regexp = "[a-zA-Z ]*", message = "The name must contain only letters")
    @ApiModelProperty(value = "Customer name", example = "John Doe")
    private String name;

    @Email(message = "The email must be valid")
    @ApiModelProperty(value = "Customer email", example = "xavier@vingadores.com")
    private String email;

    @Pattern(regexp = "[MF]", message = "The gender must be M or F")
    @Size(min = 1, max = 1, message = "The gender can not be less than 1 and greater than 1")
    @ApiModelProperty(value="Gender of the customer", example="M")
    private String gender;

    @ApiModelProperty(value = "List of addresses of the customer")
    private List<AddressCreateUpdateRequest> addresses = new ArrayList<>();
}
