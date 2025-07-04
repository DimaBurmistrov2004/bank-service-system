package gigabank.accountmanagement.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Информация о банковском счете пользователя
 */
@Data
public class BankAccount {
    private String id;
    private BigDecimal balance;
    private User owner;
    private List<Transaction> transactions;

    public BankAccount(String id, List<Transaction> transactions) {
        this.id = id;
        this.transactions = transactions;
    }
}
