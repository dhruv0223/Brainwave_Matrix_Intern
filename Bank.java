import java.util.HashMap;
import java.util.Map;

public class Bank {
    private Map<String, Account> accounts;
    private static Bank instance;
    
    private Bank() {
        accounts = new HashMap<>();
        initializeSampleAccounts();
    }
    
    public static Bank getInstance() {
        if (instance == null) {
            instance = new Bank();
        }
        return instance;
    }
    
    private void initializeSampleAccounts() {
        // Create some sample accounts for testing
        accounts.put("1234567890", new Account("1234567890", "1234", 1500.00, "John Doe"));
        accounts.put("0987654321", new Account("0987654321", "5678", 2500.00, "Jane Smith"));
        accounts.put("1111222233", new Account("1111222233", "9999", 750.50, "Bob Johnson"));
    }
    
    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }
    
    public boolean accountExists(String accountNumber) {
        return accounts.containsKey(accountNumber);
    }
    
    public void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }
    
    public Account authenticateUser(String accountNumber, String pin) {
        Account account = accounts.get(accountNumber);
        if (account != null && account.validatePin(pin)) {
            return account;
        }
        return null;
    }
}
