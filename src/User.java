import java.util.ArrayList;
import java.util.List;

public class User {

    private String id;
    private String name;
    private List<BankAccount> accounts;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.accounts = new ArrayList<>();
    }

    // Метод для добавления аккаунта к общему списку
    public List<BankAccount> addAccounts(BankAccount account) {
        accounts.add(account);
        return accounts;
    }

// Геттеры и сеттеры
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BankAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<BankAccount> accounts) {
        this.accounts = accounts;
    }
}
