import java.util.HashMap;
import java.util.Map;

public class ATMBank {
    private Map<String, ATMAccount> accounts;
    private static ATMBank instance;
    
    private ATMBank() {
        accounts = new HashMap<>();
        initializeSampleAccounts();
    }
    
    public static ATMBank getInstance() {
        if (instance == null) {
            instance = new ATMBank();
        }
        return instance;
    }
    
    private void initializeSampleAccounts() {
        // Create diverse sample accounts for testing
        accounts.put("1234567890", new ATMAccount("1234567890", "1234", 1500.00, "John Doe"));
        accounts.put("0987654321", new ATMAccount("0987654321", "5678", 2500.00, "Jane Smith"));
        accounts.put("1111222233", new ATMAccount("1111222233", "9999", 750.50, "Bob Johnson"));
        accounts.put("4444555566", new ATMAccount("4444555566", "0000", 3200.75, "Alice Williams"));
        accounts.put("7777888899", new ATMAccount("7777888899", "1111", 890.25, "Michael Brown"));
        accounts.put("2222333344", new ATMAccount("2222333344", "2222", 4500.00, "Sarah Davis"));
        accounts.put("5555666677", new ATMAccount("5555666677", "3333", 1200.80, "David Wilson"));
        accounts.put("8888999900", new ATMAccount("8888999900", "4444", 2100.45, "Emma Garcia"));
    }
    
    public ATMAccount getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }
    
    public boolean accountExists(String accountNumber) {
        return accounts.containsKey(accountNumber);
    }
    
    public ATMAccount authenticateUser(String accountNumber, String pin) {
        ATMAccount account = accounts.get(accountNumber);
        if (account != null && account.validatePin(pin)) {
            return account;
        }
        return null;
    }
}
