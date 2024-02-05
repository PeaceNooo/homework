package com.oa.homework.service;

import com.oa.homework.entity.Customer;
import com.oa.homework.entity.Transaction;
import com.oa.homework.exception.CustomerNotFoundException;
import com.oa.homework.model.TransactionDto;
import com.oa.homework.repository.CustomerRepository;
import com.oa.homework.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransactionService {

    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    public Transaction addTransaction(TransactionDto transactionDto) {
        Optional<Customer> customer = customerRepository.findById(transactionDto.getCustomerId());
        if (!customer.isPresent()) {
            throw new CustomerNotFoundException("Customer not found with ID: " + transactionDto.getCustomerId());
        }

        // 将DTO转换为实体
        Transaction transaction = new Transaction();
        transaction.setCustomer(customer.get());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setTransactionDate(transactionDto.getTransactionDate());
        transaction.setCustomer(customer.get());
//        System.out.println(transaction);
//        log.info(transaction.toString());
        // Save the transaction to the database
        return transactionRepository.save(transaction);
    }

    public List<Transaction> addTransactions(List<TransactionDto> transactionDtos) {
        List<Transaction> transactionsToSave = new ArrayList<>();

        for (TransactionDto dto : transactionDtos) {
            Optional<Customer> customer = customerRepository.findById(dto.getCustomerId());
            if (!customer.isPresent()) {
                throw new CustomerNotFoundException("Customer not found with ID: " + dto.getCustomerId());
            }

            Transaction transaction = new Transaction();
            transaction.setCustomer(customer.get());
            transaction.setAmount(dto.getAmount());
            transaction.setTransactionDate(dto.getTransactionDate());
            transactionsToSave.add(transaction);
        }

        return transactionRepository.saveAll(transactionsToSave);
    }

    public List<TransactionDto> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(transaction -> new TransactionDto(transaction.getCustomer().getId(), transaction.getAmount(), transaction.getTransactionDate()))
                .collect(Collectors.toList());
    }
}
