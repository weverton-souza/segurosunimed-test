package com.example.api.service.impl;

import com.example.api.client.ViaCepClient;
import com.example.api.domain.Address;
import com.example.api.domain.Customer;
import com.example.api.exception.ResourceNotFoundException;
import com.example.api.repository.AddressRepository;
import com.example.api.repository.CustomerRepository;
import com.example.api.service.AddressService;
import com.example.api.to.AddressCreateUpdateRequest;
import com.example.api.to.AddressResponse;
import com.example.api.to.CustomerResponse;
import com.example.api.to.ViaCepResponse;
import com.example.api.to.converter.AddressConverter;
import com.example.api.to.converter.CustomerConverter;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final ViaCepClient viaCepClient;

    public AddressServiceImpl(AddressRepository addressRepository, CustomerRepository customerRepository, ViaCepClient viaCepClient) {
        this.addressRepository = addressRepository;
        this.customerRepository = customerRepository;
        this.viaCepClient = viaCepClient;
    }

    public AddressResponse saveOrUpdate(Long id, AddressCreateUpdateRequest addressRequest) {
        log.info("SaveOrUpdate :: Saving address {} with Id {}", addressRequest, id);
        Address address = AddressConverter.toAddress(addressRequest);
        if (id != null) {
            address.setId(id);
        }

        return AddressConverter.toAddressResponse(this.addressRepository.save(address));
    }

    public CustomerResponse addAddressToCustomer(Long customerId, AddressCreateUpdateRequest addressRequest) {
        log.info("Save :: Saving address to a customer with id. customerId: {} address: {}", customerId, addressRequest);
        Customer customer = this.customerRepository.findById(customerId).orElseThrow();

        List<Address> addresses = customer.getAddresses();
        customer.getAddresses().add(AddressConverter.toAddress(addressRequest));
        customer.setAddresses(addresses);

        this.customerRepository.save(customer);

        return CustomerConverter
                .toCustomerResponse(this.customerRepository.save(customer));
    }

    public ViaCepResponse findAddressByZipCode(String zipCode) {
        log.info("FindAddressByZipCode :: Searching for address with zipCode {}", zipCode);
        return this.viaCepClient.findAddressByCep(zipCode);
    }

    public AddressResponse findById(Long id) {
        log.info("FindById :: Searching for address with id {}", id);
        return AddressConverter.toAddressResponse(this.addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The address for the given id was not found")));
    }

    public Page<AddressResponse> findAll(Pageable pageable) {
        log.info("FindAll :: Searching for addresses");
        var addressesPage = this.addressRepository.findAll(pageable);
        if (addressesPage.isEmpty()) {
            return Page.empty();
        }
        return addressesPage.map(AddressConverter::toAddressResponse);
    }

    public void deleteById(Long id) {
        log.info("DeleteById :: Deleting address with id {}", id);
        if (!this.addressRepository.existsById(id)) {
            throw new ResourceNotFoundException("The address for the given id was not found");
        }
        this.addressRepository.deleteById(id);
    }

}
