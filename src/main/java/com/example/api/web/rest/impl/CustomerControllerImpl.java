package com.example.api.web.rest.impl;

import com.example.api.service.CustomerService;
import com.example.api.to.CustomerCreateUpdateRequest;
import com.example.api.to.CustomerResponse;
import com.example.api.web.rest.CustomerController;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/customers")
public class CustomerControllerImpl implements CustomerController {

    private final CustomerService customerService;

    public CustomerControllerImpl(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public CustomerResponse save(@RequestBody @Valid CustomerCreateUpdateRequest customerRequest) {
        log.info("Save :: Saving customer {}", customerRequest);
        return this.customerService.saveOrUpdate(null, customerRequest);
    }

    @Override
    @PutMapping("/{id}")
    public CustomerResponse update(
            @PathVariable Long id,
            @RequestBody @Valid CustomerCreateUpdateRequest customerCreateUpdateRequest) {
        log.info("Update :: Updating customer {}", customerCreateUpdateRequest);
        return this.customerService.saveOrUpdate(id, customerCreateUpdateRequest);
    }

    @Override
    @GetMapping
    public Page<CustomerResponse> findAll(@RequestParam(defaultValue = "0") Integer page,
                                          @RequestParam(defaultValue = "15") Integer size) {
        log.info("FindAll :: Searching for customers");
        return this.customerService.findAll(Pageable.ofSize(size).withPage(page));
    }

    @Override
    @GetMapping("/search")
    public Page<CustomerResponse> findAllBySearch(
            @Pattern(regexp = "[a-zA-Z ]*")
            @RequestParam(required = false)
            String name,

            @Email
            @RequestParam(required = false)
            String email,

            @Size(min = 1, max = 1)
            @RequestParam(required = false)
            String gender,

            @RequestParam(defaultValue = "0")
            Integer page,

            @RequestParam(defaultValue = "15")
            Integer size
    ) {
        log.info("FindAllBySearch :: Searching for customers by search");
        return this.customerService.findAllByFilters(name, email, gender, Pageable.ofSize(size).withPage(page));
    }

    @Override
    @GetMapping("/{id}")
    public CustomerResponse findById(@PathVariable Long id) {
        log.info("FindById :: Searching for customer with id {}", id);
        return this.customerService.findById(id);
    }

    @Override
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        log.info("DeleteById :: Deleting customer with id {}", id);
        this.customerService.deleteById(id);
    }

}
