package com.example.api.web.rest.impl;

import com.example.api.service.AddressService;
import com.example.api.to.AddressCreateUpdateRequest;
import com.example.api.to.AddressResponse;
import com.example.api.to.CustomerResponse;
import com.example.api.to.ViaCepResponse;
import com.example.api.web.rest.AddressController;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@Validated
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/addresses")
public class AddressControllerImpl implements AddressController {

    private final AddressService addressService;

    public AddressControllerImpl(final AddressService addressService) {
        this.addressService = addressService;
    }

    @Override
    @PostMapping
    public AddressResponse save(@RequestBody AddressCreateUpdateRequest addressRequest) {
        log.info("Save :: Saving address {}", addressRequest);
        return this.addressService.saveOrUpdate(null, addressRequest);
    }

    @Override
    @PostMapping("customer/{customerId}")
    public CustomerResponse save(
            @PathVariable Long customerId,
            @RequestBody AddressCreateUpdateRequest addressesRequest
    ) {
        log.info("Save :: Saving addresses");
        return this.addressService.addAddressToCustomer(customerId, addressesRequest);
    }

    @Override
    @PutMapping("/{id}")
    public AddressResponse update(@PathVariable Long id, @RequestBody AddressCreateUpdateRequest addressRequest) {
        log.info("Update :: Updating address {}", addressRequest);
        return this.addressService.saveOrUpdate(id, addressRequest);
    }

    @Override
    @GetMapping
    public Page<AddressResponse> findAll(@RequestParam(defaultValue = "0") Integer page,
                                         @RequestParam(defaultValue = "15") Integer size) {
        log.info("FindAll :: Searching for addresses");
        return this.addressService.findAll(PageRequest.of(page, size));
    }

    @Override
    @GetMapping("/{id}")
    public AddressResponse findById(@PathVariable Long id) {
        log.info("FindById :: Searching for address with id {}", id);
        return this.addressService.findById(id);
    }

    @Override
    @GetMapping("/zip-code/{zipCode}")
    public ViaCepResponse findByZipCode(@PathVariable String zipCode) {
        log.info("FindAddressByZipCode :: Searching for address with zipCode {}", zipCode);
        return this.addressService.findAddressByZipCode(zipCode);
    }

    @Override
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        log.info("DeleteById :: Deleting address with id {}", id);
        this.addressService.deleteById(id);
    }
}
