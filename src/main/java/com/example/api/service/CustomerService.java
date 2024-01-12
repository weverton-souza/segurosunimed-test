package com.example.api.service;

import com.example.api.to.CustomerCreateUpdateRequest;
import com.example.api.to.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

	CustomerResponse saveOrUpdate(Long id, final CustomerCreateUpdateRequest customerCreateUpdateRequest);

	Page<CustomerResponse> findAll(Pageable pageable);

	CustomerResponse findById(final Long id);

	Page<CustomerResponse> findAllByFilters(String name, String email, String gender, Pageable pageable);

	void deleteById(final Long id);
}
