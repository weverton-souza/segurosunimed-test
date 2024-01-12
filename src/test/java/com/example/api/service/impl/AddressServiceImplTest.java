package com.example.api.service.impl;

import com.example.api.client.ViaCepClient;
import com.example.api.domain.Address;
import com.example.api.domain.Customer;
import com.example.api.exception.ResourceNotFoundException;
import com.example.api.repository.AddressRepository;
import com.example.api.repository.CustomerRepository;
import com.example.api.to.AddressCreateUpdateRequest;
import com.example.api.to.CustomerResponse;
import com.example.api.to.ViaCepResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Address Service Test")
class AddressServiceImplTest {

    @InjectMocks
    private AddressServiceImpl addressService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ViaCepClient viaCepClient;

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

    private final ViaCepResponse viaCepResponse = new ViaCepResponse(
            "Avenida Paulista",
            "Centro",
            "São Paulo",
            "SP",
            "00000-001"
    );

    @Test
    @DisplayName("Should save an address")
    void testSaveAnAddressMustCreateANewAddress() {
        when(this.addressRepository.save(any(Address.class))).thenReturn(address);

        var addressCreated = this.addressService.saveOrUpdate(null, this.addressRequest);

        assertNotNull(addressCreated);
        assertEquals(addressCreated.getStreet(), this.addressRequest.getStreet());
        assertEquals(addressCreated.getNeighborhood(), this.addressRequest.getNeighborhood());
        assertEquals(addressCreated.getCity(), this.addressRequest.getCity());
        assertEquals(addressCreated.getState(), this.addressRequest.getState());

        verify(this.addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    @DisplayName("Should update an address")
    void testUpdateAnAddressMustUpdateANewAddress() {
        var addressUpdated = new Address(
                1L,
                "Avenida Paulista, 555",
                "Centro",
                "São Paulo",
                "SP",
                "00000-001",
                "HOME"
        );

        when(this.addressRepository.save(any(Address.class))).thenReturn(addressUpdated);

        var addressCreated = this.addressService.saveOrUpdate(1L, addressRequest);

        assertNotNull(addressCreated);
        assertEquals(addressCreated.getNeighborhood(), this.addressRequest.getNeighborhood());
        assertEquals(addressCreated.getCity(), this.addressRequest.getCity());
        assertEquals(addressCreated.getState(), this.addressRequest.getState());

        verify(this.addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    @DisplayName("Should add address to customer")
    void testAddAddressToCustomer() {
        Customer mockCustomer = mock(Customer.class);
        List<Address> addressList = new ArrayList<>();
        when(mockCustomer.getAddresses()).thenReturn(addressList);

        when(this.customerRepository.findById(any(Long.class))).thenReturn(Optional.of(mockCustomer));
        when(this.customerRepository.save(any(Customer.class))).thenReturn(mockCustomer);

        CustomerResponse customerResponse = this.addressService.addAddressToCustomer(1L, this.addressRequest);

        assertNotNull(customerResponse);
        verify(this.customerRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should find address by zip code")
    void testFindAddressByZipCode() {
        when(this.viaCepClient.findAddressByCep(any(String.class))).thenReturn(viaCepResponse);

        var viaCepResponse = this.addressService.findAddressByZipCode("00000-001");

        assertNotNull(viaCepResponse);
        assertEquals(viaCepResponse.getCity(), this.viaCepResponse.getCity());
        assertEquals(viaCepResponse.getZip(), this.viaCepResponse.getZip());
        assertEquals(viaCepResponse.getNeighborhood(), this.viaCepResponse.getNeighborhood());

        verify(this.viaCepClient, times(1)).findAddressByCep("00000-001");
    }

    @Test
    @DisplayName("Should find address by id")
    void testFindById() {
        when(this.addressRepository.findById(any(Long.class))).thenReturn(Optional.of(address));

        var addressCreated = this.addressService.findById(1L);

        assertNotNull(addressCreated);
        assertEquals(addressCreated.getStreet(), this.addressRequest.getStreet());
        assertEquals(addressCreated.getNeighborhood(), this.addressRequest.getNeighborhood());
        assertEquals(addressCreated.getCity(), this.addressRequest.getCity());

        verify(this.addressRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFound exception when find address by id")
    void testFindByIdShouldThrowResourceNotFound() {
        when(this.addressRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> this.addressService.findById(1L));

        verify(this.addressRepository, times(1)).findById(any(Long.class));
    }

    @Test
    @DisplayName("Should retrieve all addresses by pagination")
    void testFindAll() {
        var pageable = PageRequest.of(0, 1);
        Page<Address> page = new PageImpl<>(Collections.singletonList(address), pageable, 1);

        when(this.addressRepository.findAll(pageable)).thenReturn(page);

        var addresses = this.addressService.findAll(pageable);

        assertNotNull(addresses);
        assertEquals(1, addresses.getTotalPages());
        assertEquals(1, addresses.getTotalElements());

        verify(this.addressRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Should find all addresses should return empty page")
    void testFindAllShouldReturnEmptyPage() {
        var pageable = PageRequest.of(0, 15);

        when(this.addressRepository.findAll(pageable)).thenReturn(Page.empty());

        var addresses = this.addressService.findAll(pageable);

        assertNotNull(addresses);
        assertEquals(1, addresses.getTotalPages());
        assertEquals(0, addresses.getTotalElements());

        verify(this.addressRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Should delete address by id")
    void testDeleteById() {
        when(this.addressRepository.existsById(any(Long.class))).thenReturn(true);
        doNothing().when(this.addressRepository).deleteById(any(Long.class));

        this.addressService.deleteById(1L);

        verify(this.addressRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFound exception when delete address by id")
    void testDeleteByIdShouldThrowResourceNotFound() {
        Long id = 1L;
        when(this.addressRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> this.addressService.deleteById(id));

        verify(this.addressRepository, times(1)).existsById(id);
    }

}
