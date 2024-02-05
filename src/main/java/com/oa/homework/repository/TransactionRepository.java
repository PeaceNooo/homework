package com.oa.homework.repository;

import com.oa.homework.entity.Customer;
import com.oa.homework.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCustomerIdAndTransactionDateBetween(Long customerId, LocalDate start, LocalDate end);

    List<Transaction> findByCustomer(Customer customer);
}
