import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;

public class ATMDesktopGUI_Production extends JFrame {
    private ATMBank bank;
    private ATMAccount currentAccount;
    private StringBuilder inputBuffer;
    private String currentOperation;
    private boolean cardInserted;
    private boolean pinEntered;
    private String tempAccountNumber;
    
    // GUI Components
    private JPanel mainDisplayPanel;
    private JLabel displayLabel;
    private JLabel statusLabel;
    private JLabel cardStatusLabel;
    private JLabel sessionLabel;
    private JButton[] keypadButtons;
    private JButton[] menuButtons;
    private JButton insertCardButton;
    private JButton cancelButton;
    
    public ATMDesktopGUI_Production() {
        bank = ATMBank.getInstance();
        inputBuffer = new StringBuilder();
        currentOperation = "WELCOME";
        cardInserted = false;
        pinEntered = false;
        
        initializeGUI();
        setupEventHandlers();
        updateDisplay();
        updateStatus();
    }
    
    private void initializeGUI() {
        setTitle("Secure Bank ATM - Desktop Application v2.1.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main layout
        setLayout(new BorderLayout());
        
        // Left panel (Menu and Card Reader)
        JPanel leftPanel = createLeftPanel();
        add(leftPanel, BorderLayout.WEST);
        
        // Center panel (Main Display)
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
        
        // Right panel (Keypad and Status)
        JPanel rightPanel = createRightPanel();
        add(rightPanel, BorderLayout.EAST);
        
        // Set background color
        getContentPane().setBackground(new Color(41, 98, 255));
    }
    
    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(250, 800));
        leftPanel.setBackground(new Color(41, 98, 255));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Bank title
        JLabel bankTitle = new JLabel("SECURE BANK");
        bankTitle.setFont(new Font("Arial", Font.BOLD, 24));
        bankTitle.setForeground(Color.WHITE);
        
        // Menu buttons
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setOpaque(false);
        
        String[] menuItems = {"Check Balance", "Withdraw Cash", "Make Deposit", "Transaction History"};
        menuButtons = new JButton[menuItems.length];
        
        for (int i = 0; i < menuItems.length; i++) {
            menuButtons[i] = new JButton(menuItems[i]);
            menuButtons[i].setPreferredSize(new Dimension(200, 50));
            menuButtons[i].setMaximumSize(new Dimension(200, 50));
            menuButtons[i].setBackground(new Color(70, 130, 255));
            menuButtons[i].setForeground(Color.WHITE);
            menuButtons[i].setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            menuButtons[i].setFocusPainted(false);
            menuButtons[i].setEnabled(false);
            menuPanel.add(menuButtons[i]);
            menuPanel.add(Box.createVerticalStrut(10));
        }
        
