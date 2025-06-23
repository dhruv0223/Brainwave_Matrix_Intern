import java.util.List;
import java.util.Scanner;

public class ATMInterface {
    private Scanner scanner;
    private Bank bank;
    private Account currentAccount;
    
    public ATMInterface() {
        scanner = new Scanner(System.in);
        bank = Bank.getInstance();
        currentAccount = null;
    }
    
    public void start() {
        System.out.println("=================================");
        System.out.println("    WELCOME TO SECURE ATM");
        System.out.println("=================================");
        
        while (true) {
            if (currentAccount == null) {
                if (!login()) {
                    System.out.println("Exiting ATM. Thank you!");
                    break;
                }
            } else {
                showMainMenu();
            }
        }
        
        scanner.close();
    }
    
    private boolean login() {
        System.out.println("\n--- LOGIN ---");
        System.out.println("Sample accounts for testing:");
        System.out.println("Account: 1234567890, PIN: 1234 (John Doe - $1500.00)");
        System.out.println("Account: 0987654321, PIN: 5678 (Jane Smith - $2500.00)");
        System.out.println("Account: 1111222233, PIN: 9999 (Bob Johnson - $750.50)");
        System.out.println();
        
        System.out.print("Enter account number (or 'exit' to quit): ");
        String accountNumber = scanner.nextLine().trim();
        
        if (accountNumber.equalsIgnoreCase("exit")) {
            return false;
        }
        
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine().trim();
        
        currentAccount = bank.authenticateUser(accountNumber, pin);
        
        if (currentAccount != null) {
            System.out.println("\nLogin successful!");
            System.out.println("Welcome, " + currentAccount.getAccountHolderName() + "!");
            return true;
        } else {
            System.out.println("Invalid account number or PIN. Please try again.");
            return true; // Continue to allow retry
        }
    }
    
