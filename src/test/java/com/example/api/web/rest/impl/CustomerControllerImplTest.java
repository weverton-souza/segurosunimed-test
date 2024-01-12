package com.example.api.web.rest.impl;

import com.example.api.service.CustomerService;
import com.example.api.to.AddressCreateUpdateRequest;
import com.example.api.to.AddressResponse;
import com.example.api.to.CustomerCreateUpdateRequest;
import com.example.api.to.CustomerResponse;
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
@DisplayName("Customer Controller Test")
class CustomerControllerImplTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CustomerControllerImpl customerController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

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

    @Mock
    private CustomerService customerService;

    @Test
    @DisplayName("Should save a customer")
    void testSave() throws Exception {
        when(this.customerService.saveOrUpdate(any(), any(CustomerCreateUpdateRequest.class)))
                .thenReturn(customerResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        String addressRequestJson = objectMapper.writeValueAsString(addressRequest);

        this.mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressRequestJson))
                .andExpect(status().is2xxSuccessful())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.id").value(customerResponse.getId()))
                .andExpect(jsonPath("$.name").value(customerResponse.getName()))
                .andExpect(jsonPath("$.email").value(customerResponse.getEmail()))
                .andExpect(jsonPath("$.gender").value(customerResponse.getGender()));


        verify(this.customerService).saveOrUpdate(any(), any(CustomerCreateUpdateRequest.class));
    }

    @Test
    @DisplayName("Should create a new customer should throw bad request")
    void testSaveShouldCreateANewCustomerShouldThrowBadRequest() throws Exception {
        CustomerCreateUpdateRequest customerRequest = new CustomerCreateUpdateRequest(
                null,
                "John Doe",
                "johndoeemail.com",
                "K",
                Collections.singletonList(addressRequest)
        );

        ObjectMapper objectMapper = new ObjectMapper();
        String addressRequestJson = objectMapper.writeValueAsString(customerRequest);

        this.mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressRequestJson))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Should update a customer")
    void testUpdate() throws Exception {
        when(this.customerService.saveOrUpdate(any(), any(CustomerCreateUpdateRequest.class)))
                .thenReturn(customerResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        String addressRequestJson = objectMapper.writeValueAsString(addressRequest);

        this.mockMvc.perform(put("/customers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressRequestJson))
                .andExpect(status().is2xxSuccessful())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.id").value(customerResponse.getId()))
                .andExpect(jsonPath("$.name").value(customerResponse.getName()))
                .andExpect(jsonPath("$.email").value(customerResponse.getEmail()))
                .andExpect(jsonPath("$.gender").value(customerResponse.getGender()));


        verify(this.customerService).saveOrUpdate(any(), any(CustomerCreateUpdateRequest.class));
    }

    @Test
    @DisplayName("Should return a customer with address")
    void testFindAll() throws Exception {
        var pageable = PageRequest.of(0, 1);
        Page<CustomerResponse> customerResponsePage =
                new PageImpl<>(Collections.singletonList(customerResponse), pageable, 1);

        when(this.customerService.findAll(any(Pageable.class))).thenReturn(customerResponsePage);

        this.mockMvc.perform(get("/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.content[0].id").value(customerResponse.getId()))
                .andExpect(jsonPath("$.content[0].name").value(customerResponse.getName()))
                .andExpect(jsonPath("$.content[0].email").value(customerResponse.getEmail()))
                .andExpect(jsonPath("$.content[0].gender").value(customerResponse.getGender()));

        verify(this.customerService).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should return a customer with address")
    void testFindAllBySearch() throws Exception {
        var pageable = PageRequest.of(0, 1);
        Page<CustomerResponse> customerResponsePage =
                new PageImpl<>(Collections.singletonList(customerResponse), pageable, 1);

        when(this.customerService.findAllByFilters(any(), any(), any(), any(Pageable.class)))
                .thenReturn(customerResponsePage);

        this.mockMvc.perform(get("/customers/search")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.content[0].id").value(customerResponse.getId()))
                .andExpect(jsonPath("$.content[0].name").value(customerResponse.getName()))
                .andExpect(jsonPath("$.content[0].email").value(customerResponse.getEmail()))
                .andExpect(jsonPath("$.content[0].gender").value(customerResponse.getGender()));

        verify(this.customerService).findAllByFilters(any(), any(), any(), any(Pageable.class));
    }

    @Test
    @DisplayName("Should return a customer by id")
    void testFindById() throws Exception {
        when(this.customerService.findById(any())).thenReturn(customerResponse);

        this.mockMvc.perform(get("/customers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.id").value(customerResponse.getId()))
                .andExpect(jsonPath("$.name").value(customerResponse.getName()))
                .andExpect(jsonPath("$.email").value(customerResponse.getEmail()))
                .andExpect(jsonPath("$.gender").value(customerResponse.getGender()));

        verify(this.customerService).findById(any());
    }

    @Test
    @DisplayName("Should delete a customer by id")
    void testDeleteById() throws Exception {
        doNothing().when(this.customerService).deleteById(any());

        this.mockMvc.perform(delete("/customers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()));

        verify(this.customerService).deleteById(any(Long.class));
    }
}