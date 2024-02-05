package com.oa.homework.service;

import com.oa.homework.entity.Customer;
import com.oa.homework.entity.Transaction;
import com.oa.homework.exception.CustomerNotFoundException;
import com.oa.homework.model.RewardsSummary;
import com.oa.homework.repository.CustomerRepository;
import com.oa.homework.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RewardsServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RewardsService rewardsService;

    @Test
    public void testCalculatePoints() {
        assertEquals(0, rewardsService.calculatePoints(new BigDecimal("49")).intValue());
        assertEquals(1, rewardsService.calculatePoints(new BigDecimal("51")).intValue());
        assertEquals(50, rewardsService.calculatePoints(new BigDecimal("100")).intValue());
        assertEquals(52, rewardsService.calculatePoints(new BigDecimal("101")).intValue());
    }

    @Test
    public void testCalculateRewardsForCustomer() {
        Long customerId = 1L;
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("120"));
        transaction.setTransactionDate(LocalDate.now());

        when(transactionRepository.findByCustomer(any(Customer.class)))
                .thenReturn(Collections.singletonList(transaction));
        when(customerRepository.findById(customerId))
                .thenReturn(Optional.of(new Customer()));

        RewardsSummary summary = rewardsService.calculateRewardsForCustomer(customerId);

        assertNotNull(summary);
        assertEquals(90, summary.getTotalPoints(), "Points is 90 = 50*1 + 20*2");
        verify(transactionRepository, times(1)).findByCustomer(any(Customer.class));
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    public void testCalculateRewardsForCustomerWithDates() {
        Long customerId = 1L;
        YearMonth start = YearMonth.now().minusMonths(1);
        YearMonth end = YearMonth.now();
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("150"));
        transaction.setTransactionDate(LocalDate.now());

        when(transactionRepository.findByCustomerIdAndTransactionDateBetween(eq(customerId), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(transaction));

        RewardsSummary summary = rewardsService.calculateRewardsForCustomer(customerId, start, end);

        assertNotNull(summary);
        assertTrue(summary.getMonthlyPoints().values().stream().mapToInt(Integer::intValue).sum() > 0);
        assertEquals(150, summary.getTotalPoints(), "Points is 150 = 50*1 + 50*2");
        verify(transactionRepository, times(1)).findByCustomerIdAndTransactionDateBetween(eq(customerId), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    public void testCalculateRewardsForMultipleTransactionsAcrossMonths() {
        Long customerId = 1L;
        YearMonth start = YearMonth.now().minusMonths(2);
        YearMonth end = YearMonth.now();

        Transaction transaction1 = new Transaction();
        transaction1.setAmount(new BigDecimal("120"));
        transaction1.setTransactionDate(start.atDay(1));

        Transaction transaction2 = new Transaction();
        transaction2.setAmount(new BigDecimal("150"));
        transaction2.setTransactionDate(end.atDay(1));

        when(transactionRepository.findByCustomerIdAndTransactionDateBetween(eq(customerId), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Arrays.asList(transaction1, transaction2));

        RewardsSummary summary = rewardsService.calculateRewardsForCustomer(customerId, start, end);

        assertNotNull(summary);
        assertEquals(240, summary.getTotalPoints(), "Overall 90 + 150 = 240");
        assertEquals(2, summary.getMonthlyPoints().size(), "Two months for the customer 1");
    }

    @Test
    public void testCalculateRewardsForNonExistentCustomer() {
        Long customerId = 999L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> {
            rewardsService.calculateRewardsForCustomer(customerId);
        });

        verify(customerRepository, times(1)).findById(customerId);
        verify(transactionRepository, never()).findByCustomer(any(Customer.class));
    }

    @Test
    public void testCalculateRewardsWithNoTransactions() {
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(new Customer()));
        when(transactionRepository.findByCustomer(any(Customer.class))).thenReturn(Collections.emptyList());

        RewardsSummary summary = rewardsService.calculateRewardsForCustomer(customerId);

        assertNotNull(summary);
        assertEquals(0, summary.getTotalPoints(), "0 points for no transaction");
        assertTrue(summary.getMonthlyPoints().isEmpty());
    }

}

