import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    private String id;
    private BigDecimal amount;
    private String type;
    private LocalDateTime date;
    private BankAccount sourceAccount;
    private BankAccount targetAccount;

    public Transaction(BigDecimal amount, String type, LocalDateTime date) {
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    public Transaction(BigDecimal amount, String type, LocalDateTime date,
                       BankAccount sourceAccount, BankAccount targetAccount) {
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
    }
// Геттеры и сеттеры
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BankAccount getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(BankAccount sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public BankAccount getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(BankAccount targetAccount) {
        this.targetAccount = targetAccount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", date=" + date +
                ", sourceAccount=" + sourceAccount +
                ", targetAccount=" + targetAccount +
                '}';
    }
}
