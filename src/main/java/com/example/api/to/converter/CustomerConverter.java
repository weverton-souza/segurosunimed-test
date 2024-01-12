package com.example.api.to.converter;

import com.example.api.domain.Customer;
import com.example.api.to.CustomerCreateUpdateRequest;
import com.example.api.to.CustomerResponse;

public class CustomerConverter {

    public static CustomerResponse toCustomerResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getGender(),
                AddressConverter.toAddressResponse(customer.getAddresses())
        );
    }

    public static Customer toCustomer(CustomerCreateUpdateRequest customerCreateUpdateRequest) {
        return new Customer(
                customerCreateUpdateRequest.getId(),
                customerCreateUpdateRequest.getName(),
                customerCreateUpdateRequest.getEmail(),
                customerCreateUpdateRequest.getGender(),
                AddressConverter.toAddress(customerCreateUpdateRequest.getAddresses())
        );
    }
}
