package com.jpmc.midascore.component;

import com.jpmc.midascore.debug.BalanceChecker;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.service.TransactionService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionListener {
    private final TransactionService transactionService;
    private final BalanceChecker balanceChecker;
    
    public TransactionListener(TransactionService transactionService, BalanceChecker balanceChecker) {
        this.transactionService = transactionService;
        this.balanceChecker = balanceChecker;
    }
    
    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core-group")
    public void handleTransaction(Transaction transaction) {
        System.out.println("Received transaction: " + transaction);
        
        boolean success = transactionService.processTransaction(transaction);
        if (success) {
            System.out.println("Transaction processed successfully");
        } else {
            System.out.println("Transaction rejected");
        }
        
        // Debug: Check waldorf's balance after each transaction
        balanceChecker.checkWaldorfBalance();
    }
}
