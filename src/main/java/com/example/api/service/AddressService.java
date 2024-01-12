package com.example.api.service;

import com.example.api.to.AddressCreateUpdateRequest;
import com.example.api.to.AddressResponse;
import com.example.api.to.CustomerResponse;
import com.example.api.to.ViaCepResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AddressService {

    AddressResponse saveOrUpdate(Long id, AddressCreateUpdateRequest addressRequest);

    CustomerResponse addAddressToCustomer(Long customerId, AddressCreateUpdateRequest addressRequest);

    ViaCepResponse findAddressByZipCode(String zipCode);

    AddressResponse findById(Long id);

    Page<AddressResponse> findAll(Pageable pageable);

     void deleteById(Long id);
}
