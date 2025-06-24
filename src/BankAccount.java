import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BankAccount {

    private String accountNumber;
    private BigDecimal balance = BigDecimal.ZERO;
    private User owner;
    private List<Transaction> transactions = new ArrayList<>();

    public BankAccount(User owner, String accountNumber) {
        this.owner = owner;
        this.accountNumber = accountNumber;
    }

    // Метод для пополнение счет
    public BigDecimal deposit(BigDecimal amount) {
        balance = balance.add(amount);

        // Добавление операции в список
        Transaction deposit = new Transaction(amount, "DEPOSIT", LocalDateTime.now());
        this.addTransaction(deposit);

        return balance;
    }

    // Метод для снятия средств со счета
    public BigDecimal withdraw(BigDecimal amount) {

        // Проверка достаточно ли средств на балансе
        if (balance.compareTo(amount) < 0) {
            System.out.println("На счете недостаточно средств");
        }

        balance = balance.subtract(amount);

        // Добавление операции в список
        Transaction withdraw = new Transaction(amount, "WITHDRAW", LocalDateTime.now());
        this.addTransaction(withdraw);

        return balance;
    }

    // Метод для добавления транзакции в историю
    public List<Transaction> addTransaction(Transaction transaction) {
        transactions.add(transaction);
        return transactions;
    }

// Геттеры и сеттеры
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
