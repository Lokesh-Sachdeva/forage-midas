package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TransactionService {
    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final IncentiveService incentiveService;
    
    public TransactionService(UserRepository userRepository, 
                            TransactionRecordRepository transactionRecordRepository,
                            IncentiveService incentiveService) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
        this.incentiveService = incentiveService;
    }
    
    @Transactional
    public boolean processTransaction(Transaction transaction) {
        // Validate sender exists
        Optional<UserRecord> senderOpt = userRepository.findById(transaction.getSenderId());
        if (senderOpt.isEmpty()) {
            System.out.println("Invalid sender ID: " + transaction.getSenderId());
            return false;
        }
        
        // Validate recipient exists
        Optional<UserRecord> recipientOpt = userRepository.findById(transaction.getRecipientId());
        if (recipientOpt.isEmpty()) {
            System.out.println("Invalid recipient ID: " + transaction.getRecipientId());
            return false;
        }
        
        UserRecord sender = senderOpt.get();
        UserRecord recipient = recipientOpt.get();
        
        // Check if sender has sufficient balance
        if (sender.getBalance() < transaction.getAmount()) {
            System.out.println("Insufficient balance for sender " + sender.getName() + 
                             ". Required: " + transaction.getAmount() + 
                             ", Available: " + sender.getBalance());
            return false;
        }
        
        // Fetch incentive from external API
        Incentive incentive = incentiveService.getIncentive(transaction);
        float incentiveAmount = incentive != null ? incentive.getAmount() : 0.0f;

        // Process the transaction with incentive (sender not charged for incentive)
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);
        
        // Save updated user balances
        userRepository.save(sender);
        userRepository.save(recipient);
        
        // Create and save transaction record (store incentive)
        TransactionRecord transactionRecord = new TransactionRecord(sender, recipient, transaction.getAmount(), incentiveAmount);
        transactionRecordRepository.save(transactionRecord);
        
        System.out.println("Transaction processed successfully with incentive=" + incentiveAmount + ": " + transaction);
        return true;
    }
}
