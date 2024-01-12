package com.example.api.service.impl;

import com.example.api.domain.Address;
import com.example.api.domain.Customer;
import com.example.api.exception.ResourceNotFoundException;
import com.example.api.repository.CustomerRepository;
import com.example.api.to.AddressCreateUpdateRequest;
import com.example.api.to.CustomerCreateUpdateRequest;
import com.example.api.to.CustomerResponse;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Customer Service Test")
class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Customer> typedQuery;


    private final Address address = new Address(
            1L,
            "Avenida Paulista, 101",
            "Centro",
            "São Paulo",
            "SP",
            "00000-001",
            "HOME"
    );

    private final AddressCreateUpdateRequest addressRequest = new AddressCreateUpdateRequest(
            "Avenida Paulista, 101",
            "Centro",
            "São Paulo",
            "SP",
            "00000-001",
            "HOME"
    );

    private final Customer customer = new Customer(
            1L,
            "John Doe",
            "johndoe@email.com",
            "M",
            Collections.singletonList(address)
    );

    private final CustomerCreateUpdateRequest customerRequest = new CustomerCreateUpdateRequest(
            1L,
            "John Doe",
            "johndoe@email.com",
            "M",
            Collections.singletonList(addressRequest)
    );

    @Test
    @DisplayName("Save a customer")
    void testSaveOrUpdateShouldSave() {
        when(customerRepository.save(any())).thenReturn(customer);

        var customerSaved = customerService.saveOrUpdate(null, customerRequest);

        assertNotNull(customerSaved);
        assertEquals(customerSaved.getId(), customer.getId());
        assertEquals(customerSaved.getName(), customer.getName());
        assertEquals(customerSaved.getEmail(), customer.getEmail());
        assertEquals(customerSaved.getGender(), customer.getGender());

        verify(customerRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Update a customer")
    void testSaveOrUpdateShouldUpdate() {
        when(customerRepository.save(any())).thenReturn(customer);

        var customerSaved = customerService.saveOrUpdate(1L, customerRequest);

        assertNotNull(customerSaved);
        assertEquals(customerSaved.getId(), customer.getId());
        assertEquals(customerSaved.getName(), customer.getName());
        assertEquals(customerSaved.getEmail(), customer.getEmail());
        assertEquals(customerSaved.getGender(), customer.getGender());

        verify(customerRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should retrieve all customers by pagination")
    void testFindAllShouldReturnAllByPagination() {
        var pageable = PageRequest.of(0, 1);
        Page<Customer> page = new PageImpl<>(Collections.singletonList(customer), pageable, 1);

        when(this.customerRepository.findAllByOrderByNameAsc(pageable)).thenReturn(page);

        var customers = this.customerService.findAll(pageable);

        assertNotNull(customers);
        assertEquals(1, customers.getTotalElements());
        assertEquals(1, customers.getTotalPages());
        assertEquals(customers.getContent().get(0).getId(), customer.getId());
        assertEquals(customers.getContent().get(0).getName(), customer.getName());
        assertEquals(customers.getContent().get(0).getEmail(), customer.getEmail());

        verify(this.customerRepository, times(1)).findAllByOrderByNameAsc(pageable);
    }

    @Test
    @DisplayName("Should find all customers should return empty page")
    void testFindAllShouldReturnEmptyPage() {
        var pageable = PageRequest.of(0, 1);

        when(this.customerRepository.findAllByOrderByNameAsc(pageable)).thenReturn(Page.empty());

        var customers = this.customerService.findAll(pageable);

        assertNotNull(customers);
        assertEquals(0, customers.getTotalElements());
        assertEquals(1, customers.getTotalPages());

        verify(this.customerRepository, times(1)).findAllByOrderByNameAsc(pageable);
    }

    @Test
    @DisplayName("Should retrieve a customer by id")
    void testFindById() {
        when(this.customerRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(customer));

        var customerCreated = this.customerService.findById(1L);

        assertNotNull(customerCreated);
        assertEquals(customerCreated.getId(), customer.getId());
        assertEquals(customerCreated.getName(), customer.getName());
        assertEquals(customerCreated.getEmail(), customer.getEmail());
        assertEquals(customerCreated.getGender(), customer.getGender());

        verify(this.customerRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFound exception when find customer by id")
    void testFindByIdShouldThrowResourceNotFound() {
        when(this.customerRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> this.customerService.findById(1L));

        verify(this.customerRepository, times(1)).findById(any(Long.class));
    }

    @Test
    @DisplayName("Should find all customers by filters when name, email and gender are not null")
    void testFindAllByFiltersWhenNameEmailAndGenderIsNotNull() {
        List<Customer> customers = List.of(customer);
        when(entityManager.createQuery(anyString(), eq(Customer.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(customers);

        String name = "Nome Teste";
        String email = "email@teste.com";
        String gender = "M";
        Pageable pageable = PageRequest.of(0, 10);

        Page<CustomerResponse> result = this.customerService.findAllByFilters(name, email, gender, pageable);

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        assertEquals(customers.size(), result.getTotalElements());

        verify(entityManager, times(1)).createQuery(anyString(), eq(Customer.class));
    }

    @Test
    @DisplayName("Should find all customers by filters when name, email and gender are null")
    void testFindAllByFiltersWhenNameEmailAndGenderIsNull() {
        List<Customer> customers = List.of(customer);
        when(entityManager.createQuery(anyString(), eq(Customer.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(customers);

        Pageable pageable = PageRequest.of(0, 10);

        Page<CustomerResponse> result = this.customerService.findAllByFilters(null, null, null, pageable);

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        assertEquals(customers.size(), result.getTotalElements());

        verify(entityManager, times(1)).createQuery(anyString(), eq(Customer.class));
    }

    @Test
    @DisplayName("Should delete a customer by id")
    void testDeleteById() {
        when(this.customerRepository.existsById(any(Long.class))).thenReturn(true);
        doNothing().when(this.customerRepository).deleteById(any(Long.class));

        this.customerService.deleteById(1L);

        verify(this.customerRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFound exception when delete customer by id")
    void testDeleteByIdShouldThrowResourceNotFound() {
        Long id = 1L;
        when(this.customerRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> this.customerService.deleteById(id));

        verify(this.customerRepository, times(1)).existsById(id);
    }
}
