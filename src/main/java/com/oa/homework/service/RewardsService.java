package com.oa.homework.service;

import com.oa.homework.entity.Customer;
import com.oa.homework.entity.Transaction;
import com.oa.homework.exception.CustomerNotFoundException;
import com.oa.homework.model.RewardsSummary;
import com.oa.homework.repository.CustomerRepository;
import com.oa.homework.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RewardsService {

    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public RewardsService(CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    public Integer calculatePoints(BigDecimal amount) {
        int points = 0;
        if (amount.compareTo(new BigDecimal("100")) > 0) {
            points += (amount.subtract(new BigDecimal("100"))).multiply(new BigDecimal("2")).intValue();
            amount = new BigDecimal("100");
        }
        if (amount.compareTo(new BigDecimal("50")) > 0) {
            points += (amount.subtract(new BigDecimal("50"))).intValue();
        }
        return points;
    }

    public RewardsSummary calculateRewardsForCustomer(Long customerId, YearMonth start, YearMonth end) {
        // Find all transaction in a time window
        LocalDate startDate = start.atDay(1);
        LocalDate endDate = end.atEndOfMonth();
        List<Transaction> transactions = transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, startDate, endDate);

        log.info("Over all " + transactions.size() + " transactions during " + startDate + " and " + endDate);

        // Calculate points
        Map<LocalDate, Integer> monthlyPoints = transactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> YearMonth.from(transaction.getTransactionDate()).atDay(1),
                        Collectors.summingInt(transaction -> calculatePoints(transaction.getAmount()))
                ));
        int totalPoints = monthlyPoints.values().stream().mapToInt(Integer::intValue).sum();
        log.info("Calculated rewards for customer ID {}: Monthly Points: {}, Total Points: {}", customerId, monthlyPoints, totalPoints);

        return new RewardsSummary(monthlyPoints, totalPoints);
    }

    public RewardsSummary calculateRewardsForCustomer(Long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            log.info("Customer not found with ID: " + customerId);
            throw new CustomerNotFoundException("Student not found: " + customerId + " can not be deleted");
        }

        List<Transaction> transactions = transactionRepository.findByCustomer(customer.get());
        log.info("Over all " + transactions.size() + " transactions for customer " + customer.get().getName());

        // Calculate points
        Map<LocalDate, Integer> monthlyPoints = transactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> YearMonth.from(transaction.getTransactionDate()).atDay(1),
                        Collectors.summingInt(transaction -> calculatePoints(transaction.getAmount()))
                ));
        int totalPoints = monthlyPoints.values().stream().mapToInt(Integer::intValue).sum();
        log.info("Calculated rewards for customer ID {}: Monthly Points: {}, Total Points: {}", customerId, monthlyPoints, totalPoints);

        return new RewardsSummary(monthlyPoints, totalPoints);
    }

}

