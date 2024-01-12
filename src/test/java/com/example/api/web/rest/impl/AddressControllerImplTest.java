package com.example.api.web.rest.impl;

import com.example.api.service.AddressService;
import com.example.api.to.AddressCreateUpdateRequest;
import com.example.api.to.AddressResponse;
import com.example.api.to.CustomerResponse;
import com.example.api.to.ViaCepResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Address Controller Test")
class AddressControllerImplTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AddressControllerImpl addressController;

    @Mock
    private AddressService addressService;

    private final AddressCreateUpdateRequest addressRequest = new AddressCreateUpdateRequest(
            "Avenida Paulista, 101",
            "Centro",
            "São Paulo",
            "SP",
            "00000001",
            "HOME"
    );

    private final AddressResponse addressResponse = new AddressResponse(
            1L,
            "Avenida Paulista, 101",
            "Centro",
            "São Paulo",
            "SP",
            "00000001",
            "HOME"
    );

    private final CustomerResponse customerResponse = new CustomerResponse(
            1L,
            "John Doe",
            "johndoe@rmail.com",
            "M",
            Collections.singletonList(addressResponse)
    );

    private final ViaCepResponse viaCepResponse = new ViaCepResponse(
            "Avenida Paulista, 101",
            "Centro",
            "São Paulo",
            "SP",
            "00000001"
    );

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(addressController).build();
    }

    @Test
    @DisplayName("Should create a new address")
    void testSaveShouldCreateANewAddress() throws Exception {
        when(this.addressService.saveOrUpdate(any(), any(AddressCreateUpdateRequest.class)))
                .thenReturn(addressResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        String addressRequestJson = objectMapper.writeValueAsString(addressRequest);

        this.mockMvc.perform(post("/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressRequestJson))
                .andExpect(status().is2xxSuccessful())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.street").value("Avenida Paulista, 101"))
                .andExpect(jsonPath("$.neighborhood").value("Centro"))
                .andExpect(jsonPath("$.city").value("São Paulo"))
                .andExpect(jsonPath("$.state").value("SP"))
                .andExpect(jsonPath("$.zip").value("00000001"))
                .andExpect(jsonPath("$.type").value("HOME"));

        verify(this.addressService).saveOrUpdate(any(), any(AddressCreateUpdateRequest.class));
    }

    @Test
    @DisplayName("Should create a new address should throw bad request")
    void testSaveShouldCreateANewAddressShouldThrowBadRequest() throws Exception {
        final AddressCreateUpdateRequest addressRequest = new AddressCreateUpdateRequest(
                "Avenida Paulista, 101",
                "Centro",
                "São Paulo",
                "P",
                "000001",
                "HOME"
        );

        ObjectMapper objectMapper = new ObjectMapper();
        String addressRequestJson = objectMapper.writeValueAsString(addressRequest);

        this.mockMvc.perform(post("/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressRequestJson))
                .andExpect(status().is4xxClientError());
    }


    @Test
    @DisplayName("Should add a new address to an customer")
    void testSaveShouldAddANewAddressToAnCustomer() throws Exception {
        when(this.addressService.addAddressToCustomer(any(), any(AddressCreateUpdateRequest.class)))
                .thenReturn(customerResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        String addressRequestJson = objectMapper.writeValueAsString(addressRequest);

        this.mockMvc.perform(post("/addresses/customer/{customerId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressRequestJson))
                .andExpect(status().is2xxSuccessful())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("johndoe@rmail.com"))
                .andExpect(jsonPath("$.address.size()").value(1))
                .andExpect(jsonPath("$.address[0].street").value("Avenida Paulista, 101"))
                .andExpect(jsonPath("$.address[0].neighborhood").value("Centro"))
                .andExpect(jsonPath("$.address[0].city").value("São Paulo"))
                .andExpect(jsonPath("$.address[0].state").value("SP"))
                .andExpect(jsonPath("$.address[0].zip").value("00000001"))
                .andExpect(jsonPath("$.address[0].type").value("HOME"));

        verify(this.addressService).addAddressToCustomer(any(), any(AddressCreateUpdateRequest.class));
    }

    @Test
    @DisplayName("Should update an address")
    void update() throws Exception {
        when(this.addressService.saveOrUpdate(any(), any(AddressCreateUpdateRequest.class)))
                .thenReturn(addressResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        String addressRequestJson = objectMapper.writeValueAsString(addressRequest);

        this.mockMvc.perform(put("/addresses/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressRequestJson))
                .andExpect(status().is2xxSuccessful())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.street").value("Avenida Paulista, 101"))
                .andExpect(jsonPath("$.neighborhood").value("Centro"))
                .andExpect(jsonPath("$.city").value("São Paulo"))
                .andExpect(jsonPath("$.state").value("SP"))
                .andExpect(jsonPath("$.zip").value("00000001"))
                .andExpect(jsonPath("$.type").value("HOME"));

        verify(this.addressService).saveOrUpdate(any(), any(AddressCreateUpdateRequest.class));
    }

    @Test
    @DisplayName("Should retrieve all addresses")
    void testFindAllShouldRetrieveAllAddresses() throws Exception {
        var pageable = PageRequest.of(0, 1);
        Page<AddressResponse> addressResponsePage =
                new PageImpl<>(Collections.singletonList(addressResponse), pageable, 1);

        when(this.addressService.findAll(any(Pageable.class))).thenReturn(addressResponsePage);

        this.mockMvc.perform(get("/addresses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.content[0].street").value("Avenida Paulista, 101"))
                .andExpect(jsonPath("$.content[0].neighborhood").value("Centro"))
                .andExpect(jsonPath("$.content[0].city").value("São Paulo"))
                .andExpect(jsonPath("$.content[0].state").value("SP"))
                .andExpect(jsonPath("$.content[0].zip").value("00000001"))
                .andExpect(jsonPath("$.content[0].type").value("HOME"));

        verify(this.addressService).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should retrieve an address by id")
    void testFindById() throws Exception {
        when(this.addressService.findById(any(Long.class))).thenReturn(addressResponse);

        this.mockMvc.perform(get("/addresses/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.street").value("Avenida Paulista, 101"))
                .andExpect(jsonPath("$.neighborhood").value("Centro"))
                .andExpect(jsonPath("$.city").value("São Paulo"))
                .andExpect(jsonPath("$.state").value("SP"))
                .andExpect(jsonPath("$.zip").value("00000001"));

        verify(this.addressService).findById(any(Long.class));
    }

    @Test
    @DisplayName("Should retrieve an address by zip code")
    void testFindByZipCode() throws Exception {
        when(this.addressService.findAddressByZipCode(any(String.class))).thenReturn(viaCepResponse);

        this.mockMvc.perform(get("/addresses/zip-code/{zipCode}", "00000001")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.street").value("Avenida Paulista, 101"))
                .andExpect(jsonPath("$.neighborhood").value("Centro"))
                .andExpect(jsonPath("$.city").value("São Paulo"))
                .andExpect(jsonPath("$.state").value("SP"));

        verify(this.addressService).findAddressByZipCode(any(String.class));
    }

    @Test
    @DisplayName("Should delete an address by id")
    void testDeleteById() throws Exception {
        doNothing().when(this.addressService).deleteById(any(Long.class));

        this.mockMvc.perform(delete("/addresses/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()));

        verify(this.addressService).deleteById(any(Long.class));
    }
}