    private void showMainMenu() {
        System.out.println("\n=================================");
        System.out.println("           MAIN MENU");
        System.out.println("=================================");
        System.out.println("1. Check Balance");
        System.out.println("2. Withdraw Cash");
        System.out.println("3. Deposit Cash");
        System.out.println("4. Transfer Money");
        System.out.println("5. Transaction History");
        System.out.println("6. Change PIN");
        System.out.println("7. Logout");
        System.out.println("8. Exit");
        System.out.println("=================================");
        System.out.print("Select an option (1-8): ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                checkBalance();
                break;
            case "2":
                withdrawCash();
                break;
            case "3":
                depositCash();
                break;
            case "4":
                transferMoney();
                break;
            case "5":
                showTransactionHistory();
                break;
            case "6":
                changePin();
                break;
            case "7":
                logout();
                break;
            case "8":
                currentAccount = null;
                System.out.println("Thank you for using our ATM!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
    
    private void checkBalance() {
        System.out.println("\n--- BALANCE INQUIRY ---");
        System.out.printf("Account: %s\n", currentAccount.getAccountNumber());
        System.out.printf("Account Holder: %s\n", currentAccount.getAccountHolderName());
        System.out.printf("Current Balance: $%.2f\n", currentAccount.getBalance());
        
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private void withdrawCash() {
        System.out.println("\n--- CASH WITHDRAWAL ---");
        System.out.printf("Current Balance: $%.2f\n", currentAccount.getBalance());
        System.out.println("Available denominations: $20, $50, $100");
        System.out.print("Enter withdrawal amount: $");
        
        try {
            double amount = Double.parseDouble(scanner.nextLine().trim());
            
            if (amount <= 0) {
                System.out.println("Invalid amount. Please enter a positive number.");
                return;
            }
            
            if (amount % 20 != 0) {
                System.out.println("Amount must be in multiples of $20.");
                return;
            }
            
            if (currentAccount.withdraw(amount)) {
                System.out.printf("Withdrawal successful!\n");
                System.out.printf("Amount withdrawn: $%.2f\n", amount);
                System.out.printf("Remaining balance: $%.2f\n", currentAccount.getBalance());
                System.out.println("Please take your cash.");
            } else {
                System.out.println("Insufficient funds or invalid amount.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
        
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private void depositCash() {
        System.out.println("\n--- CASH DEPOSIT ---");
        System.out.printf("Current Balance: $%.2f\n", currentAccount.getBalance());
        System.out.print("Enter deposit amount: $");
        
        try {
            double amount = Double.parseDouble(scanner.nextLine().trim());
            
            if (amount <= 0) {
                System.out.println("Invalid amount. Please enter a positive number.");
                return;
            }
            
            currentAccount.deposit(amount);
            System.out.printf("Deposit successful!\n");
            System.out.printf("Amount deposited: $%.2f\n", amount);
            System.out.printf("New balance: $%.2f\n", currentAccount.getBalance());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
        
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private void transferMoney() {
        System.out.println("\n--- MONEY TRANSFER ---");
        System.out.printf("Current Balance: $%.2f\n", currentAccount.getBalance());
        System.out.print("Enter target account number: ");
        
        String targetAccountNumber = scanner.nextLine().trim();
        
        if (targetAccountNumber.equals(currentAccount.getAccountNumber())) {
            System.out.println("Cannot transfer to the same account.");
            return;
        }
        
        if (!bank.accountExists(targetAccountNumber)) {
            System.out.println("Target account does not exist.");
            return;
        }
        
        Account targetAccount = bank.getAccount(targetAccountNumber);
        System.out.printf("Target Account Holder: %s\n", targetAccount.getAccountHolderName());
        System.out.print("Enter transfer amount: $");
        
        try {
            double amount = Double.parseDouble(scanner.nextLine().trim());
            
            if (amount <= 0) {
                System.out.println("Invalid amount. Please enter a positive number.");
                return;
            }
            
            if (currentAccount.transfer(targetAccount, amount)) {
                System.out.printf("Transfer successful!\n");
                System.out.printf("Amount transferred: $%.2f\n", amount);
                System.out.printf("To: %s (%s)\n", targetAccount.getAccountHolderName(), targetAccountNumber);
                System.out.printf("Remaining balance: $%.2f\n", currentAccount.getBalance());
            } else {
                System.out.println("Transfer failed. Insufficient funds.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
        
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private void showTransactionHistory() {
        System.out.println("\n--- TRANSACTION HISTORY ---");
        List<Transaction> history = currentAccount.getTransactionHistory();
        
        if (history.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            System.out.println("TYPE         | AMOUNT   | BALANCE    | DATE & TIME         | DESCRIPTION");
            System.out.println("-------------|----------|------------|---------------------|------------------");
            
            // Show last 10 transactions
            int start = Math.max(0, history.size() - 10);
            for (int i = start; i < history.size(); i++) {
                System.out.println(history.get(i));
            }
            
            if (history.size() > 10) {
                System.out.printf("\nShowing last 10 of %d transactions.\n", history.size());
            }
        }
        
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private void changePin() {
        System.out.println("\n--- CHANGE PIN ---");
        System.out.print("Enter current PIN: ");
        String currentPin = scanner.nextLine().trim();
        
        if (!currentAccount.validatePin(currentPin)) {
            System.out.println("Incorrect current PIN.");
            return;
        }
        
        System.out.print("Enter new PIN (4 digits): ");
        String newPin = scanner.nextLine().trim();
        
        if (newPin.length() != 4 || !newPin.matches("\\d{4}")) {
            System.out.println("PIN must be exactly 4 digits.");
            return;
        }
        
        System.out.print("Confirm new PIN: ");
        String confirmPin = scanner.nextLine().trim();
        
        if (!newPin.equals(confirmPin)) {
            System.out.println("PINs do not match.");
            return;
        }
        
        currentAccount.changePin(newPin);
        System.out.println("PIN changed successfully!");
        
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private void logout() {
        System.out.println("\nLogging out...");
        System.out.println("Thank you, " + currentAccount.getAccountHolderName() + "!");
        currentAccount = null;
    }
}
