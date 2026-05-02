import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class User {
    String username;
    String password;
    double balance;
    ArrayList<String> history = new ArrayList<>();

    public User(String u, String p) {
        username = u;
        password = p;
        balance = 0;
    }
}

public class BankApp{

    static HashMap<String, User> users = new HashMap<>();
    static User currentUser;

    public static void main(String[] args) {
        showLoginUI();
    }

    // ================= LOGIN UI =================
    static void showLoginUI() {
        JFrame frame = new JFrame("Bank Login");
        frame.setSize(400, 300);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(20, 20, 40));

        JLabel title = new JLabel("Online Banking");
        title.setBounds(110, 30, 200, 30);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JTextField userField = new JTextField();
        userField.setBounds(100, 90, 200, 30);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(100, 130, 200, 30);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(100, 180, 90, 30);

        JButton registerBtn = new JButton("Register");
        registerBtn.setBounds(210, 180, 90, 30);

        frame.add(title);
        frame.add(userField);
        frame.add(passField);
        frame.add(loginBtn);
        frame.add(registerBtn);

        loginBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            if (users.containsKey(user) && users.get(user).password.equals(pass)) {
                currentUser = users.get(user);
                frame.dispose();
                showDashboard();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Login");
            }
        });

        registerBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            if (!users.containsKey(user)) {
                users.put(user, new User(user, pass));
                JOptionPane.showMessageDialog(frame, "Account Created!");
            } else {
                JOptionPane.showMessageDialog(frame, "User already exists!");
            }
        });

        frame.setVisible(true);
    }

    // ================= DASHBOARD =================
    static void showDashboard() {
        JFrame frame = new JFrame("Dashboard");
        frame.setSize(900, 550);
        frame.setLayout(new BorderLayout());

        // ===== SIDEBAR =====
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(200, 500));
        sidebar.setBackground(new Color(25, 25, 50));
        sidebar.setLayout(new GridLayout(6, 1, 10, 10));

        JButton btnDeposit = createButton("Deposit");
        JButton btnWithdraw = createButton("Withdraw");
        JButton btnBalance = createButton("Refresh Balance");
        JButton btnHistory = createButton("Transaction History");
        JButton btnLogout = createButton("Logout");

        sidebar.add(new JLabel("   MENU", JLabel.CENTER));
        sidebar.add(btnDeposit);
        sidebar.add(btnWithdraw);
        sidebar.add(btnBalance);
        sidebar.add(btnHistory);
        sidebar.add(btnLogout);

        // ===== MAIN PANEL =====
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(240, 240, 255));

        JLabel welcome = new JLabel("Welcome, " + currentUser.username);
        welcome.setBounds(20, 20, 400, 30);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JLabel balanceCard = new JLabel("Balance: ₹" + currentUser.balance);
        balanceCard.setBounds(20, 80, 300, 60);
        balanceCard.setOpaque(true);
        balanceCard.setBackground(new Color(70, 130, 180));
        balanceCard.setForeground(Color.WHITE);
        balanceCard.setFont(new Font("Segoe UI", Font.BOLD, 18));
        balanceCard.setHorizontalAlignment(SwingConstants.CENTER);

        JTextArea historyArea = new JTextArea();
        historyArea.setBounds(20, 180, 600, 250);
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Consolas", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(historyArea);
        scroll.setBounds(20, 180, 600, 250);

        mainPanel.add(welcome);
        mainPanel.add(balanceCard);
        mainPanel.add(scroll);

        // ===== BUTTON ACTIONS =====
        btnDeposit.addActionListener(e -> {
            String amt = JOptionPane.showInputDialog("Enter amount:");
            if (amt != null) {
                double amount = Double.parseDouble(amt);
                currentUser.balance += amount;
                currentUser.history.add("Deposited ₹" + amount);
                balanceCard.setText("Balance: ₹" + currentUser.balance);
            }
        });

        btnWithdraw.addActionListener(e -> {
            String amt = JOptionPane.showInputDialog("Enter amount:");
            if (amt != null) {
                double amount = Double.parseDouble(amt);

                if (currentUser.balance >= amount) {
                    currentUser.balance -= amount;
                    currentUser.history.add("Withdrew ₹" + amount);
                    balanceCard.setText("Balance: ₹" + currentUser.balance);
                } else {
                    JOptionPane.showMessageDialog(frame, "Insufficient Balance!");
                }
            }
        });

        btnBalance.addActionListener(e -> {
            balanceCard.setText("Balance: ₹" + currentUser.balance);
        });

        btnHistory.addActionListener(e -> {
            historyArea.setText("");
            for (String h : currentUser.history) {
                historyArea.append(h + "\n");
            }
        });

        btnLogout.addActionListener(e -> {
            frame.dispose();
            showLoginUI();
        });

        // ===== ADD PANELS =====
        frame.add(sidebar, BorderLayout.WEST);
        frame.add(mainPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // ===== BUTTON STYLE =====
    static JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(45, 45, 80));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return btn;
    }
}