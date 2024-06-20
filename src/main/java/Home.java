import database.Account;
import database.Transaction;
import database.User;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Home class represents the main user interface of the WalletWise application, displaying user transactions.
 */
public class Home extends JFrame {
    private final User user;
    private final ArrayList<Account> accounts;
    private ArrayList<Transaction> transactions;
    private JPanel homePanel;
    private JPanel navbarPanel;
    private JLabel ImageLogo;
    private JLabel homenickname;
    private JScrollPane tranzactiiScrollPane;
    protected DefaultListModel<Transaction> transactionListModel;
    private JList<Transaction> transactionList;
    private DefaultListModel<String> accountsListModel;
    private JList<String> accountsList;
    private JTextField searchBarText;
    private JLabel searchBacIcon;
    private JButton newTransaction;
    private JLabel noTransactionsLabel;
    private JButton importTransactions;
    private JScrollPane accountsScrollPane;
    private JButton addNewProfileButton;
    private JPanel statistics;
    private JButton editTransaction;
    private JButton deleteTransaction;
    private JButton deleteProfile;
    private JLabel balanceLabel;
    private JButton logoutButton;

    /**
     * Constructs a new Home frame with all its components and initializes event listeners.
     *
     * @param usr            The user for whom the home screen is displayed.
     * @param accountsFromdb The list of accounts associated with the user.
     */
    public Home(User usr, ArrayList<Account> accountsFromdb) {
        user = usr;
        accounts = accountsFromdb;
        setContentPane(homePanel);
        setTitle("WalletWise");
        setSize(1280, 720);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        transactionList.setCellRenderer(new CustomListCellRendererTransaction());

        searchBarText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterTransactions(searchBarText.getText());
            }
        });

        searchBacIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                filterTransactions(searchBarText.getText());
            }
        });

        accountsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedIndex = accountsList.getSelectedIndex();
                    if (selectedIndex != -1 && selectedIndex < accounts.size()) {
                        Account selectedAccount = accounts.get(selectedIndex);
                        updateTransactionList(selectedAccount);
                        clearSearchField();
                    } else {
                        clearTransactionList();
                        clearSearchField();
                    }
                }
            }
        });
        newTransaction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (accountsList.getSelectedIndex() != -1) {
                    addNewTransactionDialog();
                } else {
                    JOptionPane.showMessageDialog(Home.this, "Please select an account.", "Account Not Selected", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        addNewProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewAccount();
            }
        });
        editTransaction.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (transactionList.getSelectedIndex() != -1) {
                    editTransactionFunction();
                } else {
                    JOptionPane.showMessageDialog(Home.this, "Please select an transaction.", "Account Not Selected", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        deleteTransaction.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (transactionList.getSelectedIndex() != -1) {
                    deleteTransactionFunction();
                } else {
                    JOptionPane.showMessageDialog(Home.this, "Please select an transaction.", "Account Not Selected", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        importTransactions.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (accountsList.getSelectedIndex() == -1) {
                    JOptionPane.showMessageDialog(Home.this, "Please select an account before importing transactions.", "Account Not Selected", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                showFileChooser();
            }
        });
        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int option = JOptionPane.showConfirmDialog(Home.this, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    dispose();
                    Login loginScreen = new Login();
                    loginScreen.setVisible(true);
                }
            }
        });
        deleteProfile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (accountsList.getSelectedIndex() != -1) {
                    deleteAccountFunction();
                } else {
                    JOptionPane.showMessageDialog(
                            Home.this,
                            "Please select an account to delete.",
                            "Account Not Selected",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        });
    }

    /**
     * Deletes the selected account and its transactions if confirmed by the user.
     */
    private void deleteAccountFunction() {
        int selectedAccountIndex = accountsList.getSelectedIndex();

        if (selectedAccountIndex != -1) {
            Account selectedAccount = accounts.get(selectedAccountIndex);

            int choice = JOptionPane.showConfirmDialog(
                    Home.this,
                    "Are you sure you want to delete this account and its transactions?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                int accountId = selectedAccount.getIdaccounts();

                user.deleteAccount(accountId);

                accountsListModel.removeElementAt(selectedAccountIndex);
                accounts.remove(selectedAccount);

                if (accountsListModel.isEmpty()) {
                    clearTransactionList();
                } else {
                    accountsList.setSelectedIndex(0);
                }
            }
        }
    }

    /**
     * Deletes the selected transaction from the selected account if confirmed by the user.
     */
    private void deleteTransactionFunction() {
        int selectedAccountIndex = accountsList.getSelectedIndex();
        int selectedTransactionIndex = transactionList.getSelectedIndex();

        if (selectedAccountIndex != -1 && selectedTransactionIndex != -1) {
            Account selectedAccount = accounts.get(selectedAccountIndex);
            Transaction selectedTransaction = selectedAccount.getTransactions().get(selectedTransactionIndex);

            int choice = JOptionPane.showConfirmDialog(
                    Home.this,
                    "Are you sure you want to delete this transaction?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                selectedAccount.removeTransaction(selectedTransaction);
                updateTransactionList(selectedAccount);
                clearSearchField();
            }
        } else {
            JOptionPane.showMessageDialog(
                    Home.this,
                    "Please select an account and a transaction to delete.",
                    "Selection Error",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    /**
     * Calculates the balance based on the provided list of transactions.
     *
     * @param transactions The list of transactions.
     * @return The balance calculated from the transactions.
     */
    private double calculateBalance(ArrayList<Transaction> transactions) {
        double balance = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getType() == 0) { // Income
                balance += transaction.getAmount();
            } else { // Expense
                balance -= transaction.getAmount();
            }
        }

        return balance;
    }

    /**
     * Displays a file chooser dialog for selecting a PDF file to import transactions from.
     */
    private void showFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            if (isPDF(selectedFile)) {
                importTransactions(selectedFile);
            } else {
                JOptionPane.showMessageDialog(this, "Selected file is not a PDF.", "Invalid File", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Checks if the provided file is a PDF file.
     *
     * @param file The file to check.
     * @return True if the file is a PDF, false otherwise.
     */
    private boolean isPDF(File file) {
        try {
            byte[] fileSignature = Files.readAllBytes(file.toPath());
            String fileHeader = new String(fileSignature);
            return fileHeader.startsWith("%PDF-");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Imports transactions from a PDF file.
     *
     * @param pdfFile The PDF file to import transactions from.
     */
    private void importTransactions(File pdfFile) {
        try {
            PDDocument document = Loader.loadPDF(pdfFile);
            PDFTextStripper stripper = new PDFTextStripper();

            String pdfText = stripper.getText(document);

            if (pdfText.contains("BANCA COMERCIALA ROMANA S.A.") && pdfText.contains("RB-PJR-40-008/18.02.1999")) {
                extractTransactionBCR(pdfText);
                JOptionPane.showMessageDialog(this, "The transactions have been added.", "Succes", JOptionPane.INFORMATION_MESSAGE);
            } else if (pdfText.contains("Revolut Bank UAB") && pdfText.contains("LB002119")) {
                extractTransactionRVT(pdfText);
                JOptionPane.showMessageDialog(this, "The transactions have been added.", "Succes", JOptionPane.INFORMATION_MESSAGE);
            } else if (pdfText.contains("RAIFFEISEN BANK S.A") && pdfText.contains("RZBRROBU")) {
                extractTransactionRFB(pdfText);
                JOptionPane.showMessageDialog(this, "The transactions have been added.", "Succes", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Bank or not recognised bank.", "Invalid File", JOptionPane.WARNING_MESSAGE);
            }
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading the PDF file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Extracts transactions from a PDF file from Raiffeisen Bank.
     *
     * @param pdfText The text content of the PDF file.
     */
    private void extractTransactionRFB(String pdfText) {
        Pattern transactionPattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4}) (\\d{2}\\.\\d{2}\\.\\d{4})([\\s\\S]*?)\\n([\\s\\S]*?)(utilizarii|Transfer)([\\s\\S]*?)(\\d+\\.\\d{2})");
        Matcher transactionMatcher = transactionPattern.matcher(pdfText);

        while (transactionMatcher.find()) {
            String dateFromPDF = transactionMatcher.group(1);
            SimpleDateFormat fromUser = new SimpleDateFormat("dd.MM.yyyy");
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                String date = myFormat.format(fromUser.parse(dateFromPDF));
                String name = transactionMatcher.group(3);
                String valueStr = transactionMatcher.group(7);
                double amount = Double.parseDouble(valueStr);
                String typeText = transactionMatcher.group(5);
                int type;
                if (typeText.equals("utilizarii")) {
                    type = 1;
                } else {
                    type = 0;
                }
                int selectedIndex = accountsList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Account selectedAccount = accounts.get(selectedIndex);

                    Transaction newTransaction = new Transaction(selectedAccount.getNextTransactionID(), selectedAccount.getIdaccounts(), name, amount, date, type);

                    selectedAccount.addTransaction(newTransaction);
                    updateTransactionList(selectedAccount);
                    clearSearchField();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Extracts transactions from a PDF file from Revolut Bank.
     *
     * @param pdfText The text content of the PDF file.
     */
    private void extractTransactionRVT(String pdfText) {

        Pattern transactionPattern = Pattern.compile("((Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) \\d{1,2}, \\d{4}) ([\\s\\S]*?)(\\d+\\.\\d{2})([\\s\\S]*?)(To|From)");
        Matcher transactionMatcher = transactionPattern.matcher(pdfText);

        while (transactionMatcher.find()) {
            String dateFromPDF = transactionMatcher.group(1);
            SimpleDateFormat fromUser = new SimpleDateFormat("MMM dd, yyyy");
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                String date = myFormat.format(fromUser.parse(dateFromPDF));
                String name = transactionMatcher.group(3);
                String valueStr = transactionMatcher.group(4);
                double amount = Double.parseDouble(valueStr);
                String typeText = transactionMatcher.group(6);
                int type;
                if (typeText.equals("To")) {
                    type = 1;
                } else {
                    type = 0;
                }
                int selectedIndex = accountsList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Account selectedAccount = accounts.get(selectedIndex);

                    Transaction newTransaction = new Transaction(selectedAccount.getNextTransactionID(), selectedAccount.getIdaccounts(), name, amount, date, type);

                    selectedAccount.addTransaction(newTransaction);
                    updateTransactionList(selectedAccount);
                    clearSearchField();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Extracts transactions from a PDF file from Banca Comercială Română.
     *
     * @param pdfText The text content of the PDF file.
     */
    private void extractTransactionBCR(String pdfText) {
        Pattern transactionPattern = Pattern.compile("(\\d{2}/\\d{2}/\\d{4}) (\\d{2}:\\d{2}) ([\\s\\S]*?)(Nota|Ordin)([\\s\\S]*?)(\\d+\\.\\d{3,}\\.\\d{2})");
        Matcher transactionMatcher = transactionPattern.matcher(pdfText);

        while (transactionMatcher.find()) {
            String dateFromPDF = transactionMatcher.group(1);
            SimpleDateFormat fromUser = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                String date = myFormat.format(fromUser.parse(dateFromPDF));
                String DescFromPDF = transactionMatcher.group(3);

                Pattern namePattern = Pattern.compile("Locatie: \\S+ \\S+ ([\\s\\S]*?)\\.");
                Matcher nameMatcher = namePattern.matcher(DescFromPDF);
                String name = nameMatcher.find() ? nameMatcher.group(1) : "Untitled";
                String valuesFromPDF = transactionMatcher.group(6);

                Pattern creditPattern = Pattern.compile("(\\d+\\.\\d{2})");
                Matcher creditMatcher = creditPattern.matcher(valuesFromPDF);
                String creditString = creditMatcher.find() ? creditMatcher.group(1) : "0";

                Pattern debitPattern = Pattern.compile("(\\d+\\.\\d{2})(\\d{1,}\\.\\d{2})");
                Matcher debitMatcher = debitPattern.matcher(valuesFromPDF);
                String debitString = debitMatcher.find() ? debitMatcher.group(2) : "0";

                double credit = Double.parseDouble(creditString);
                double debit = Double.parseDouble(debitString);
                int type;
                double amount;
                if (credit > debit) {
                    type = 0;
                    amount = credit;
                } else {
                    type = 1;
                    amount = debit;
                }

                int selectedIndex = accountsList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Account selectedAccount = accounts.get(selectedIndex);

                    Transaction newTransaction = new Transaction(selectedAccount.getNextTransactionID(), selectedAccount.getIdaccounts(), name, amount, date, type);

                    selectedAccount.addTransaction(newTransaction);
                    updateTransactionList(selectedAccount);
                    clearSearchField();
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearTransactionList() {
        transactionListModel.clear();
        noTransactionsLabel.setVisible(true);
    }

    private void clearSearchField() {
        searchBarText.setText("");
        filterTransactions("");
    }

    /**
     * Opens a dialog for editing a selected transaction.
     * If both an account and a transaction are selected, a dialog is opened to edit the transaction's details,
     * including name, amount, date, and type.
     * If successful, the transaction list is updated with the new information.
     */
    private void editTransactionFunction() {
        int selectedAccountIndex = accountsList.getSelectedIndex();
        int selectedTransactionIndex = transactionList.getSelectedIndex();

        if (selectedAccountIndex != -1 && selectedTransactionIndex != -1) {
            Account selectedAccount = accounts.get(selectedAccountIndex);
            Transaction selectedTransaction = selectedAccount.getTransactions().get(selectedTransactionIndex);


            JDialog dialog = new JDialog(this, "Edit Transaction", true);
            dialog.setSize(500, 250);
            dialog.setResizable(false);
            dialog.setLayout(new GridLayout(6, 2));
            dialog.setLocationRelativeTo(this);

            JTextField nameField = new JTextField(selectedTransaction.getName());
            JTextField amountField = new JTextField(String.valueOf(selectedTransaction.getAmount()));

            // Create a UtilDateModel for JDatePicker
            UtilDateModel dateModel = new UtilDateModel();
            try {
                Date selectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(selectedTransaction.getDate());
                dateModel.setValue(selectedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Properties p = new Properties();
            JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
            JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());


            JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Income", "Expense"});
            typeComboBox.setSelectedIndex(selectedTransaction.getType());

            JButton saveButton = new JButton("Save Changes");
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String name = nameField.getText();
                    String amountText = amountField.getText();

                    if (name.isEmpty() || amountText.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Please fill in all fields.", "Incomplete Fields", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    double amount;
                    try {
                        amount = Double.parseDouble(amountText);
                        if (amount <= 0) {
                            JOptionPane.showMessageDialog(dialog, "Amount must be a positive number.", "Invalid Amount", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Amount must be a valid number.", "Invalid Amount", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    Date selectedDate = (Date) datePicker.getModel().getValue();
                    if (selectedDate == null) {
                        JOptionPane.showMessageDialog(dialog, "Please select a date.", "Date Not Selected", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String date = dateFormat.format(selectedDate);

                    int type = typeComboBox.getSelectedIndex();

                    selectedAccount.updateTransaction(selectedTransaction, name, amount, date, type);
                    updateTransactionList(selectedAccount);
                    clearSearchField();
                    dialog.dispose();
                }
            });


            dialog.add(new JLabel("Name:"));
            dialog.add(nameField);
            dialog.add(new JLabel("Amount:"));
            dialog.add(amountField);
            dialog.add(new JLabel("Date:"));
            dialog.add(datePicker);
            dialog.add(new JLabel("Type:"));
            dialog.add(typeComboBox);
            dialog.add(new JLabel()); // for spacing
            dialog.add(saveButton);

            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(
                    Home.this,
                    "Please select an account and a transaction to edit.",
                    "Selection Error",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    /**
     * Opens a dialog for adding a new transaction.
     * If an account is selected, a dialog is opened to add a new transaction with details such as name, amount, date, and type.
     * If successful, the new transaction is added to the selected account and the transaction list is updated.
     */
    private void addNewTransactionDialog() {
        JDialog dialog = new JDialog(this, "Add New Transaction", true);
        dialog.setSize(500, 250);
        dialog.setResizable(false);
        dialog.setLayout(new GridLayout(6, 2));
        dialog.setLocationRelativeTo(this);

        JTextField nameField = new JTextField();
        JTextField amountField = new JTextField();

        UtilDateModel dateModel = new UtilDateModel();
        Properties p = new Properties();
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Income", "Expense"});

        JButton addButton = new JButton("Add Transaction");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String amountText = amountField.getText();

                if (name.isEmpty() || amountText.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill in all fields.", "Incomplete Fields", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double amount;
                try {
                    amount = Double.parseDouble(amountText);
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(dialog, "Amount must be a positive number.", "Invalid Amount", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Amount must be a valid number.", "Invalid Amount", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Date selectedDate = (Date) datePicker.getModel().getValue();
                if (selectedDate == null) {
                    JOptionPane.showMessageDialog(dialog, "Please select a date.", "Date Not Selected", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = dateFormat.format(selectedDate);
                int type = typeComboBox.getSelectedIndex();

                int selectedIndex = accountsList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Account selectedAccount = accounts.get(selectedIndex);

                    Transaction newTransaction = new Transaction(selectedAccount.getNextTransactionID(), selectedAccount.getIdaccounts(), name, amount, date, type);

                    selectedAccount.addTransaction(newTransaction);
                    updateTransactionList(selectedAccount);
                    clearSearchField();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Please select an account.", "Account Not Selected", JOptionPane.WARNING_MESSAGE);
                }
            }
        });


        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Amount:"));
        dialog.add(amountField);
        dialog.add(new JLabel("Date:"));
        dialog.add(datePicker);
        dialog.add(new JLabel("Type:"));
        dialog.add(typeComboBox);
        dialog.add(new JLabel());
        dialog.add(addButton);

        dialog.setVisible(true);
    }

    /**
     * Adds a transaction to the transaction list model.
     *
     * @param transaction The transaction to be added.
     */
    public void addTransaction(Transaction transaction) {
        transactionListModel.addElement(transaction);
        noTransactionsLabel.setVisible(false);
    }

    /**
     * Opens a dialog for creating a new account.
     * Prompts the user to enter a name for the new account.
     * If a valid name is provided and the account creation is successful, the new account is added to the user's account list,
     * and the corresponding entry is added to the accounts list model.
     * If creation fails, an error message is displayed.
     */
    private void createNewAccount() {
        String accountName = JOptionPane.showInputDialog(this, "Enter the name for the new account:", "New Account", JOptionPane.PLAIN_MESSAGE);

        if (accountName != null && !accountName.isEmpty()) {

            Account newAccount = user.addNewAccount(accountName);

            if (newAccount != null) {
                accountsListModel.addElement(newAccount.getName());
                accounts.add(newAccount);
                accountsList.setSelectedIndex(accountsListModel.getSize() - 1);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create a new account.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Filters transactions based on the search text.
     *
     * @param searchText The text to filter transactions.
     */
    private void filterTransactions(String searchText) {
        DefaultListModel<Transaction> filteredModel = new DefaultListModel<>();
        for (int i = 0; i < transactionListModel.size(); i++) {
            Transaction transaction = transactionListModel.getElementAt(i);
            if (transactionMatchesSearch(transaction, searchText)) {
                filteredModel.addElement(transaction);
            }
        }

        boolean isEmpty = filteredModel.isEmpty() && !searchText.isEmpty();
        noTransactionsLabel.setVisible(isEmpty);

        if (isEmpty) {
            JOptionPane.showMessageDialog(this, "No transactions found for the given search.", "No Transactions", JOptionPane.INFORMATION_MESSAGE);
        }

        transactionList.setModel(filteredModel);
        transactionList.revalidate();
        transactionList.repaint();
    }

    /**
     * Checks if a transaction's description contains the specified search text.
     *
     * @param transaction The transaction to check.
     * @param searchText  The text to search for in the transaction's description.
     * @return True if the transaction's description contains the search text, otherwise false.
     */
    protected boolean transactionMatchesSearch(Transaction transaction, String searchText) {
        return transaction.getDescription().toLowerCase().contains(searchText.toLowerCase());
    }

    /**
     * Updates the transaction list based on the selected account.
     * Clears the transaction list model, adds transactions for the selected account, and updates the balance label accordingly.
     *
     * @param selectedAccount The account whose transactions will be displayed.
     */
    protected void updateTransactionList(Account selectedAccount) {
        ArrayList<Transaction> transactionsForAccount = selectedAccount.getTransactions();
        transactionListModel.clear();

        for (Transaction transaction : transactionsForAccount) {
            transactionListModel.addElement(transaction);
        }
        double balance = calculateBalance(transactionsForAccount);
        balanceLabel.setText(String.format("%.2f", balance) + " RON");
        if (balance < 0) {
            balanceLabel.setForeground(Color.RED);
        } else if (balance == 0) {
            balanceLabel.setForeground(Color.GRAY);
        } else {
            balanceLabel.setForeground(Color.GREEN);
        }
    }

    /**
     * Custom component creation method for setting up the application logo and other components.
     * Initializes and configures UI components like logos, text fields, lists, and cell renderers.
     */
    private void createUIComponents() {
        Border emptyBorder = new EmptyBorder(3, 3, 3, 3);

        ImageIcon imageIcon = new ImageIcon("src/icon/logo.png");
        Image image = imageIcon.getImage();
        Image newimg = image.getScaledInstance(100, 70, java.awt.Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(newimg);
        ImageLogo = new JLabel(icon);
        ImageLogo.setBorder(emptyBorder);
        ImageLogo.setHorizontalAlignment(JLabel.CENTER);

        ImageIcon searchIcon = new ImageIcon("src/icon/searchicon.png");
        image = searchIcon.getImage();
        newimg = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
        searchIcon = new ImageIcon(newimg);
        searchBacIcon = new JLabel(searchIcon);
        searchBacIcon.setBorder(emptyBorder);
        searchBacIcon.setHorizontalAlignment(JLabel.CENTER);

        homenickname = new JLabel(user.getNickname());
        homenickname.setText(user.getNickname());

        transactionListModel = new DefaultListModel<>();
        transactionList = new JList<>(transactionListModel);
        tranzactiiScrollPane = new JScrollPane(transactionList);

        accountsListModel = new DefaultListModel<>();
        for (Account account : accounts) {
            accountsListModel.addElement(account.getName());
        }
        accountsList = new JList<>(accountsListModel);
        accountsScrollPane = new JScrollPane(accountsList);
        accountsList.setCellRenderer(new CustomListCellRendererAccounts());
        searchBarText = new JTextField();
    }

    /**
     * Helper class for formatting dates in the date picker.
     * Provides custom string-to-value and value-to-string conversions for date formatting.
     */
    public static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final String datePattern = "yyyy-MM-dd";
        private final SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            Date date = dateFormat.parse(text);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        }

        @Override
        public String valueToString(Object value) {
            if (value instanceof Calendar) {
                Date date = ((Calendar) value).getTime();
                return dateFormat.format(date);
            }
            return "";
        }
    }

    /**
     * Custom list cell renderer for styling transaction list items.
     * Renders transaction information with customized formatting and styling.
     */
    private static class CustomListCellRendererTransaction extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            panel.setPreferredSize(new Dimension(panel.getWidth(), 130));

            Transaction transaction = (Transaction) value;

            JLabel nameLabel = new JLabel(transaction.getName());
            nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
            nameLabel.setForeground(isSelected ? Color.BLACK : Color.WHITE);

            JLabel dateLabel = new JLabel(transaction.getDate());
            dateLabel.setForeground(isSelected ? Color.DARK_GRAY : Color.LIGHT_GRAY);
            JLabel amountLabel;
            if (transaction.getType() == 1) {
                amountLabel = new JLabel("-" + transaction.getValue() + " RON");
                amountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                amountLabel.setVerticalAlignment(SwingConstants.BOTTOM);
                amountLabel.setForeground(Color.RED);
                amountLabel.setFont(new Font("Arial", Font.BOLD, 16));
            } else {
                amountLabel = new JLabel(transaction.getValue() + " RON");
                amountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                amountLabel.setForeground(new Color(32, 155, 35));
                amountLabel.setFont(new Font("Arial", Font.BOLD, 16));
            }

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            panel.add(nameLabel, gbc);

            gbc.gridy = 1;
            panel.add(dateLabel, gbc);

            gbc.gridx = 1;
            panel.add(amountLabel, gbc);

            gbc.gridx = 0;
            gbc.gridwidth = 2;
            gbc.gridy = 2;
            gbc.weighty = 1.0;
            gbc.anchor = GridBagConstraints.PAGE_END;
            JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
            panel.add(separator, gbc);

            if (isSelected) {
                panel.setBackground(new Color(200, 220, 255));
            } else {
                panel.setBackground(list.getBackground());
            }

            return panel;
        }
    }

    /**
     * Custom list cell renderer for styling account list items.
     * Renders account names with customized formatting and styling.
     */
    private class CustomListCellRendererAccounts extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.setFont(new Font("Dialog", Font.PLAIN, 16));
            panel.setPreferredSize(new Dimension(panel.getWidth(), 80));

            JLabel accountLabel = new JLabel(value.toString());
            accountLabel.setFont(new Font("Dialog", isSelected ? Font.BOLD : Font.PLAIN, 16));
            accountLabel.setForeground(Color.WHITE);
            panel.add(accountLabel, BorderLayout.CENTER);

            JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
            separator.setForeground(Color.DARK_GRAY);
            panel.add(separator, BorderLayout.SOUTH);

            if (isSelected) {
                panel.setBackground(new Color(21, 20, 26));
            } else {
                panel.setBackground(list.getBackground());
            }

            noTransactionsLabel.setVisible(transactionListModel.isEmpty());

            return panel;
        }
    }
}
