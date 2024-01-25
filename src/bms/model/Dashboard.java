/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package bms.model;

import bms.classes.Account;
import bms.classes.AuditTrail;
import bms.classes.Cryptographer;
import bms.classes.Customer;
import bms.classes.ExpensesTransaction;
import bms.classes.ImageManipulator;
import bms.classes.Item;
import bms.classes.JournalEntry;
import bms.classes.LedgerAccount;
import bms.classes.PurchaseTransaction;
import bms.classes.SalesTransaction;
import bms.classes.User;
import bms.classes.Vendor;
import bms.dbcontroller.AccountDBController;
import bms.dbcontroller.AuditTrailDBController;
import bms.dbcontroller.CustomerDBController;
import bms.dbcontroller.EntryDBController;
import bms.dbcontroller.ProductDBController;
import bms.dbcontroller.UserDBController;
import bms.dbcontroller.VendorDBController;
import bms.listeners.DashboardListener;
import bms.model.chartofaccounts.AddAccount;
import bms.model.chartofaccounts.UpdateAccount;
import bms.model.customermanagement.AddCustomer;
import bms.model.customermanagement.UpdateCustomer;
import bms.model.expenses.AddExpense;
import bms.model.expenses.UpdateExpense;
import bms.model.generaljournal.AddEntry;
import bms.model.generaljournal.UpdateEntry;
import bms.model.generaljournal.ViewEntry;
import bms.model.purchases.AddPurchases;
import bms.model.purchases.ViewPurchases;
import bms.model.report.LedgerAccountChooser;
import bms.model.report.ReportController;
import bms.model.report.ReportDate;
import bms.model.sales.AddSales;
import bms.model.sales.ViewSales;
import bms.model.settings.AddProduct;
import bms.model.settings.SetPaymentMethod;
import bms.model.settings.UpdateProduct;
import bms.model.usermanagement.AddUser;
import bms.model.usermanagement.UpdateUser;
import bms.model.vendormanagement.AddVendor;
import bms.model.vendormanagement.UpdateVendor;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Vienji
 */
public class Dashboard extends javax.swing.JFrame implements DashboardListener {
    private User currentUser;
    private String tempFromRangeDate = ""; 
    private String tempToRangeDate = ""; 
    private Account account;
    private ArrayList<Account> chartOfAccounts = new ArrayList();
    private ArrayList<Item> products = new ArrayList();
    /**
     * Creates new form Dashboard
     */
    
    public Dashboard(){
        initComponents();
    }
    
    public Dashboard(User currentUser) {
        initComponents();
        /*
        initIcons();
        initDates();
        initLedger();
        */
        //initSearchFieldListeners();
        /*
        initNetworkSettings(); 
        initAccountingConfigurations();
        initSalesConfiguration();
        initPurchasesConfiguration();
        initProductsData();
        initSummary();
        */
        //initChartOfAccountsVar();
        this.currentUser = currentUser;
        summaryButtonP.setBackground(new Color(8, 82, 157));
        username.setText(currentUser.getName());
        level.setText(currentUser.getAccessLevel());
        addWindowListener(new CloseWindow());
        //initAccessLevel(isAdmin);
    }

    public void initAccessLevel(boolean isAdmin){
        if(isAdmin){
            //Sales
            addSales.setVisible(false);
            deleteSales.setVisible(false);
            //Expenses
            addExpenses.setVisible(false);
            deleteExpenses.setVisible(false);
            editExpenses.setVisible(false);
            //Purchases
            addPurchases.setVisible(false);
            deletePurchases.setVisible(false);
            //Journal Entry
            addEntry.setVisible(false);
            deleteEntry.setVisible(false);
            editEntry.setVisible(false);
            //Chart of Accounts
            addAccount.setVisible(false);
            deleteAccount.setVisible(false);
            updateAccount.setVisible(false);
            //Settings
            setOpeningBalanceDate.setEnabled(false);     
            setSalesPaymentMethod.setEnabled(false);
            setSalesRemainingBalanceAccount.setEnabled(false);
            setSalesRevenueAccount.setEnabled(false);
            setPurchasesPaymentMethod.setEnabled(false);
            setPurchasesRemainingBalanceAccount.setEnabled(false);
        } else {
            userManagementButtonP.setVisible(false);
            userManagementButtonL.setVisible(false);
            userManagementIcon.setVisible(false);
        }
    }
    
