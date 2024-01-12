package com.example.api.service.impl;

import com.example.api.domain.Customer;
import com.example.api.exception.ResourceNotFoundException;
import com.example.api.repository.CustomerRepository;
import com.example.api.service.CustomerService;
import com.example.api.to.CustomerCreateUpdateRequest;
import com.example.api.to.CustomerResponse;
import com.example.api.to.converter.CustomerConverter;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Log4j2
@Service
@Validated
public class CustomerServiceImpl implements CustomerService {

	private final CustomerRepository customerRepository;
	private final EntityManager entityManager;

	public CustomerServiceImpl(CustomerRepository customerRepository, EntityManager entityManager) {
		this.customerRepository = customerRepository;
		this.entityManager = entityManager;
	}

	public CustomerResponse saveOrUpdate(Long id, CustomerCreateUpdateRequest customerRequest) {
		log.info("SaveOrUpdate :: Saving Customer {} with Id {}", customerRequest, id);
		var customer = CustomerConverter.toCustomer(customerRequest);
		if (id != null) {
			customer.setId(id);
		}

		return CustomerConverter.toCustomerResponse(
				this.customerRepository.save(CustomerConverter.toCustomer(customerRequest))
		);
	}

	public Page<CustomerResponse> findAll(Pageable pageable) {
		log.info("FindAll :: Searching for customers");
		var customersPage = this.customerRepository.findAllByOrderByNameAsc(pageable);
		if (customersPage.isEmpty()) {
			return Page.empty();
		}
		return customersPage.map(CustomerConverter::toCustomerResponse);
	}

	public CustomerResponse findById(final Long id) {
		log.info("FindById :: Searching for customer with id {}", id);
		var customer = this.customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
		return CustomerConverter.toCustomerResponse(customer);
	}

	@SuppressWarnings("unchecked")
	public Page<CustomerResponse> findAllByFilters(String name, String email, String gender, Pageable pageable) {
		log.info("FindAllByFilters :: Searching for customers by filters");
		String queryStr = "SELECT c FROM Customer c WHERE 1=1";

		if (name != null && !name.isEmpty()) {
			log.info("FindAllByFilters :: Searching for customers by name {}", name);
			queryStr += " AND c.name LIKE :name";
		}

		if (email != null && !email.isEmpty()) {
			log.info("FindAllByFilters :: Searching for customers by email {}", email);
			queryStr += " AND c.email LIKE :email";
		}

		if (gender != null && !gender.isEmpty()) {
			log.info("FindAllByFilters :: Searching for customers by Gender {}", gender);
			queryStr += " AND c.gender = :gender";
		}

		Query query = entityManager.createQuery(queryStr, Customer.class);

		if (name != null && !name.isEmpty()) {
			query.setParameter("name", "%" + name + "%");
		}

		if (email != null && !email.isEmpty()) {
			query.setParameter("email", "%" + email + "%");
		}

		if (gender != null && !gender.isEmpty()) {
			query.setParameter("gender", gender);
		}

		List<Customer> resultList = query.getResultList();

		int totalRows = resultList.size();
		int start = (int) pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), totalRows);

		log.info("FindAllByFilters :: Total rows {} from pageable {}", totalRows, pageable);
		List<CustomerResponse> pageList = resultList.subList(start, end)
				.stream()
				.map(CustomerConverter::toCustomerResponse)
				.collect(Collectors.toList());

		return new PageImpl<>(pageList, pageable, totalRows);
	}

	public void deleteById(final Long id) {
		log.info("DeleteById :: Deleting customer with id {}", id);
		if (!this.customerRepository.existsById(id)) {
			throw new ResourceNotFoundException("The address for the given id was not found");
		}

		this.customerRepository.deleteById(id);
	}
}
