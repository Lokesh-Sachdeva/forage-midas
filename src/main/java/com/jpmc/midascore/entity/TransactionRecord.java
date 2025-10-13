package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TransactionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserRecord sender;
    
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private UserRecord recipient;
    
    @Column(nullable = false)
    private float amount;
    
    @Column(nullable = false)
    private float incentive;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    protected TransactionRecord() {}
    
    public TransactionRecord(UserRecord sender, UserRecord recipient, float amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }
    
    public TransactionRecord(UserRecord sender, UserRecord recipient, float amount, float incentive) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.incentive = incentive;
        this.timestamp = LocalDateTime.now();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public UserRecord getSender() {
        return sender;
    }
    
    public void setSender(UserRecord sender) {
        this.sender = sender;
    }
    
    public UserRecord getRecipient() {
        return recipient;
    }
    
    public void setRecipient(UserRecord recipient) {
        this.recipient = recipient;
    }
    
    public float getAmount() {
        return amount;
    }
    
    public void setAmount(float amount) {
        this.amount = amount;
    }
    
    public float getIncentive() {
        return incentive;
    }
    
    public void setIncentive(float incentive) {
        this.incentive = incentive;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "TransactionRecord{" +
                "id=" + id +
                ", sender=" + (sender != null ? sender.getName() : "null") +
                ", recipient=" + (recipient != null ? recipient.getName() : "null") +
                ", amount=" + amount +
                ", incentive=" + incentive +
                ", timestamp=" + timestamp +
                '}';
    }
}
