package com.jpmc.midascore.debug;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BalanceChecker {
    @Autowired
    private DatabaseConduit databaseConduit;
    
    public void checkWaldorfBalance() {
        UserRecord waldorf = databaseConduit.findUserByName("waldorf");
        if (waldorf != null) {
            System.out.println("=== WALDORF BALANCE DEBUG ===");
            System.out.println("Waldorf's current balance: " + waldorf.getBalance());
            System.out.println("Waldorf's balance (rounded down): " + (int) waldorf.getBalance());
            System.out.println("=== END DEBUG ===");
        } else {
            System.out.println("Waldorf not found!");
        }
    }
}
