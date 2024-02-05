package com.oa.homework.service;

import com.oa.homework.entity.Customer;
import com.oa.homework.exception.CustomerNotFoundException;
import com.oa.homework.model.CustomerDto;
import com.oa.homework.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    public void testAddCustomer() {
        CustomerDto customerDto = new CustomerDto("John Doe");
        Customer customer = new Customer();
        customer.setName("John Doe");

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDto savedCustomer = customerService.addCustomer(customerDto);

        assertEquals(customerDto.getName(), savedCustomer.getName(), "Names should be the same");
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void testGetAllCustomers() {
        Customer customer1 = new Customer();
        customer1.setName("John Doe");
        Customer customer2 = new Customer();
        customer2.setName("Jane Doe");

        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1, customer2));

        List<CustomerDto> customers = customerService.getAllCustomers();

        assertEquals(2, customers.size());
        assertEquals("John Doe", customers.get(0).getName(), "Name should be the same");
        assertEquals("Jane Doe", customers.get(1).getName(), "Name should be the same");
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    public void testGetCustomerByIdFound() {
        Customer customer = new Customer();
        customer.setName("John Doe");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerDto customerDto = customerService.getCustomerById(1L);

        assertEquals("John Doe", customerDto.getName(), "Name should be the same");
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetCustomerByIdNotFound() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.getCustomerById(1L);
        }, "getCustomerById should return CustomerNotFoundException");

        verify(customerRepository, times(1)).findById(anyLong());
    }
}