    public void initIcons(){
        ImageIcon icon = new ImageIcon("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\bms_logo.png");
        setIconImage(icon.getImage());
        
        ImageManipulator icons = new ImageManipulator();
        
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\search.png", searchUser);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\refresh.png", refreshUser);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\search.png", searchVendor);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\refresh.png", refreshVendor);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\search.png", searchCustomer);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\refresh.png", refreshCustomer);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\search.png", searchAccount);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\refresh.png", refreshAccount);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\refresh.png", refreshLedger);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\search.png", searchJournal);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\refresh.png", refreshJournal);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\filter.png", filterJournal);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\search.png", searchPurchases);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\refresh.png", refreshPurchases);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\filter.png", filterPurchases);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\search.png", searchExpenses);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\refresh.png", refreshExpenses);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\filter.png", filterExpenses);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\search.png", searchSales);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\refresh.png", refreshSales);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\filter.png", filterSales);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\filter.png", filterLedger);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\jar_logo.png", aimsLogo);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\coin.png", summaryRevenueIcon);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\expenses.png", summaryExpensesIcon);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\recent_transaction.png", summaryRecentTransactionsIcon);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\profit_and_loss.png", summaryProfitAndLossIcon);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\summaryaccounts.png", summaryAccountsIcon);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\summary.png", summaryIcon);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\sales.png", salesIcon);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\expensesmenu.png", expensesIcon);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\purchases.png", purchasesIcon);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\taxes.png", taxesIcon);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\journal.png", generalJournalIcon);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\ledger.png", generalLedgerIcon);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\chart_of_accounts.png", chartOfAccountIcon);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\report.png", reportsIcon);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\customer.png", customerManagementIcon);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\vendor.png", vendorManagementIcon);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\usermanagement.png", userManagementIcon);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\settings.png", settingsIcon);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\logout.png", logout);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\search.png", searchAuditTrail);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\refresh.png", refreshAuditTrail);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\filter.png", filterAuditTrail);
        icons.setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\bms_round_logo.png", bmsLogo);
    }
    
    private void showUserList(ArrayList<User> userCollections){
        userTable.setModel(new DefaultTableModel(null, new String[]{"ID", "First Name", "Last Name", "Username", "Access level", "Status"}));
        DefaultTableModel populatedUserTable = (DefaultTableModel) userTable.getModel();
        ArrayList<User> userList = userCollections;

        if(userList.size() == 0){
            String[] data = {"No Data", "No Data", "No Data", "No Data", "No Data", "No Data"};
            populatedUserTable.addRow(data);
        } else {
            Iterator i = (Iterator) userList.iterator();

            while(i.hasNext()){
                User user = (User) i.next();
                String[] userData = {user.getId(), user.getFirstName(), user.getLastName(), 
                    user.getUsername(), user.getAccessLevel(), user.getStatus()};

                populatedUserTable.addRow(userData);
            }
        }
    }
    
    private void showVendorList(ArrayList<Vendor> vendorCollections){
        vendorTable.setModel(new DefaultTableModel(null, new String[]{"ID", "Name", "Phone Number", "Email", "Address", "Balance"}));
        DefaultTableModel populatedVendorTable = (DefaultTableModel) vendorTable.getModel();
        tableColumnRenderer(vendorTable, 5, 5);   
        ArrayList<Vendor> vendorList = vendorCollections;  

        if(vendorList.size() == 0){
            String[] data = {"No Data", "No Data", "No Data", "No Data", "No Data", "No Data"};
            populatedVendorTable.addRow(data);
        } else {
            Iterator i = (Iterator) vendorList.iterator();
            
            while(i.hasNext()){
                Vendor vendor = (Vendor) i.next();
                String[] vendorData = {vendor.getId(), vendor.getName(), vendor.getPhoneNumber(), vendor.getEmail(), 
                    vendor.getAddress(), "₱ "+ setDecimalFormat(vendor.getBalance())};

                populatedVendorTable.addRow(vendorData);
            }
        }
    }
    
    private void showCustomerList(ArrayList<Customer> customerCollection){
        customerTable.setModel(new DefaultTableModel(null, new String[]{"ID", "First Name", "Last Name", "Phone Number", 
            "Organization", "Position", "Addres", "Balance"}));
        DefaultTableModel populatedCustomerTable = (DefaultTableModel) customerTable.getModel();
        tableColumnRenderer(customerTable, 7, 7);   
        ArrayList<Customer> customerList = customerCollection;
        
        if(customerList.size() == 0){
            String[] data = {"No Data", "No Data", "No Data", "No Data", "No Data", "No Data", "No Data", "No Data"};
            populatedCustomerTable.addRow(data);
        } else {
            Iterator i = (Iterator) customerList.iterator();

            while(i.hasNext()){
                Customer customer = (Customer) i.next();

                String organization = !customer.getOrganization().equalsIgnoreCase("Null") ? customer.getOrganization() : "N/A";
                String position = !customer.getPosition().equalsIgnoreCase("Null") ? customer.getPosition() : "N/A";
                String address = !customer.getAddress().equalsIgnoreCase("Null") ? customer.getAddress() : "N/A"; 

                String[] customerData = {customer.getId(), customer.getFirstName(), customer.getLastName(), 
                    customer.getPhoneNumber(), organization, position, address,
                    "₱ "+ setDecimalFormat(customer.getBalance())};

                populatedCustomerTable.addRow(customerData);
            }
        }  
    }
    
    private void showAccountList(ArrayList<Account> accountCollection){
        accountTable.setModel(new DefaultTableModel(null, new String[]{"Code", "Account Name", "Account Type", "Normally", "Balance"}));
        DefaultTableModel populatedAccountTable = (DefaultTableModel) accountTable.getModel();
        tableColumnRenderer(accountTable, 4, 4);   
        ArrayList<Account> accountList = accountCollection;
        
        if(accountList.size() == 0){
            String[] data = {"No Data", "No Data", "No Data", "No Data", "No Data"};
            populatedAccountTable.addRow(data);
        } else {
            Iterator i = (Iterator) accountList.iterator();

            while(i.hasNext()){
                Account account = (Account) i.next();
                String[] accountData = {String.valueOf(account.getCode()), account.getAccountName(), 
                    account.getAccountType(), account.getNormally(), "₱ "+ setDecimalFormat(account.getBalance())};

                populatedAccountTable.addRow(accountData);
            }
        }    
    }
    
    private void showJournalEntryList(ArrayList<JournalEntry> entryCollection){
        journalTable.setModel(new DefaultTableModel(null, new String[]{"ID", "Date", "Description", "Encoder"}));
        DefaultTableModel populatedJournalTable = (DefaultTableModel) journalTable.getModel();
        ArrayList<JournalEntry> entryList = entryCollection;
        
        if(entryList.size() == 0){
            String[] data = {"No Data", "No Data", "No Data", "No Data"};
            populatedJournalTable.addRow(data);
        } else {
            Iterator i = (Iterator) entryList.iterator();

            while(i.hasNext()){
                JournalEntry entry = (JournalEntry) i.next();
                String[] entryData = {entry.getId(), entry.getDate(), entry.getDescription(), entry.getEncoder()};

                populatedJournalTable.addRow(entryData);
            }
        } 
    }
    
    private void showSalesList(ArrayList<SalesTransaction> salesCollection){
        salesTable.setModel(new DefaultTableModel(null, new String[]{"ID", "Date", "Customer", "Description", "Total Amount", "Amount Paid", "Balance"}));
        DefaultTableModel populatedSalesTable = (DefaultTableModel) salesTable.getModel();
        tableColumnRenderer(salesTable, 4, 6);   
        
        ArrayList<SalesTransaction> salesList = salesCollection;
        
        if(salesList.size() == 0){
            String[] data = {"No Data", "No Data", "No Data", "No Data", "No Data", "No Data", "No Data"};
            populatedSalesTable.addRow(data);
        } else {
            Iterator i = (Iterator) salesList.iterator();

            while(i.hasNext()){
                SalesTransaction sales = (SalesTransaction) i.next();
                String[] salesData = {sales.getId(), sales.getDate(), sales.getCustomerName(), sales.getDescription(), 
                    "₱ "+ setDecimalFormat(sales.getTotalAmount()), "₱ "+ setDecimalFormat(sales.getAmountPaid()), 
                    "₱ "+ setDecimalFormat(sales.getRemainingBalance())};

                populatedSalesTable.addRow(salesData);
            }
        }  
    }
    
    private void showExpensesList(ArrayList<ExpensesTransaction> expensesCollection){
        expensesTable.setModel(new DefaultTableModel(null, new String[]{"ID", "Date", "Description", "Encoder"}));
        DefaultTableModel populatedExpensesTable = (DefaultTableModel) expensesTable.getModel();
        ArrayList<ExpensesTransaction> expensesList = expensesCollection;
        
        if(expensesList.size() == 0){
            String[] data = {"No Data", "No Data", "No Data", "No Data"};
            populatedExpensesTable.addRow(data);
        } else {
            Iterator i = (Iterator) expensesList.iterator();

            while(i.hasNext()){
                ExpensesTransaction expenses = (ExpensesTransaction) i.next();
                String[] expensesData = {expenses.getId(), expenses.getDate(), expenses.getDescription(),expenses.getEncoder()};

                populatedExpensesTable.addRow(expensesData);
            }
        }   
    }
    
    private void showPurchasesList(ArrayList<PurchaseTransaction> purchaseCollection){
        purchasesTable.setModel(new DefaultTableModel(null, new String[]{"ID", "Date", "Supplier", "Description", "Total Amount", 
            "Amount Paid", "Balance" ,"Asset Account"}));
        DefaultTableModel populatedPurchasesTable = (DefaultTableModel) purchasesTable.getModel();
        tableColumnRenderer(purchasesTable, 4, 6);   
        
        ArrayList<PurchaseTransaction> purchasesList = purchaseCollection;
        
        if(purchasesList.size() == 0){
            String[] data = {"No Data", "No Data", "No Data", "No Data", "No Data", "No Data", "No Data", "No Data"};
            populatedPurchasesTable.addRow(data);
        } else {
            Iterator i = (Iterator) purchasesList.iterator();

            while(i.hasNext()){
                PurchaseTransaction purchases = (PurchaseTransaction) i.next();
                String[] purchasesData = {purchases.getId(), purchases.getDate(), purchases.getSupplier(), purchases.getDescription(), 
                    "₱ " + setDecimalFormat(purchases.getTotalAmount()),    "₱ " + setDecimalFormat(purchases.getAmountPaid()), 
                    "₱ " + setDecimalFormat(purchases.getRemainingBalance()), purchases.getAssetAccount().getAccountName()};

                populatedPurchasesTable.addRow(purchasesData);
            }
        }   
    }
    
    private void showLedger(int accountCode, String accountNormally, String fromDate, String toDate){
        ledgerTable.setModel(new DefaultTableModel(null, new String[]{"ID", "Date", "Description", "Debit", "Credit"}));
        tableColumnRenderer(ledgerTable, 3, 4);   
        
        Double balance = 0.00;
        Double totalDebit = 0.00;
        Double totalCredit = 0.00;
        
        ArrayList<LedgerAccount> postedEntries = new EntryDBController().getPostedEntries(accountCode, fromDate, toDate);
        
        DefaultTableModel ledger = (DefaultTableModel) ledgerTable.getModel();
        
        Iterator i = (Iterator) postedEntries.iterator();       
        
        while(i.hasNext()){
            LedgerAccount ledgerAccount = (LedgerAccount) i.next();
            
            if(accountNormally.equalsIgnoreCase("Debit")){
                if(ledgerAccount.getAction().equalsIgnoreCase("Debit")){
                    String[] entryData = {ledgerAccount.getId(), ledgerAccount.getDate(), ledgerAccount.getDescription(), 
                        " ₱ " + setDecimalFormat(ledgerAccount.getAmount()), ""};
                    ledger.addRow(entryData);
                    totalDebit += ledgerAccount.getAmount();
                } else {
                    String[] entryData = {ledgerAccount.getId(), ledgerAccount.getDate(), ledgerAccount.getDescription(), "",
                        " ₱ " + setDecimalFormat(ledgerAccount.getAmount())};
                    ledger.addRow(entryData);
                    totalCredit += ledgerAccount.getAmount();
                }
            } else {
                if(ledgerAccount.getAction().equalsIgnoreCase("Debit")){
                    String[] entryData = {ledgerAccount.getId(), ledgerAccount.getDate(), ledgerAccount.getDescription(), 
                        " ₱ " + setDecimalFormat(ledgerAccount.getAmount()), ""};
                    ledger.addRow(entryData);
                    totalDebit += ledgerAccount.getAmount();
                } else {
                    String[] entryData = {ledgerAccount.getId(), ledgerAccount.getDate(), ledgerAccount.getDescription(), "",
                        " ₱ " + setDecimalFormat(ledgerAccount.getAmount())};
                    ledger.addRow(entryData);
                    totalCredit += ledgerAccount.getAmount();
                }    
            }  
        }     
        
        if(accountNormally.equalsIgnoreCase("Debit")){
            balance = totalDebit - totalCredit;
        } else {
            balance = totalCredit - totalDebit;
        }

        if(postedEntries.size() != 0){
            String[] totalData = {"", "", "Total", " ₱ " + setDecimalFormat(totalDebit), " ₱ " + setDecimalFormat(totalCredit)};
            ledger.addRow(totalData);
            ledgerTotalBalance.setText("₱ " + setDecimalFormat(balance));
        } else {
            String[] totalData = {"No Data Found", "No Data Found", "No Data Found", "No Data Found", "No Data Found"};
            ledger.addRow(totalData);
            ledgerTotalBalance.setText("₱ " + setDecimalFormat(balance));
        }
    }
    
    private void showProductTable(){
        productTable.setModel(new DefaultTableModel(null, new String[]{"Name", "Description", "Qty/Size", "Price"}));
        ArrayList<Item> productList =  products;
        tableColumnRenderer(productTable, 3, 3);   
        DefaultTableModel populatedProductTable = (DefaultTableModel) productTable.getModel();
        
        if(productList.size() == 0){
            String[] data = {"No Data", "No Data", "No Data", "No Data"};
            populatedProductTable.addRow(data);
        } else {
            Iterator i = (Iterator) productList.iterator();

            while(i.hasNext()){
                Item item = (Item) i.next();
                String[] data = {item.getName(), item.getDescription(), item.getQuantity(), "₱ " + setDecimalFormat(item.getPrice())};
                populatedProductTable.addRow(data);
            }
        }  
    }
    
    //Summary
    public void initSummary(){
        setSummaryRevenue();
        setSummaryExpenses();
        setSummaryAccountsList();
        setSummaryProfitAndLoss();
        showRecentTransactionsList();
    }
    
    private void setSummaryRevenue(){
        int i = revenueFor.getSelectedIndex();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate dateToday = LocalDate.now();
        String currentDate = dateToday.format(dateFormat);
        String firstDayOfWeek = dateToday.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).format(dateFormat);
        String lastDayOfWeek = dateToday.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).format(dateFormat);
        String firstDayOfMonth = dateToday.with(TemporalAdjusters.firstDayOfMonth()).format(dateFormat);
        String lastDayOfMonth = dateToday.with(TemporalAdjusters.lastDayOfMonth()).format(dateFormat);
        String firstDayOfYear = dateToday.with(TemporalAdjusters.firstDayOfYear()).format(dateFormat);
        String lastDayOfYear = dateToday.with(TemporalAdjusters.lastDayOfYear()).format(dateFormat);
        
        switch(i){
            case 0 :
                summarizedRevenue.setText("₱ " + setDecimalFormat(new EntryDBController().getTotalRevenue(currentDate, currentDate)));
                break;
                
            case 1 :
                summarizedRevenue.setText("₱ " + setDecimalFormat(new EntryDBController().getTotalRevenue(firstDayOfWeek, lastDayOfWeek)));
                break;
                
            case 2 :
                summarizedRevenue.setText("₱ " + setDecimalFormat(new EntryDBController().getTotalRevenue(firstDayOfMonth, lastDayOfMonth)));
                break;
                
            case 3 :    
                summarizedRevenue.setText("₱ " + setDecimalFormat(new EntryDBController().getTotalRevenue(firstDayOfYear, lastDayOfYear)));
                break;
        }
    }
    
    private void setSummaryExpenses(){
        int i = expensesFor.getSelectedIndex();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate dateToday = LocalDate.now();
        String currentDate = dateToday.format(dateFormat);
        String firstDayOfWeek = dateToday.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).format(dateFormat);
        String lastDayOfWeek = dateToday.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).format(dateFormat);
        String firstDayOfMonth = dateToday.with(TemporalAdjusters.firstDayOfMonth()).format(dateFormat);
        String lastDayOfMonth = dateToday.with(TemporalAdjusters.lastDayOfMonth()).format(dateFormat);
        String firstDayOfYear = dateToday.with(TemporalAdjusters.firstDayOfYear()).format(dateFormat);
        String lastDayOfYear = dateToday.with(TemporalAdjusters.lastDayOfYear()).format(dateFormat);
        
        switch(i){
            case 0 :               
                summarizedExpenses.setText("₱ " + setDecimalFormat(new EntryDBController().getTotalExpenses(currentDate, currentDate)));
                break;
                
            case 1 :
                summarizedExpenses.setText("₱ " + setDecimalFormat(new EntryDBController().getTotalExpenses(firstDayOfWeek, lastDayOfWeek)));
                break;
                
            case 2 :
                summarizedExpenses.setText("₱ " + setDecimalFormat(new EntryDBController().getTotalExpenses(firstDayOfMonth, lastDayOfMonth)));
                break;
                
            case 3 :    
                summarizedExpenses.setText("₱ " + setDecimalFormat(new EntryDBController().getTotalExpenses(firstDayOfYear, lastDayOfYear)));
                break;
        }
    }
    
    private void setSummaryAccountsList(){
        summaryAccountsList.setModel(new DefaultTableModel(null, new String[]{"Account", "Balance"}));
        tableColumnRenderer(summaryAccountsList, 1, 1);   
        DefaultTableModel populatedAccountsTable = (DefaultTableModel) summaryAccountsList.getModel();
        
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate dateToday = LocalDate.now();
        
        ArrayList<Account>  accountsList =  new AccountDBController().getAllAccounts(
                getAccountingConfig(true, false, false, false), dateToday.format(dateFormat));
        
        Iterator i = (Iterator) accountsList.iterator();
        
        while(i.hasNext()){
            Account account = (Account) i.next();
            
            String[] accountData = {account.getAccountName(), "₱ " + setDecimalFormat(account.getBalance())};
            populatedAccountsTable.addRow(accountData);
        }
    }
    
    private void setSummaryProfitAndLoss(){
        Double revenue = 0.00;
        Double expenses = 0.00;
        Double netIncome = 0.00;
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate dateToday = LocalDate.now();
        LocalDate date3DaysBefore = dateToday.minusDays(3);
        LocalDate date2DaysBefore = dateToday.minusDays(2);
        String currentDate = dateToday.format(dateFormat);
        String firstDayOfWeek = dateToday.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).format(dateFormat);
        String lastDayOfWeek = dateToday.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).format(dateFormat);
        String firstDayOfMonth = dateToday.with(TemporalAdjusters.firstDayOfMonth()).format(dateFormat);
        String lastDayOfMonth = dateToday.with(TemporalAdjusters.lastDayOfMonth()).format(dateFormat);
        String dateThreeDaysBefore = date3DaysBefore.format(dateFormat);
        String dateTwoDaysBefore = date2DaysBefore.format(dateFormat);    
        
        int i = Integer.parseInt(getAccountingConfig(false,false, false, true));
        
        switch(i){
            case 0 :
                revenue = new EntryDBController().getTotalRevenue(currentDate, currentDate);
                expenses = new EntryDBController().getTotalExpenses(currentDate, currentDate);
                palRange.setText("For Today");
                break;
                
            case 1 :    
                revenue = new EntryDBController().getTotalRevenue(dateTwoDaysBefore, currentDate);
                expenses = new EntryDBController().getTotalExpenses(dateTwoDaysBefore, currentDate);
                palRange.setText("For Past 2 Days");
                break;
                
            case 2 :
                revenue = new EntryDBController().getTotalRevenue(dateThreeDaysBefore, currentDate);
                expenses = new EntryDBController().getTotalExpenses(dateThreeDaysBefore, currentDate);
                palRange.setText("For Past 3 Days");
                break;
                
            case 3 :
                revenue = new EntryDBController().getTotalRevenue(firstDayOfWeek, lastDayOfWeek);
                expenses = new EntryDBController().getTotalExpenses(firstDayOfWeek, lastDayOfWeek);
                palRange.setText("For This Week");
                break;
                
            case 4 :    
                revenue = new EntryDBController().getTotalRevenue(firstDayOfMonth, lastDayOfMonth);
                expenses = new EntryDBController().getTotalExpenses(firstDayOfMonth, lastDayOfMonth);
                palRange.setText("For This Month");
                break;
        }
        
        netIncome = revenue - expenses;
        
        palRevenue.setText("₱ " + setDecimalFormat(revenue));
        palExpenses.setText("₱ " + setDecimalFormat(expenses));
        palNetIncome.setText("₱ " + setDecimalFormat(netIncome));
        
        if(revenue > expenses){
            palStatus.setText("( Gained )");
        } else if (revenue < expenses){
            palStatus.setText("( Loss )");
        } else {
            palStatus.setText("( Neutral )");
        }
    }
    
    private void showRecentTransactionsList(){
        recentTransactionsList.setModel(new DefaultTableModel(null, new String[]{"ID", "Date", "Description", "Amount"}));
        tableColumnRenderer(recentTransactionsList, 3, 3);  
        recentTransactionsList.getColumnModel().getColumn(0).setPreferredWidth(20);
        recentTransactionsList.getColumnModel().getColumn(1).setPreferredWidth(5);
        recentTransactionsList.getColumnModel().getColumn(2).setPreferredWidth(200);
        recentTransactionsList.getColumnModel().getColumn(3).setPreferredWidth(20);
        
        DefaultTableModel populatedRecentTransactionsList = (DefaultTableModel) recentTransactionsList.getModel();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate dateToday = LocalDate.now();
        LocalDate date3DaysBefore = dateToday.minusDays(3);
        LocalDate date2DaysBefore = dateToday.minusDays(2);
        String currentDate = dateToday.format(dateFormat);
        String firstDayOfWeek = dateToday.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).format(dateFormat);
        String lastDayOfWeek = dateToday.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).format(dateFormat);
        String dateThreeDaysBefore = date3DaysBefore.format(dateFormat);
        String dateTwoDaysBefore = date2DaysBefore.format(dateFormat); 
               
        ArrayList<LedgerAccount> recentTransactions = new ArrayList();
        
        int i = Integer.parseInt(getAccountingConfig(false,false, true, false));
        
        switch(i){
            case 0 :
                recentTransactions = new EntryDBController().getRecentTransactions(currentDate, currentDate);
                break;
                
            case 1 :    
                recentTransactions = new EntryDBController().getRecentTransactions(dateTwoDaysBefore, currentDate);
                break;
                
            case 2 :
                recentTransactions = new EntryDBController().getRecentTransactions(dateThreeDaysBefore, currentDate);
                break;
                
            case 3 :
                recentTransactions = new EntryDBController().getRecentTransactions(firstDayOfWeek, lastDayOfWeek);
                break;
        }
        
        if(recentTransactions.size() == 0){
            String[] data = {"No Data", "No Data", "No Data", "No Data"};
            populatedRecentTransactionsList.addRow(data);
        } else {
            Iterator r = (Iterator) recentTransactions.iterator();

            while(r.hasNext()){
                LedgerAccount recent = (LedgerAccount) r.next();
                String[] recentTransactionData = {recent.getId(), recent.getDate(), recent.getDescription(), "₱ " + setDecimalFormat(recent.getAmount())};
                populatedRecentTransactionsList.addRow(recentTransactionData);
            }
        }    
    }
    
    //Reports  
    private void showBalanceSheetReport(){
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        DateTimeFormatter dateFormat2 = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate dateToday = LocalDate.now();
        reportTable.setModel(new DefaultTableModel(null, new String[]{"  ", "  ", "  "}));
        DefaultTableModel populatedTableModel = (DefaultTableModel) reportTable.getModel();
        tableColumnRenderer(reportTable,2,2);
        tableColumnRenderer(reportTable,0,0);
        ArrayList<Account>  accountsList =  new AccountDBController().getAllAccounts(
                getAccountingConfig(true, false,false, false), dateToday.format(dateFormat2));
        String[] space = {" ", " ", " "};
        int stopper = 0;
        int assetCount = 0; 
        int liabilitiesCount = 0;
        int equityCount = 0;
        Double totalAsset = 0.00;
        Double totalLiability = 0.00;
        Double totalEquity = 0.00;
        Double retainedEarnings = 0.00; 
        Double revenue = 0.00;
        Double expenses = 0.00;
        
        String[] dataAsOF = {"", "", "As of "+ dateToday.format(dateFormat)};
        populatedTableModel.addRow(dataAsOF);
        
        //Assets
        for(int i = 0; i < accountsList.size(); i++){
            if(accountsList.get(i).getAccountType().equalsIgnoreCase("Assets")){
                assetCount++;
            }
            
            if(assetCount == 1 && stopper == 0){
                String[] accountData = {"ASSETS ", "", ""};
                populatedTableModel.addRow(accountData);
            }
            
            if(accountsList.get(i).getAccountType().equalsIgnoreCase("Assets") && accountsList.get(i).getBalance() > 0.00){
                String[] accountData = {"", accountsList.get(i).getAccountName(), "₱ " + setDecimalFormat(accountsList.get(i).getBalance())};
                populatedTableModel.addRow(accountData);
                totalAsset += accountsList.get(i).getBalance();
            }
            
            if(assetCount == 1){ stopper++;}
        }

        String[] totalAssetsRow = {" ", "Total Assets", "₱ " + setDecimalFormat(totalAsset)};
        populatedTableModel.addRow(totalAssetsRow);
        populatedTableModel.addRow(space);
             
        //Liabilities
        stopper = 0;
        for(int i = 0; i < accountsList.size(); i++){
            if(accountsList.get(i).getAccountType().equalsIgnoreCase("Liabilities")){
                liabilitiesCount++;
            }
            
            if(liabilitiesCount == 1 && stopper == 0){
                String[] accountData = {"LIABILITIES ", "", ""};
                populatedTableModel.addRow(accountData);
            }
            
            if(accountsList.get(i).getAccountType().equalsIgnoreCase("Liabilities") && accountsList.get(i).getBalance() > 0.00){
                String[] accountData = {"", accountsList.get(i).getAccountName(), "₱ " + setDecimalFormat(accountsList.get(i).getBalance())};
                populatedTableModel.addRow(accountData);
                totalLiability += accountsList.get(i).getBalance();
            }
            
            if(liabilitiesCount == 1){ stopper++;}
        }
        
        String[] totalLiabilitiesRow = {" ", "Total Liabilities", "₱ " + setDecimalFormat(totalLiability)};
        populatedTableModel.addRow(totalLiabilitiesRow );
        populatedTableModel.addRow(space);
        
        //Equity
        stopper = 0;
        for(int i = 0; i < accountsList.size(); i++){
            if(accountsList.get(i).getAccountType().equalsIgnoreCase("Equity")){
                equityCount++;
            }
            
            if(equityCount == 1 && stopper == 0){
                String[] accountData = {"EQUITY ", "", ""};
                populatedTableModel.addRow(accountData);
            }
            
            if(accountsList.get(i).getAccountType().equalsIgnoreCase("Equity") && accountsList.get(i).getBalance() > 0.00){
                String[] accountData = {"", accountsList.get(i).getAccountName(), "₱ " + setDecimalFormat(accountsList.get(i).getBalance())};
                populatedTableModel.addRow(accountData);
                totalEquity += accountsList.get(i).getBalance();
            }
            
            if(equityCount == 1){stopper++;}
        }
        
        for(int i = 0; i < accountsList.size(); i++){
            if(accountsList.get(i).getAccountType().equalsIgnoreCase("Revenue")){
                revenue += accountsList.get(i).getBalance();
            } else if (accountsList.get(i).getAccountType().equalsIgnoreCase("Expenses")){
                expenses += accountsList.get(i).getBalance();
            }
        }
        
        retainedEarnings = revenue - expenses;
        
        String[] retainedEarningsRow = {" ", "Retained Earnings", "₱ " + setDecimalFormat(retainedEarnings)};
        populatedTableModel.addRow(retainedEarningsRow);
        
        totalEquity += retainedEarnings; 
        String[] totalEquityRow = {" ", "Total Equity", "₱ " + setDecimalFormat(totalEquity)};
        populatedTableModel.addRow(totalEquityRow);
        
        Double totalLiabilitiesAndEquity = totalLiability + totalEquity;
        String[] totalLiabilitiesAndEquityRow = {" ", "Total Liabilities and Equity", "₱ " + setDecimalFormat(totalLiabilitiesAndEquity)};
        populatedTableModel.addRow(totalLiabilitiesAndEquityRow);
    }
    
    private void showIncomeStatementReport(String fromDate, String toDate){
        reportTable.setModel(new DefaultTableModel(null, new String[]{"  ", "  ", "  "}));
        DefaultTableModel populatedTableModel = (DefaultTableModel) reportTable.getModel();
        tableColumnRenderer(reportTable,2,2);
        tableColumnRenderer(reportTable,0,0);
        ArrayList<Account>  accountsList =  new AccountDBController().getAllAccounts(fromDate, toDate);
        String[] space = {" ", " ", " "};
        int stopper = 0;
        int revenueCount = 0; 
        int expensesCount = 0;
        Double totalRevenue = 0.00;
        Double totalExpenses = 0.00;
        
        String[] dataFor = {"", "", "For " + fromDate + " to " + toDate};
        populatedTableModel.addRow(dataFor);
        
        //Revenue
        for(int i = 0; i < accountsList.size(); i++){
            if(accountsList.get(i).getAccountType().equalsIgnoreCase("Revenue")){
                revenueCount++;
            }
            
            if(revenueCount == 1 && stopper == 0){
                String[] accountData = {"REVENUE ", "", ""};
                populatedTableModel.addRow(accountData);
            }
            
            if(accountsList.get(i).getAccountType().equalsIgnoreCase("Revenue") && accountsList.get(i).getBalance() > 0.00){
                String[] accountData = {"", accountsList.get(i).getAccountName(), "₱ " + setDecimalFormat(accountsList.get(i).getBalance())};
                populatedTableModel.addRow(accountData);
                totalRevenue += accountsList.get(i).getBalance();
            }
            
            if(revenueCount == 1){ stopper++;}
        }
        
        String[] totalRevenueRow = {" ", "Total Revenue", "₱ " + setDecimalFormat(totalRevenue)};
        populatedTableModel.addRow(totalRevenueRow);
        populatedTableModel.addRow(space);
        
        //Expenses
        stopper = 0;
        for(int i = 0; i < accountsList.size(); i++){
            if(accountsList.get(i).getAccountType().equalsIgnoreCase("Expenses")){
                expensesCount++;
            }
            
            if(expensesCount == 1 && stopper == 0){
                String[] accountData = {"EXPENSES ", "", ""};
                populatedTableModel.addRow(accountData);
            }
            
            if(accountsList.get(i).getAccountType().equalsIgnoreCase("Expenses") && accountsList.get(i).getBalance() > 0.00){
                String[] accountData = {"", accountsList.get(i).getAccountName(), "₱ " + setDecimalFormat(accountsList.get(i).getBalance())};
                populatedTableModel.addRow(accountData);
                totalExpenses += accountsList.get(i).getBalance();
            }
            
            if(expensesCount == 1){ stopper++;}
        }
        
        String[] totalExpensesRow = {" ", "Total Expenses", "₱ " + setDecimalFormat(totalExpenses)};
        populatedTableModel.addRow(totalExpensesRow);
        populatedTableModel.addRow(space);
        
        Double netIncome = totalRevenue - totalExpenses;
        
        String[] netIncomeRow = {"NET INCOME", "( Total Revenue - Total Expenses)", "₱ " + setDecimalFormat(netIncome)};
        populatedTableModel.addRow(netIncomeRow);
    }
    
    private void showLedgerAccountReport(String fromDate, String toDate, Account account){
        reportTable.setModel(new DefaultTableModel(null, new String[]{" ", " ", " ", " ", " "}));
        DefaultTableModel populatedTableModel = (DefaultTableModel) reportTable.getModel();
        tableColumnRenderer(reportTable,2,4);
        String[] space = {" ", " ", " ", " ", " "};
        
        String[] accountTitle = {account.getAccountName(), "from " + fromDate, " to " + toDate, "", ""};
        populatedTableModel.addRow(accountTitle);
        populatedTableModel.addRow(space);
        String[] columnHeader = {"Date", "Description", "Debit", "Credit", "Balance"};
        populatedTableModel.addRow(columnHeader);
        populatedTableModel.addRow(space);
        
        ArrayList<Double> trueBalance = new ArrayList();
        ArrayList<LedgerAccount> rawBalance = new EntryDBController().getTruePostedEntries(account.getCode(), 
                getAccountingConfig(true, false, false, false), toDate);
        Double balance = 0.00;
        
        Iterator t = (Iterator) rawBalance.iterator();
        
        while(t.hasNext()){
            LedgerAccount ledgerAccount = (LedgerAccount) t.next();
            
            if(account.getNormally().equalsIgnoreCase("Debit")){
                if(ledgerAccount.getAction().equalsIgnoreCase("Debit")){
                    balance += ledgerAccount.getAmount();
                } else {
                    balance -= ledgerAccount.getAmount();
                }
            } else {
                if(ledgerAccount.getAction().equalsIgnoreCase("Debit")){
                    balance -= ledgerAccount.getAmount();
                } else {
                    balance += ledgerAccount.getAmount();
                }
            }
            trueBalance.add(balance);
        }
        
        ArrayList<LedgerAccount> postedEntries = new EntryDBController().getPostedEntries(account.getCode(), fromDate, toDate);
        
        Iterator i = (Iterator) postedEntries.iterator();
        
        int position = trueBalance.size() - postedEntries.size();
        
        while(i.hasNext()){
            LedgerAccount ledgerAccount = (LedgerAccount) i.next();
            
            if(account.getNormally().equalsIgnoreCase("Debit")){
                if(ledgerAccount.getAction().equalsIgnoreCase("Debit")){
                    String[] entryData = {ledgerAccount.getDate(), ledgerAccount.getDescription(), 
                        " ₱ " + setDecimalFormat(ledgerAccount.getAmount()), "", " ₱ " + setDecimalFormat(trueBalance.get(position))};
                    populatedTableModel.addRow(entryData);
                } else {
                    String[] entryData = {ledgerAccount.getDate(), ledgerAccount.getDescription(), "",
                        " ₱ " + setDecimalFormat(ledgerAccount.getAmount()), " ₱ " + setDecimalFormat(trueBalance.get(position))};
                    populatedTableModel.addRow(entryData);
                }
            } else {
                if(ledgerAccount.getAction().equalsIgnoreCase("Debit")){
                    String[] entryData = {ledgerAccount.getDate(), ledgerAccount.getDescription(), 
                        " ₱ " + setDecimalFormat(ledgerAccount.getAmount()), "", " ₱ " + setDecimalFormat(trueBalance.get(position))};
                    populatedTableModel.addRow(entryData);
                } else {
                    String[] entryData = {ledgerAccount.getDate(), ledgerAccount.getDescription(), "",
                        " ₱ " + setDecimalFormat(ledgerAccount.getAmount()), " ₱ " + setDecimalFormat(trueBalance.get(position))};
                    populatedTableModel.addRow(entryData);
                }    
            }  

            position++;
        } 
    }
    
    private void showTrialBalanceReport(){
        DateTimeFormatter dateFormat1 = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        DateTimeFormatter dateFormat2 = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate dateToday = LocalDate.now();
        String asOf ="As of " + dateToday.format(dateFormat1);
        reportTable.setModel(new DefaultTableModel(null, new String[]{" ", " ", " "}));
        DefaultTableModel populatedTableModel = (DefaultTableModel) reportTable.getModel();
        tableColumnRenderer(reportTable,1,2);
        String[] space = {" ", " ", " "};
        
        ArrayList<Account>  accountsList =  new AccountDBController().getAllAccounts(
                getAccountingConfig(true, false,false, false), dateToday.format(dateFormat2));
        Double totalDebit = 0.00;
        Double totalCredit = 0.00;
        
        String[] date = {" ", " ", asOf};
        populatedTableModel.addRow(date);
        populatedTableModel.addRow(space);
        String[] columnHeader = {"Account", "Debit", "Credit"};
        populatedTableModel.addRow(columnHeader);
        populatedTableModel.addRow(space);
        
        Iterator i = (Iterator) accountsList.iterator();
        
        while(i.hasNext()){
            Account account = (Account) i.next();
            
            if(account.getBalance() > 0){
                if(account.getNormally().equalsIgnoreCase("Debit")){
                    String[] data = {account.getAccountName(), "₱ " + setDecimalFormat(account.getBalance()), " "};
                    populatedTableModel.addRow(data);
                    totalDebit += account.getBalance();
                } else {
                    String[] data = {account.getAccountName(), " ", "₱ " + setDecimalFormat(account.getBalance())};
                    populatedTableModel.addRow(data);
                    totalCredit += account.getBalance();
                }
            }
        }
        String[] total = {"Total","₱ " + setDecimalFormat(totalDebit), "₱ " + setDecimalFormat(totalCredit)};
        populatedTableModel.addRow(total);
    }
    
    //Other functions     
    public String stringEscape(String text){
        String escape = "";
        
        if(text.contains("'")){
            int size = text.length();
            for(int i = 0; i < size; i++){
                if(text.charAt(i) == '\''){
                    escape += "\\" + text.charAt(i);
                } else {
                    escape += text.charAt(i);
                }
            }
        } else {
            return text;
        }
        
        return escape;
    }
    
    private Date stringToDate(String date){
        Date convertedDate = new Date();
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            convertedDate = simpleDateFormat.parse(date);
        } catch (ParseException e){e.printStackTrace();}
        
        return convertedDate;
    }
    
    public void tableColumnRenderer(JTable table, int firstColumn, int lastColumn){
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.RIGHT);
        
        for(int i = firstColumn; i <= lastColumn; i++ ){
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }            
    }
    
    private String setDecimalFormat(Double amount){
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        
        return decimalFormat.format(amount);
    }
    
    private Double reverseDecimalFormat(String amount){
        String convertedNumber = "";
        
        for(int i = 0; i < amount.length(); i++){
            if(amount.charAt(i) != ','){
                convertedNumber += amount.charAt(i);
            }
        }

        return Double.parseDouble(convertedNumber);
    }
    
    public void initLedger(){
        ArrayList<Account> accountList = new AccountDBController().getAllAccounts();
        
        accountList.forEach((e) -> {
            ledgerAccountSelectionBox.addItem(e.getAccountName());
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        
        showLedger(accountList.get(ledgerAccountSelectionBox.getSelectedIndex()).getCode(), 
                accountList.get(ledgerAccountSelectionBox.getSelectedIndex()).getNormally(),
                dateFormat.format(fromLedger.getDate()) , dateFormat.format(toLedger.getDate()) );
    }
    
    public void initDates(){
        Date date = new Date();
        fromSales.setDate(date);
        toSales.setDate(date);
        fromExpenses.setDate(date);
        toExpenses.setDate(date);
        fromPurchases.setDate(date);
        toPurchases.setDate(date);
        fromTaxes.setDate(date);
        toTaxes.setDate(date);
        fromJournal.setDate(date);
        toJournal.setDate(date);
        fromLedger.setDate(date);
        toLedger.setDate(date);
        fromTrail.setDate(date);
        toTrail.setDate(date);
    }
    
    public void initProductsData(){
        this.products = new ProductDBController().getAllProducts();
    }
    
    //Listeners
    @Override
    public void updateProductTable(){
        initProductsData();
        showProductTable();
    }
    
    public void updatePurchasesPaymentConfig(ArrayList<Account> accountList){
        purchasesPaymentConfig(accountList);
        viewPurchasesPaymentMethod.setText(initPurchasesPaymentConfig());
    }    
    
    @Override
    public void updateSalesPaymentConfig(ArrayList<Account> accountList){
        salesPaymentConfig(accountList);
        viewSalesPaymentMethod.setText(initSalesPaymentConfig());
    }
    
    @Override
    public void setSelectedReportSheet(){
        reportSheet.setSelectedIndex(0);
        showBalanceSheetReport(); 
    }    
    
    @Override
    public void reportDateRange(String fromDate, String toDate, int sheet){
        switch(sheet){
            case 1 :
                showIncomeStatementReport(fromDate, toDate);
                break;
        }
        this.tempFromRangeDate = fromDate;
        this.tempToRangeDate = toDate;
    }
    
    @Override
    public void reportDateRangeForAccount(String fromDate, String toDate, Account account){
        showLedgerAccountReport(fromDate, toDate, account);
        this.account = account;
        this.tempFromRangeDate = fromDate;
        this.tempToRangeDate = toDate;
    }
    
    @Override
    public void updateUserTable(){
        showUserList(new UserDBController().getAllUsers());
    }
    
    @Override
    public void updateVendorTable(){
        showVendorList(new VendorDBController().getAllVendors());
    }
    
    @Override
    public void updateCustomerTable(){
        showCustomerList(new CustomerDBController().getAllCustomers());
    }
    
    @Override
    public void updateChartOfAccountTable(){
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate dateToday = LocalDate.now();
        showAccountList(new AccountDBController().getAllAccounts(getAccountingConfig(true, false, false, false), dateToday.format(dateFormat)));
        initChartOfAccountsVar();
    }
    
    @Override
    public void updateJournalTable(){
        showJournalEntryList(new EntryDBController().getAllJournalEntry());
        showRecentTransactionsList();
    }
    
    @Override
    public void updateSalesTable(){
        showSalesList(new EntryDBController().getAllSales());
        showRecentTransactionsList();
    }
    
    @Override
    public void updateExpensesTable(){
        showExpensesList(new EntryDBController().getAllExpensesEntry());
        showRecentTransactionsList();
    }
    
    @Override
    public void updatePurchasesTable(){
        showPurchasesList( new EntryDBController().getAllPurchases());
        showRecentTransactionsList();
    }
    
    @Override
    public void enableTaxRateConfig(){
        taxRateConfig.setEditable(true);
        changeTaxRate.setText("Save");
    }
    
    private void initSearchFieldListeners(){
        userSearchFieldListener();
        vendorSearchFieldListener();
        customerSearchFieldListener();
        accountSearchFieldListener();
        journalSearchFieldListener();
        purchasesSearchFieldListener();
        expensesSearchFieldListener();
        salesSearchFieldListener();
    }
    
    private void userSearchFieldListener(){
        userSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateUserList();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateUserList();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateUserList();
            }
            
            private void updateUserList(){
                if(!userSearchField.getText().isBlank()){
                    showUserList(new UserDBController().searchUser(userSearchField.getText()));
                } else {
                    showUserList(new UserDBController().getAllUsers());
                }
            }
        });
    }
    
    private void vendorSearchFieldListener(){
        vendorSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateVendorList();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateVendorList();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateVendorList();
            }
            
            private void updateVendorList(){
                if(!vendorSearchField.getText().isBlank()){
                    showVendorList(new VendorDBController().searchVendor(vendorSearchField.getText()));
                } else {
                    showVendorList(new VendorDBController().getAllVendors());
                }
            }
        });
    }
    
    private void customerSearchFieldListener(){
        customerSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateCustomerList();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateCustomerList();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateCustomerList();
            }
            
            private void updateCustomerList(){
                if(!customerSearchField.getText().isBlank()){
                    showCustomerList(new CustomerDBController().searchCustomer(customerSearchField.getText()));
                } else {
                    showCustomerList(new CustomerDBController().getAllCustomers());
                }
            }
        });
    }
    
    private void accountSearchFieldListener(){
        accountSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateAccountList();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateAccountList();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateAccountList();
            }
            
            private void updateAccountList(){
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate dateToday = LocalDate.now();
                if(!accountSearchField.getText().isBlank()){
                    showAccountList(new AccountDBController().searchAccount(accountSearchField.getText(), getAccountingConfig(true, false, false, false), dateToday.format(dateFormat)));
                } else {
                    showAccountList(new AccountDBController().getAllAccounts(getAccountingConfig(true, false, false, false), dateToday.format(dateFormat)));
                }
            }
        });
    }
    
    private void journalSearchFieldListener(){
        journalSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateJournalList();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateJournalList();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateJournalList();
            }
            
            private void updateJournalList(){
                if(!journalSearchField.getText().isBlank()){
                    showJournalEntryList(new EntryDBController().searchJournalEntry(journalSearchField.getText()));
                } else {
                    showJournalEntryList(new EntryDBController().getAllJournalEntry());
                }
            }
        });
    }
    
    private void purchasesSearchFieldListener(){
        purchasesSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updatePurchasesList();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updatePurchasesList();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updatePurchasesList();
            }
            
            private void updatePurchasesList(){
                if(!purchasesSearchField.getText().isBlank()){
                    showPurchasesList(new EntryDBController().searchPurchases(purchasesSearchField.getText()));
                } else {
                    showPurchasesList(new EntryDBController().getAllPurchases());
                }
            }
        });
    }
    
    private void expensesSearchFieldListener(){
        expensesSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateExpensesList();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateExpensesList();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateExpensesList();
            }
            
            private void updateExpensesList(){
                if(!expensesSearchField.getText().isBlank()){
                    showExpensesList(new EntryDBController().searchExpensesEntry(expensesSearchField.getText()));
                } else {
                    showExpensesList(new EntryDBController().getAllExpensesEntry());
                }
            }
        });
    }
    
    private void salesSearchFieldListener(){
        salesSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSalesList();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSalesList();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSalesList();
            }
            
            private void updateSalesList(){
                if(!salesSearchField.getText().isBlank()){
                    showSalesList(new EntryDBController().searchSales(salesSearchField.getText()));
                } else {
                    showSalesList(new EntryDBController().getAllSales());
                }
            }
        });
    }
    
    private class CloseWindow extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e){
            int n = JOptionPane.showConfirmDialog(rootPane, "Are you sure you wanted to exit?");
            if(n == 0) {
                System.exit(0);
            }
        }
    }
    
    //Configurations   
    public void initSalesConfiguration(){
        viewSalesPaymentMethod.setText(initSalesPaymentConfig());
        ArrayList<Account> accountList = new AccountDBController().getAllAccounts();
        
        accountList.forEach((e) -> {
            setSalesRemainingBalanceAccount.addItem(e.getAccountName());
        });
      
        setSalesRemainingBalanceAccount.setSelectedItem(getAccountConfig("salesconfig.properties", "sales_remaining_balance_account"));
        
        accountList.forEach((e) -> {
            setSalesRevenueAccount.addItem(e.getAccountName());
        });
        
        setSalesRevenueAccount.setSelectedItem(getAccountConfig("salesconfig.properties", "sales_revenue_account"));
    }
    
    public void initPurchasesConfiguration(){
        viewPurchasesPaymentMethod.setText(initPurchasesPaymentConfig());
        ArrayList<Account> accountList = new AccountDBController().getAllAccounts();
        
        accountList.forEach((e) -> {
            setPurchasesRemainingBalanceAccount.addItem(e.getAccountName());
        });
        
        setPurchasesRemainingBalanceAccount.setSelectedItem(getAccountConfig("purchasesconfig.properties", "purchases_remaining_balance_account"));    
    }
    
    private void initChartOfAccountsVar(){
        this.chartOfAccounts = new AccountDBController().getAllAccounts();
    }
    
    public void initNetworkSettings(){
        try(InputStream input = new FileInputStream("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Project\\BMS\\src\\bms\\path\\to\\config.properties")){
            Properties network = new Properties();
            network.load(input);
            
            Cryptographer cryptographer = new Cryptographer();
            
            String decryptedPassword = "";
            
            try {
                decryptedPassword = cryptographer.decrypt(network.getProperty("password"));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, e.getMessage());
                e.printStackTrace();
            }
              
            networkUsername.setText(network.getProperty("username"));
            networkPassword.setText(decryptedPassword);
            networkServer.setText(network.getProperty("server"));
            networkPort.setText(network.getProperty("port"));
            networkDatabase.setText(network.getProperty("database"));               
        } catch (IOException io){
            io.printStackTrace();
        }   
        
        saveNetwork.setEnabled(false);
        networkUsername.enableInputMethods(false);
        networkPassword.enableInputMethods(false);
        networkServer.enableInputMethods(false);
        networkPort.enableInputMethods(false);
        networkDatabase.enableInputMethods(false);
    }
    
    private void accountingConfiguration(){
        try(OutputStream output = new FileOutputStream("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Project\\BMS\\src\\bms\\path\\to\\accconfig.properties")){
            Properties network = new Properties();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String formattedDate = dateFormat.format(openingBalanceDate.getDate());
                
            network.setProperty("opening_balance_date", formattedDate);
            network.setProperty("tax_rate", taxRateConfig.getText());
            network.setProperty("recent_transactions_range", String.valueOf(recentTransactionsRange.getSelectedIndex()));
            network.setProperty("profit_and_loss_range", String.valueOf(profitAndLossRange.getSelectedIndex()));
                
            network.store(output, null);

        } catch (IOException io){
            io.printStackTrace();
        }    
    }
    
    private void salesConfig(){
        try(OutputStream output = new FileOutputStream("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Project\\BMS\\src\\bms\\path\\to\\salesconfig.properties")){
            Properties network = new Properties();

            network.setProperty("sales_remaining_balance_account", String.valueOf(setSalesRemainingBalanceAccount.getSelectedItem()));
            network.setProperty("sales_revenue_account", String.valueOf(setSalesRevenueAccount.getSelectedItem()));
                
            network.store(output, null);

        } catch (IOException io){
            io.printStackTrace();
        }
    }
    
    private void purchasesConfig(){
        try(OutputStream output = new FileOutputStream("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Project\\BMS\\src\\bms\\path\\to\\purchasesconfig.properties")){
            Properties network = new Properties();

            network.setProperty("purchases_remaining_balance_account", String.valueOf(setPurchasesRemainingBalanceAccount.getSelectedItem()));
                
            network.store(output, null);

        } catch (IOException io){
            io.printStackTrace();
        }
    }
    
    public String getAccountConfig(String fileName, String variableName){
        String account = "";
        try(InputStream input = new FileInputStream("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Project\\BMS\\src\\bms\\path\\to\\" + fileName)){
            Properties network = new Properties();
            network.load(input);           
            
            account = network.getProperty(variableName);
        } catch (Exception io){
            io.printStackTrace();
        }
        return account;
    }
    
    private void salesPaymentConfig(ArrayList<Account> accountList){
        try(OutputStream output = new FileOutputStream("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Project\\BMS\\src\\bms\\path\\to\\salespaymentconfig.properties")){
            Properties network = new Properties();
            
            network.setProperty("size", String.valueOf(accountList.size()));
            
            for(int i = 0; i < accountList.size(); i++){
                network.setProperty("method" + i, String.valueOf(accountList.get(i).getCode()));
            }

            network.store(output, null);

        } catch (IOException io){
            io.printStackTrace();
        }
    }
    
    private void purchasesPaymentConfig(ArrayList<Account> accountList){
        try(OutputStream output = new FileOutputStream("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Project\\BMS\\src\\bms\\path\\to\\purchasespaymentconfig.properties")){
            Properties network = new Properties();
            
            network.setProperty("size", String.valueOf(accountList.size()));
            
            for(int i = 0; i < accountList.size(); i++){
                network.setProperty("method" + i, String.valueOf(accountList.get(i).getCode()));
            }

            network.store(output, null);

        } catch (IOException io){
            io.printStackTrace();
        }
    }
    
    public String initSalesPaymentConfig(){
        String paymentMethods = "";
        try(InputStream input = new FileInputStream("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Project\\BMS\\src\\bms\\path\\to\\salespaymentconfig.properties")){
            Properties network = new Properties();
            network.load(input);
            
            int size = Integer.parseInt(network.getProperty("size"));
            
            ArrayList<String> accountCodes = new ArrayList();
            
            for(int i = 0; i < size; i++){
                accountCodes.add(network.getProperty("method" + i));
            }
            
            ArrayList<Account> accountList = new AccountDBController().getAccountList(accountCodes);
            
            int n = 1;
            for(Account account : accountList){
                if(accountList.size() == 1){
                    return account.getAccountName();
                }
                
                if(n < accountList.size()){
                   paymentMethods += account.getAccountName() + ", ";
                } else {
                  paymentMethods += " and " + account.getAccountName();  
                }
                
                n++;
            }
            
        } catch (Exception io){
            io.printStackTrace();
        }
        return paymentMethods;
    }
    
    public String initPurchasesPaymentConfig(){
        String paymentMethods = "";
        try(InputStream input = new FileInputStream("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Project\\BMS\\src\\bms\\path\\to\\purchasespaymentconfig.properties")){
            Properties network = new Properties();
            network.load(input);
            
            int size = Integer.parseInt(network.getProperty("size"));
            
            ArrayList<String> accountCodes = new ArrayList();
            
            for(int i = 0; i < size; i++){
                accountCodes.add(network.getProperty("method" + i));
            }
            
            ArrayList<Account> accountList = new AccountDBController().getAccountList(accountCodes);
            
            int n = 1;
            for(Account account : accountList){
                if(accountList.size() == 1){
                    return account.getAccountName();
                }
                
                if(n < accountList.size()){
                   paymentMethods += account.getAccountName() + ", ";
                } else {
                  paymentMethods += " and " + account.getAccountName();  
                }
                
                n++;
            }
            
        } catch (Exception io){
            io.printStackTrace();
        }
        return paymentMethods;
    }
    
    private ArrayList<Account> getSalesPaymentAccounts(){
        ArrayList<Account> accountList = new ArrayList();
        try(InputStream input = new FileInputStream("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Project\\BMS\\src\\bms\\path\\to\\salespaymentconfig.properties")){
            Properties network = new Properties();
            network.load(input);
            
            int size = Integer.parseInt(network.getProperty("size"));
            
            ArrayList<String> accountCodes = new ArrayList();
            
            for(int i = 0; i < size; i++){
                accountCodes.add(network.getProperty("method" + i));
            }
            
            ArrayList<Account> accounts = new AccountDBController().getAccountList(accountCodes);
            
            for(Account account : accounts){
                accountList.add(account);
            }
            
        } catch (Exception io){
            io.printStackTrace();
        }
        return accountList;
    }
    
    private ArrayList<Account> getPurchasesPaymentAccounts(){
        ArrayList<Account> accountList = new ArrayList();
        try(InputStream input = new FileInputStream("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Project\\BMS\\src\\bms\\path\\to\\purchasespaymentconfig.properties")){
            Properties network = new Properties();
            network.load(input);
            
            int size = Integer.parseInt(network.getProperty("size"));
            
            ArrayList<String> accountCodes = new ArrayList();
            
            for(int i = 0; i < size; i++){
                accountCodes.add(network.getProperty("method" + i));
            }
            
            ArrayList<Account> accounts = new AccountDBController().getAccountList(accountCodes);
            
            for(Account account : accounts){
                accountList.add(account);
            }
            
        } catch (Exception io){
            io.printStackTrace();
        }
        return accountList;
    }
    
    public void initAccountingConfigurations(){
        try(InputStream input = new FileInputStream("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Project\\BMS\\src\\bms\\path\\to\\accconfig.properties")){
            Properties network = new Properties();
            network.load(input);
            Date date = new SimpleDateFormat("MM/dd/yyyy").parse(network.getProperty("opening_balance_date"));
            openingBalanceDate.setDate(date);
            taxRateConfig.setText(network.getProperty("tax_rate"));
            taxRate.setText(network.getProperty("tax_rate"));
            recentTransactionsRange.setSelectedIndex(Integer.parseInt(network.getProperty("recent_transactions_range")));
            profitAndLossRange.setSelectedIndex(Integer.parseInt(network.getProperty("profit_and_loss_range")));
        } catch (Exception io){
            io.printStackTrace();
        }
    }
    
    private String getAccountingConfig(boolean isOpeningBalance, boolean isTaxRate, boolean isRecentTransactionsRange, boolean isProfitAndLoss){
        String value = "";
        try(InputStream input = new FileInputStream("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Project\\BMS\\src\\bms\\path\\to\\accconfig.properties")){
            Properties network = new Properties();
            network.load(input);
            
            if(isOpeningBalance){value = network.getProperty("opening_balance_date");}
            if(isTaxRate){value = network.getProperty("tax_rate");}
            if(isRecentTransactionsRange){value = network.getProperty("recent_transactions_range");}
            if(isProfitAndLoss){value = network.getProperty("profit_and_loss_range");}
        } catch (Exception io){
            io.printStackTrace();
        }
        
        return value;
    }
    
    private void showAuditTrail(ArrayList<AuditTrail> trailList){
        auditTrailTable.setModel(new DefaultTableModel(null, new String[]{"ID", "Timestamp", "Module", "Event", 
            "User", "Transaction ID", "Accounts", "Before Value", "After Value", "Total Amount"}));  
        DefaultTableModel populatedAuditTrailTable = (DefaultTableModel) auditTrailTable.getModel();
        tableColumnRenderer(auditTrailTable, 7, 9);
        
        ArrayList<AuditTrail> trails = trailList;
        
        Iterator i = (Iterator) trails.iterator();
        
        while(i.hasNext()){
            AuditTrail trail = (AuditTrail) i.next();
            
            String[] log = {trail.getId(), trail.getTimestamp(), trail.getModule(), trail.getEvent(), trail.getUser(), 
                trail.getTransactionId(), trail.getAffectedAccounts(), "₱ " + setDecimalFormat(trail.getBeforeValue()),
                "₱ " + setDecimalFormat(trail.getAfterValue()), "₱ " + setDecimalFormat(trail.getTotalAmount())};
            populatedAuditTrailTable.addRow(log);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuSidePanel = new javax.swing.JPanel();
        summaryButtonP = new javax.swing.JPanel();
        summaryButtonL = new javax.swing.JLabel();
        summaryIcon = new javax.swing.JLabel();
        salesButtonP = new javax.swing.JPanel();
        salesButtonL = new javax.swing.JLabel();
        salesIcon = new javax.swing.JLabel();
        expensesButtonP = new javax.swing.JPanel();
        expensesButtonL = new javax.swing.JLabel();
        expensesIcon = new javax.swing.JLabel();
        purchasesButtonP = new javax.swing.JPanel();
        purchasesButtonL = new javax.swing.JLabel();
        purchasesIcon = new javax.swing.JLabel();
        taxesButtonP = new javax.swing.JPanel();
        taxesButtonL = new javax.swing.JLabel();
        taxesIcon = new javax.swing.JLabel();
        generalJournalButtonP = new javax.swing.JPanel();
        generalJournalButtonL = new javax.swing.JLabel();
        generalJournalIcon = new javax.swing.JLabel();
        generalLedgerButtonP = new javax.swing.JPanel();
        generalLedgerButtonL = new javax.swing.JLabel();
        generalLedgerIcon = new javax.swing.JLabel();
        chartOfAccountsButtonP = new javax.swing.JPanel();
        chartOfAccountsButtonL = new javax.swing.JLabel();
        chartOfAccountIcon = new javax.swing.JLabel();
        reportsButtonP = new javax.swing.JPanel();
        reportsButtonL = new javax.swing.JLabel();
        reportsIcon = new javax.swing.JLabel();
        customerManagementButtonP = new javax.swing.JPanel();
        customerManagementButtonL = new javax.swing.JLabel();
        customerManagementIcon = new javax.swing.JLabel();
        vendorManagementButtonP = new javax.swing.JPanel();
        vendorManagementButtonL = new javax.swing.JLabel();
        vendorManagementIcon = new javax.swing.JLabel();
        userManagementButtonP = new javax.swing.JPanel();
        userManagementButtonL = new javax.swing.JLabel();
        userManagementIcon = new javax.swing.JLabel();
        settingsButtonP = new javax.swing.JPanel();
        settingsButtonL = new javax.swing.JLabel();
        settingsIcon = new javax.swing.JLabel();
        header = new javax.swing.JPanel();
        logout = new javax.swing.JLabel();
        aimsLogo = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        username = new javax.swing.JLabel();
        level = new javax.swing.JLabel();
        dashboardTabbedPane = new javax.swing.JTabbedPane();
        summaryTab = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        summarizedRevenue = new javax.swing.JLabel();
        revenueFor = new javax.swing.JComboBox<>();
        summaryRevenueIcon = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        summarizedExpenses = new javax.swing.JLabel();
        expensesFor = new javax.swing.JComboBox<>();
        summaryExpensesIcon = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        summaryAccountsList = new javax.swing.JTable();
        jLabel42 = new javax.swing.JLabel();
        summaryAccountsIcon = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        recentTransactionsList = new javax.swing.JTable();
        summaryRecentTransactionsIcon = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        palRevenue = new javax.swing.JLabel();
        palExpenses = new javax.swing.JLabel();
        palNetIncome = new javax.swing.JLabel();
        palStatus = new javax.swing.JLabel();
        summaryProfitAndLossIcon = new javax.swing.JLabel();
        palRange = new javax.swing.JLabel();
        salesTab = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        searchSales = new javax.swing.JLabel();
        salesSearchField = new javax.swing.JTextField();
        jScrollPane9 = new javax.swing.JScrollPane();
        salesTable = new javax.swing.JTable();
        salesSort = new javax.swing.JComboBox<>();
        refreshSales = new javax.swing.JLabel();
        filterSales = new javax.swing.JLabel();
        fromSales = new com.toedter.calendar.JDateChooser();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        toSales = new com.toedter.calendar.JDateChooser();
        addSales = new javax.swing.JButton();
        deleteSales = new javax.swing.JButton();
        viewSales = new javax.swing.JButton();
        expensesTab = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        searchExpenses = new javax.swing.JLabel();
        expensesSearchField = new javax.swing.JTextField();
        jScrollPane8 = new javax.swing.JScrollPane();
        expensesTable = new javax.swing.JTable();
        expensesSort = new javax.swing.JComboBox<>();
        refreshExpenses = new javax.swing.JLabel();
        filterExpenses = new javax.swing.JLabel();
        fromExpenses = new com.toedter.calendar.JDateChooser();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        toExpenses = new com.toedter.calendar.JDateChooser();
        addExpenses = new javax.swing.JButton();
        deleteExpenses = new javax.swing.JButton();
        viewExpenses = new javax.swing.JButton();
        editExpenses = new javax.swing.JButton();
        purchasesTab = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        searchPurchases = new javax.swing.JLabel();
        purchasesSearchField = new javax.swing.JTextField();
        jScrollPane7 = new javax.swing.JScrollPane();
        purchasesTable = new javax.swing.JTable();
        purchasesSort = new javax.swing.JComboBox<>();
        fromPurchases = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        toPurchases = new com.toedter.calendar.JDateChooser();
        addPurchases = new javax.swing.JButton();
        deletePurchases = new javax.swing.JButton();
        viewPurchases = new javax.swing.JButton();
        filterPurchases = new javax.swing.JLabel();
        refreshPurchases = new javax.swing.JLabel();
        taxesTab = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        fromTaxes = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        toTaxes = new com.toedter.calendar.JDateChooser();
        calculateGrossSales = new javax.swing.JButton();
        grossSales1 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        calculateTax = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        grossSales2 = new javax.swing.JTextField();
        taxRate = new javax.swing.JTextField();
        taxResult = new javax.swing.JTextField();
        generalJournalTab = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        journalSort = new javax.swing.JComboBox<>();
        journalSearchField = new javax.swing.JTextField();
        searchJournal = new javax.swing.JLabel();
        refreshJournal = new javax.swing.JLabel();
        filterJournal = new javax.swing.JLabel();
        fromJournal = new com.toedter.calendar.JDateChooser();
        toJournal = new com.toedter.calendar.JDateChooser();
        jScrollPane6 = new javax.swing.JScrollPane();
        journalTable = new javax.swing.JTable();
        addEntry = new javax.swing.JButton();
        deleteEntry = new javax.swing.JButton();
        editEntry = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        viewEntry = new javax.swing.JButton();
        generalLedgerTab = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        ledgerTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        ledgerAccountSelectionBox = new javax.swing.JComboBox<>();
        refreshLedger = new javax.swing.JLabel();
        filterLedger = new javax.swing.JLabel();
        fromLedger = new com.toedter.calendar.JDateChooser();
        toLedger = new com.toedter.calendar.JDateChooser();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        ledgerTotalBalance = new javax.swing.JLabel();
        viewTransactionSummary = new javax.swing.JButton();
        chartOfAccountsTab = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        searchAccount = new javax.swing.JLabel();
        accountSearchField = new javax.swing.JTextField();
        accountSort = new javax.swing.JComboBox<>();
        refreshAccount = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        accountTable = new javax.swing.JTable();
        addAccount = new javax.swing.JButton();
        deleteAccount = new javax.swing.JButton();
        updateAccount = new javax.swing.JButton();
        reportsTab = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        reportTable = new javax.swing.JTable();
        print = new javax.swing.JButton();
        reportSheet = new javax.swing.JComboBox<>();
        customerManagementTab = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        searchCustomer = new javax.swing.JLabel();
        customerSearchField = new javax.swing.JTextField();
        customerSort = new javax.swing.JComboBox<>();
        refreshCustomer = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        customerTable = new javax.swing.JTable();
        addCustomer = new javax.swing.JButton();
        deleteCustomer = new javax.swing.JButton();
        updateCustomer = new javax.swing.JButton();
        vendorManagementTab = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        searchVendor = new javax.swing.JLabel();
        vendorSearchField = new javax.swing.JTextField();
        vendorSort = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        vendorTable = new javax.swing.JTable();
        addVendor = new javax.swing.JButton();
        deleteVendor = new javax.swing.JButton();
        updateVendor = new javax.swing.JButton();
        refreshVendor = new javax.swing.JLabel();
        userManagementTab = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        searchUser = new javax.swing.JLabel();
        userSearchField = new javax.swing.JTextField();
        userSort = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        userTable = new javax.swing.JTable();
        refreshUser = new javax.swing.JLabel();
        updateUser = new javax.swing.JButton();
        deactivateUser = new javax.swing.JButton();
        addUser = new javax.swing.JButton();
        settingsTab = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        settingsTabbedPane = new javax.swing.JTabbedPane();
        jScrollPane15 = new javax.swing.JScrollPane();
        jPanel11 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        profitAndLossRange = new javax.swing.JComboBox<>();
        recentTransactionsRange = new javax.swing.JComboBox<>();
        taxRateConfig = new javax.swing.JTextField();
        openingBalanceDate = new com.toedter.calendar.JDateChooser();
        setOpeningBalanceDate = new javax.swing.JButton();
        changeTaxRate = new javax.swing.JButton();
        bmsLogo = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel54 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        viewSalesPaymentMethod = new javax.swing.JTextField();
        setSalesPaymentMethod = new javax.swing.JButton();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        viewPurchasesPaymentMethod = new javax.swing.JTextField();
        setPurchasesPaymentMethod = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        setSalesRemainingBalanceAccount = new javax.swing.JComboBox<>();
        setSalesRevenueAccount = new javax.swing.JComboBox<>();
        setPurchasesRemainingBalanceAccount = new javax.swing.JComboBox<>();
        jScrollPane14 = new javax.swing.JScrollPane();
        productTable = new javax.swing.JTable();
        jLabel62 = new javax.swing.JLabel();
        addProduct = new javax.swing.JButton();
        editProduct = new javax.swing.JButton();
        removeProduct = new javax.swing.JButton();
        removeAllProduct = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        networkUsername = new javax.swing.JTextField();
        networkServer = new javax.swing.JTextField();
        networkPort = new javax.swing.JTextField();
        networkDatabase = new javax.swing.JTextField();
        networkPassword = new javax.swing.JPasswordField();
        editNetwork = new javax.swing.JButton();
        saveNetwork = new javax.swing.JButton();
        cancelNetworkChanges = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        auditTrailTable = new javax.swing.JTable();
        searchAuditTrail = new javax.swing.JLabel();
        auditTrailSearchField = new javax.swing.JTextField();
        toTrail = new com.toedter.calendar.JDateChooser();
        fromTrail = new com.toedter.calendar.JDateChooser();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        filterAuditTrail = new javax.swing.JLabel();
        refreshAuditTrail = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Bookkeeping Management System");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        menuSidePanel.setBackground(new java.awt.Color(8, 61, 119));

        summaryButtonP.setBackground(new java.awt.Color(8, 61, 119));
        summaryButtonP.setPreferredSize(new java.awt.Dimension(162, 47));
        summaryButtonP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                summaryButtonPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                summaryButtonPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                summaryButtonPMouseExited(evt);
            }
        });

        summaryButtonL.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        summaryButtonL.setForeground(new java.awt.Color(255, 255, 255));
        summaryButtonL.setText("Summary");
        summaryButtonL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                summaryButtonLMouseClicked(evt);
            }
        });

        summaryIcon.setMaximumSize(new java.awt.Dimension(30, 16));
        summaryIcon.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout summaryButtonPLayout = new javax.swing.GroupLayout(summaryButtonP);
        summaryButtonP.setLayout(summaryButtonPLayout);
        summaryButtonPLayout.setHorizontalGroup(
            summaryButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(summaryButtonPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(summaryIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(summaryButtonL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        summaryButtonPLayout.setVerticalGroup(
            summaryButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(summaryButtonPLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(summaryButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(summaryButtonL)
                    .addComponent(summaryIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        salesButtonP.setBackground(new java.awt.Color(8, 61, 119));
        salesButtonP.setPreferredSize(new java.awt.Dimension(129, 47));
        salesButtonP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                salesButtonPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                salesButtonPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                salesButtonPMouseExited(evt);
            }
        });

        salesButtonL.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        salesButtonL.setForeground(new java.awt.Color(255, 255, 255));
        salesButtonL.setText("Sales");
        salesButtonL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                salesButtonLMouseClicked(evt);
            }
        });

        salesIcon.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout salesButtonPLayout = new javax.swing.GroupLayout(salesButtonP);
        salesButtonP.setLayout(salesButtonPLayout);
        salesButtonPLayout.setHorizontalGroup(
            salesButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(salesButtonPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(salesIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(salesButtonL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        salesButtonPLayout.setVerticalGroup(
            salesButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, salesButtonPLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(salesButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(salesButtonL)
                    .addComponent(salesIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        expensesButtonP.setBackground(new java.awt.Color(8, 61, 119));
        expensesButtonP.setPreferredSize(new java.awt.Dimension(157, 47));
        expensesButtonP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                expensesButtonPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                expensesButtonPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                expensesButtonPMouseExited(evt);
            }
        });

        expensesButtonL.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        expensesButtonL.setForeground(new java.awt.Color(255, 255, 255));
        expensesButtonL.setText("Expenses");
        expensesButtonL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                expensesButtonLMouseClicked(evt);
            }
        });

        expensesIcon.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout expensesButtonPLayout = new javax.swing.GroupLayout(expensesButtonP);
        expensesButtonP.setLayout(expensesButtonPLayout);
        expensesButtonPLayout.setHorizontalGroup(
            expensesButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(expensesButtonPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(expensesIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(expensesButtonL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        expensesButtonPLayout.setVerticalGroup(
            expensesButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(expensesButtonPLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(expensesButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(expensesButtonL)
                    .addComponent(expensesIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        purchasesButtonP.setBackground(new java.awt.Color(8, 61, 119));
        purchasesButtonP.setPreferredSize(new java.awt.Dimension(162, 47));
        purchasesButtonP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                purchasesButtonPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                purchasesButtonPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                purchasesButtonPMouseExited(evt);
            }
        });

        purchasesButtonL.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        purchasesButtonL.setForeground(new java.awt.Color(255, 255, 255));
        purchasesButtonL.setText("Purchases");
        purchasesButtonL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                purchasesButtonLMouseClicked(evt);
            }
        });

        purchasesIcon.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout purchasesButtonPLayout = new javax.swing.GroupLayout(purchasesButtonP);
        purchasesButtonP.setLayout(purchasesButtonPLayout);
        purchasesButtonPLayout.setHorizontalGroup(
            purchasesButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(purchasesButtonPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(purchasesIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(purchasesButtonL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        purchasesButtonPLayout.setVerticalGroup(
            purchasesButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, purchasesButtonPLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(purchasesButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(purchasesButtonL)
                    .addComponent(purchasesIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        taxesButtonP.setBackground(new java.awt.Color(8, 61, 119));
        taxesButtonP.setPreferredSize(new java.awt.Dimension(132, 47));
        taxesButtonP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                taxesButtonPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                taxesButtonPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                taxesButtonPMouseExited(evt);
            }
        });

        taxesButtonL.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        taxesButtonL.setForeground(new java.awt.Color(255, 255, 255));
        taxesButtonL.setText("Taxes");
        taxesButtonL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                taxesButtonLMouseClicked(evt);
            }
        });

        taxesIcon.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout taxesButtonPLayout = new javax.swing.GroupLayout(taxesButtonP);
        taxesButtonP.setLayout(taxesButtonPLayout);
        taxesButtonPLayout.setHorizontalGroup(
            taxesButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(taxesButtonPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(taxesIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(taxesButtonL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        taxesButtonPLayout.setVerticalGroup(
            taxesButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, taxesButtonPLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(taxesButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(taxesButtonL)
                    .addComponent(taxesIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        generalJournalButtonP.setBackground(new java.awt.Color(8, 61, 119));
        generalJournalButtonP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                generalJournalButtonPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                generalJournalButtonPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                generalJournalButtonPMouseExited(evt);
            }
        });

        generalJournalButtonL.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        generalJournalButtonL.setForeground(new java.awt.Color(255, 255, 255));
        generalJournalButtonL.setText("General Journal");
        generalJournalButtonL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                generalJournalButtonLMouseClicked(evt);
            }
        });

        generalJournalIcon.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout generalJournalButtonPLayout = new javax.swing.GroupLayout(generalJournalButtonP);
        generalJournalButtonP.setLayout(generalJournalButtonPLayout);
        generalJournalButtonPLayout.setHorizontalGroup(
            generalJournalButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, generalJournalButtonPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(generalJournalIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(generalJournalButtonL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        generalJournalButtonPLayout.setVerticalGroup(
            generalJournalButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, generalJournalButtonPLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(generalJournalButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(generalJournalButtonL)
                    .addComponent(generalJournalIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        generalLedgerButtonP.setBackground(new java.awt.Color(8, 61, 119));
        generalLedgerButtonP.setPreferredSize(new java.awt.Dimension(197, 47));
        generalLedgerButtonP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                generalLedgerButtonPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                generalLedgerButtonPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                generalLedgerButtonPMouseExited(evt);
            }
        });

        generalLedgerButtonL.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        generalLedgerButtonL.setForeground(new java.awt.Color(255, 255, 255));
        generalLedgerButtonL.setText("General Ledger");
        generalLedgerButtonL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                generalLedgerButtonLMouseClicked(evt);
            }
        });

        generalLedgerIcon.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout generalLedgerButtonPLayout = new javax.swing.GroupLayout(generalLedgerButtonP);
        generalLedgerButtonP.setLayout(generalLedgerButtonPLayout);
        generalLedgerButtonPLayout.setHorizontalGroup(
            generalLedgerButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalLedgerButtonPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(generalLedgerIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(generalLedgerButtonL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        generalLedgerButtonPLayout.setVerticalGroup(
            generalLedgerButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalLedgerButtonPLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(generalLedgerButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(generalLedgerButtonL)
                    .addComponent(generalLedgerIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        chartOfAccountsButtonP.setBackground(new java.awt.Color(8, 61, 119));
        chartOfAccountsButtonP.setPreferredSize(new java.awt.Dimension(258, 47));
        chartOfAccountsButtonP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chartOfAccountsButtonPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                chartOfAccountsButtonPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                chartOfAccountsButtonPMouseExited(evt);
            }
        });

        chartOfAccountsButtonL.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        chartOfAccountsButtonL.setForeground(new java.awt.Color(255, 255, 255));
        chartOfAccountsButtonL.setText("Chart of Accounts");
        chartOfAccountsButtonL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chartOfAccountsButtonLMouseClicked(evt);
            }
        });

        chartOfAccountIcon.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout chartOfAccountsButtonPLayout = new javax.swing.GroupLayout(chartOfAccountsButtonP);
        chartOfAccountsButtonP.setLayout(chartOfAccountsButtonPLayout);
        chartOfAccountsButtonPLayout.setHorizontalGroup(
            chartOfAccountsButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, chartOfAccountsButtonPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chartOfAccountIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(chartOfAccountsButtonL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        chartOfAccountsButtonPLayout.setVerticalGroup(
            chartOfAccountsButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, chartOfAccountsButtonPLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(chartOfAccountsButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chartOfAccountsButtonL)
                    .addComponent(chartOfAccountIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        reportsButtonP.setBackground(new java.awt.Color(8, 61, 119));
        reportsButtonP.setPreferredSize(new java.awt.Dimension(145, 47));
        reportsButtonP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                reportsButtonPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                reportsButtonPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reportsButtonPMouseExited(evt);
            }
        });

        reportsButtonL.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        reportsButtonL.setForeground(new java.awt.Color(255, 255, 255));
        reportsButtonL.setText("Reports");
        reportsButtonL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                reportsButtonLMouseClicked(evt);
            }
        });

        reportsIcon.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout reportsButtonPLayout = new javax.swing.GroupLayout(reportsButtonP);
        reportsButtonP.setLayout(reportsButtonPLayout);
        reportsButtonPLayout.setHorizontalGroup(
            reportsButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportsButtonPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(reportsIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(reportsButtonL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        reportsButtonPLayout.setVerticalGroup(
            reportsButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportsButtonPLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(reportsButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(reportsButtonL)
                    .addComponent(reportsIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        customerManagementButtonP.setBackground(new java.awt.Color(8, 61, 119));
        customerManagementButtonP.setPreferredSize(new java.awt.Dimension(259, 47));
        customerManagementButtonP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customerManagementButtonPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                customerManagementButtonPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                customerManagementButtonPMouseExited(evt);
            }
        });

        customerManagementButtonL.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        customerManagementButtonL.setForeground(new java.awt.Color(255, 255, 255));
        customerManagementButtonL.setText("Customer Management");
        customerManagementButtonL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customerManagementButtonLMouseClicked(evt);
            }
        });

        customerManagementIcon.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout customerManagementButtonPLayout = new javax.swing.GroupLayout(customerManagementButtonP);
        customerManagementButtonP.setLayout(customerManagementButtonPLayout);
        customerManagementButtonPLayout.setHorizontalGroup(
            customerManagementButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, customerManagementButtonPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(customerManagementIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(customerManagementButtonL)
                .addContainerGap(65, Short.MAX_VALUE))
        );
        customerManagementButtonPLayout.setVerticalGroup(
            customerManagementButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerManagementButtonPLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(customerManagementButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(customerManagementButtonL)
                    .addComponent(customerManagementIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        vendorManagementButtonP.setBackground(new java.awt.Color(8, 61, 119));
        vendorManagementButtonP.setPreferredSize(new java.awt.Dimension(260, 47));
        vendorManagementButtonP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                vendorManagementButtonPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                vendorManagementButtonPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                vendorManagementButtonPMouseExited(evt);
            }
        });

        vendorManagementButtonL.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        vendorManagementButtonL.setForeground(new java.awt.Color(255, 255, 255));
        vendorManagementButtonL.setText("Vendor Management");
        vendorManagementButtonL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                vendorManagementButtonLMouseClicked(evt);
            }
        });

        vendorManagementIcon.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout vendorManagementButtonPLayout = new javax.swing.GroupLayout(vendorManagementButtonP);
        vendorManagementButtonP.setLayout(vendorManagementButtonPLayout);
        vendorManagementButtonPLayout.setHorizontalGroup(
            vendorManagementButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, vendorManagementButtonPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(vendorManagementIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(vendorManagementButtonL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        vendorManagementButtonPLayout.setVerticalGroup(
            vendorManagementButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vendorManagementButtonPLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(vendorManagementButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vendorManagementButtonL)
                    .addComponent(vendorManagementIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        userManagementButtonP.setBackground(new java.awt.Color(8, 61, 119));
        userManagementButtonP.setPreferredSize(new java.awt.Dimension(262, 47));
        userManagementButtonP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                userManagementButtonPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                userManagementButtonPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                userManagementButtonPMouseExited(evt);
            }
        });

        userManagementButtonL.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        userManagementButtonL.setForeground(new java.awt.Color(255, 255, 255));
        userManagementButtonL.setText("User Management");
        userManagementButtonL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                userManagementButtonLMouseClicked(evt);
            }
        });

        userManagementIcon.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout userManagementButtonPLayout = new javax.swing.GroupLayout(userManagementButtonP);
        userManagementButtonP.setLayout(userManagementButtonPLayout);
        userManagementButtonPLayout.setHorizontalGroup(
            userManagementButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userManagementButtonPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(userManagementIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(userManagementButtonL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        userManagementButtonPLayout.setVerticalGroup(
            userManagementButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userManagementButtonPLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(userManagementButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userManagementIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userManagementButtonL))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        settingsButtonP.setBackground(new java.awt.Color(8, 61, 119));
        settingsButtonP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                settingsButtonPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                settingsButtonPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                settingsButtonPMouseExited(evt);
            }
        });

        settingsButtonL.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        settingsButtonL.setForeground(new java.awt.Color(255, 255, 255));
        settingsButtonL.setText("Settings");
        settingsButtonL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                settingsButtonLMouseClicked(evt);
            }
        });

        settingsIcon.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout settingsButtonPLayout = new javax.swing.GroupLayout(settingsButtonP);
        settingsButtonP.setLayout(settingsButtonPLayout);
        settingsButtonPLayout.setHorizontalGroup(
            settingsButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsButtonPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(settingsIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(settingsButtonL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        settingsButtonPLayout.setVerticalGroup(
            settingsButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsButtonPLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(settingsButtonPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(settingsButtonL)
                    .addComponent(settingsIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout menuSidePanelLayout = new javax.swing.GroupLayout(menuSidePanel);
        menuSidePanel.setLayout(menuSidePanelLayout);
        menuSidePanelLayout.setHorizontalGroup(
            menuSidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(summaryButtonP, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
            .addComponent(salesButtonP, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
            .addComponent(expensesButtonP, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
            .addComponent(purchasesButtonP, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
            .addComponent(taxesButtonP, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
            .addComponent(generalJournalButtonP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(generalLedgerButtonP, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
            .addComponent(chartOfAccountsButtonP, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
            .addComponent(reportsButtonP, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
            .addComponent(customerManagementButtonP, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
            .addComponent(vendorManagementButtonP, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
            .addComponent(userManagementButtonP, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
            .addComponent(settingsButtonP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        menuSidePanelLayout.setVerticalGroup(
            menuSidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuSidePanelLayout.createSequentialGroup()
                .addComponent(summaryButtonP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(salesButtonP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(expensesButtonP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(purchasesButtonP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(taxesButtonP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(generalJournalButtonP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(generalLedgerButtonP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chartOfAccountsButtonP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reportsButtonP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customerManagementButtonP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(vendorManagementButtonP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userManagementButtonP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(settingsButtonP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 16, Short.MAX_VALUE))
        );

        getContentPane().add(menuSidePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 290, 720));

        header.setBackground(new java.awt.Color(239, 236, 202));
        header.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logout.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        logout.setToolTipText("Logout");
        logout.setPreferredSize(new java.awt.Dimension(47, 47));
        logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutMouseClicked(evt);
            }
        });
        header.add(logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(1330, 20, -1, -1));

        aimsLogo.setPreferredSize(new java.awt.Dimension(50, 50));
        header.add(aimsLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        jLabel39.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel39.setText("JAR");
        header.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, -1, -1));

        jLabel41.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(0, 153, 0));
        jLabel41.setText("Creatives");
        header.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, -1, -1));

        username.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        username.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        username.setText("Username");
        header.add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 30, 300, -1));

        level.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        level.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        level.setText("Level");
        header.add(level, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 50, 190, -1));

        getContentPane().add(header, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1400, 80));

        summaryTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 153, 153));
        jLabel2.setText("Summary");
        jLabel2.setToolTipText("");
        summaryTab.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 34, 100, -1));

        jPanel6.setBackground(new java.awt.Color(51, 204, 0));

        jLabel38.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("Revenue for");

        summarizedRevenue.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        summarizedRevenue.setForeground(new java.awt.Color(255, 255, 255));
        summarizedRevenue.setText("₱ 0.00");

        revenueFor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Today", "This week", "This month", "This year" }));
        revenueFor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                revenueForActionPerformed(evt);
            }
        });

        summaryRevenueIcon.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(summaryRevenueIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(summarizedRevenue)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(revenueFor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(revenueFor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(summarizedRevenue)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(summaryRevenueIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        summaryTab.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(44, 77, -1, 94));

        jPanel7.setBackground(new java.awt.Color(255, 51, 51));

        jLabel40.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setText("Expenses for");

        summarizedExpenses.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        summarizedExpenses.setForeground(new java.awt.Color(255, 255, 255));
        summarizedExpenses.setText("₱ 0.00");

        expensesFor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Today", "This week", "This month", "This year" }));
        expensesFor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expensesForActionPerformed(evt);
            }
        });

        summaryExpensesIcon.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(summaryExpensesIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(summarizedExpenses)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(expensesFor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel40)
                            .addComponent(expensesFor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(summarizedExpenses))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(summaryExpensesIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        summaryTab.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 77, -1, -1));

        jPanel8.setBackground(new java.awt.Color(255, 147, 79));
        jPanel8.setForeground(new java.awt.Color(0, 153, 0));

        summaryAccountsList.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        summaryAccountsList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Account", "Balance"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        summaryAccountsList.setRowHeight(25);
        summaryAccountsList.getTableHeader().setReorderingAllowed(false);
        jScrollPane10.setViewportView(summaryAccountsList);
        if (summaryAccountsList.getColumnModel().getColumnCount() > 0) {
            summaryAccountsList.getColumnModel().getColumn(0).setResizable(false);
            summaryAccountsList.getColumnModel().getColumn(1).setResizable(false);
        }

        jLabel42.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setText("Accounts");

        summaryAccountsIcon.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(summaryAccountsIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel42)))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(summaryAccountsIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42))
                .addGap(31, 31, 31)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        summaryTab.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(686, 77, -1, 361));

        jPanel3.setBackground(new java.awt.Color(169, 203, 183));

        jLabel43.setBackground(new java.awt.Color(255, 255, 255));
        jLabel43.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setText("Recent Transactions");

        recentTransactionsList.setFont(new java.awt.Font("Segoe UI", 0, 17)); // NOI18N
        recentTransactionsList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Date", "Description", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        recentTransactionsList.setRowHeight(27);
        recentTransactionsList.getTableHeader().setReorderingAllowed(false);
        jScrollPane11.setViewportView(recentTransactionsList);
        if (recentTransactionsList.getColumnModel().getColumnCount() > 0) {
            recentTransactionsList.getColumnModel().getColumn(0).setResizable(false);
            recentTransactionsList.getColumnModel().getColumn(0).setPreferredWidth(20);
            recentTransactionsList.getColumnModel().getColumn(1).setResizable(false);
            recentTransactionsList.getColumnModel().getColumn(1).setPreferredWidth(5);
            recentTransactionsList.getColumnModel().getColumn(2).setResizable(false);
            recentTransactionsList.getColumnModel().getColumn(2).setPreferredWidth(200);
            recentTransactionsList.getColumnModel().getColumn(3).setResizable(false);
            recentTransactionsList.getColumnModel().getColumn(3).setPreferredWidth(10);
        }

        summaryRecentTransactionsIcon.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 988, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(summaryRecentTransactionsIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel43)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43)
                    .addComponent(summaryRecentTransactionsIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        summaryTab.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(44, 471, -1, -1));

        jPanel9.setBackground(new java.awt.Color(0, 153, 255));

        jLabel44.setBackground(new java.awt.Color(255, 255, 255));
        jLabel44.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setText("Profit and Loss");

        jLabel45.setBackground(new java.awt.Color(255, 255, 255));
        jLabel45.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setText("Revenue");

        jLabel46.setBackground(new java.awt.Color(255, 255, 255));
        jLabel46.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setText("Expenses");

        jLabel47.setBackground(new java.awt.Color(255, 255, 255));
        jLabel47.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setText("Net Income");

        palRevenue.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        palRevenue.setForeground(new java.awt.Color(255, 255, 255));
        palRevenue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        palRevenue.setText("₱ 0.00");

        palExpenses.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        palExpenses.setForeground(new java.awt.Color(255, 255, 255));
        palExpenses.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        palExpenses.setText("₱ 0.00");

        palNetIncome.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        palNetIncome.setForeground(new java.awt.Color(255, 255, 255));
        palNetIncome.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        palNetIncome.setText("₱ 0.00");

        palStatus.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        palStatus.setForeground(new java.awt.Color(255, 255, 255));
        palStatus.setText("( Gained )");

        summaryProfitAndLossIcon.setPreferredSize(new java.awt.Dimension(40, 40));

        palRange.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        palRange.setForeground(new java.awt.Color(255, 255, 255));
        palRange.setText("For This Month");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel45)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(palRevenue))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel46)
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addComponent(jLabel47)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(palStatus)))
                                .addGap(315, 315, 315)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(palExpenses)
                                    .addComponent(palNetIncome)))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(summaryProfitAndLossIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel44)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(palRange)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(104, 104, 104))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel44)
                        .addComponent(palRange))
                    .addComponent(summaryProfitAndLossIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(palRevenue))
                .addGap(38, 38, 38)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel46)
                    .addComponent(palExpenses))
                .addGap(32, 32, 32)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47)
                    .addComponent(palNetIncome)
                    .addComponent(palStatus))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        summaryTab.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(44, 203, 608, -1));

        dashboardTabbedPane.addTab("Summary", summaryTab);

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(153, 153, 153));
        jLabel16.setText("Sales");
        jLabel16.setToolTipText("");

        searchSales.setToolTipText("Search");
        searchSales.setPreferredSize(new java.awt.Dimension(30, 30));
        searchSales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchSalesMouseClicked(evt);
            }
        });

        salesTable.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        salesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Date", "Customer", "Total Amount", "Amount Paid", "Balance"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        salesTable.setRowHeight(25);
        salesTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane9.setViewportView(salesTable);

        salesSort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sort", "ID", "Date", "Customer" }));
        salesSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salesSortActionPerformed(evt);
            }
        });

        refreshSales.setToolTipText("Refresh");
        refreshSales.setPreferredSize(new java.awt.Dimension(30, 30));
        refreshSales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshSalesMouseClicked(evt);
            }
        });

        filterSales.setToolTipText("Filter");
        filterSales.setPreferredSize(new java.awt.Dimension(30, 30));
        filterSales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                filterSalesMouseClicked(evt);
            }
        });

        fromSales.setDateFormatString("MM/dd/yyyy");

        jLabel30.setText("From:");

        jLabel31.setText("To:");

        toSales.setDateFormatString("MM/dd/yyyy");

        addSales.setText("Add");
        addSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSalesActionPerformed(evt);
            }
        });

        deleteSales.setText("Delete");
        deleteSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteSalesActionPerformed(evt);
            }
        });

        viewSales.setText("View");
        viewSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewSalesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout salesTabLayout = new javax.swing.GroupLayout(salesTab);
        salesTab.setLayout(salesTabLayout);
        salesTabLayout.setHorizontalGroup(
            salesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(salesTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(salesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(salesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(salesTabLayout.createSequentialGroup()
                            .addComponent(addSales, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(deleteSales)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(viewSales, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(salesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 1071, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(salesTabLayout.createSequentialGroup()
                                .addComponent(searchSales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(salesSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(salesSort, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(refreshSales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(filterSales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(salesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fromSales, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel30))
                                .addGap(18, 18, 18)
                                .addGroup(salesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel31)
                                    .addComponent(toSales, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        salesTabLayout.setVerticalGroup(
            salesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(salesTabLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel16)
                .addGap(36, 36, 36)
                .addGroup(salesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jLabel31))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(salesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(salesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(salesSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(salesSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(searchSales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshSales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterSales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fromSales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(toSales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(salesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteSales)
                    .addComponent(addSales)
                    .addComponent(viewSales))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        dashboardTabbedPane.addTab("Sales", salesTab);

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(153, 153, 153));
        jLabel17.setText("Expenses");
        jLabel17.setToolTipText("");

        searchExpenses.setToolTipText("Search");
        searchExpenses.setPreferredSize(new java.awt.Dimension(30, 30));
        searchExpenses.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchExpensesMouseClicked(evt);
            }
        });

        expensesTable.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        expensesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Date", "Description", "Encoder"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        expensesTable.setRowHeight(25);
        expensesTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane8.setViewportView(expensesTable);
        if (expensesTable.getColumnModel().getColumnCount() > 0) {
            expensesTable.getColumnModel().getColumn(0).setResizable(false);
            expensesTable.getColumnModel().getColumn(1).setResizable(false);
            expensesTable.getColumnModel().getColumn(2).setResizable(false);
            expensesTable.getColumnModel().getColumn(3).setResizable(false);
        }

        expensesSort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sort", "ID", "Date", "Description ", "Encoder" }));
        expensesSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expensesSortActionPerformed(evt);
            }
        });

        refreshExpenses.setToolTipText("Refresh");
        refreshExpenses.setPreferredSize(new java.awt.Dimension(30, 30));
        refreshExpenses.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshExpensesMouseClicked(evt);
            }
        });

        filterExpenses.setToolTipText("Filter");
        filterExpenses.setPreferredSize(new java.awt.Dimension(30, 30));
        filterExpenses.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                filterExpensesMouseClicked(evt);
            }
        });

        fromExpenses.setDateFormatString("MM/dd/yyyy");

        jLabel28.setText("From:");

        jLabel29.setText("To:");

        toExpenses.setDateFormatString("MM/dd/yyyy");

        addExpenses.setText("Add");
        addExpenses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addExpensesActionPerformed(evt);
            }
        });

        deleteExpenses.setText("Delete");
        deleteExpenses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteExpensesActionPerformed(evt);
            }
        });

        viewExpenses.setText("View");
        viewExpenses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewExpensesActionPerformed(evt);
            }
        });

        editExpenses.setText("Edit");
        editExpenses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editExpensesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout expensesTabLayout = new javax.swing.GroupLayout(expensesTab);
        expensesTab.setLayout(expensesTabLayout);
        expensesTabLayout.setHorizontalGroup(
            expensesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(expensesTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(expensesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(expensesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(expensesTabLayout.createSequentialGroup()
                            .addComponent(addExpenses, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(deleteExpenses)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(editExpenses, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(viewExpenses, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 1071, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(expensesTabLayout.createSequentialGroup()
                            .addComponent(searchExpenses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(expensesSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(expensesSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(refreshExpenses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(filterExpenses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addGroup(expensesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(fromExpenses, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel28))
                            .addGap(18, 18, 18)
                            .addGroup(expensesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel29)
                                .addComponent(toExpenses, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        expensesTabLayout.setVerticalGroup(
            expensesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(expensesTabLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel17)
                .addGap(34, 34, 34)
                .addGroup(expensesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(expensesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(expensesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(expensesSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(expensesSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(searchExpenses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshExpenses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterExpenses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fromExpenses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(toExpenses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(expensesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editExpenses)
                    .addComponent(deleteExpenses)
                    .addComponent(addExpenses)
                    .addComponent(viewExpenses))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        dashboardTabbedPane.addTab("Expenses", expensesTab);

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(153, 153, 153));
        jLabel18.setText("Purchases");
        jLabel18.setToolTipText("");

        searchPurchases.setToolTipText("Search");
        searchPurchases.setPreferredSize(new java.awt.Dimension(30, 30));
        searchPurchases.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchPurchasesMouseClicked(evt);
            }
        });

        purchasesTable.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        purchasesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Date", "Supplier", "Description", "Total Amount", "Amount Paid", "Balance", "Asset Account"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        purchasesTable.setRowHeight(25);
        purchasesTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane7.setViewportView(purchasesTable);
        if (purchasesTable.getColumnModel().getColumnCount() > 0) {
            purchasesTable.getColumnModel().getColumn(0).setResizable(false);
            purchasesTable.getColumnModel().getColumn(1).setResizable(false);
            purchasesTable.getColumnModel().getColumn(2).setResizable(false);
            purchasesTable.getColumnModel().getColumn(3).setResizable(false);
            purchasesTable.getColumnModel().getColumn(4).setResizable(false);
            purchasesTable.getColumnModel().getColumn(5).setResizable(false);
            purchasesTable.getColumnModel().getColumn(6).setResizable(false);
            purchasesTable.getColumnModel().getColumn(7).setResizable(false);
        }

        purchasesSort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sort", "ID", "Date", "Supplier", "Asset Account" }));
        purchasesSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchasesSortActionPerformed(evt);
            }
        });

        fromPurchases.setDateFormatString("MM/dd/yyyy");

        jLabel14.setText("From:");

        jLabel15.setText("To:");

        toPurchases.setDateFormatString("MM/dd/yyyy");

        addPurchases.setText("Add");
        addPurchases.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPurchasesActionPerformed(evt);
            }
        });

        deletePurchases.setText("Delete");
        deletePurchases.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletePurchasesActionPerformed(evt);
            }
        });

        viewPurchases.setText("View");
        viewPurchases.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewPurchasesActionPerformed(evt);
            }
        });

        filterPurchases.setToolTipText("Filter");
        filterPurchases.setPreferredSize(new java.awt.Dimension(30, 30));
        filterPurchases.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                filterPurchasesMouseClicked(evt);
            }
        });

        refreshPurchases.setToolTipText("Refresh");
        refreshPurchases.setPreferredSize(new java.awt.Dimension(30, 30));
        refreshPurchases.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshPurchasesMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout purchasesTabLayout = new javax.swing.GroupLayout(purchasesTab);
        purchasesTab.setLayout(purchasesTabLayout);
        purchasesTabLayout.setHorizontalGroup(
            purchasesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(purchasesTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(purchasesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(purchasesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(purchasesTabLayout.createSequentialGroup()
                            .addComponent(addPurchases, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(deletePurchases)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(viewPurchases, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(purchasesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 1071, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(purchasesTabLayout.createSequentialGroup()
                                .addComponent(searchPurchases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(purchasesSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(purchasesSort, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(refreshPurchases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(filterPurchases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(purchasesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fromPurchases, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14))
                                .addGap(18, 18, 18)
                                .addGroup(purchasesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel15)
                                    .addComponent(toPurchases, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        purchasesTabLayout.setVerticalGroup(
            purchasesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(purchasesTabLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel18)
                .addGap(31, 31, 31)
                .addGroup(purchasesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(purchasesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(purchasesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(purchasesSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(purchasesSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(searchPurchases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshPurchases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterPurchases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fromPurchases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(toPurchases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(purchasesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deletePurchases)
                    .addComponent(addPurchases)
                    .addComponent(viewPurchases))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        dashboardTabbedPane.addTab("Purchases", purchasesTab);

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(153, 153, 153));
        jLabel19.setText("Taxes");
        jLabel19.setToolTipText("");

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Calculate Gross Sales from");

        fromTaxes.setDateFormatString("MM/dd/yyyy");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Gross Sales  =");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("to");

        toTaxes.setDateFormatString("MM/dd/yyyy");

        calculateGrossSales.setText("Calculate");
        calculateGrossSales.setToolTipText("Click to calculate gross income");
        calculateGrossSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateGrossSalesActionPerformed(evt);
            }
        });

        grossSales1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fromTaxes, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(toTaxes, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(calculateGrossSales)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(grossSales1)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(fromTaxes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(toTaxes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(calculateGrossSales))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(grossSales1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Percentage tax calculator");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Formula: Quarterly Gross Sales x Tax rate = Percentage Tax");

        calculateTax.setText("Calculate");
        calculateTax.setToolTipText("Click to calculate tax");
        calculateTax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateTaxActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Gross Sales");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Tax Rate");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("Tax        =");

        grossSales2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        taxRate.setEditable(false);
        taxRate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        taxResult.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(183, 183, 183)
                        .addComponent(jLabel6))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(taxRate, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(grossSales2, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(taxResult, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(65, 65, 65))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(calculateTax, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(154, 154, 154))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(49, 49, 49)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(grossSales2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(taxRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(taxResult, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(calculateTax)
                .addContainerGap())
        );

        javax.swing.GroupLayout taxesTabLayout = new javax.swing.GroupLayout(taxesTab);
        taxesTab.setLayout(taxesTabLayout);
        taxesTabLayout.setHorizontalGroup(
            taxesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(taxesTabLayout.createSequentialGroup()
                .addGroup(taxesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(taxesTabLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(taxesTabLayout.createSequentialGroup()
                        .addGap(276, 276, 276)
                        .addGroup(taxesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(287, Short.MAX_VALUE))
        );
        taxesTabLayout.setVerticalGroup(
            taxesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(taxesTabLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel19)
                .addGap(85, 85, 85)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(204, Short.MAX_VALUE))
        );

        dashboardTabbedPane.addTab("Taxes", taxesTab);

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(153, 153, 153));
        jLabel20.setText("General Journal");
        jLabel20.setToolTipText("");

        journalSort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sort", "ID", "Date", "Description", "Encoder" }));
        journalSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                journalSortActionPerformed(evt);
            }
        });

        searchJournal.setToolTipText("Search");
        searchJournal.setPreferredSize(new java.awt.Dimension(30, 30));
        searchJournal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchJournalMouseClicked(evt);
            }
        });

        refreshJournal.setToolTipText("Refresh");
        refreshJournal.setPreferredSize(new java.awt.Dimension(30, 30));
        refreshJournal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshJournalMouseClicked(evt);
            }
        });

        filterJournal.setToolTipText("Filter");
        filterJournal.setPreferredSize(new java.awt.Dimension(30, 30));
        filterJournal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                filterJournalMouseClicked(evt);
            }
        });

        fromJournal.setDateFormatString("MM/dd/yyyy");

        toJournal.setDateFormatString("MM/dd/yyyy");

        journalTable.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        journalTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Date", "Description", "Encoder"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        journalTable.setRowHeight(25);
        journalTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane6.setViewportView(journalTable);
        if (journalTable.getColumnModel().getColumnCount() > 0) {
            journalTable.getColumnModel().getColumn(0).setResizable(false);
            journalTable.getColumnModel().getColumn(1).setResizable(false);
            journalTable.getColumnModel().getColumn(2).setResizable(false);
            journalTable.getColumnModel().getColumn(3).setResizable(false);
        }

        addEntry.setText("Add");
        addEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEntryActionPerformed(evt);
            }
        });

        deleteEntry.setText("Delete");
        deleteEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteEntryActionPerformed(evt);
            }
        });

        editEntry.setText("Edit");
        editEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editEntryActionPerformed(evt);
            }
        });

        jLabel12.setText("From:");

        jLabel13.setText("To:");

        viewEntry.setText("View");
        viewEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewEntryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout generalJournalTabLayout = new javax.swing.GroupLayout(generalJournalTab);
        generalJournalTab.setLayout(generalJournalTabLayout);
        generalJournalTabLayout.setHorizontalGroup(
            generalJournalTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalJournalTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(generalJournalTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(generalJournalTabLayout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(generalJournalTabLayout.createSequentialGroup()
                        .addGroup(generalJournalTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(generalJournalTabLayout.createSequentialGroup()
                                .addComponent(addEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deleteEntry)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(editEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(viewEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 1071, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(generalJournalTabLayout.createSequentialGroup()
                                .addComponent(searchJournal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(journalSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(journalSort, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(refreshJournal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(filterJournal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(generalJournalTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fromJournal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12))
                                .addGap(18, 18, 18)
                                .addGroup(generalJournalTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addComponent(toJournal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 49, Short.MAX_VALUE))))
        );
        generalJournalTabLayout.setVerticalGroup(
            generalJournalTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalJournalTabLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel20)
                .addGap(33, 33, 33)
                .addGroup(generalJournalTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(generalJournalTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(generalJournalTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(journalSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(journalSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(searchJournal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshJournal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterJournal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fromJournal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(toJournal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(generalJournalTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editEntry)
                    .addComponent(deleteEntry)
                    .addComponent(addEntry)
                    .addComponent(viewEntry))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        dashboardTabbedPane.addTab("General Journal", generalJournalTab);

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(153, 153, 153));
        jLabel21.setText("General Ledger");
        jLabel21.setToolTipText("");

        ledgerTable.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        ledgerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Date", "Description", "Debit", "Credit"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ledgerTable.setRowHeight(25);
        ledgerTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(ledgerTable);
        if (ledgerTable.getColumnModel().getColumnCount() > 0) {
            ledgerTable.getColumnModel().getColumn(0).setResizable(false);
            ledgerTable.getColumnModel().getColumn(1).setResizable(false);
            ledgerTable.getColumnModel().getColumn(2).setResizable(false);
            ledgerTable.getColumnModel().getColumn(3).setResizable(false);
            ledgerTable.getColumnModel().getColumn(4).setResizable(false);
        }

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Account Name:");

        ledgerAccountSelectionBox.setBorder(null);
        ledgerAccountSelectionBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ledgerAccountSelectionBoxActionPerformed(evt);
            }
        });

        refreshLedger.setToolTipText("Refresh");
        refreshLedger.setPreferredSize(new java.awt.Dimension(30, 30));
        refreshLedger.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshLedgerMouseClicked(evt);
            }
        });

        filterLedger.setToolTipText("Filter");
        filterLedger.setPreferredSize(new java.awt.Dimension(30, 30));
        filterLedger.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                filterLedgerMouseClicked(evt);
            }
        });

        fromLedger.setDateFormatString("MM/dd/yyyy");

        toLedger.setDateFormatString("MM/dd/yyyy");

        jLabel32.setText("From:");

        jLabel33.setText("To:");

        jLabel63.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel63.setText("Total Balance:");

        ledgerTotalBalance.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ledgerTotalBalance.setText("₱ 0.00");

        viewTransactionSummary.setText("View Transaction Summary");
        viewTransactionSummary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewTransactionSummaryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout generalLedgerTabLayout = new javax.swing.GroupLayout(generalLedgerTab);
        generalLedgerTab.setLayout(generalLedgerTabLayout);
        generalLedgerTabLayout.setHorizontalGroup(
            generalLedgerTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalLedgerTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(generalLedgerTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(generalLedgerTabLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ledgerAccountSelectionBox, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel63)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ledgerTotalBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(refreshLedger, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(filterLedger, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(generalLedgerTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fromLedger, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(generalLedgerTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(toLedger, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel33))
                        .addGap(35, 35, 35))
                    .addGroup(generalLedgerTabLayout.createSequentialGroup()
                        .addGroup(generalLedgerTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(viewTransactionSummary)
                            .addGroup(generalLedgerTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 1071, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(22, Short.MAX_VALUE))))
        );
        generalLedgerTabLayout.setVerticalGroup(
            generalLedgerTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalLedgerTabLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(generalLedgerTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, generalLedgerTabLayout.createSequentialGroup()
                        .addGroup(generalLedgerTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(ledgerAccountSelectionBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel63)
                            .addComponent(ledgerTotalBalance))
                        .addGap(6, 6, 6))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, generalLedgerTabLayout.createSequentialGroup()
                        .addGroup(generalLedgerTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32)
                            .addComponent(jLabel33))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(generalLedgerTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(toLedger, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fromLedger, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, generalLedgerTabLayout.createSequentialGroup()
                        .addGroup(generalLedgerTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(refreshLedger, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(filterLedger, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)))
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(viewTransactionSummary)
                .addGap(39, 39, 39))
        );

        dashboardTabbedPane.addTab("General Ledger", generalLedgerTab);

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(153, 153, 153));
        jLabel22.setText("Chart of Accounts");
        jLabel22.setToolTipText("");

        searchAccount.setToolTipText("Refresh");
        searchAccount.setPreferredSize(new java.awt.Dimension(30, 30));
        searchAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchAccountMouseClicked(evt);
            }
        });

        accountSort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sort", "Code", "Name", "Type", "Normally" }));
        accountSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accountSortActionPerformed(evt);
            }
        });

        refreshAccount.setToolTipText("Refresh");
        refreshAccount.setPreferredSize(new java.awt.Dimension(30, 30));
        refreshAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshAccountMouseClicked(evt);
            }
        });

        accountTable.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        accountTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Code", "Account Name", "Account Type", "Normally", "Balance"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        accountTable.setRowHeight(25);
        accountTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(accountTable);
        if (accountTable.getColumnModel().getColumnCount() > 0) {
            accountTable.getColumnModel().getColumn(0).setResizable(false);
            accountTable.getColumnModel().getColumn(1).setResizable(false);
            accountTable.getColumnModel().getColumn(2).setResizable(false);
            accountTable.getColumnModel().getColumn(3).setResizable(false);
            accountTable.getColumnModel().getColumn(4).setResizable(false);
        }

        addAccount.setText("Add");
        addAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAccountActionPerformed(evt);
            }
        });

        deleteAccount.setText("Delete");
        deleteAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteAccountActionPerformed(evt);
            }
        });

        updateAccount.setText("Update");
        updateAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateAccountActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout chartOfAccountsTabLayout = new javax.swing.GroupLayout(chartOfAccountsTab);
        chartOfAccountsTab.setLayout(chartOfAccountsTabLayout);
        chartOfAccountsTabLayout.setHorizontalGroup(
            chartOfAccountsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chartOfAccountsTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(chartOfAccountsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(chartOfAccountsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(chartOfAccountsTabLayout.createSequentialGroup()
                            .addComponent(addAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(deleteAccount)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(updateAccount))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, chartOfAccountsTabLayout.createSequentialGroup()
                            .addComponent(searchAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(accountSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(accountSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(refreshAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1071, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        chartOfAccountsTabLayout.setVerticalGroup(
            chartOfAccountsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chartOfAccountsTabLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addGroup(chartOfAccountsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(accountSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(accountSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(chartOfAccountsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateAccount)
                    .addComponent(deleteAccount)
                    .addComponent(addAccount))
                .addGap(41, 41, 41))
        );

        dashboardTabbedPane.addTab("Chart of Accounts", chartOfAccountsTab);

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(153, 153, 153));
        jLabel23.setText("Reports");
        jLabel23.setToolTipText("");

        reportTable.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        reportTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        reportTable.setRowHeight(25);
        reportTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane13.setViewportView(reportTable);

        print.setText("Print");
        print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printActionPerformed(evt);
            }
        });

        reportSheet.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        reportSheet.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Balance Sheet", "Income Statement", "Trial Balance", "Ledger Account" }));
        reportSheet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportSheetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout reportsTabLayout = new javax.swing.GroupLayout(reportsTab);
        reportsTab.setLayout(reportsTabLayout);
        reportsTabLayout.setHorizontalGroup(
            reportsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportsTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(reportsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(reportsTabLayout.createSequentialGroup()
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, reportsTabLayout.createSequentialGroup()
                        .addGap(0, 153, Short.MAX_VALUE)
                        .addGroup(reportsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(reportsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(print, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(reportSheet, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(140, 140, 140))))
        );
        reportsTabLayout.setVerticalGroup(
            reportsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportsTabLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(reportSheet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(print)
                .addGap(79, 79, 79))
        );

        dashboardTabbedPane.addTab("Reports", reportsTab);

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(153, 153, 153));
        jLabel24.setText("Customer Management");
        jLabel24.setToolTipText("");

        searchCustomer.setToolTipText("Search");
        searchCustomer.setPreferredSize(new java.awt.Dimension(30, 30));
        searchCustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchCustomerMouseClicked(evt);
            }
        });

        customerSort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sort", "ID", "First Name", "Last Name" }));
        customerSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerSortActionPerformed(evt);
            }
        });

        refreshCustomer.setToolTipText("Refresh");
        refreshCustomer.setPreferredSize(new java.awt.Dimension(30, 30));
        refreshCustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshCustomerMouseClicked(evt);
            }
        });

        customerTable.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        customerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "First Name", "Last Name", "Phone Number", "Organization", "Position", "Address", "Balance"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        customerTable.setRowHeight(25);
        customerTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(customerTable);
        if (customerTable.getColumnModel().getColumnCount() > 0) {
            customerTable.getColumnModel().getColumn(0).setResizable(false);
            customerTable.getColumnModel().getColumn(1).setResizable(false);
            customerTable.getColumnModel().getColumn(2).setResizable(false);
            customerTable.getColumnModel().getColumn(3).setResizable(false);
            customerTable.getColumnModel().getColumn(7).setResizable(false);
        }

        addCustomer.setText("Add");
        addCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCustomerActionPerformed(evt);
            }
        });

        deleteCustomer.setText("Delete");
        deleteCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteCustomerActionPerformed(evt);
            }
        });

        updateCustomer.setText("Update");
        updateCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateCustomerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout customerManagementTabLayout = new javax.swing.GroupLayout(customerManagementTab);
        customerManagementTab.setLayout(customerManagementTabLayout);
        customerManagementTabLayout.setHorizontalGroup(
            customerManagementTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerManagementTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(customerManagementTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(customerManagementTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(customerManagementTabLayout.createSequentialGroup()
                            .addComponent(addCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(deleteCustomer)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(updateCustomer))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, customerManagementTabLayout.createSequentialGroup()
                            .addComponent(searchCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(customerSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(customerSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(refreshCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1071, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        customerManagementTabLayout.setVerticalGroup(
            customerManagementTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerManagementTabLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addGroup(customerManagementTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customerSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customerSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(customerManagementTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateCustomer)
                    .addComponent(deleteCustomer)
                    .addComponent(addCustomer))
                .addGap(41, 41, 41))
        );

        dashboardTabbedPane.addTab("Customer Management", customerManagementTab);

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(153, 153, 153));
        jLabel25.setText("Vendor Management");
        jLabel25.setToolTipText("");

        searchVendor.setToolTipText("Search");
        searchVendor.setPreferredSize(new java.awt.Dimension(30, 30));
        searchVendor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchVendorMouseClicked(evt);
            }
        });

        vendorSort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sort", "ID", "Name", "Email", "Address" }));
        vendorSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vendorSortActionPerformed(evt);
            }
        });

        vendorTable.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        vendorTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Phone Number", "Address", "Email", "Balance"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        vendorTable.setRowHeight(25);
        vendorTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(vendorTable);
        if (vendorTable.getColumnModel().getColumnCount() > 0) {
            vendorTable.getColumnModel().getColumn(0).setResizable(false);
            vendorTable.getColumnModel().getColumn(1).setResizable(false);
            vendorTable.getColumnModel().getColumn(2).setResizable(false);
            vendorTable.getColumnModel().getColumn(3).setResizable(false);
            vendorTable.getColumnModel().getColumn(4).setResizable(false);
            vendorTable.getColumnModel().getColumn(5).setResizable(false);
        }

        addVendor.setText("Add");
        addVendor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addVendorActionPerformed(evt);
            }
        });

        deleteVendor.setText("Delete");
        deleteVendor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteVendorActionPerformed(evt);
            }
        });

        updateVendor.setText("Update");
        updateVendor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateVendorActionPerformed(evt);
            }
        });

        refreshVendor.setToolTipText("Refresh");
        refreshVendor.setPreferredSize(new java.awt.Dimension(30, 30));
        refreshVendor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshVendorMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout vendorManagementTabLayout = new javax.swing.GroupLayout(vendorManagementTab);
        vendorManagementTab.setLayout(vendorManagementTabLayout);
        vendorManagementTabLayout.setHorizontalGroup(
            vendorManagementTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vendorManagementTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(vendorManagementTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(vendorManagementTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(vendorManagementTabLayout.createSequentialGroup()
                            .addComponent(addVendor, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(deleteVendor)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(updateVendor))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, vendorManagementTabLayout.createSequentialGroup()
                            .addComponent(searchVendor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(vendorSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(vendorSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(refreshVendor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1071, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        vendorManagementTabLayout.setVerticalGroup(
            vendorManagementTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vendorManagementTabLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                .addGroup(vendorManagementTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchVendor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(vendorSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(vendorSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshVendor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(vendorManagementTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateVendor)
                    .addComponent(deleteVendor)
                    .addComponent(addVendor))
                .addGap(42, 42, 42))
        );

        dashboardTabbedPane.addTab("Vendor Management", vendorManagementTab);

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(153, 153, 153));
        jLabel26.setText("User Management ");
        jLabel26.setToolTipText("");

        searchUser.setToolTipText("Search");
        searchUser.setPreferredSize(new java.awt.Dimension(30, 30));
        searchUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchUserMouseClicked(evt);
            }
        });

        userSort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sort", "ID", "First Name", "Last Name", "Username", "Access Level", "Status" }));
        userSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userSortActionPerformed(evt);
            }
        });

        userTable.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        userTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "First Name", "Last Name", "Username", "Access Level", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        userTable.setRowHeight(25);
        userTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(userTable);
        if (userTable.getColumnModel().getColumnCount() > 0) {
            userTable.getColumnModel().getColumn(0).setResizable(false);
            userTable.getColumnModel().getColumn(1).setResizable(false);
            userTable.getColumnModel().getColumn(2).setResizable(false);
            userTable.getColumnModel().getColumn(3).setResizable(false);
            userTable.getColumnModel().getColumn(4).setResizable(false);
            userTable.getColumnModel().getColumn(5).setResizable(false);
        }

        refreshUser.setToolTipText("Refresh");
        refreshUser.setPreferredSize(new java.awt.Dimension(30, 30));
        refreshUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshUserMouseClicked(evt);
            }
        });

        updateUser.setText("Update");
        updateUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateUserActionPerformed(evt);
            }
        });

        deactivateUser.setText("Deactivate");
        deactivateUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deactivateUserActionPerformed(evt);
            }
        });

        addUser.setText("Add");
        addUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addUserActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout userManagementTabLayout = new javax.swing.GroupLayout(userManagementTab);
        userManagementTab.setLayout(userManagementTabLayout);
        userManagementTabLayout.setHorizontalGroup(
            userManagementTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userManagementTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(userManagementTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(userManagementTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(userManagementTabLayout.createSequentialGroup()
                            .addComponent(addUser, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(deactivateUser)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(updateUser))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, userManagementTabLayout.createSequentialGroup()
                            .addComponent(searchUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(userSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(userSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(refreshUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1071, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        userManagementTabLayout.setVerticalGroup(
            userManagementTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userManagementTabLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel26)
                .addGap(58, 58, 58)
                .addGroup(userManagementTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(userManagementTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateUser)
                    .addComponent(deactivateUser)
                    .addComponent(addUser))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        dashboardTabbedPane.addTab("User Management", userManagementTab);

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(153, 153, 153));
        jLabel27.setText("Settings");
        jLabel27.setToolTipText("");

        jScrollPane15.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane15.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jLabel51.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel51.setText("General");

        jLabel50.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel50.setText("Opening balance date");

        jLabel53.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel53.setText("Tax rate");

        jLabel48.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel48.setText("Summary");

        jLabel49.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel49.setText("Recent transactions range");

        jLabel52.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel52.setText("Profit and Loss range");

        profitAndLossRange.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1 day", "2 days", "3 days", "1 week", "1 month" }));
        profitAndLossRange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profitAndLossRangeActionPerformed(evt);
            }
        });

        recentTransactionsRange.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1 day", "2 days", "3 days", "1 week" }));
        recentTransactionsRange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recentTransactionsRangeActionPerformed(evt);
            }
        });

        taxRateConfig.setEditable(false);
        taxRateConfig.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        openingBalanceDate.setDateFormatString("MM/dd/yyyy");

        setOpeningBalanceDate.setText("Set");
        setOpeningBalanceDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setOpeningBalanceDateActionPerformed(evt);
            }
        });

        changeTaxRate.setText("Change");
        changeTaxRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeTaxRateActionPerformed(evt);
            }
        });

        bmsLogo.setPreferredSize(new java.awt.Dimension(200, 200));

        jLabel54.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel54.setText("Sales");

        jLabel57.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel57.setText("Set payment method accounts");

        setSalesPaymentMethod.setText("Set");
        setSalesPaymentMethod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setSalesPaymentMethodActionPerformed(evt);
            }
        });

        jLabel58.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel58.setText("Set remaining balance account");

        jLabel59.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel59.setText("Set revenue account");

        jLabel60.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel60.setText("Purchases");

        jLabel61.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel61.setText("Set payment method accounts");

        jLabel64.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel64.setText("Set remaining balance account");

        setPurchasesPaymentMethod.setText("Set");
        setPurchasesPaymentMethod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setPurchasesPaymentMethodActionPerformed(evt);
            }
        });

        setSalesRemainingBalanceAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setSalesRemainingBalanceAccountActionPerformed(evt);
            }
        });

        setSalesRevenueAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setSalesRevenueAccountActionPerformed(evt);
            }
        });

        setPurchasesRemainingBalanceAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setPurchasesRemainingBalanceAccountActionPerformed(evt);
            }
        });

        productTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Description", "Qty/Size", "Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        productTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane14.setViewportView(productTable);
        if (productTable.getColumnModel().getColumnCount() > 0) {
            productTable.getColumnModel().getColumn(0).setResizable(false);
            productTable.getColumnModel().getColumn(1).setResizable(false);
            productTable.getColumnModel().getColumn(2).setResizable(false);
            productTable.getColumnModel().getColumn(3).setResizable(false);
        }

        jLabel62.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel62.setText("Products/Services List");

        addProduct.setText("Add");
        addProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addProductActionPerformed(evt);
            }
        });

        editProduct.setText("Edit");
        editProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editProductActionPerformed(evt);
            }
        });

        removeProduct.setText("Remove");
        removeProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeProductActionPerformed(evt);
            }
        });

        removeAllProduct.setText("Remove All");
        removeAllProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAllProductActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 1024, Short.MAX_VALUE)
                            .addComponent(jLabel60)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel61)
                                    .addComponent(jLabel64))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(setPurchasesRemainingBalanceAccount, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(viewPurchasesPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(setPurchasesPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator4)))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 999, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel54)
                            .addComponent(jLabel48)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel11Layout.createSequentialGroup()
                                        .addComponent(jLabel52)
                                        .addGap(40, 40, 40)
                                        .addComponent(profitAndLossRange, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel11Layout.createSequentialGroup()
                                        .addComponent(jLabel49)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(recentTransactionsRange, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel11Layout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel50)
                                            .addComponent(jLabel53))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(openingBalanceDate, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(taxRateConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(changeTaxRate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(setOpeningBalanceDate, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel51)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel11Layout.createSequentialGroup()
                                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel58)
                                            .addComponent(jLabel59))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(viewSalesPaymentMethod)
                                            .addComponent(setSalesRemainingBalanceAccount, javax.swing.GroupLayout.Alignment.LEADING, 0, 200, Short.MAX_VALUE)
                                            .addComponent(setSalesRevenueAccount, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(setSalesPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel11Layout.createSequentialGroup()
                                        .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(addProduct, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(editProduct, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(removeProduct, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(removeAllProduct, javax.swing.GroupLayout.Alignment.LEADING)))))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(jLabel57))
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 1012, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(395, 395, 395)
                        .addComponent(bmsLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(73, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel51)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel50)
                        .addComponent(openingBalanceDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(setOpeningBalanceDate))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel53)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(taxRateConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(changeTaxRate)))
                .addGap(24, 24, 24)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel48)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49)
                    .addComponent(recentTransactionsRange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(profitAndLossRange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel52))
                .addGap(29, 29, 29)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel54)
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel57)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel58)
                                .addGap(45, 45, 45))
                            .addComponent(jLabel59)))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(viewSalesPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(setSalesPaymentMethod))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(setSalesRemainingBalanceAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(setSalesRevenueAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel62)
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(addProduct)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editProduct)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeProduct)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeAllProduct)))
                .addGap(29, 29, 29)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel60)
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel61)
                    .addComponent(viewPurchasesPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(setPurchasesPaymentMethod))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel64)
                    .addComponent(setPurchasesRemainingBalanceAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(bmsLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jScrollPane15.setViewportView(jPanel11);

        settingsTabbedPane.addTab("Accounting Configuration", jScrollPane15);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel11.setText("Username");

        jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel34.setText("Password");

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel35.setText("Server");

        jLabel36.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel36.setText("Port");

        jLabel37.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel37.setText("Database");

        networkUsername.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        networkServer.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        networkPort.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        networkDatabase.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        networkPassword.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        editNetwork.setText("Edit");
        editNetwork.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editNetworkActionPerformed(evt);
            }
        });

        saveNetwork.setText("Save");
        saveNetwork.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveNetworkActionPerformed(evt);
            }
        });

        cancelNetworkChanges.setText("Cancel");
        cancelNetworkChanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelNetworkChangesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(334, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(editNetwork, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveNetwork)
                        .addGap(34, 34, 34)
                        .addComponent(cancelNetworkChanges))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel34)
                            .addComponent(jLabel35)
                            .addComponent(jLabel36)
                            .addComponent(jLabel37))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(networkUsername)
                            .addComponent(networkPort)
                            .addComponent(networkServer)
                            .addComponent(networkDatabase)
                            .addComponent(networkPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(355, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(networkUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel11)
                        .addGap(56, 56, 56)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34)
                            .addComponent(networkPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(46, 46, 46)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel35)
                            .addComponent(networkServer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(43, 43, 43)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel36)
                            .addComponent(networkPort, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel37)
                            .addComponent(networkDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editNetwork)
                    .addComponent(saveNetwork)
                    .addComponent(cancelNetworkChanges))
                .addGap(88, 88, 88))
        );

        settingsTabbedPane.addTab("Network Configuration", jPanel5);

        auditTrailTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Timestamp", "Module", "Event", "User", "Transaction ID", "Accounts", "Before Value", "After Value", "Total Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        auditTrailTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane12.setViewportView(auditTrailTable);
        if (auditTrailTable.getColumnModel().getColumnCount() > 0) {
            auditTrailTable.getColumnModel().getColumn(0).setResizable(false);
            auditTrailTable.getColumnModel().getColumn(1).setResizable(false);
            auditTrailTable.getColumnModel().getColumn(2).setResizable(false);
            auditTrailTable.getColumnModel().getColumn(3).setResizable(false);
            auditTrailTable.getColumnModel().getColumn(4).setResizable(false);
            auditTrailTable.getColumnModel().getColumn(5).setResizable(false);
            auditTrailTable.getColumnModel().getColumn(6).setResizable(false);
            auditTrailTable.getColumnModel().getColumn(7).setResizable(false);
            auditTrailTable.getColumnModel().getColumn(8).setResizable(false);
            auditTrailTable.getColumnModel().getColumn(9).setResizable(false);
        }

        searchAuditTrail.setToolTipText("Search");
        searchAuditTrail.setPreferredSize(new java.awt.Dimension(30, 30));
        searchAuditTrail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchAuditTrailMouseClicked(evt);
            }
        });

        toTrail.setDateFormatString("MM/dd/yyyy");

        fromTrail.setDateFormatString("MM/dd/yyyy");

        jLabel55.setText("From:");

        jLabel56.setText("To:");

        filterAuditTrail.setToolTipText("Filter");
        filterAuditTrail.setPreferredSize(new java.awt.Dimension(30, 30));
        filterAuditTrail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                filterAuditTrailMouseClicked(evt);
            }
        });

        refreshAuditTrail.setToolTipText("Refresh");
        refreshAuditTrail.setPreferredSize(new java.awt.Dimension(30, 30));
        refreshAuditTrail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshAuditTrailMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane12)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(searchAuditTrail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(auditTrailSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 460, Short.MAX_VALUE)
                        .addComponent(refreshAuditTrail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(filterAuditTrail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fromTrail, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel55))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel56)
                            .addComponent(toTrail, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchAuditTrail, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(filterAuditTrail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(refreshAuditTrail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(toTrail, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel55)
                            .addComponent(jLabel56))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fromTrail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(auditTrailSearchField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 453, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        settingsTabbedPane.addTab("Audit Trail", jPanel10);

        javax.swing.GroupLayout settingsTabLayout = new javax.swing.GroupLayout(settingsTab);
        settingsTab.setLayout(settingsTabLayout);
        settingsTabLayout.setHorizontalGroup(
            settingsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(settingsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(settingsTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1073, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        settingsTabLayout.setVerticalGroup(
            settingsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsTabLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addComponent(settingsTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );

        dashboardTabbedPane.addTab("Settings", settingsTab);

        getContentPane().add(dashboardTabbedPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 30, 1110, 770));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void summaryButtonPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_summaryButtonPMouseEntered
        summaryButtonP.setBackground(new Color(8, 82, 157));
    }//GEN-LAST:event_summaryButtonPMouseEntered

    private void summaryButtonPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_summaryButtonPMouseExited
        if(dashboardTabbedPane.getSelectedIndex() != 0){summaryButtonP.setBackground(new Color(8, 61, 119));} 
    }//GEN-LAST:event_summaryButtonPMouseExited

    private void salesButtonPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salesButtonPMouseEntered
        salesButtonP.setBackground(new Color(8, 82, 157));
    }//GEN-LAST:event_salesButtonPMouseEntered

    private void salesButtonPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salesButtonPMouseExited
        if(dashboardTabbedPane.getSelectedIndex() != 1){salesButtonP.setBackground(new Color(8, 61, 119));} 
    }//GEN-LAST:event_salesButtonPMouseExited

    private void expensesButtonPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_expensesButtonPMouseEntered
        expensesButtonP.setBackground(new Color(8, 82, 157));
    }//GEN-LAST:event_expensesButtonPMouseEntered

    private void expensesButtonPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_expensesButtonPMouseExited
        if(dashboardTabbedPane.getSelectedIndex() != 2){expensesButtonP.setBackground(new Color(8, 61, 119));} 
    }//GEN-LAST:event_expensesButtonPMouseExited

    private void purchasesButtonPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_purchasesButtonPMouseEntered
        purchasesButtonP.setBackground(new Color(8, 82, 157));
    }//GEN-LAST:event_purchasesButtonPMouseEntered

    private void purchasesButtonPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_purchasesButtonPMouseExited
        if(dashboardTabbedPane.getSelectedIndex() != 3){purchasesButtonP.setBackground(new Color(8, 61, 119));} 
    }//GEN-LAST:event_purchasesButtonPMouseExited

    private void taxesButtonPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_taxesButtonPMouseEntered
        taxesButtonP.setBackground(new Color(8, 82, 157));
    }//GEN-LAST:event_taxesButtonPMouseEntered

    private void taxesButtonPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_taxesButtonPMouseExited
        if(dashboardTabbedPane.getSelectedIndex() != 4){taxesButtonP.setBackground(new Color(8, 61, 119));} 
    }//GEN-LAST:event_taxesButtonPMouseExited

    private void generalJournalButtonPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_generalJournalButtonPMouseEntered
        generalJournalButtonP.setBackground(new Color(8, 82, 157));
    }//GEN-LAST:event_generalJournalButtonPMouseEntered

    private void generalJournalButtonPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_generalJournalButtonPMouseExited
        if(dashboardTabbedPane.getSelectedIndex() != 5){generalJournalButtonP.setBackground(new Color(8, 61, 119));} 
    }//GEN-LAST:event_generalJournalButtonPMouseExited

    private void generalLedgerButtonPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_generalLedgerButtonPMouseEntered
        generalLedgerButtonP.setBackground(new Color(8, 82, 157));
    }//GEN-LAST:event_generalLedgerButtonPMouseEntered

    private void generalLedgerButtonPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_generalLedgerButtonPMouseExited
        if(dashboardTabbedPane.getSelectedIndex() != 6){generalLedgerButtonP.setBackground(new Color(8, 61, 119));} 
    }//GEN-LAST:event_generalLedgerButtonPMouseExited

    private void chartOfAccountsButtonPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chartOfAccountsButtonPMouseEntered
        chartOfAccountsButtonP.setBackground(new Color(8, 82, 157));
    }//GEN-LAST:event_chartOfAccountsButtonPMouseEntered

    private void chartOfAccountsButtonPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chartOfAccountsButtonPMouseExited
        if(dashboardTabbedPane.getSelectedIndex() != 7){chartOfAccountsButtonP.setBackground(new Color(8, 61, 119));} 
    }//GEN-LAST:event_chartOfAccountsButtonPMouseExited

    private void reportsButtonPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reportsButtonPMouseEntered
        reportsButtonP.setBackground(new Color(8, 82, 157));
    }//GEN-LAST:event_reportsButtonPMouseEntered

    private void reportsButtonPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reportsButtonPMouseExited
        if(dashboardTabbedPane.getSelectedIndex() != 8){reportsButtonP.setBackground(new Color(8, 61, 119));}
    }//GEN-LAST:event_reportsButtonPMouseExited

    private void customerManagementButtonPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customerManagementButtonPMouseEntered
        customerManagementButtonP.setBackground(new Color(8, 82, 157));
    }//GEN-LAST:event_customerManagementButtonPMouseEntered

    private void customerManagementButtonPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customerManagementButtonPMouseExited
        if(dashboardTabbedPane.getSelectedIndex() != 9){customerManagementButtonP.setBackground(new Color(8, 61, 119));}
    }//GEN-LAST:event_customerManagementButtonPMouseExited

    private void vendorManagementButtonPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vendorManagementButtonPMouseEntered
        vendorManagementButtonP.setBackground(new Color(8, 82, 157));
    }//GEN-LAST:event_vendorManagementButtonPMouseEntered

    private void vendorManagementButtonPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vendorManagementButtonPMouseExited
        if(dashboardTabbedPane.getSelectedIndex() != 10){vendorManagementButtonP.setBackground(new Color(8, 61, 119));}
    }//GEN-LAST:event_vendorManagementButtonPMouseExited

    private void userManagementButtonPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_userManagementButtonPMouseEntered
        userManagementButtonP.setBackground(new Color(8, 82, 157));
    }//GEN-LAST:event_userManagementButtonPMouseEntered

    private void userManagementButtonPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_userManagementButtonPMouseExited
        if(dashboardTabbedPane.getSelectedIndex() != 11){userManagementButtonP.setBackground(new Color(8, 61, 119));}
    }//GEN-LAST:event_userManagementButtonPMouseExited

    private void settingsButtonPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsButtonPMouseEntered
        settingsButtonP.setBackground(new Color(8, 82, 157));
    }//GEN-LAST:event_settingsButtonPMouseEntered

    private void settingsButtonPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsButtonPMouseExited
        if(dashboardTabbedPane.getSelectedIndex() != 12){settingsButtonP.setBackground(new Color(8, 61, 119));}
    }//GEN-LAST:event_settingsButtonPMouseExited

    private void summaryButtonPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_summaryButtonPMouseClicked
        initSummary();
        dashboardTabbedPane.setSelectedIndex(0);
        salesButtonP.setBackground(new Color(8, 61, 119));
        expensesButtonP.setBackground(new Color(8, 61, 119));
        purchasesButtonP.setBackground(new Color(8, 61, 119));
        taxesButtonP.setBackground(new Color(8, 61, 119));
        generalJournalButtonP.setBackground(new Color(8, 61, 119));
        generalLedgerButtonP.setBackground(new Color(8, 61, 119));
        chartOfAccountsButtonP.setBackground(new Color(8, 61, 119));
        reportsButtonP.setBackground(new Color(8, 61, 119));
        customerManagementButtonP.setBackground(new Color(8, 61, 119));
        vendorManagementButtonP.setBackground(new Color(8, 61, 119));
        userManagementButtonP.setBackground(new Color(8, 61, 119));
        settingsButtonP.setBackground(new Color(8, 61, 119));
    }//GEN-LAST:event_summaryButtonPMouseClicked

    private void salesButtonPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salesButtonPMouseClicked
        showSalesList(new EntryDBController().getAllSales());
        dashboardTabbedPane.setSelectedIndex(1);
        summaryButtonP.setBackground(new Color(8, 61, 119));
        expensesButtonP.setBackground(new Color(8, 61, 119));
        purchasesButtonP.setBackground(new Color(8, 61, 119));
        taxesButtonP.setBackground(new Color(8, 61, 119));
        generalJournalButtonP.setBackground(new Color(8, 61, 119));
        generalLedgerButtonP.setBackground(new Color(8, 61, 119));
        chartOfAccountsButtonP.setBackground(new Color(8, 61, 119));
        reportsButtonP.setBackground(new Color(8, 61, 119));
        customerManagementButtonP.setBackground(new Color(8, 61, 119));
        vendorManagementButtonP.setBackground(new Color(8, 61, 119));
        userManagementButtonP.setBackground(new Color(8, 61, 119));
        settingsButtonP.setBackground(new Color(8, 61, 119));
    }//GEN-LAST:event_salesButtonPMouseClicked

    private void expensesButtonPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_expensesButtonPMouseClicked
        showExpensesList(new EntryDBController().getAllExpensesEntry());
        dashboardTabbedPane.setSelectedIndex(2);
        summaryButtonP.setBackground(new Color(8, 61, 119));
        salesButtonP.setBackground(new Color(8, 61, 119));
        purchasesButtonP.setBackground(new Color(8, 61, 119));
        taxesButtonP.setBackground(new Color(8, 61, 119));
        generalJournalButtonP.setBackground(new Color(8, 61, 119));
        generalLedgerButtonP.setBackground(new Color(8, 61, 119));
        chartOfAccountsButtonP.setBackground(new Color(8, 61, 119));
        reportsButtonP.setBackground(new Color(8, 61, 119));
        customerManagementButtonP.setBackground(new Color(8, 61, 119));
        vendorManagementButtonP.setBackground(new Color(8, 61, 119));
        userManagementButtonP.setBackground(new Color(8, 61, 119));
        settingsButtonP.setBackground(new Color(8, 61, 119));
    }//GEN-LAST:event_expensesButtonPMouseClicked

    private void purchasesButtonPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_purchasesButtonPMouseClicked
        showPurchasesList(new EntryDBController().getAllPurchases());
        dashboardTabbedPane.setSelectedIndex(3);
        summaryButtonP.setBackground(new Color(8, 61, 119));
        salesButtonP.setBackground(new Color(8, 61, 119));
        expensesButtonP.setBackground(new Color(8, 61, 119));
        taxesButtonP.setBackground(new Color(8, 61, 119));
        generalJournalButtonP.setBackground(new Color(8, 61, 119));
        generalLedgerButtonP.setBackground(new Color(8, 61, 119));
        chartOfAccountsButtonP.setBackground(new Color(8, 61, 119));
        reportsButtonP.setBackground(new Color(8, 61, 119));
        customerManagementButtonP.setBackground(new Color(8, 61, 119));
        vendorManagementButtonP.setBackground(new Color(8, 61, 119));
        userManagementButtonP.setBackground(new Color(8, 61, 119));
        settingsButtonP.setBackground(new Color(8, 61, 119));
    }//GEN-LAST:event_purchasesButtonPMouseClicked

    private void taxesButtonPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_taxesButtonPMouseClicked
        dashboardTabbedPane.setSelectedIndex(4);
        summaryButtonP.setBackground(new Color(8, 61, 119));
        salesButtonP.setBackground(new Color(8, 61, 119));
        expensesButtonP.setBackground(new Color(8, 61, 119));
        purchasesButtonP.setBackground(new Color(8, 61, 119));
        generalJournalButtonP.setBackground(new Color(8, 61, 119));
        generalLedgerButtonP.setBackground(new Color(8, 61, 119));
        chartOfAccountsButtonP.setBackground(new Color(8, 61, 119));
        reportsButtonP.setBackground(new Color(8, 61, 119));
        customerManagementButtonP.setBackground(new Color(8, 61, 119));
        vendorManagementButtonP.setBackground(new Color(8, 61, 119));
        userManagementButtonP.setBackground(new Color(8, 61, 119));
        settingsButtonP.setBackground(new Color(8, 61, 119));
    }//GEN-LAST:event_taxesButtonPMouseClicked

    private void generalJournalButtonPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_generalJournalButtonPMouseClicked
        showJournalEntryList(new EntryDBController().getAllJournalEntry());
        dashboardTabbedPane.setSelectedIndex(5);
        summaryButtonP.setBackground(new Color(8, 61, 119));
        salesButtonP.setBackground(new Color(8, 61, 119));
        expensesButtonP.setBackground(new Color(8, 61, 119));
        purchasesButtonP.setBackground(new Color(8, 61, 119));
        taxesButtonP.setBackground(new Color(8, 61, 119));
        generalLedgerButtonP.setBackground(new Color(8, 61, 119));
        chartOfAccountsButtonP.setBackground(new Color(8, 61, 119));
        reportsButtonP.setBackground(new Color(8, 61, 119));
        customerManagementButtonP.setBackground(new Color(8, 61, 119));
        vendorManagementButtonP.setBackground(new Color(8, 61, 119));
        userManagementButtonP.setBackground(new Color(8, 61, 119));
        settingsButtonP.setBackground(new Color(8, 61, 119));
    }//GEN-LAST:event_generalJournalButtonPMouseClicked

    private void generalLedgerButtonPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_generalLedgerButtonPMouseClicked
        dashboardTabbedPane.setSelectedIndex(6);
        summaryButtonP.setBackground(new Color(8, 61, 119));
        salesButtonP.setBackground(new Color(8, 61, 119));
        expensesButtonP.setBackground(new Color(8, 61, 119));
        purchasesButtonP.setBackground(new Color(8, 61, 119));
        taxesButtonP.setBackground(new Color(8, 61, 119));
        generalJournalButtonP.setBackground(new Color(8, 61, 119));
        chartOfAccountsButtonP.setBackground(new Color(8, 61, 119));
        reportsButtonP.setBackground(new Color(8, 61, 119));
        customerManagementButtonP.setBackground(new Color(8, 61, 119));
        vendorManagementButtonP.setBackground(new Color(8, 61, 119));
        userManagementButtonP.setBackground(new Color(8, 61, 119));
        settingsButtonP.setBackground(new Color(8, 61, 119));
    }//GEN-LAST:event_generalLedgerButtonPMouseClicked

    private void chartOfAccountsButtonPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chartOfAccountsButtonPMouseClicked
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate dateToday = LocalDate.now();
        showAccountList(new AccountDBController().getAllAccounts(getAccountingConfig(true, false, false, false), dateToday.format(dateFormat)));
        dashboardTabbedPane.setSelectedIndex(7);
        summaryButtonP.setBackground(new Color(8, 61, 119));
        salesButtonP.setBackground(new Color(8, 61, 119));
        expensesButtonP.setBackground(new Color(8, 61, 119));
        purchasesButtonP.setBackground(new Color(8, 61, 119));
        taxesButtonP.setBackground(new Color(8, 61, 119));
        generalJournalButtonP.setBackground(new Color(8, 61, 119));
        generalLedgerButtonP.setBackground(new Color(8, 61, 119));
        reportsButtonP.setBackground(new Color(8, 61, 119));
        customerManagementButtonP.setBackground(new Color(8, 61, 119));
        vendorManagementButtonP.setBackground(new Color(8, 61, 119));
        userManagementButtonP.setBackground(new Color(8, 61, 119));
        settingsButtonP.setBackground(new Color(8, 61, 119));
    }//GEN-LAST:event_chartOfAccountsButtonPMouseClicked

    private void reportsButtonPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reportsButtonPMouseClicked
        setSelectedReportSheet();
        dashboardTabbedPane.setSelectedIndex(8);
        summaryButtonP.setBackground(new Color(8, 61, 119));
        salesButtonP.setBackground(new Color(8, 61, 119));
        expensesButtonP.setBackground(new Color(8, 61, 119));
        purchasesButtonP.setBackground(new Color(8, 61, 119));
        taxesButtonP.setBackground(new Color(8, 61, 119));
        generalJournalButtonP.setBackground(new Color(8, 61, 119));
        generalLedgerButtonP.setBackground(new Color(8, 61, 119));
        chartOfAccountsButtonP.setBackground(new Color(8, 61, 119));
        customerManagementButtonP.setBackground(new Color(8, 61, 119));
        vendorManagementButtonP.setBackground(new Color(8, 61, 119));
        userManagementButtonP.setBackground(new Color(8, 61, 119));
        settingsButtonP.setBackground(new Color(8, 61, 119));
    }//GEN-LAST:event_reportsButtonPMouseClicked

    private void customerManagementButtonPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customerManagementButtonPMouseClicked
        dashboardTabbedPane.setSelectedIndex(9);
        summaryButtonP.setBackground(new Color(8, 61, 119));
        salesButtonP.setBackground(new Color(8, 61, 119));
        expensesButtonP.setBackground(new Color(8, 61, 119));
        purchasesButtonP.setBackground(new Color(8, 61, 119));
        taxesButtonP.setBackground(new Color(8, 61, 119));
        generalJournalButtonP.setBackground(new Color(8, 61, 119));
        generalLedgerButtonP.setBackground(new Color(8, 61, 119));
        chartOfAccountsButtonP.setBackground(new Color(8, 61, 119));
        reportsButtonP.setBackground(new Color(8, 61, 119));
        vendorManagementButtonP.setBackground(new Color(8, 61, 119));
        userManagementButtonP.setBackground(new Color(8, 61, 119));
        settingsButtonP.setBackground(new Color(8, 61, 119));
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
            ArrayList<Customer> customerLists = new ArrayList();
            @Override
            protected Void doInBackground() throws Exception {
                ArrayList<Customer> customerList = new CustomerDBController().getAllCustomers();
                customerLists = customerList;
                return null;
            }
            
            @Override
            protected void done(){
                SwingUtilities.invokeLater(() -> {  
                    showCustomerList(customerLists);
                 });
            }
        };
        
        worker.execute();
    }//GEN-LAST:event_customerManagementButtonPMouseClicked

    private void vendorManagementButtonPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vendorManagementButtonPMouseClicked
        showVendorList(new VendorDBController().getAllVendors());
        dashboardTabbedPane.setSelectedIndex(10);
        summaryButtonP.setBackground(new Color(8, 61, 119));
        salesButtonP.setBackground(new Color(8, 61, 119));
        expensesButtonP.setBackground(new Color(8, 61, 119));
        purchasesButtonP.setBackground(new Color(8, 61, 119));
        taxesButtonP.setBackground(new Color(8, 61, 119));
        generalJournalButtonP.setBackground(new Color(8, 61, 119));
        generalLedgerButtonP.setBackground(new Color(8, 61, 119));
        chartOfAccountsButtonP.setBackground(new Color(8, 61, 119));
        reportsButtonP.setBackground(new Color(8, 61, 119));
        customerManagementButtonP.setBackground(new Color(8, 61, 119));
        userManagementButtonP.setBackground(new Color(8, 61, 119));
        settingsButtonP.setBackground(new Color(8, 61, 119));
    }//GEN-LAST:event_vendorManagementButtonPMouseClicked

    private void userManagementButtonPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_userManagementButtonPMouseClicked
        showUserList(new UserDBController().getAllUsers());
        dashboardTabbedPane.setSelectedIndex(11);
        summaryButtonP.setBackground(new Color(8, 61, 119));
        salesButtonP.setBackground(new Color(8, 61, 119));
        expensesButtonP.setBackground(new Color(8, 61, 119));
        purchasesButtonP.setBackground(new Color(8, 61, 119));
        taxesButtonP.setBackground(new Color(8, 61, 119));
        generalJournalButtonP.setBackground(new Color(8, 61, 119));
        generalLedgerButtonP.setBackground(new Color(8, 61, 119));
        chartOfAccountsButtonP.setBackground(new Color(8, 61, 119));
        reportsButtonP.setBackground(new Color(8, 61, 119));
        customerManagementButtonP.setBackground(new Color(8, 61, 119));
        vendorManagementButtonP.setBackground(new Color(8, 61, 119));
        settingsButtonP.setBackground(new Color(8, 61, 119));
    }//GEN-LAST:event_userManagementButtonPMouseClicked

    private void settingsButtonPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsButtonPMouseClicked
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String formattedFromTrail = dateFormat.format(fromTrail.getDate());
        String formattedToTrail = dateFormat.format(toTrail.getDate());
        showAuditTrail(new AuditTrailDBController().getAuditTrails(formattedFromTrail, formattedToTrail));
        showProductTable();
        settingsTabbedPane.setSelectedIndex(0);
        dashboardTabbedPane.setSelectedIndex(12);
        summaryButtonP.setBackground(new Color(8, 61, 119));
        salesButtonP.setBackground(new Color(8, 61, 119));
        expensesButtonP.setBackground(new Color(8, 61, 119));
        purchasesButtonP.setBackground(new Color(8, 61, 119));
        taxesButtonP.setBackground(new Color(8, 61, 119));
        generalJournalButtonP.setBackground(new Color(8, 61, 119));
        generalLedgerButtonP.setBackground(new Color(8, 61, 119));
        chartOfAccountsButtonP.setBackground(new Color(8, 61, 119));
        reportsButtonP.setBackground(new Color(8, 61, 119));
        customerManagementButtonP.setBackground(new Color(8, 61, 119));
        vendorManagementButtonP.setBackground(new Color(8, 61, 119));
        userManagementButtonP.setBackground(new Color(8, 61, 119));
    }//GEN-LAST:event_settingsButtonPMouseClicked

    private void summaryButtonLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_summaryButtonLMouseClicked
        initSummary();
        dashboardTabbedPane.setSelectedIndex(0);
        salesButtonP.setBackground(new Color(8, 61, 119));
        expensesButtonP.setBackground(new Color(8, 61, 119));
        purchasesButtonP.setBackground(new Color(8, 61, 119));
        taxesButtonP.setBackground(new Color(8, 61, 119));
        generalJournalButtonP.setBackground(new Color(8, 61, 119));
        generalLedgerButtonP.setBackground(new Color(8, 61, 119));
        chartOfAccountsButtonP.setBackground(new Color(8, 61, 119));
        reportsButtonP.setBackground(new Color(8, 61, 119));
        customerManagementButtonP.setBackground(new Color(8, 61, 119));
        vendorManagementButtonP.setBackground(new Color(8, 61, 119));
        userManagementButtonP.setBackground(new Color(8, 61, 119));
        settingsButtonP.setBackground(new Color(8, 61, 119));
    }//GEN-LAST:event_summaryButtonLMouseClicked

    private void salesButtonLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salesButtonLMouseClicked
        showSalesList(new EntryDBController().getAllSales());
        dashboardTabbedPane.setSelectedIndex(1);
        summaryButtonP.setBackground(new Color(8, 61, 119));
        expensesButtonP.setBackground(new Color(8, 61, 119));
        purchasesButtonP.setBackground(new Color(8, 61, 119));
        taxesButtonP.setBackground(new Color(8, 61, 119));
        generalJournalButtonP.setBackground(new Color(8, 61, 119));
        generalLedgerButtonP.setBackground(new Color(8, 61, 119));
        chartOfAccountsButtonP.setBackground(new Color(8, 61, 119));
        reportsButtonP.setBackground(new Color(8, 61, 119));
        customerManagementButtonP.setBackground(new Color(8, 61, 119));
        vendorManagementButtonP.setBackground(new Color(8, 61, 119));
        userManagementButtonP.setBackground(new Color(8, 61, 119));
        settingsButtonP.setBackground(new Color(8, 61, 119));
    }//GEN-LAST:event_salesButtonLMouseClicked

    private void expensesButtonLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_expensesButtonLMouseClicked
        showExpensesList(new EntryDBController().getAllExpensesEntry());
        dashboardTabbedPane.setSelectedIndex(2);
        summaryButtonP.setBackground(new Color(8, 61, 119));
        salesButtonP.setBackground(new Color(8, 61, 119));
        purchasesButtonP.setBackground(new Color(8, 61, 119));
        taxesButtonP.setBackground(new Color(8, 61, 119));
        generalJournalButtonP.setBackground(new Color(8, 61, 119));
        generalLedgerButtonP.setBackground(new Color(8, 61, 119));
        chartOfAccountsButtonP.setBackground(new Color(8, 61, 119));
        reportsButtonP.setBackground(new Color(8, 61, 119));
        customerManagementButtonP.setBackground(new Color(8, 61, 119));
        vendorManagementButtonP.setBackground(new Color(8, 61, 119));
        userManagementButtonP.setBackground(new Color(8, 61, 119));
        settingsButtonP.setBackground(new Color(8, 61, 119));
    }//GEN-LAST:event_expensesButtonLMouseClicked

    private void purchasesButtonLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_purchasesButtonLMouseClicked
        showPurchasesList(new EntryDBController().getAllPurchases());
        dashboardTabbedPane.setSelectedIndex(3);
        summaryButtonP.setBackground(new Color(8, 61, 119));
        salesButtonP.setBackground(new Color(8, 61, 119));
        expensesButtonP.setBackground(new Color(8, 61, 119));
        taxesButtonP.setBackground(new Color(8, 61, 119));
        generalJournalButtonP.setBackground(new Color(8, 61, 119));
        generalLedgerButtonP.setBackground(new Color(8, 61, 119));
        chartOfAccountsButtonP.setBackground(new Color(8, 61, 119));
        reportsButtonP.setBackground(new Color(8, 61, 119));
        customerManagementButtonP.setBackground(new Color(8, 61, 119));
        vendorManagementButtonP.setBackground(new Color(8, 61, 119));
        userManagementButtonP.setBackground(new Color(8, 61, 119));
        settingsButtonP.setBackground(new Color(8, 61, 119));
    }//GEN-LAST:event_purchasesButtonLMouseClicked

    private void taxesButtonLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_taxesButtonLMouseClicked
        dashboardTabbedPane.setSelectedIndex(4);
        summaryButtonP.setBackground(new Color(8, 61, 119));
        salesButtonP.setBackground(new Color(8, 61, 119));
        expensesButtonP.setBackground(new Color(8, 61, 119));
        purchasesButtonP.setBackground(new Color(8, 61, 119));
        generalJournalButtonP.setBackground(new Color(8, 61, 119));
        generalLedgerButtonP.setBackground(new Color(8, 61, 119));
        chartOfAccountsButtonP.setBackground(new Color(8, 61, 119));
        reportsButtonP.setBackground(new Color(8, 61, 119));
        customerManagementButtonP.setBackground(new Color(8, 61, 119));
        vendorManagementButtonP.setBackground(new Color(8, 61, 119));
        userManagementButtonP.setBackground(new Color(8, 61, 119));
        settingsButtonP.setBackground(new Color(8, 61, 119));
    }//GEN-LAST:event_taxesButtonLMouseClicked

    private void generalJournalButtonLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_generalJournalButtonLMouseClicked
        showJournalEntryList(new EntryDBController().getAllJournalEntry());
        dashboardTabbedPane.setSelectedIndex(5);
        summaryButtonP.setBackground(new Color(8, 61, 119));
        salesButtonP.setBackground(new Color(8, 61, 119));
        expensesButtonP.setBackground(new Color(8, 61, 119));
        purchasesButtonP.setBackground(new Color(8, 61, 119));
        taxesButtonP.setBackground(new Color(8, 61, 119));
        generalLedgerButtonP.setBackground(new Color(8, 61, 119));
        chartOfAccountsButtonP.setBackground(new Color(8, 61, 119));
        reportsButtonP.setBackground(new Color(8, 61, 119));
        customerManagementButtonP.setBackground(new Color(8, 61, 119));
        vendorManagementButtonP.setBackground(new Color(8, 61, 119));
        userManagementButtonP.setBackground(new Color(8, 61, 119));
        settingsButtonP.setBackground(new Color(8, 61, 119));
    }//GEN-LAST:event_generalJournalButtonLMouseClicked

    private void generalLedgerButtonLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_generalLedgerButtonLMouseClicked
        dashboardTabbedPane.setSelectedIndex(6);
        summaryButtonP.setBackground(new Color(8, 61, 119));
        salesButtonP.setBackground(new Color(8, 61, 119));
        expensesButtonP.setBackground(new Color(8, 61, 119));
        purchasesButtonP.setBackground(new Color(8, 61, 119));
        taxesButtonP.setBackground(new Color(8, 61, 119));
        generalJournalButtonP.setBackground(new Color(8, 61, 119));
        chartOfAccountsButtonP.setBackground(new Color(8, 61, 119));
        reportsButtonP.setBackground(new Color(8, 61, 119));
        customerManagementButtonP.setBackground(new Color(8, 61, 119));
        vendorManagementButtonP.setBackground(new Color(8, 61, 119));
        userManagementButtonP.setBackground(new Color(8, 61, 119));
        settingsButtonP.setBackground(new Color(8, 61, 119));
    }//GEN-LAST:event_generalLedgerButtonLMouseClicked

    private void chartOfAccountsButtonLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chartOfAccountsButtonLMouseClicked
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate dateToday = LocalDate.now();
        showAccountList(new AccountDBController().getAllAccounts(getAccountingConfig(true, false, false, false), dateToday.format(dateFormat)));
        dashboardTabbedPane.setSelectedIndex(7);
        summaryButtonP.setBackground(new Color(8, 61, 119));
        salesButtonP.setBackground(new Color(8, 61, 119));
        expensesButtonP.setBackground(new Color(8, 61, 119));
        purchasesButtonP.setBackground(new Color(8, 61, 119));
        taxesButtonP.setBackground(new Color(8, 61, 119));
        generalJournalButtonP.setBackground(new Color(8, 61, 119));
        generalLedgerButtonP.setBackground(new Color(8, 61, 119));
        reportsButtonP.setBackground(new Color(8, 61, 119));
        customerManagementButtonP.setBackground(new Color(8, 61, 119));
        vendorManagementButtonP.setBackground(new Color(8, 61, 119));
        userManagementButtonP.setBackground(new Color(8, 61, 119));
        settingsButtonP.setBackground(new Color(8, 61, 119));
    }//GEN-LAST:event_chartOfAccountsButtonLMouseClicked

    private void reportsButtonLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reportsButtonLMouseClicked
        setSelectedReportSheet();
        dashboardTabbedPane.setSelectedIndex(8);
        summaryButtonP.setBackground(new Color(8, 61, 119));
        salesButtonP.setBackground(new Color(8, 61, 119));
        expensesButtonP.setBackground(new Color(8, 61, 119));
        purchasesButtonP.setBackground(new Color(8, 61, 119));
        taxesButtonP.setBackground(new Color(8, 61, 119));
        generalJournalButtonP.setBackground(new Color(8, 61, 119));
        generalLedgerButtonP.setBackground(new Color(8, 61, 119));
        chartOfAccountsButtonP.setBackground(new Color(8, 61, 119));
        customerManagementButtonP.setBackground(new Color(8, 61, 119));
        vendorManagementButtonP.setBackground(new Color(8, 61, 119));
        userManagementButtonP.setBackground(new Color(8, 61, 119));
        settingsButtonP.setBackground(new Color(8, 61, 119));
    }//GEN-LAST:event_reportsButtonLMouseClicked

    private void customerManagementButtonLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customerManagementButtonLMouseClicked
        dashboardTabbedPane.setSelectedIndex(9);
        summaryButtonP.setBackground(new Color(8, 61, 119));
        salesButtonP.setBackground(new Color(8, 61, 119));
        expensesButtonP.setBackground(new Color(8, 61, 119));
        purchasesButtonP.setBackground(new Color(8, 61, 119));
        taxesButtonP.setBackground(new Color(8, 61, 119));
        generalJournalButtonP.setBackground(new Color(8, 61, 119));
        generalLedgerButtonP.setBackground(new Color(8, 61, 119));
        chartOfAccountsButtonP.setBackground(new Color(8, 61, 119));
        reportsButtonP.setBackground(new Color(8, 61, 119));
        vendorManagementButtonP.setBackground(new Color(8, 61, 119));
        userManagementButtonP.setBackground(new Color(8, 61, 119));
        settingsButtonP.setBackground(new Color(8, 61, 119));
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
            ArrayList<Customer> customerLists = new ArrayList();
            @Override
            protected Void doInBackground() throws Exception {
                ArrayList<Customer> customerList = new CustomerDBController().getAllCustomers();
                customerLists = customerList;
                return null;
            }
            
            @Override
            protected void done(){
                SwingUtilities.invokeLater(() -> {  
                    showCustomerList(customerLists);
                 });
            }
        };
        
        worker.execute();
    }//GEN-LAST:event_customerManagementButtonLMouseClicked

    private void vendorManagementButtonLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vendorManagementButtonLMouseClicked
        showVendorList(new VendorDBController().getAllVendors());
        dashboardTabbedPane.setSelectedIndex(10);
        summaryButtonP.setBackground(new Color(8, 61, 119));
        salesButtonP.setBackground(new Color(8, 61, 119));
        expensesButtonP.setBackground(new Color(8, 61, 119));
        purchasesButtonP.setBackground(new Color(8, 61, 119));
        taxesButtonP.setBackground(new Color(8, 61, 119));
        generalJournalButtonP.setBackground(new Color(8, 61, 119));
        generalLedgerButtonP.setBackground(new Color(8, 61, 119));
        chartOfAccountsButtonP.setBackground(new Color(8, 61, 119));
        reportsButtonP.setBackground(new Color(8, 61, 119));
        customerManagementButtonP.setBackground(new Color(8, 61, 119));
        userManagementButtonP.setBackground(new Color(8, 61, 119));
        settingsButtonP.setBackground(new Color(8, 61, 119));
    }//GEN-LAST:event_vendorManagementButtonLMouseClicked

    private void userManagementButtonLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_userManagementButtonLMouseClicked
        showUserList(new UserDBController().getAllUsers());
        dashboardTabbedPane.setSelectedIndex(11);
        summaryButtonP.setBackground(new Color(8, 61, 119));
        salesButtonP.setBackground(new Color(8, 61, 119));
        expensesButtonP.setBackground(new Color(8, 61, 119));
        purchasesButtonP.setBackground(new Color(8, 61, 119));
        taxesButtonP.setBackground(new Color(8, 61, 119));
        generalJournalButtonP.setBackground(new Color(8, 61, 119));
        generalLedgerButtonP.setBackground(new Color(8, 61, 119));
        chartOfAccountsButtonP.setBackground(new Color(8, 61, 119));
        reportsButtonP.setBackground(new Color(8, 61, 119));
        customerManagementButtonP.setBackground(new Color(8, 61, 119));
        vendorManagementButtonP.setBackground(new Color(8, 61, 119));
        settingsButtonP.setBackground(new Color(8, 61, 119));
    }//GEN-LAST:event_userManagementButtonLMouseClicked

    private void settingsButtonLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsButtonLMouseClicked
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String formattedFromTrail = dateFormat.format(fromTrail.getDate());
        String formattedToTrail = dateFormat.format(toTrail.getDate());
        showAuditTrail(new AuditTrailDBController().getAuditTrails(formattedFromTrail, formattedToTrail));
        showProductTable();
        settingsTabbedPane.setSelectedIndex(0);
        dashboardTabbedPane.setSelectedIndex(12);
        summaryButtonP.setBackground(new Color(8, 61, 119));
        salesButtonP.setBackground(new Color(8, 61, 119));
        expensesButtonP.setBackground(new Color(8, 61, 119));
        purchasesButtonP.setBackground(new Color(8, 61, 119));
        taxesButtonP.setBackground(new Color(8, 61, 119));
        generalJournalButtonP.setBackground(new Color(8, 61, 119));
        generalLedgerButtonP.setBackground(new Color(8, 61, 119));
        chartOfAccountsButtonP.setBackground(new Color(8, 61, 119));
        reportsButtonP.setBackground(new Color(8, 61, 119));
        customerManagementButtonP.setBackground(new Color(8, 61, 119));
        vendorManagementButtonP.setBackground(new Color(8, 61, 119));
        userManagementButtonP.setBackground(new Color(8, 61, 119));
    }//GEN-LAST:event_settingsButtonLMouseClicked

    private void addUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUserActionPerformed
        AddUser addUser = new AddUser(this);
        addUser.setVisible(true);
    }//GEN-LAST:event_addUserActionPerformed

    private void logoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseClicked
        int n = JOptionPane.showConfirmDialog(rootPane, "Are you sure you wanted to logout?");
        if(n == 0){
            new LoginUI().setVisible(true);
            dispose();
        }
    }//GEN-LAST:event_logoutMouseClicked

    private void updateUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateUserActionPerformed
        int selectedRow = userTable.getSelectedRow();
        if(selectedRow > -1){
            if(!String.valueOf(userTable.getValueAt(selectedRow, 5)).equalsIgnoreCase("deactivated")){
                Cryptographer cryptographer = new Cryptographer();
                User user = new UserDBController().getCredential(userTable.getValueAt(selectedRow, 0).toString());
                UpdateUser updateUser = new UpdateUser(userTable.getValueAt(selectedRow, 0).toString(), this);
                updateUser.firstName.setText(user.getFirstName());
                updateUser.lastName.setText(user.getLastName());
                updateUser.username.setText(user.getUsername());

                String decryptedPassword = "";

                try{
                    decryptedPassword = cryptographer.decrypt(user.getPassword());
                } catch (Exception e){}

                updateUser.password.setText(decryptedPassword);
                updateUser.accessLevel.setSelectedItem(user.getAccessLevel());

                updateUser.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(rootPane, "You can't update a deactivated user!");
            }  
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a row to be updated!");
        }     
    }//GEN-LAST:event_updateUserActionPerformed

    private void refreshUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshUserMouseClicked
        showUserList(new UserDBController().getAllUsers());
        userSearchField.setText("");
        userSort.setSelectedIndex(0);
    }//GEN-LAST:event_refreshUserMouseClicked

    private void deactivateUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deactivateUserActionPerformed
        int selectedRow = userTable.getSelectedRow();
        
        if(userTable.getValueAt(selectedRow, 0).toString().equalsIgnoreCase("deactivated")){
            JOptionPane.showMessageDialog(rootPane, "This user account is already deactivated!");
        } else if(selectedRow > -1){
            int n = JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to deactivate this user?");
            
            if(n == 0){
                new UserDBController().deactivate(userTable.getValueAt(selectedRow, 0).toString());
                JOptionPane.showMessageDialog(rootPane, "User has been successfully deactivated!");
                showUserList(new UserDBController().getAllUsers());
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a user to be deactivated!");
        }
    }//GEN-LAST:event_deactivateUserActionPerformed

    private void addVendorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addVendorActionPerformed
        AddVendor addVendor = new AddVendor(this, false);
        addVendor.setVisible(true);
    }//GEN-LAST:event_addVendorActionPerformed

    private void refreshVendorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshVendorMouseClicked
        showVendorList(new VendorDBController().getAllVendors());
        vendorSearchField.setText("");
        vendorSort.setSelectedIndex(0);
    }//GEN-LAST:event_refreshVendorMouseClicked

    private void updateVendorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateVendorActionPerformed
        int selectedRow = vendorTable.getSelectedRow();
        if(selectedRow > -1){
            Vendor vendor = new VendorDBController().getVendor(vendorTable.getValueAt(selectedRow, 0).toString());

            UpdateVendor updateVendor = new UpdateVendor(vendorTable.getValueAt(selectedRow, 0).toString(), this);
            updateVendor.name.setText(vendor.getName());
            updateVendor.phoneNumber.setText(vendor.getPhoneNumber());
            updateVendor.email.setText(vendor.getEmail());
            updateVendor.address.setText(vendor.getAddress());

            updateVendor.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a row to be updated!");
        }
    }//GEN-LAST:event_updateVendorActionPerformed

    private void addCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCustomerActionPerformed
       AddCustomer addCustomer = new AddCustomer(this, false);
       addCustomer.setVisible(true);
    }//GEN-LAST:event_addCustomerActionPerformed

    private void refreshCustomerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshCustomerMouseClicked
        showCustomerList(new CustomerDBController().getAllCustomers());
        customerSearchField.setText("");
        customerSort.setSelectedIndex(0);
    }//GEN-LAST:event_refreshCustomerMouseClicked

    private void updateCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateCustomerActionPerformed
        int selectedRow = customerTable.getSelectedRow();
        if(selectedRow > -1){
            Customer customer = new CustomerDBController().getCustomer(customerTable.getValueAt(selectedRow, 0).toString());
            
            UpdateCustomer updateCustomer = new UpdateCustomer(customerTable.getValueAt(selectedRow, 0).toString(), this);
            updateCustomer.firstName.setText(customer.getFirstName());
            updateCustomer.lastName.setText(customer.getLastName());
            updateCustomer.phoneNumber.setText(customer.getPhoneNumber());
            updateCustomer.organization.setText(customer.getOrganization());
            updateCustomer.position.setText(customer.getPosition());
            updateCustomer.address.setText(customer.getAddress());
            
            updateCustomer.setVisible(true);
        } else {
           JOptionPane.showMessageDialog(rootPane, "Please select a row to be updated!"); 
        }
    }//GEN-LAST:event_updateCustomerActionPerformed

    private void addAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAccountActionPerformed
        AddAccount addAccount = new AddAccount(this);
        addAccount.setVisible(true);
    }//GEN-LAST:event_addAccountActionPerformed

    private void refreshAccountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshAccountMouseClicked
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate dateToday = LocalDate.now();
        showAccountList(new AccountDBController().getAllAccounts(getAccountingConfig(true, false, false, false), dateToday.format(dateFormat)));
        accountSearchField.setText("");
        accountSort.setSelectedIndex(0);
    }//GEN-LAST:event_refreshAccountMouseClicked

    private void updateAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateAccountActionPerformed
        int selectedRow = accountTable.getSelectedRow();
        if(selectedRow > -1){
            Account account = new AccountDBController().getAccount( 
                    Integer.parseInt(accountTable.getValueAt(selectedRow, 0).toString()));
            
            UpdateAccount updateAccount = new UpdateAccount(this);
            updateAccount.code.setText(accountTable.getValueAt(selectedRow, 0).toString());
            updateAccount.accountName.setText(account.getAccountName());
            updateAccount.accountType.setSelectedIndex(account.getAccountTypeID());
            
            updateAccount.setVisible(true);
        } else {
           JOptionPane.showMessageDialog(rootPane, "Please select a row to be updated!"); 
        }
    }//GEN-LAST:event_updateAccountActionPerformed

    private void addEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEntryActionPerformed
        Date date = new Date();  
        AddEntry addEntry = new AddEntry(currentUser, this);
        addEntry.date.setDate(date);
        addEntry.encoder.setText(currentUser.getName());
        addEntry.setVisible(true);
    }//GEN-LAST:event_addEntryActionPerformed

    private void refreshJournalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshJournalMouseClicked
        showJournalEntryList(new EntryDBController().getAllJournalEntry());
        journalSearchField.setText("");
        journalSort.setSelectedIndex(0);
    }//GEN-LAST:event_refreshJournalMouseClicked

    private void viewEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewEntryActionPerformed
        int selectedRow = journalTable.getSelectedRow();
        if(selectedRow > -1){
            ViewEntry viewEntry = new ViewEntry(new EntryDBController().getAccountItems(
                    journalTable.getValueAt(selectedRow, 0).toString()), journalTable.getValueAt(selectedRow, 0).toString());
            viewEntry.encoder.setText(journalTable.getValueAt(selectedRow, 3).toString());
            viewEntry.date.setText(journalTable.getValueAt(selectedRow, 1).toString());
            viewEntry.description.setText(journalTable.getValueAt(selectedRow, 2).toString());
            viewEntry.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a row to be viewed!");
        }       
    }//GEN-LAST:event_viewEntryActionPerformed

    private void addSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSalesActionPerformed
        Date date = new Date();      
        AddSales addSales = new AddSales(currentUser, this, products);
        addSales.date.setDate(date);
        addSales.username.setText(currentUser.getName());
        addSales.setVisible(true);
    }//GEN-LAST:event_addSalesActionPerformed

    private void refreshSalesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshSalesMouseClicked
        showSalesList(new EntryDBController().getAllSales());
        salesSearchField.setText("");
        salesSort.setSelectedIndex(0);
    }//GEN-LAST:event_refreshSalesMouseClicked

    private void viewSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewSalesActionPerformed
        int selectedRow = salesTable.getSelectedRow();
        if(selectedRow > -1){
            ViewSales viewSales = new ViewSales(new EntryDBController().getSaleTransaction(
                    salesTable.getValueAt(selectedRow, 0).toString()), this, this.currentUser);
            viewSales.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a row to be viewed!");
        }
    }//GEN-LAST:event_viewSalesActionPerformed

    private void addExpensesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addExpensesActionPerformed
        Date date = new Date();  
        AddExpense addExpenses = new AddExpense(currentUser, this);
        addExpenses.date.setDate(date);
        addExpenses.encoder.setText(currentUser.getName());
        addExpenses.setVisible(true);
    }//GEN-LAST:event_addExpensesActionPerformed

    private void refreshExpensesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshExpensesMouseClicked
        showExpensesList(new EntryDBController().getAllExpensesEntry());
        expensesSearchField.setText("");
        expensesSort.setSelectedIndex(0);
    }//GEN-LAST:event_refreshExpensesMouseClicked

    private void viewExpensesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewExpensesActionPerformed
        int selectedRow = expensesTable.getSelectedRow();
        if(selectedRow > -1){
            ViewEntry viewEntry = new ViewEntry(new EntryDBController().getAccountItems(
                    expensesTable.getValueAt(selectedRow, 0).toString()), expensesTable.getValueAt(selectedRow, 0).toString());
            viewEntry.encoder.setText(expensesTable.getValueAt(selectedRow, 3).toString());
            viewEntry.date.setText(expensesTable.getValueAt(selectedRow, 1).toString());
            viewEntry.description.setText(expensesTable.getValueAt(selectedRow, 2).toString());
            viewEntry.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a row to be viewed!");
        }
    }//GEN-LAST:event_viewExpensesActionPerformed

    private void addPurchasesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPurchasesActionPerformed
        Date date = new Date();  
        AddPurchases addPurchases = new AddPurchases(this, currentUser);
        addPurchases.date.setDate(date);
        addPurchases.setVisible(true);
    }//GEN-LAST:event_addPurchasesActionPerformed

    private void viewPurchasesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewPurchasesActionPerformed
        int selectedRow = purchasesTable.getSelectedRow();
        if(selectedRow > -1){
            ViewPurchases viewPurchases = new ViewPurchases(new EntryDBController().getPurchaseTransaction(
                    String.valueOf(purchasesTable.getValueAt(selectedRow, 0))), this, this.currentUser);
            viewPurchases.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a row to be viewed!");
        }
    }//GEN-LAST:event_viewPurchasesActionPerformed

    private void ledgerAccountSelectionBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ledgerAccountSelectionBoxActionPerformed
        ArrayList<Account> accountList = new AccountDBController().getAllAccounts();
        Account account = accountList.get(ledgerAccountSelectionBox.getSelectedIndex());

        if(fromLedger.getDate() != null && toLedger.getDate() != null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            showLedger(account.getCode(), account.getNormally(),
                    dateFormat.format(fromLedger.getDate()) , dateFormat.format(toLedger.getDate()) );
        }    
    }//GEN-LAST:event_ledgerAccountSelectionBoxActionPerformed

    private void refreshLedgerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshLedgerMouseClicked
        Date date = new Date();
        fromLedger.setDate(date);
        toLedger.setDate(date);
        initLedger();
    }//GEN-LAST:event_refreshLedgerMouseClicked

    private void filterLedgerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_filterLedgerMouseClicked
        ArrayList<Account> accountList = new AccountDBController().getAllAccounts();
        Account account = accountList.get(ledgerAccountSelectionBox.getSelectedIndex());
         
        if(fromLedger.getDate() != null && toLedger.getDate() != null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

            showLedger(account.getCode(), account.getNormally(),
                    dateFormat.format(fromLedger.getDate()) , dateFormat.format(toLedger.getDate()) );
        }               
    }//GEN-LAST:event_filterLedgerMouseClicked

    private void calculateGrossSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculateGrossSalesActionPerformed
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        grossSales1.setText(setDecimalFormat(new EntryDBController().getTotalGrossIncome(
                dateFormat.format(fromTaxes.getDate()), dateFormat.format(toTaxes.getDate()))));
        grossSales2.setText(setDecimalFormat(new EntryDBController().getTotalGrossIncome(
                dateFormat.format(fromTaxes.getDate()), dateFormat.format(toTaxes.getDate()))));
    }//GEN-LAST:event_calculateGrossSalesActionPerformed

    private void userSortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userSortActionPerformed
        int selectedIndex = userSort.getSelectedIndex();
        
        switch(selectedIndex){
            case 1:
                showUserList(new UserDBController().sortUsers("uid"));
                break;
            case 2:
                showUserList(new UserDBController().sortUsers("firstname"));
                break;
            case 3:
                showUserList(new UserDBController().sortUsers("lastname"));
                break;
            case 4:
                showUserList(new UserDBController().sortUsers("username"));
                break;    
            case 5:
                showUserList(new UserDBController().sortUsers("accesslevel"));
                break;
            case 6:
                showUserList(new UserDBController().sortUsers("status"));
                break;    
        }
    }//GEN-LAST:event_userSortActionPerformed

    private void searchUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchUserMouseClicked
        if(!userSearchField.getText().isBlank()){
            showUserList(new UserDBController().searchUser(new Dashboard().stringEscape(userSearchField.getText())));
        } else {
            showUserList(new UserDBController().getAllUsers());
        }
    }//GEN-LAST:event_searchUserMouseClicked

    private void vendorSortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vendorSortActionPerformed
        int selectedIndex = vendorSort.getSelectedIndex();
        
        switch(selectedIndex){
            case 1:
                showVendorList(new VendorDBController().sortVendor("vid"));
                break;
            case 2:
                showVendorList(new VendorDBController().sortVendor("name"));
                break;
            case 3:
                showVendorList(new VendorDBController().sortVendor("email"));
                break;
            case 4:
                showVendorList(new VendorDBController().sortVendor("address"));
                break;                  
        }
    }//GEN-LAST:event_vendorSortActionPerformed

    private void searchVendorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchVendorMouseClicked
        if(!vendorSearchField.getText().isBlank()){
            showVendorList(new VendorDBController().searchVendor(new Dashboard().stringEscape(vendorSearchField.getText())));
        } else {
            showVendorList(new VendorDBController().getAllVendors());
        }
    }//GEN-LAST:event_searchVendorMouseClicked

    private void customerSortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerSortActionPerformed
        int selectedIndex = customerSort.getSelectedIndex();
        
        switch(selectedIndex){
            case 1:
                showCustomerList(new CustomerDBController().sortCustomer("cid"));
                break;
            case 2:
                showCustomerList(new CustomerDBController().sortCustomer("firstname"));
                break;
            case 3:
                showCustomerList(new CustomerDBController().sortCustomer("lastname"));
                break;          
        }
    }//GEN-LAST:event_customerSortActionPerformed

    private void searchCustomerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchCustomerMouseClicked
        if(!customerSearchField.getText().isBlank()){
            showCustomerList(new CustomerDBController().searchCustomer(new Dashboard().stringEscape(customerSearchField.getText())));
        } else {
            showCustomerList(new CustomerDBController().getAllCustomers());
        }
    }//GEN-LAST:event_searchCustomerMouseClicked

    private void searchAccountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchAccountMouseClicked
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate dateToday = LocalDate.now();
        
        if(!accountSearchField.getText().isBlank()){
            showAccountList(new AccountDBController().searchAccount(new Dashboard().stringEscape(accountSearchField.getText()), getAccountingConfig(true, false, false, false), dateToday.format(dateFormat)));
        } else {
            showAccountList(new AccountDBController().getAllAccounts(getAccountingConfig(true, false, false, false), dateToday.format(dateFormat)));
        }
    }//GEN-LAST:event_searchAccountMouseClicked

    private void accountSortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accountSortActionPerformed
        int selectedIndex = accountSort.getSelectedIndex();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate dateToday = LocalDate.now();
        
        switch(selectedIndex){
            case 1:
                showAccountList(new AccountDBController().sortAccount("account.code", 
                        getAccountingConfig(true, false, false, false), 
                        dateToday.format(dateFormat)));
                break;
            case 2:
                showAccountList(new AccountDBController().sortAccount("account.accountname", 
                        getAccountingConfig(true, false, false, false), 
                        dateToday.format(dateFormat)));
                break;
            case 3:
                showAccountList(new AccountDBController().sortAccount("accounttype.type", 
                        getAccountingConfig(true, false, false, false), 
                        dateToday.format(dateFormat)));
                break; 
            case 4:
                showAccountList(new AccountDBController().sortAccount("accounttype.normally", 
                        getAccountingConfig(true, false, false, false), 
                        dateToday.format(dateFormat)));
                break;      
        }
    }//GEN-LAST:event_accountSortActionPerformed

    private void journalSortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_journalSortActionPerformed
        int selectedIndex = journalSort.getSelectedIndex();
        
        switch(selectedIndex){
            case 1:
                showJournalEntryList(new EntryDBController().sortJournalEntry("journalentry.jid"));
                break;
            case 2:
                showJournalEntryList(new EntryDBController().sortJournalEntry("journalentry.date"));
                break;
            case 3:
                showJournalEntryList(new EntryDBController().sortJournalEntry("journalentry.description"));
                break; 
            case 4:
                showJournalEntryList(new EntryDBController().sortJournalEntry("user.firstname"));
                break;      
        }
    }//GEN-LAST:event_journalSortActionPerformed

    private void searchJournalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchJournalMouseClicked
        if(!journalSearchField.getText().isBlank()){
            showJournalEntryList(new EntryDBController().searchJournalEntry(new Dashboard().stringEscape(journalSearchField.getText())));
        } else {
            showJournalEntryList(new EntryDBController().getAllJournalEntry());
        }
    }//GEN-LAST:event_searchJournalMouseClicked

    private void refreshPurchasesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshPurchasesMouseClicked
       showPurchasesList(new EntryDBController().getAllPurchases());
       purchasesSearchField.setText("");
       purchasesSort.setSelectedIndex(0);
    }//GEN-LAST:event_refreshPurchasesMouseClicked

    private void searchPurchasesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchPurchasesMouseClicked
        if(!purchasesSearchField.getText().isBlank()){
            showPurchasesList(new EntryDBController().searchPurchases(new Dashboard().stringEscape(purchasesSearchField.getText())));
        } else {
            showPurchasesList(new EntryDBController().getAllPurchases());
        }                                
    }//GEN-LAST:event_searchPurchasesMouseClicked

    private void purchasesSortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchasesSortActionPerformed
        int selectedIndex = purchasesSort.getSelectedIndex();
        
        switch(selectedIndex){
            case 1:
                showPurchasesList(new EntryDBController().sortPurchases("pid"));
                break;
            case 2:
                showPurchasesList(new EntryDBController().sortPurchases("date"));
                break;
            case 3:
                showPurchasesList(new EntryDBController().sortPurchases("vendor.name"));
                break; 
            case 4:
                showPurchasesList(new EntryDBController().sortPurchases("account.accountname"));
                break;      
        }
    }//GEN-LAST:event_purchasesSortActionPerformed

    private void expensesSortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expensesSortActionPerformed
        int selectedIndex = expensesSort.getSelectedIndex();
        
        switch(selectedIndex){
            case 1:
                showExpensesList(new EntryDBController().sortExpensesEntry("expensestransaction.eid"));
                break;
            case 2:
                showExpensesList(new EntryDBController().sortExpensesEntry("expensestransaction.date"));
                break;
            case 3:
                showExpensesList(new EntryDBController().sortExpensesEntry("expensestransaction.description"));
                break; 
            case 4:
                showExpensesList(new EntryDBController().sortExpensesEntry("user.firstname"));
                break;           
        }
    }//GEN-LAST:event_expensesSortActionPerformed

    private void searchExpensesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchExpensesMouseClicked
        if(!expensesSearchField.getText().isBlank()){
            showExpensesList(new EntryDBController().searchExpensesEntry(new Dashboard().stringEscape(expensesSearchField.getText())));
        } else {
            showExpensesList(new EntryDBController().getAllExpensesEntry());
        }
    }//GEN-LAST:event_searchExpensesMouseClicked

    private void searchSalesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchSalesMouseClicked
        if(!salesSearchField.getText().isBlank()){
            showSalesList(new EntryDBController().searchSales(new Dashboard().stringEscape(salesSearchField.getText())));
        } else {
            showSalesList(new EntryDBController().getAllSales());
        }
    }//GEN-LAST:event_searchSalesMouseClicked

    private void salesSortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salesSortActionPerformed
        int selectedIndex = salesSort.getSelectedIndex();
        
        switch(selectedIndex){
            case 1:
                showSalesList(new EntryDBController().sortSales("sid", "sid"));
                break;
            case 2:
                showSalesList(new EntryDBController().sortSales("date", "date"));
                break;
            case 3:
                showSalesList(new EntryDBController().sortSales("customer.firstname", "customer"));
                break;                  
        }
    }//GEN-LAST:event_salesSortActionPerformed

    private void calculateTaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculateTaxActionPerformed
        Double grossSales = reverseDecimalFormat(grossSales2.getText());
        Double taxPercentage = reverseDecimalFormat(taxRate.getText());
        Double taxToPay = grossSales * taxPercentage;
        taxResult.setText(setDecimalFormat(taxToPay));   
    }//GEN-LAST:event_calculateTaxActionPerformed

    private void revenueForActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_revenueForActionPerformed
        setSummaryRevenue();
    }//GEN-LAST:event_revenueForActionPerformed

    private void expensesForActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expensesForActionPerformed
        setSummaryExpenses();
    }//GEN-LAST:event_expensesForActionPerformed

    private void reportSheetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportSheetActionPerformed
        int selectedSheet = reportSheet.getSelectedIndex();
        ReportDate reportDate = new ReportDate(this, selectedSheet);
        
        switch(selectedSheet){
            case 0 :
                showBalanceSheetReport();
                break;
                
            case 1 :
                reportDate.setVisible(true);
                break;  
                
            case 2 :
                showTrialBalanceReport();
                break; 
                
            case 3 :
                LedgerAccountChooser ledgerAccountChooser = new LedgerAccountChooser(this);
                ledgerAccountChooser.setVisible(true);
                break;     
        }   
    }//GEN-LAST:event_reportSheetActionPerformed

    private void printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printActionPerformed
        ReportController report = new ReportController();
        int selectedSheet = reportSheet.getSelectedIndex();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate dateToday = LocalDate.now();
        
        switch(selectedSheet){
            case 0 :
                report.printBalanceSheet(getAccountingConfig(
                        true, false,false, false), dateToday.format(dateFormat));
                break;
            
            case 1 :
                report.printIncomeStatement(tempFromRangeDate, tempToRangeDate);
                break; 
            
            case 2 :
                report.printTrialBalance(getAccountingConfig(
                        true, false,false, false), dateToday.format(dateFormat));
                break;
                
            case 3 :
                report.printAccountTransactions(tempFromRangeDate, tempToRangeDate, account, getAccountingConfig(
                        true, false,false, false));      
                break;
        }
    }//GEN-LAST:event_printActionPerformed

    private void filterJournalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_filterJournalMouseClicked
        if(fromJournal.getDate() != null && toJournal.getDate() != null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

            showJournalEntryList(new EntryDBController().getAllJournalEntry(
                    dateFormat.format(fromJournal.getDate()), dateFormat.format(toJournal.getDate())));
        }
    }//GEN-LAST:event_filterJournalMouseClicked

    private void filterSalesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_filterSalesMouseClicked
        if(fromSales.getDate() != null && toSales.getDate() != null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

            showSalesList(new EntryDBController().getAllSales(
                    dateFormat.format(fromSales.getDate()), dateFormat.format(toSales.getDate())));
        }
    }//GEN-LAST:event_filterSalesMouseClicked

    private void filterExpensesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_filterExpensesMouseClicked
        if(fromExpenses.getDate() != null && toExpenses.getDate() != null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

            showExpensesList(new EntryDBController().getAllExpensesEntry(
                    dateFormat.format(fromExpenses.getDate()), dateFormat.format(toExpenses.getDate())));
        }
    }//GEN-LAST:event_filterExpensesMouseClicked

    private void filterPurchasesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_filterPurchasesMouseClicked
        if(fromPurchases.getDate() != null && toPurchases.getDate() != null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

            showPurchasesList(new EntryDBController().getAllPurchases(
                    dateFormat.format(fromPurchases.getDate()), dateFormat.format(toPurchases.getDate())));
        }
    }//GEN-LAST:event_filterPurchasesMouseClicked

    private void deleteSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteSalesActionPerformed
        int selectedRow = salesTable.getSelectedRow();
        
        if(selectedRow > -1){
            int n = JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to delete this transaction?");
            
            if(n == 0){
                new EntryDBController().deleteSales(salesTable.getValueAt(selectedRow, 0).toString(), currentUser.getId());   
                updateSalesTable();           
            }          
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a row to be deleted!");          
        }
    }//GEN-LAST:event_deleteSalesActionPerformed

    private void deleteExpensesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteExpensesActionPerformed
        int selectedRow = expensesTable.getSelectedRow();
        
        if(selectedRow > -1){
            int n = JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to delete this transaction?");
            
            if(n == 0){
                new EntryDBController().deleteExpensesEntry(expensesTable.getValueAt(selectedRow, 0).toString(), currentUser.getId());   
                updateExpensesTable();           
            }          
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a row to be deleted!");           
        }
    }//GEN-LAST:event_deleteExpensesActionPerformed

    private void deletePurchasesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletePurchasesActionPerformed
        int selectedRow = purchasesTable.getSelectedRow();
        
        if(selectedRow > -1){
            int n = JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to delete this transaction?");
            
            if(n == 0){
                new EntryDBController().deletePurchases(purchasesTable.getValueAt(selectedRow, 0).toString(), currentUser.getId());   
                updatePurchasesTable();           
            }          
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a row to be deleted!");            
        }
    }//GEN-LAST:event_deletePurchasesActionPerformed

    private void deleteEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteEntryActionPerformed
        int selectedRow = journalTable.getSelectedRow();
        
        if(selectedRow > -1){
            int n = JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to delete this transaction?");
            
            if(n == 0){
                new EntryDBController().deleteEntry(journalTable.getValueAt(selectedRow, 0).toString(), currentUser.getId());   
                updateJournalTable();           
            }          
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a row to be deleted!");     
        }
    }//GEN-LAST:event_deleteEntryActionPerformed

    private void changeTaxRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeTaxRateActionPerformed
        if(changeTaxRate.getText().equalsIgnoreCase("Change")){
            AdministratorPrompt adminPrompt = new AdministratorPrompt(this);
            adminPrompt.setVisible(true);
        } else if (changeTaxRate.getText().equalsIgnoreCase("Save")) {
            int n = JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to save these changes? any changes that has been made may affect the tax calculations. Are you sure you want to continue?");

            if(n == 0){
                accountingConfiguration();
                changeTaxRate.setText("Change");
                taxRateConfig.setEditable(false);
                taxRate.setText(taxRateConfig.getText());
            } else {
                changeTaxRate.setText("Change");
                taxRateConfig.setEditable(false);
            }
        }
    }//GEN-LAST:event_changeTaxRateActionPerformed

    private void setOpeningBalanceDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setOpeningBalanceDateActionPerformed
        int n = JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to save this changes?");
        if(n == 0){
            accountingConfiguration();
        } else {
            try(InputStream input = new FileInputStream("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Project\\BMS\\src\\bms\\path\\to\\accconfig.properties")){
                Properties network = new Properties();
                network.load(input);
                Date date = new SimpleDateFormat("MM/dd/yyyy").parse(network.getProperty("opening_balance_date"));
                openingBalanceDate.setDate(date);
            } catch (Exception io){
                io.printStackTrace();
            }
        }
    }//GEN-LAST:event_setOpeningBalanceDateActionPerformed

    private void recentTransactionsRangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recentTransactionsRangeActionPerformed
        accountingConfiguration();
        showRecentTransactionsList();
    }//GEN-LAST:event_recentTransactionsRangeActionPerformed

    private void profitAndLossRangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profitAndLossRangeActionPerformed
        accountingConfiguration();
        setSummaryProfitAndLoss();
    }//GEN-LAST:event_profitAndLossRangeActionPerformed

    private void refreshAuditTrailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshAuditTrailMouseClicked
        if(fromTrail.getDate() != null && toTrail.getDate() != null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String formattedFromTrail = dateFormat.format(fromTrail.getDate());
            String formattedToTrail = dateFormat.format(toTrail.getDate());
            showAuditTrail(new AuditTrailDBController().getAuditTrails(formattedFromTrail, formattedToTrail));
        }
    }//GEN-LAST:event_refreshAuditTrailMouseClicked

    private void filterAuditTrailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_filterAuditTrailMouseClicked
        if(fromTrail.getDate() != null && toTrail.getDate() != null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String formattedFromTrail = dateFormat.format(fromTrail.getDate());
            String formattedToTrail = dateFormat.format(toTrail.getDate());
            showAuditTrail(new AuditTrailDBController().getAuditTrails(formattedFromTrail, formattedToTrail));
        }
    }//GEN-LAST:event_filterAuditTrailMouseClicked

    private void cancelNetworkChangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelNetworkChangesActionPerformed
        initNetworkSettings();
    }//GEN-LAST:event_cancelNetworkChangesActionPerformed

    private void saveNetworkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveNetworkActionPerformed
        int n = JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to save this changes?");

        if(n == 0) {
            try(OutputStream output = new FileOutputStream("src\\bms\\path\\to\\config.properties")){
                Properties network = new Properties();

                Cryptographer cryptographer = new Cryptographer();

                String encryptedPassword = "";

                try {
                    encryptedPassword = cryptographer.encrypt(networkPassword.getText());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(rootPane, e.getMessage());
                    e.printStackTrace();
                }

                network.setProperty("username", networkUsername.getText());
                network.setProperty("password", encryptedPassword);
                network.setProperty("server", networkServer.getText());
                network.setProperty("port", networkPort.getText());
                network.setProperty("database", networkDatabase.getText());

                network.store(output, null);

                saveNetwork.setEnabled(false);
            } catch (IOException io){
                io.printStackTrace();
            }
        } else {
            try(InputStream input = new FileInputStream("src\\bms\\path\\to\\config.properties")){
                Properties network = new Properties();
                network.load(input);

                Cryptographer cryptographer = new Cryptographer();

                String decryptedPassword = "";

                try {
                    decryptedPassword = cryptographer.decrypt(network.getProperty("password"));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(rootPane, e.getMessage());
                    e.printStackTrace();
                }

                networkUsername.setText(network.getProperty("username"));
                networkPassword.setText(decryptedPassword);
                networkServer.setText(network.getProperty("server"));
                networkPort.setText(network.getProperty("port"));
                networkDatabase.setText(network.getProperty("database"));
            } catch (IOException io){
                io.printStackTrace();
            }

            saveNetwork.setEnabled(false);
        }
    }//GEN-LAST:event_saveNetworkActionPerformed

    private void editNetworkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editNetworkActionPerformed
        saveNetwork.setEnabled(true);
        networkUsername.enableInputMethods(true);
        networkPassword.enableInputMethods(true);
        networkServer.enableInputMethods(true);
        networkPort.enableInputMethods(true);
        networkDatabase.enableInputMethods(true);
    }//GEN-LAST:event_editNetworkActionPerformed

    private void setSalesPaymentMethodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setSalesPaymentMethodActionPerformed
        SetPaymentMethod setPaymentMethod = new SetPaymentMethod(this, getSalesPaymentAccounts(), true);
        setPaymentMethod.setVisible(true);
    }//GEN-LAST:event_setSalesPaymentMethodActionPerformed

    private void setSalesRemainingBalanceAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setSalesRemainingBalanceAccountActionPerformed
        if(dashboardTabbedPane.getSelectedIndex() == 12){salesConfig();}       
    }//GEN-LAST:event_setSalesRemainingBalanceAccountActionPerformed

    private void setSalesRevenueAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setSalesRevenueAccountActionPerformed
        if(dashboardTabbedPane.getSelectedIndex() == 12){salesConfig();}
    }//GEN-LAST:event_setSalesRevenueAccountActionPerformed

    private void setPurchasesPaymentMethodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setPurchasesPaymentMethodActionPerformed
        SetPaymentMethod setPaymentMethod = new SetPaymentMethod(this, getPurchasesPaymentAccounts(), false);
        setPaymentMethod.setVisible(true);
    }//GEN-LAST:event_setPurchasesPaymentMethodActionPerformed

    private void setPurchasesRemainingBalanceAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setPurchasesRemainingBalanceAccountActionPerformed
        if(dashboardTabbedPane.getSelectedIndex() == 12){purchasesConfig();}
    }//GEN-LAST:event_setPurchasesRemainingBalanceAccountActionPerformed

    private void searchAuditTrailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchAuditTrailMouseClicked
        if(!auditTrailSearchField.getText().isBlank()){
            showAuditTrail(new AuditTrailDBController().searchAuditTrail(new Dashboard().stringEscape(auditTrailSearchField.getText())));
        } else {
            showAuditTrail(new AuditTrailDBController().getAuditTrails());
        }
    }//GEN-LAST:event_searchAuditTrailMouseClicked

    private void addProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addProductActionPerformed
        AddProduct addProduct = new AddProduct(this);
        addProduct.setVisible(true);
    }//GEN-LAST:event_addProductActionPerformed

    private void editProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editProductActionPerformed
        int selectedRow = productTable.getSelectedRow();
        if(selectedRow >= 0){
            UpdateProduct updateProduct = new UpdateProduct(this, products.get(selectedRow).getId());
            updateProduct.productName.setText(products.get(selectedRow).getName());
            updateProduct.quantity.setText(products.get(selectedRow).getQuantity());
            updateProduct.description.setText(products.get(selectedRow).getDescription());
            updateProduct.price.setText(String.valueOf(products.get(selectedRow).getPrice()));
            updateProduct.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select an item to edit!");
        }
    }//GEN-LAST:event_editProductActionPerformed

    private void removeProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeProductActionPerformed
        int selectedRow = productTable.getSelectedRow();
        if(selectedRow >= 0){
            int n = JOptionPane.showConfirmDialog(rootPane, "Are you sure you wanted to remove this item?");
            if(n == 0){
                new ProductDBController().delete(products.get(selectedRow).getId());
                updateProductTable();
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select an item to be removed!");
        }
    }//GEN-LAST:event_removeProductActionPerformed

    private void removeAllProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAllProductActionPerformed
        int n = JOptionPane.showConfirmDialog(rootPane, "Are you sure you wanted to remove all of the items? This cannot be undone.");
        if(n == 0){
            new ProductDBController().deleteAll();
            updateProductTable();
        }
    }//GEN-LAST:event_removeAllProductActionPerformed

    private void editEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editEntryActionPerformed
        int selectedRow = journalTable.getSelectedRow();
        if(selectedRow > -1){
            String id = journalTable.getValueAt(selectedRow, 0).toString();
            UpdateEntry updateEntry = new UpdateEntry(currentUser,  this, new EntryDBController().getAccountItems(id), id);
            updateEntry.encoder.setText(journalTable.getValueAt(selectedRow, 3).toString());
            updateEntry.date.setDate(stringToDate(journalTable.getValueAt(selectedRow, 1).toString()));
            updateEntry.description.setText(journalTable.getValueAt(selectedRow, 2).toString());
            updateEntry.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a row to be edited!");
        } 
    }//GEN-LAST:event_editEntryActionPerformed

    private void editExpensesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editExpensesActionPerformed
        int selectedRow = expensesTable.getSelectedRow();
        if(selectedRow > -1){
            String id = expensesTable.getValueAt(selectedRow, 0).toString();
            UpdateExpense updateExpense = new UpdateExpense(currentUser,  this, new EntryDBController().getAccountItems(id), id);
            updateExpense.encoder.setText(expensesTable.getValueAt(selectedRow, 3).toString());
            updateExpense.date.setDate(stringToDate(expensesTable.getValueAt(selectedRow, 1).toString()));
            updateExpense.description.setText(expensesTable.getValueAt(selectedRow, 2).toString());
            updateExpense.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a row to be edited!");
        } 
    }//GEN-LAST:event_editExpensesActionPerformed

    private void deleteAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteAccountActionPerformed
        int selectedRow = accountTable.getSelectedRow();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate dateToday = LocalDate.now();
        if(selectedRow > -1){
            int transactionCount = new AccountDBController().countRelatedTransactions(String.valueOf(accountTable.getValueAt(selectedRow, 0)));
            String message = "Are you sure you wanted to delete " + String.valueOf(accountTable.getValueAt(selectedRow, 1)) + " account? \n"
                    + "Removing this will also remove " + transactionCount + " related transactions on this account and this can not be undone.";
            int n = JOptionPane.showConfirmDialog(rootPane, message);
            
            if(n == 0){
                 new AccountDBController().delete(Integer.parseInt(String.valueOf(accountTable.getValueAt(selectedRow, 0))), 
                         String.valueOf(accountTable.getValueAt(selectedRow, 1)), currentUser.getId(), 
                         new AccountDBController().getAccountBalance(Integer.parseInt(String.valueOf(accountTable.getValueAt(selectedRow, 0))), 
                                 getAccountingConfig(true, false, false, false), dateToday.format(dateFormat)));
                 JOptionPane.showMessageDialog(rootPane, "Account was successfully deleted!");
                 updateChartOfAccountTable();
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select account to be deleted!");
        } 
    }//GEN-LAST:event_deleteAccountActionPerformed

    private void deleteCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteCustomerActionPerformed
        int selectedRow = customerTable.getSelectedRow();
        if(selectedRow >= 0){
            int n = JOptionPane.showConfirmDialog(rootPane, "Are you sure you wanted to delete this customer?\n"
                    + "Deleting this customer information will also remove any related transactions and this can not be undone.");
            if(n == 0){
                new CustomerDBController().delete(String.valueOf(customerTable.getValueAt(selectedRow, 0)), currentUser.getId());
                JOptionPane.showMessageDialog(rootPane, "Customer was successfully deleted!");
                updateCustomerTable();
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a customer to be deleted!");
        }
    }//GEN-LAST:event_deleteCustomerActionPerformed

    private void deleteVendorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteVendorActionPerformed
        int selectedRow = vendorTable.getSelectedRow();
        if(selectedRow >= 0){
            int n = JOptionPane.showConfirmDialog(rootPane, "Are you sure you wanted to delete this supplier?\n"
                    + "Deleting this supplier information will also remove any related transactions and this can not be undone.");
            if(n == 0){
                new VendorDBController().delete(String.valueOf(vendorTable.getValueAt(selectedRow, 0)), currentUser.getId());
                JOptionPane.showMessageDialog(rootPane, "Supplier was successfully deleted!");
                updateVendorTable();
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a supplier to be deleted!");
        }
    }//GEN-LAST:event_deleteVendorActionPerformed

    private void viewTransactionSummaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewTransactionSummaryActionPerformed
        int selectedRow = ledgerTable.getSelectedRow();
        
        if(selectedRow > -1){
            String id = ledgerTable.getValueAt(selectedRow, 0).toString();
            
            if(id.contains("CSID") || id.contains("GSID")){
                ViewSales viewSales = new ViewSales(new EntryDBController().getSaleTransaction(id), 
                        this, this.currentUser);
                viewSales.setVisible(true);
            } else if (id.contains("EID")){
                ExpensesTransaction expensesEntry = new EntryDBController().getExpensesTransaction(id);
                
                ViewEntry viewEntry = new ViewEntry(expensesEntry.getAccountList(), expensesEntry.getId());
                viewEntry.encoder.setText(expensesEntry.getEncoder());
                viewEntry.date.setText(expensesEntry.getDate());
                viewEntry.description.setText(expensesEntry.getDescription());
                viewEntry.setVisible(true);
            } else if (id.contains("PID")){
                ViewPurchases viewPurchases = new ViewPurchases(new EntryDBController().getPurchaseTransaction(id), 
                        this, this.currentUser);
                viewPurchases.setVisible(true);
            } else if (id.contains("JID")){
                JournalEntry journalEntry = new EntryDBController().getJournalEntry(id);
                
                ViewEntry viewEntry = new ViewEntry(journalEntry.getAccountList(), journalEntry.getId());
                viewEntry.encoder.setText(journalEntry.getEncoder());
                viewEntry.date.setText(journalEntry.getDate());
                viewEntry.description.setText(journalEntry.getDescription());
                viewEntry.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a row to be viewed!");
        }        
    }//GEN-LAST:event_viewTransactionSummaryActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField accountSearchField;
    private javax.swing.JComboBox<String> accountSort;
    private javax.swing.JTable accountTable;
    private javax.swing.JButton addAccount;
    private javax.swing.JButton addCustomer;
    private javax.swing.JButton addEntry;
    private javax.swing.JButton addExpenses;
    private javax.swing.JButton addProduct;
    private javax.swing.JButton addPurchases;
    private javax.swing.JButton addSales;
    private javax.swing.JButton addUser;
    private javax.swing.JButton addVendor;
    private javax.swing.JLabel aimsLogo;
    private javax.swing.JTextField auditTrailSearchField;
    private javax.swing.JTable auditTrailTable;
    private javax.swing.JLabel bmsLogo;
    private javax.swing.JButton calculateGrossSales;
    private javax.swing.JButton calculateTax;
    private javax.swing.JButton cancelNetworkChanges;
    private javax.swing.JButton changeTaxRate;
    private javax.swing.JLabel chartOfAccountIcon;
    private javax.swing.JLabel chartOfAccountsButtonL;
    private javax.swing.JPanel chartOfAccountsButtonP;
    private javax.swing.JPanel chartOfAccountsTab;
    private javax.swing.JLabel customerManagementButtonL;
    private javax.swing.JPanel customerManagementButtonP;
    private javax.swing.JLabel customerManagementIcon;
    private javax.swing.JPanel customerManagementTab;
    private javax.swing.JTextField customerSearchField;
    private javax.swing.JComboBox<String> customerSort;
    private javax.swing.JTable customerTable;
    private javax.swing.JTabbedPane dashboardTabbedPane;
    private javax.swing.JButton deactivateUser;
    private javax.swing.JButton deleteAccount;
    private javax.swing.JButton deleteCustomer;
    private javax.swing.JButton deleteEntry;
    private javax.swing.JButton deleteExpenses;
    private javax.swing.JButton deletePurchases;
    private javax.swing.JButton deleteSales;
    private javax.swing.JButton deleteVendor;
    private javax.swing.JButton editEntry;
    private javax.swing.JButton editExpenses;
    private javax.swing.JButton editNetwork;
    private javax.swing.JButton editProduct;
    private javax.swing.JLabel expensesButtonL;
    private javax.swing.JPanel expensesButtonP;
    private javax.swing.JComboBox<String> expensesFor;
    private javax.swing.JLabel expensesIcon;
    private javax.swing.JTextField expensesSearchField;
    private javax.swing.JComboBox<String> expensesSort;
    private javax.swing.JPanel expensesTab;
    private javax.swing.JTable expensesTable;
    private javax.swing.JLabel filterAuditTrail;
    private javax.swing.JLabel filterExpenses;
    private javax.swing.JLabel filterJournal;
    private javax.swing.JLabel filterLedger;
    private javax.swing.JLabel filterPurchases;
    private javax.swing.JLabel filterSales;
    private com.toedter.calendar.JDateChooser fromExpenses;
    private com.toedter.calendar.JDateChooser fromJournal;
    private com.toedter.calendar.JDateChooser fromLedger;
    private com.toedter.calendar.JDateChooser fromPurchases;
    private com.toedter.calendar.JDateChooser fromSales;
    private com.toedter.calendar.JDateChooser fromTaxes;
    private com.toedter.calendar.JDateChooser fromTrail;
    private javax.swing.JLabel generalJournalButtonL;
    private javax.swing.JPanel generalJournalButtonP;
    private javax.swing.JLabel generalJournalIcon;
    private javax.swing.JPanel generalJournalTab;
    private javax.swing.JLabel generalLedgerButtonL;
    private javax.swing.JPanel generalLedgerButtonP;
    private javax.swing.JLabel generalLedgerIcon;
    private javax.swing.JPanel generalLedgerTab;
    private javax.swing.JTextField grossSales1;
    private javax.swing.JTextField grossSales2;
    private javax.swing.JPanel header;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTextField journalSearchField;
    private javax.swing.JComboBox<String> journalSort;
    private javax.swing.JTable journalTable;
    private javax.swing.JComboBox<String> ledgerAccountSelectionBox;
    private javax.swing.JTable ledgerTable;
    private javax.swing.JLabel ledgerTotalBalance;
    private javax.swing.JLabel level;
    private javax.swing.JLabel logout;
    private javax.swing.JPanel menuSidePanel;
    private javax.swing.JTextField networkDatabase;
    private javax.swing.JPasswordField networkPassword;
    private javax.swing.JTextField networkPort;
    private javax.swing.JTextField networkServer;
    private javax.swing.JTextField networkUsername;
    private com.toedter.calendar.JDateChooser openingBalanceDate;
    private javax.swing.JLabel palExpenses;
    private javax.swing.JLabel palNetIncome;
    private javax.swing.JLabel palRange;
    private javax.swing.JLabel palRevenue;
    private javax.swing.JLabel palStatus;
    private javax.swing.JButton print;
    private javax.swing.JTable productTable;
    private javax.swing.JComboBox<String> profitAndLossRange;
    private javax.swing.JLabel purchasesButtonL;
    private javax.swing.JPanel purchasesButtonP;
    private javax.swing.JLabel purchasesIcon;
    private javax.swing.JTextField purchasesSearchField;
    private javax.swing.JComboBox<String> purchasesSort;
    private javax.swing.JPanel purchasesTab;
    private javax.swing.JTable purchasesTable;
    private javax.swing.JTable recentTransactionsList;
    private javax.swing.JComboBox<String> recentTransactionsRange;
    private javax.swing.JLabel refreshAccount;
    private javax.swing.JLabel refreshAuditTrail;
    private javax.swing.JLabel refreshCustomer;
    private javax.swing.JLabel refreshExpenses;
    private javax.swing.JLabel refreshJournal;
    private javax.swing.JLabel refreshLedger;
    private javax.swing.JLabel refreshPurchases;
    private javax.swing.JLabel refreshSales;
    private javax.swing.JLabel refreshUser;
    private javax.swing.JLabel refreshVendor;
    private javax.swing.JButton removeAllProduct;
    private javax.swing.JButton removeProduct;
    private javax.swing.JComboBox<String> reportSheet;
    private javax.swing.JTable reportTable;
    private javax.swing.JLabel reportsButtonL;
    private javax.swing.JPanel reportsButtonP;
    private javax.swing.JLabel reportsIcon;
    private javax.swing.JPanel reportsTab;
    private javax.swing.JComboBox<String> revenueFor;
    private javax.swing.JLabel salesButtonL;
    private javax.swing.JPanel salesButtonP;
    private javax.swing.JLabel salesIcon;
    private javax.swing.JTextField salesSearchField;
    private javax.swing.JComboBox<String> salesSort;
    private javax.swing.JPanel salesTab;
    private javax.swing.JTable salesTable;
    private javax.swing.JButton saveNetwork;
    private javax.swing.JLabel searchAccount;
    private javax.swing.JLabel searchAuditTrail;
    private javax.swing.JLabel searchCustomer;
    private javax.swing.JLabel searchExpenses;
    private javax.swing.JLabel searchJournal;
    private javax.swing.JLabel searchPurchases;
    private javax.swing.JLabel searchSales;
    private javax.swing.JLabel searchUser;
    private javax.swing.JLabel searchVendor;
    private javax.swing.JButton setOpeningBalanceDate;
    private javax.swing.JButton setPurchasesPaymentMethod;
    private javax.swing.JComboBox<String> setPurchasesRemainingBalanceAccount;
    private javax.swing.JButton setSalesPaymentMethod;
    private javax.swing.JComboBox<String> setSalesRemainingBalanceAccount;
    private javax.swing.JComboBox<String> setSalesRevenueAccount;
    private javax.swing.JLabel settingsButtonL;
    private javax.swing.JPanel settingsButtonP;
    private javax.swing.JLabel settingsIcon;
    private javax.swing.JPanel settingsTab;
    private javax.swing.JTabbedPane settingsTabbedPane;
    private javax.swing.JLabel summarizedExpenses;
    private javax.swing.JLabel summarizedRevenue;
    private javax.swing.JLabel summaryAccountsIcon;
    private javax.swing.JTable summaryAccountsList;
    private javax.swing.JLabel summaryButtonL;
    private javax.swing.JPanel summaryButtonP;
    private javax.swing.JLabel summaryExpensesIcon;
    private javax.swing.JLabel summaryIcon;
    private javax.swing.JLabel summaryProfitAndLossIcon;
    private javax.swing.JLabel summaryRecentTransactionsIcon;
    private javax.swing.JLabel summaryRevenueIcon;
    private javax.swing.JPanel summaryTab;
    private javax.swing.JTextField taxRate;
    private javax.swing.JTextField taxRateConfig;
    private javax.swing.JTextField taxResult;
    private javax.swing.JLabel taxesButtonL;
    private javax.swing.JPanel taxesButtonP;
    private javax.swing.JLabel taxesIcon;
    private javax.swing.JPanel taxesTab;
    private com.toedter.calendar.JDateChooser toExpenses;
    private com.toedter.calendar.JDateChooser toJournal;
    private com.toedter.calendar.JDateChooser toLedger;
    private com.toedter.calendar.JDateChooser toPurchases;
    private com.toedter.calendar.JDateChooser toSales;
    private com.toedter.calendar.JDateChooser toTaxes;
    private com.toedter.calendar.JDateChooser toTrail;
    private javax.swing.JButton updateAccount;
    private javax.swing.JButton updateCustomer;
    private javax.swing.JButton updateUser;
    private javax.swing.JButton updateVendor;
    private javax.swing.JLabel userManagementButtonL;
    private javax.swing.JPanel userManagementButtonP;
    private javax.swing.JLabel userManagementIcon;
    private javax.swing.JPanel userManagementTab;
    private javax.swing.JTextField userSearchField;
    private javax.swing.JComboBox<String> userSort;
    private javax.swing.JTable userTable;
    private javax.swing.JLabel username;
    private javax.swing.JLabel vendorManagementButtonL;
    private javax.swing.JPanel vendorManagementButtonP;
    private javax.swing.JLabel vendorManagementIcon;
    private javax.swing.JPanel vendorManagementTab;
    private javax.swing.JTextField vendorSearchField;
    private javax.swing.JComboBox<String> vendorSort;
    private javax.swing.JTable vendorTable;
    private javax.swing.JButton viewEntry;
    private javax.swing.JButton viewExpenses;
    private javax.swing.JButton viewPurchases;
    private javax.swing.JTextField viewPurchasesPaymentMethod;
    private javax.swing.JButton viewSales;
    private javax.swing.JTextField viewSalesPaymentMethod;
    private javax.swing.JButton viewTransactionSummary;
    // End of variables declaration//GEN-END:variables
}