        // Card reader panel
        JPanel cardReaderPanel = new JPanel();
        cardReaderPanel.setLayout(new BorderLayout());
        cardReaderPanel.setBackground(new Color(30, 60, 180));
        cardReaderPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE), "CARD READER", 
            0, 0, new Font("Arial", Font.BOLD, 12), Color.WHITE));
        cardReaderPanel.setPreferredSize(new Dimension(200, 120));
        
        JLabel insertLabel = new JLabel("INSERT CARD");
        insertLabel.setForeground(Color.WHITE);
        insertLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        insertCardButton = new JButton("Insert Test Card");
        insertCardButton.setBackground(new Color(41, 98, 255));
        insertCardButton.setForeground(Color.WHITE);
        insertCardButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JLabel instructionLabel = new JLabel("◯ Insert card to begin");
        instructionLabel.setForeground(Color.LIGHT_GRAY);
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        
        cardReaderPanel.add(insertLabel, BorderLayout.NORTH);
        cardReaderPanel.add(insertCardButton, BorderLayout.CENTER);
        cardReaderPanel.add(instructionLabel, BorderLayout.SOUTH);
        
        leftPanel.add(bankTitle, BorderLayout.NORTH);
        leftPanel.add(menuPanel, BorderLayout.CENTER);
        leftPanel.add(cardReaderPanel, BorderLayout.SOUTH);
        
        return leftPanel;
    }
    
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(41, 98, 255));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Main display
        mainDisplayPanel = new JPanel();
        mainDisplayPanel.setBackground(new Color(25, 25, 112));
        mainDisplayPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        mainDisplayPanel.setLayout(new BorderLayout());
        
        displayLabel = new JLabel();
        displayLabel.setForeground(Color.WHITE);
        displayLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        displayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        displayLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        mainDisplayPanel.add(displayLabel, BorderLayout.CENTER);
        centerPanel.add(mainDisplayPanel, BorderLayout.CENTER);
        
        return centerPanel;
    }
    
    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(300, 800));
        rightPanel.setBackground(new Color(41, 98, 255));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Keypad
        JPanel keypadPanel = new JPanel();
        keypadPanel.setLayout(new BorderLayout());
        keypadPanel.setBackground(Color.WHITE);
        keypadPanel.setBorder(BorderFactory.createTitledBorder("Input Keypad"));
        
        JPanel buttonGrid = new JPanel(new GridLayout(4, 3, 5, 5));
        buttonGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] keypadLabels = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "CLR", "0", "ENT"};
        keypadButtons = new JButton[keypadLabels.length];
        
        for (int i = 0; i < keypadLabels.length; i++) {
            keypadButtons[i] = new JButton(keypadLabels[i]);
            keypadButtons[i].setPreferredSize(new Dimension(60, 40));
            keypadButtons[i].setBackground(Color.LIGHT_GRAY);
            keypadButtons[i].setBorder(BorderFactory.createRaisedBevelBorder());
            keypadButtons[i].setFocusPainted(false);
            buttonGrid.add(keypadButtons[i]);
        }
        
        cancelButton = new JButton("CANCEL TRANSACTION");
        cancelButton.setBackground(new Color(220, 20, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setPreferredSize(new Dimension(250, 40));
        cancelButton.setBorder(BorderFactory.createRaisedBevelBorder());
        cancelButton.setFocusPainted(false);
        
        keypadPanel.add(buttonGrid, BorderLayout.CENTER);
        keypadPanel.add(cancelButton, BorderLayout.SOUTH);
        
        // Status panel
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BorderLayout());
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setBorder(BorderFactory.createTitledBorder("System Status"));
        statusPanel.setPreferredSize(new Dimension(250, 150));
        
        JPanel statusContent = new JPanel();
        statusContent.setLayout(new BoxLayout(statusContent, BoxLayout.Y_AXIS));
        statusContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        statusLabel = new JLabel("Connection: Online");
        statusLabel.setForeground(new Color(34, 139, 34));
        
        cardStatusLabel = new JLabel("Card Status: Not Inserted");
        cardStatusLabel.setForeground(Color.GRAY);
        
        sessionLabel = new JLabel("Session: Inactive");
        sessionLabel.setForeground(Color.GRAY);
        
        statusContent.add(statusLabel);
        statusContent.add(cardStatusLabel);
        statusContent.add(sessionLabel);
        
        statusPanel.add(statusContent, BorderLayout.CENTER);
        
        rightPanel.add(keypadPanel, BorderLayout.CENTER);
        rightPanel.add(statusPanel, BorderLayout.SOUTH);
        
        return rightPanel;
    }
    
    private void setupEventHandlers() {
        // Insert card button
        insertCardButton.addActionListener(e -> {
            if (!cardInserted) {
                cardInserted = true;
                currentOperation = "ENTER_ACCOUNT";
                updateDisplay();
                updateStatus();
                enableMenuButtons(false);
            }
        });
        
        // Keypad buttons
        for (int i = 0; i < keypadButtons.length; i++) {
            final String buttonText = keypadButtons[i].getText();
            keypadButtons[i].addActionListener(e -> handleKeypadInput(buttonText));
        }
        
        // Menu buttons
        menuButtons[0].addActionListener(e -> handleMenuAction("CHECK_BALANCE"));
        menuButtons[1].addActionListener(e -> handleMenuAction("WITHDRAW"));
        menuButtons[2].addActionListener(e -> handleMenuAction("DEPOSIT"));
        menuButtons[3].addActionListener(e -> handleMenuAction("HISTORY"));
        
        // Cancel button
        cancelButton.addActionListener(e -> {
            resetSession();
            updateDisplay();
            updateStatus();
        });
    }
    
    private void handleKeypadInput(String input) {
        switch (input) {
            case "CLR":
                inputBuffer.setLength(0);
                updateDisplay();
                break;
            case "ENT":
                processInput();
                break;
            default:
                if (inputBuffer.length() < 20) {
                    inputBuffer.append(input);
                    updateDisplay();
                }
                break;
        }
    }
    
    private void processInput() {
        String input = inputBuffer.toString();
        inputBuffer.setLength(0);
        
        switch (currentOperation) {
            case "ENTER_ACCOUNT":
                if (input.length() >= 8) {
                    currentOperation = "ENTER_PIN";
                    tempAccountNumber = input;
                    updateDisplay();
                } else {
                    ATMNotification.showError(this, "Invalid Account Number", "Account number must be at least 8 digits");
                }
                break;
                
            case "ENTER_PIN":
                if (input.length() == 4) {
                    currentAccount = bank.authenticateUser(tempAccountNumber, input);
                    if (currentAccount != null) {
                        pinEntered = true;
                        currentOperation = "MAIN_MENU";
                        enableMenuButtons(true);
                        ATMNotification.showSuccess(this, "Login Successful", 
                            "Welcome, " + currentAccount.getAccountHolderName() + "!");
                        updateDisplay();
                        updateStatus();
                    } else {
                        ATMNotification.showError(this, "Authentication Failed", "Invalid account number or PIN");
                        resetSession();
                        updateDisplay();
                        updateStatus();
                    }
                } else {
                    ATMNotification.showError(this, "Invalid PIN", "PIN must be exactly 4 digits");
                }
                break;
                
            case "WITHDRAW_AMOUNT":
                try {
                    double amount = Double.parseDouble(input);
                    if (amount > 0 && amount % 20 == 0) {
                        if (currentAccount.withdraw(amount)) {
                            DecimalFormat df = new DecimalFormat("#,##0.00");
                            ATMNotification.showSuccess(this, "Withdrawal Successful", 
                                "Amount: $" + df.format(amount) + "\nRemaining Balance: $" + df.format(currentAccount.getBalance()));
                            currentOperation = "MAIN_MENU";
                            updateDisplay();
                        } else {
                            ATMNotification.showError(this, "Withdrawal Failed", "Insufficient funds");
                        }
                    } else {
                        ATMNotification.showError(this, "Invalid Amount", "Amount must be positive and in multiples of $20");
                    }
                } catch (NumberFormatException e) {
                    ATMNotification.showError(this, "Invalid Input", "Please enter a valid number");
                }
                break;
                
            case "DEPOSIT_AMOUNT":
                try {
                    double amount = Double.parseDouble(input);
                    if (amount > 0) {
                        currentAccount.deposit(amount);
                        DecimalFormat df = new DecimalFormat("#,##0.00");
                        ATMNotification.showSuccess(this, "Deposit Successful", 
                            "Amount: $" + df.format(amount) + "\nNew Balance: $" + df.format(currentAccount.getBalance()));
                        currentOperation = "MAIN_MENU";
                        updateDisplay();
                    } else {
                        ATMNotification.showError(this, "Invalid Amount", "Amount must be positive");
                    }
                } catch (NumberFormatException e) {
                    ATMNotification.showError(this, "Invalid Input", "Please enter a valid number");
                }
                break;
        }
    }
    
    private void handleMenuAction(String action) {
        if (currentAccount == null) return;
        
        switch (action) {
            case "CHECK_BALANCE":
                DecimalFormat df = new DecimalFormat("#,##0.00");
                ATMNotification.showSuccess(this, "Balance Inquiry", 
                    "Account: " + currentAccount.getAccountNumber() + "\n" +
                    "Holder: " + currentAccount.getAccountHolderName() + "\n" +
                    "Balance: $" + df.format(currentAccount.getBalance()));
                break;
                
            case "WITHDRAW":
                currentOperation = "WITHDRAW_AMOUNT";
                updateDisplay();
                break;
                
            case "DEPOSIT":
                currentOperation = "DEPOSIT_AMOUNT";
                updateDisplay();
                break;
                
            case "HISTORY":
                showTransactionHistory();
                break;
        }
    }
    
    private void showTransactionHistory() {
        List<ATMTransaction> history = currentAccount.getTransactionHistory();
        
        JDialog historyDialog = new JDialog(this, "Transaction History", true);
        historyDialog.setSize(800, 400);
        historyDialog.setLocationRelativeTo(this);
        
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        textArea.setEditable(false);
        
        StringBuilder sb = new StringBuilder();
        sb.append("TRANSACTION HISTORY - ").append(currentAccount.getAccountHolderName()).append("\n");
        sb.append("Account: ").append(currentAccount.getAccountNumber()).append("\n\n");
        sb.append("TYPE         | AMOUNT   | BALANCE    | DATE & TIME         | DESCRIPTION\n");
        sb.append("-------------|----------|------------|---------------------|--------------------\n");
        
        int start = Math.max(0, history.size() - 10);
        for (int i = start; i < history.size(); i++) {
            sb.append(history.get(i).toString()).append("\n");
        }
        
        if (history.size() > 10) {
            sb.append("\nShowing last 10 of ").append(history.size()).append(" transactions.");
        }
        
        textArea.setText(sb.toString());
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        historyDialog.add(scrollPane);
        historyDialog.setVisible(true);
    }
    
    private void updateDisplay() {
        String displayText = "";
        
        switch (currentOperation) {
            case "WELCOME":
                displayText = "<html><center><br><br>" +
                    "<div style='font-size: 48px; color: #4A90E2;'>💳</div><br>" +
                    "<div style='font-size: 24px; font-weight: bold;'>Welcome to Secure Bank</div><br>" +
                    "<div style='font-size: 16px;'>Please insert your card to begin</div><br><br>" +
                    "<div style='background-color: #4A90E2; height: 4px; width: 200px; margin: 0 auto;'></div><br>" +
                    "<div style='font-size: 12px; color: #CCCCCC;'>Insert card above ↑</div>" +
                    "</center></html>";
                break;
                
            case "ENTER_ACCOUNT":
                displayText = "<html><center><br><br>" +
                    "<div style='font-size: 20px; font-weight: bold;'>Enter Account Number</div><br><br>" +
                    "<div style='font-size: 16px;'>Account Number:</div>" +
                    "<div style='font-size: 18px; font-family: monospace; background-color: #333; padding: 10px; margin: 10px;'>" +
                    (inputBuffer.length() > 0 ? inputBuffer.toString() : "_") + "</div><br>" +
                    "<div style='font-size: 12px; color: #CCCCCC;'>Press ENT when complete</div>" +
                    "</center></html>";
                break;
                
            case "ENTER_PIN":
                displayText = "<html><center><br><br>" +
                    "<div style='font-size: 20px; font-weight: bold;'>Enter PIN</div><br><br>" +
                    "<div style='font-size: 16px;'>PIN:</div>" +
                    "<div style='font-size: 18px; font-family: monospace; background-color: #333; padding: 10px; margin: 10px;'>" +
                    "*".repeat(inputBuffer.length()) + "</div><br>" +
                    "<div style='font-size: 12px; color: #CCCCCC;'>Enter 4-digit PIN and press ENT</div>" +
                    "</center></html>";
                break;
                
            case "MAIN_MENU":
                DecimalFormat df = new DecimalFormat("#,##0.00");
                displayText = "<html><center><br>" +
                    "<div style='font-size: 18px; font-weight: bold;'>Welcome, " + 
                    (currentAccount != null ? currentAccount.getAccountHolderName() : "User") + "</div><br>" +
                    "<div style='font-size: 14px;'>Account: " + 
                    (currentAccount != null ? currentAccount.getAccountNumber() : "N/A") + "</div>" +
                    "<div style='font-size: 14px;'>Balance: $" + 
                    (currentAccount != null ? df.format(currentAccount.getBalance()) : "0.00") + "</div><br><br>" +
                    "<div style='font-size: 16px;'>Select a transaction from the menu</div><br>" +
                    "<div style='font-size: 12px; color: #CCCCCC;'>Use the buttons on the left</div>" +
                    "</center></html>";
                break;
                
            case "WITHDRAW_AMOUNT":
                displayText = "<html><center><br><br>" +
                    "<div style='font-size: 20px; font-weight: bold;'>Cash Withdrawal</div><br>" +
                    "<div style='font-size: 14px;'>Current Balance: $" + 
                    new DecimalFormat("#,##0.00").format(currentAccount.getBalance()) + "</div><br>" +
                    "<div style='font-size: 16px;'>Enter Amount:</div>" +
                    "<div style='font-size: 18px; font-family: monospace; background-color: #333; padding: 10px; margin: 10px;'>$" +
                    (inputBuffer.length() > 0 ? inputBuffer.toString() : "0.00") + "</div><br>" +
                    "<div style='font-size: 12px; color: #CCCCCC;'>Amount must be in multiples of $20</div>" +
                    "</center></html>";
                break;
                
            case "DEPOSIT_AMOUNT":
                displayText = "<html><center><br><br>" +
                    "<div style='font-size: 20px; font-weight: bold;'>Cash Deposit</div><br>" +
                    "<div style='font-size: 14px;'>Current Balance: $" + 
                    new DecimalFormat("#,##0.00").format(currentAccount.getBalance()) + "</div><br>" +
                    "<div style='font-size: 16px;'>Enter Amount:</div>" +
                    "<div style='font-size: 18px; font-family: monospace; background-color: #333; padding: 10px; margin: 10px;'>$" +
                    (inputBuffer.length() > 0 ? inputBuffer.toString() : "0.00") + "</div><br>" +
                    "<div style='font-size: 12px; color: #CCCCCC;'>Press ENT when complete</div>" +
                    "</center></html>";
                break;
        }
        
        displayLabel.setText(displayText);
    }
    
    private void updateStatus() {
        statusLabel.setText("Connection: Online");
        statusLabel.setForeground(new Color(34, 139, 34));
        
        if (cardInserted) {
            cardStatusLabel.setText("Card Status: Inserted");
            cardStatusLabel.setForeground(new Color(34, 139, 34));
        } else {
            cardStatusLabel.setText("Card Status: Not Inserted");
            cardStatusLabel.setForeground(Color.GRAY);
        }
        
        if (pinEntered && currentAccount != null) {
            sessionLabel.setText("Session: Active - " + currentAccount.getAccountHolderName());
            sessionLabel.setForeground(new Color(34, 139, 34));
        } else if (cardInserted) {
            sessionLabel.setText("Session: Authenticating");
            sessionLabel.setForeground(Color.ORANGE);
        } else {
            sessionLabel.setText("Session: Inactive");
            sessionLabel.setForeground(Color.GRAY);
        }
    }
    
    private void enableMenuButtons(boolean enabled) {
        for (JButton button : menuButtons) {
            button.setEnabled(enabled);
        }
    }
    
    private void resetSession() {
        cardInserted = false;
        pinEntered = false;
        currentAccount = null;
        currentOperation = "WELCOME";
        inputBuffer.setLength(0);
        tempAccountNumber = null;
        enableMenuButtons(false);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new ATMDesktopGUI_Production().setVisible(true);
        });
    }
}
