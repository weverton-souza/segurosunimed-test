package com.example.api.to.converter;

import com.example.api.domain.Address;
import com.example.api.to.AddressCreateUpdateRequest;
import com.example.api.to.AddressResponse;
import java.util.List;
import java.util.stream.Collectors;

public class AddressConverter {

    private AddressConverter() {}

    public static AddressResponse toAddressResponse(Address address) {
        return new AddressResponse(
                address.getId(),
                address.getStreet(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getZip(),
                address.getType());
    }

    public static List<AddressResponse> toAddressResponse(List<Address> addresses) {
        return addresses
                .stream()
                .map(AddressConverter::toAddressResponse)
                .collect(Collectors.toList());
    }

    public static Address toAddress(AddressResponse addressResponse) {
        return new Address(
                null,
                addressResponse.getStreet(),
                addressResponse.getNeighborhood(),
                addressResponse.getCity(),
                addressResponse.getState(),
                addressResponse.getZip(),
                addressResponse.getType()
        );
    }

    public static Address toAddress(AddressCreateUpdateRequest request) {
        return new Address(
                null,
                request.getStreet(),
                request.getNeighborhood(),
                request.getCity(),
                request.getState(),
                request.getZip(),
                request.getType()
        );
    }

    public static List<Address> toAddress(List<AddressCreateUpdateRequest> addresses) {
        return addresses.stream().map(AddressConverter::toAddress).collect(Collectors.toList());
    }
}
