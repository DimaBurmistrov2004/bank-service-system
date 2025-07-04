package analyticsSystem;

import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.entity.Transaction;
import gigabank.accountmanagement.entity.TransactionType;
import gigabank.accountmanagement.entity.User;
import gigabank.accountmanagement.service.AnalyticsService;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@DisplayName("Тесты для класса аналитики")
public class AnalyticsServiceTest {

    private BankAccount emptyAccount;
    private BankAccount account;
    private AnalyticsService service;
    private User emptyUser;
    private User user;
    private Set<String> categories;

    @BeforeEach
    public void setValues() {
        emptyAccount = new BankAccount("ACC-123", new ArrayList<>());
        account = new BankAccount("ACC-456", null);
        service = new AnalyticsService();
        user = new User();

        Transaction sportPayment1 = new Transaction("32", new BigDecimal("100"),
                TransactionType.PAYMENT, "Sport", LocalDateTime.now().minusDays(1));

        Transaction oldSportPayment = new Transaction("75", new BigDecimal("1000"),
                TransactionType.PAYMENT, "Sport", LocalDateTime.now().minusMonths(2));

        Transaction sportPayment2 = new Transaction("35", new BigDecimal("1100"),
                TransactionType.PAYMENT, "Sport", LocalDateTime.now().minusDays(1));

        Transaction oldPayment = new Transaction("11", new BigDecimal("570"),
                TransactionType.PAYMENT, "Food", LocalDateTime.now().minusMonths(3));

        Transaction deposit = new Transaction("43", new BigDecimal("700"),
                TransactionType.DEPOSIT, "People", LocalDateTime.now().minusDays(10));

        account.setTransactions(List.of(sportPayment1, oldSportPayment, sportPayment2,
                oldPayment, deposit));

        categories = new TreeSet<>();
        categories.add("Food");
        categories.add("Sport");
        user.setBankAccounts(List.of(account));
    }

    @Test
    @DisplayName("Первый метод с пустым аккаунтом")
    public void getMonthlySpendingByCategory_BankAccountIsNull_ReturnZero() {
        BigDecimal result = service.getMonthlySpendingByCategory(emptyAccount, "Sport");
        Assertions.assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @DisplayName("Первый метод с пустой категорией")
    public void getMonthlySpendingByCategory_CategoryIsEmpty_ReturnZero() {
        BigDecimal result = service.getMonthlySpendingByCategory(account, "");
        Assertions.assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @DisplayName("Первый метод с корректными входными данными")
    public void getMonthlySpendingByCategory_ShouldBeReturnAmount() {
        BigDecimal result = service.getMonthlySpendingByCategory(account, "Sport");
        Assertions.assertEquals(new BigDecimal(1100), result);
    }

    @Test
    @DisplayName("Второй метод с пустым пользователем")
    public void getMonthlySpendingByCategories_UserIsNull_ReturnEmptyMap() {
        Map<String, BigDecimal> result = service.getMonthlySpendingByCategories(emptyUser,categories);
        Assertions.assertEquals(Collections.emptyMap(), result);
    }

    @Test
    @DisplayName("Второй метод с пустыми категориями")
    public void getMonthlySpendingByCategories_CategoriesIsNull_ReturnEmptyMap() {
        Map<String, BigDecimal> result = service.getMonthlySpendingByCategories(user,
                Collections.emptySet());
        Assertions.assertEquals(Collections.emptyMap(), result);
    }

    @Test
    @DisplayName("Второй метод с корректыми входными данными")
    public void getMonthlySpendingByCategories_ShouldBeReturnMap() {
        Map<String, BigDecimal> result = service.getMonthlySpendingByCategories(user, categories);
        Assertions.assertEquals(1, result.size());

        Assertions.assertEquals(new BigDecimal("1100"), result.get("Sport"));

        Assertions.assertTrue(result.containsKey("Sport"));

    }

    @Test
    @DisplayName("Третий метод с пустым пользователем")
    public void getTransactionHistorySortedByAmount_UserIsNull_ReturnEmptyMap() {
        LinkedHashMap<String, List<Transaction>> result = service.getTransactionHistorySortedByAmount(emptyUser);
        Assertions.assertEquals(new LinkedHashMap<>(), result);
    }

    @Test
    @DisplayName("Третий метод с корректными входными данными")
    public void getTransactionHistorySortedByAmount_ShouldBeReturnMap() {
        LinkedHashMap<String, List<Transaction>> result = service.getTransactionHistorySortedByAmount(user);
        Assertions.assertEquals(3, result.size());

        Assertions.assertEquals(new BigDecimal("2100"), result.get("Sport"));
        Assertions.assertEquals(new BigDecimal("570"), result.get("Food"));
        Assertions.assertEquals(new BigDecimal("700"), result.get("People"));
    }

    @Test
    @DisplayName("Четвертый метод с пустым пользователем")
    public void getLastNTransaction_UserIsNull_ReturnEmptyList() {
        List<Transaction> result = service.getLastNTransaction(emptyUser, 1);

        Assertions.assertEquals(new ArrayList<>(), result);
    }

    @Test
    @DisplayName("Четвертый метод с корректными входными данными")
    public void getLastNTransaction_ShouldBeReturnList() {
        List<Transaction> result = service.getLastNTransaction(user, 2);

        Assertions.assertEquals(2, result.size());

        Assertions.assertEquals("32", result.get(0).getId());
    }

    @Test
    @DisplayName("Пятый метод с пустым пользователем")
    public void getTopNLargestTransactions_UserIsNull_ReturnEmptyQueue() {
        PriorityQueue<Transaction> result = service.getTopNLargestTransactions(emptyUser,1);
        PriorityQueue<Transaction> emptyQueue = new PriorityQueue<>();

        Assertions.assertEquals(new ArrayList<>(emptyQueue), new ArrayList<>(result));
    }

    @Test
    @DisplayName("Пятый метод с корректными входными данными")
    public void getTopNLargestTransactions_ShouldReturnQueue() {
        PriorityQueue<Transaction> result = service.getTopNLargestTransactions(user, 3);

        Assertions.assertEquals(3, result.size());

        ArrayList<BigDecimal> amount = new ArrayList<>();
        while (!result.isEmpty()) {
            amount.add(result.poll().getValue());
        }

        Assertions.assertEquals(new BigDecimal("1100"), amount.get(0));
        Assertions.assertEquals(new BigDecimal("1000"), amount.get(1));
        Assertions.assertEquals(new BigDecimal("750"), amount.get(2));
    }
}
