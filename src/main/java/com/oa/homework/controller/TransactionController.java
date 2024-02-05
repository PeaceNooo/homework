package com.oa.homework.controller;

import com.oa.homework.entity.Transaction;
import com.oa.homework.model.TransactionDto;
import com.oa.homework.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/batch")
    public ResponseEntity<?> addTransactions(@RequestBody List<TransactionDto> transactionInputs) {

        List<Transaction> savedTransactions = transactionService.addTransactions(transactionInputs);
        List<TransactionDto> responses = savedTransactions.stream()
                .map(transaction -> new TransactionDto(transaction.getCustomer().getId(), transaction.getAmount(), transaction.getTransactionDate()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<?> addTransaction(@RequestBody TransactionDto transactionInput) {
        Transaction savedTransaction = transactionService.addTransaction(transactionInput);

        // Create a response that includes the transaction details and the points earned
        TransactionDto response = new TransactionDto();
        response.setCustomerId(savedTransaction.getCustomer().getId());
        response.setAmount(savedTransaction.getAmount());
        response.setTransactionDate(savedTransaction.getTransactionDate());

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllTransactions() {
        List<TransactionDto> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

}

