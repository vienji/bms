/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package bms.model.sales;

import bms.classes.Account;
import bms.classes.Cheque;
import bms.classes.Customer;
import bms.classes.ImageManipulator;
import bms.classes.Item;
import bms.classes.SalesTransaction;
import bms.classes.User;
import bms.dbcontroller.AccountDBController;
import bms.dbcontroller.CustomerDBController;
import bms.dbcontroller.EntryDBController;
import bms.listeners.DashboardListener;
import bms.listeners.SalesListener;
import bms.model.Dashboard;
import bms.model.customermanagement.AddCustomer;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Vienji
 */
public class AddSales extends javax.swing.JFrame implements SalesListener{
    private DashboardListener listener;
    private User currentUser;
    private SalesTransaction salesTransaction = new SalesTransaction();
    private Double total = 0.00;
    private Double remainingBalance = 0.00;
    private Customer customer = new Customer();
    private ArrayList<Customer> customerList = new CustomerDBController().getCustomersList();
    private ArrayList<Integer> accountCodes = getAccountCodes();
    private ArrayList<Item> productList = new ArrayList();
    /**
     * Creates new form AddSales
     */
    public AddSales() {
        initComponents();
        amountPaidListener();  
        showCustomerTable();
        
        new ImageManipulator().setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\search.png", searchCustomer);
    }
    
    public AddSales(User user, DashboardListener listener, ArrayList<Item> productList) {
        initComponents();
        amountPaidListener();  
        showCustomerTable();
        this.currentUser = user;
        this.listener = listener;
        username.setText(currentUser.getName());
        new ImageManipulator().setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\search.png", searchCustomer);
        initPaymentMethods();
        this.productList = productList;
    }
    
    @Override
    public void addItem(Item item){
        salesTransaction.addItem(item);
        showItemTable();
        description.setText(itemDescription(salesTransaction.getItemList()));
        setTotalAmount(salesTransaction.getItemList());
        setBalance(total, amountPaidConverter(amountPaidChecker(amountPaid.getText())));
        totalAmount.setText(setDecimalFormat(this.total));
        balance.setText(setDecimalFormat(this.remainingBalance));
    }
    
    @Override
    public void updateItem(int itemIndex, Item item){
        salesTransaction.setItem(itemIndex, item);
        showItemTable();
        description.setText(itemDescription(salesTransaction.getItemList()));
        setTotalAmount(salesTransaction.getItemList());
        setBalance(total, amountPaidConverter(amountPaidChecker(amountPaid.getText())));
        totalAmount.setText(setDecimalFormat(this.total));
        balance.setText(setDecimalFormat(this.remainingBalance));
    }
    
    @Override
    public void updateCustomerTable(){
        customerList = new CustomerDBController().getAllCustomers();
        showCustomerTable();
    }
    
    private void initPaymentMethods(){
        try(InputStream input = new FileInputStream("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Project\\BMS\\src\\bms\\path\\to\\salespaymentconfig.properties")){
            Properties network = new Properties();
            network.load(input);
            
            int size = Integer.parseInt(network.getProperty("size"));
            
            ArrayList<String> accountCodes = new ArrayList();
            
            for(int i = 0; i < size; i++){
                accountCodes.add(network.getProperty("method" + i));
            }
            
            ArrayList<Account> accountList = new AccountDBController().getAccountList(accountCodes);
            
            for(Account account : accountList){
                paymentMethod.addItem(account.getAccountName());
            }
            
        } catch (Exception io){
            io.printStackTrace();
        }
    }
    
    private ArrayList<Integer> getAccountCodes(){
        ArrayList<Integer> codes = new ArrayList();
        try(InputStream input = new FileInputStream("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Project\\BMS\\src\\bms\\path\\to\\salespaymentconfig.properties")){
            Properties network = new Properties();
            network.load(input);
            
            int size = Integer.parseInt(network.getProperty("size"));
                     
            for(int i = 0; i < size; i++){
                codes.add(Integer.parseInt(network.getProperty("method" + i)));
            }
            
        } catch (Exception io){
            io.printStackTrace();
        }
        return codes;
    } 
    
    private void showItemTable(){
        itemTable.setModel(new DefaultTableModel(null, new String[]{"Item/Service", "Qty.", "Description", "Price"}));
        DefaultTableModel populatedItemTable = (DefaultTableModel) itemTable.getModel();
        
        Iterator i = (Iterator) salesTransaction.getItemList().iterator();
        
        while(i.hasNext()){
            Item item = (Item) i.next();
            
            String[] itemData = {item.getName(), String.valueOf(item.getQuantity()), 
                item.getDescription(), " ₱ " + setDecimalFormat(item.getPrice())};
            populatedItemTable.addRow(itemData);
        }
    }
    
    private void showCustomerTable(){
        customerTable.setModel(new DefaultTableModel(null, new String[]{"Name", "Phone Number"}));
        DefaultTableModel populatedCustomerTable = (DefaultTableModel) customerTable.getModel();
        
        Iterator i = (Iterator) customerList.iterator();
        
        while(i.hasNext()){
            Customer customer = (Customer) i.next();
            
            String[] customerData = {customer.getName(), customer.getPhoneNumber()};
            populatedCustomerTable.addRow(customerData);
        }
    }
    
    private void setTotalAmount(ArrayList<Item> itemList){       
        double amount = 0;
        
        for(Item item : itemList){
            amount += item.getPrice();
        }
        
        this.total = amount;
    }
    
    private void setBalance(Double totalAmount, Double amountPaid){
        if(amountPaid <= totalAmount){
            this.remainingBalance = totalAmount - amountPaid;
        } else {
            this.remainingBalance = 0.00;
        }      
    }
    
    private String setDecimalFormat(Double amount){
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        
        return decimalFormat.format(amount);
    }
    
    private Double amountPaidConverter(String amountPaid){
        String amount;
        if(amountPaid.isBlank()){
            amount = "0.00";
        } else {
            amount = amountPaid;
        }
        
        return Double.parseDouble(amount);
    }
    
    private String amountPaidChecker(String amountPaid){
        try{
            Double.parseDouble(amountPaid);
            return amountPaid;
        } catch (NumberFormatException e){
            return "0.00";
        }
    }
    
    private boolean isNumeric(String number){       
        try{
            Double.parseDouble(number);
            return true;
        } catch (NumberFormatException e){
            return false;
        }      
    }
    
    private void amountPaidListener(){
        amountPaid.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateBalance();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateBalance();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateBalance();
            }
        
            private void updateBalance(){
                setBalance(total, amountPaidConverter(amountPaidChecker(amountPaid.getText())));
                balance.setText(setDecimalFormat(remainingBalance));
            }
        });
    }
    
    private String itemDescription(ArrayList<Item> itemList){
        String description = "Sold ";
        int n = 1;
        for(Item item: itemList){
            if(itemList.size() == 1){ return description + item.getName() + " to a customer.";}
            
            if(n < itemList.size()){
                description += item.getName() + ", ";
            } else {
                description += "and " + item.getName() + " to a customer.";
            }
            
            n++;
        }
                
        return itemList.isEmpty() ? "" : description;
    }
    
    private String remarks(){
        return (remainingBalance == 0.0) ? "Fully paid" : "Partially paid";
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addSalesTabbedPane = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        phoneNumber = new javax.swing.JTextField();
        customerName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        date = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        itemTable = new javax.swing.JTable();
        editItem = new javax.swing.JButton();
        removeAllItems = new javax.swing.JButton();
        removeItem = new javax.swing.JButton();
        addItem = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        totalAmount = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        amountPaid = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        balance = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        paymentMethod = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        description = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        username = new javax.swing.JLabel();
        chequeNumber = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        dateIssued = new com.toedter.calendar.JDateChooser();
        accountNumber = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        bankName = new javax.swing.JTextField();
        payee = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        addSales = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        customerTable = new javax.swing.JTable();
        addCustomer = new javax.swing.JButton();
        selectCustomer = new javax.swing.JButton();
        searchCustomer = new javax.swing.JLabel();
        searchCustomerField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add Sales");
        setResizable(false);

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jLabel2.setText("Customer Name:");

        jLabel3.setText("Phone Number:");

        jLabel1.setText("Date:");

        date.setDateFormatString("MM/dd/yyyy");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("List of Items and Services Sold");

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        itemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item/Service", "Qty.", "Description", "Price"
            }
        ));
        jScrollPane1.setViewportView(itemTable);

        editItem.setText("Edit Item");
        editItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editItemActionPerformed(evt);
            }
        });

        removeAllItems.setText("Remove All");
        removeAllItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAllItemsActionPerformed(evt);
            }
        });

        removeItem.setText("Remove");
        removeItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeItemActionPerformed(evt);
            }
        });

        addItem.setText("Add Item");
        addItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addItemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(addItem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(removeItem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(removeAllItems)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(editItem)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editItem)
                    .addComponent(removeAllItems)
                    .addComponent(removeItem)
                    .addComponent(addItem))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setText("Total Amount:");

        totalAmount.setText("0.00");

        jLabel6.setText("Amount Paid:");

        jLabel7.setText("Balance:");

        balance.setText("0.00");

        jLabel8.setText("Payment Method:");

        paymentMethod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));

        jLabel12.setText("₱");

        jLabel13.setText("₱");

        jLabel11.setText("Description:");

        description.setColumns(20);
        description.setRows(5);
        jScrollPane2.setViewportView(description);

        jLabel9.setText("Sales Person:");

        username.setText("username");

        jLabel14.setText("Cheque Number:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("For Cheques:");

        jLabel15.setText("Date Issued:");

        dateIssued.setDateFormatString("MM/dd/yyyy");

        jLabel16.setText("Account Number:");

        jLabel17.setText("Bank Name:");

        jLabel18.setText("Payee");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel9)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(username))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(18, 18, 18)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(phoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(customerName, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(chequeNumber)
                                                .addComponent(jLabel14)
                                                .addComponent(jLabel16))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(dateIssued, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING)))
                                        .addComponent(accountNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(payee)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel17)
                                                .addComponent(jLabel18))
                                            .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(bankName)))
                                .addComponent(jLabel10))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(amountPaid, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(paymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(totalAmount))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(balance)))))))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(customerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(phoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(34, 34, 34)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(totalAmount)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(amountPaid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(balance)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(paymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chequeNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateIssued, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(payee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(accountNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bankName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(username))
                .addContainerGap())
        );

        jScrollPane4.setViewportView(jPanel2);

        addSales.setText("Add");
        addSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSalesActionPerformed(evt);
            }
        });

        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(addSales, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cancel)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancel)
                    .addComponent(addSales))
                .addGap(26, 26, 26))
        );

        addSalesTabbedPane.addTab("Add Sales", jPanel4);

        customerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Phone Number"
            }
        ));
        jScrollPane3.setViewportView(customerTable);

        addCustomer.setText("Add Customer");
        addCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCustomerActionPerformed(evt);
            }
        });

        selectCustomer.setText("Select");
        selectCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectCustomerActionPerformed(evt);
            }
        });

        searchCustomer.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(searchCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(searchCustomerField, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(selectCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addCustomer)
                .addGap(24, 24, 24))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(searchCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchCustomerField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 581, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectCustomer)
                    .addComponent(addCustomer))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        addSalesTabbedPane.addTab("Customer List", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(addSalesTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 553, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(addSalesTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 737, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void addItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addItemActionPerformed
        AddSalesItem addSalesItem = new AddSalesItem(this, productList);
        addSalesItem.setVisible(true);
    }//GEN-LAST:event_addItemActionPerformed

    private void editItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editItemActionPerformed
        int selectedRow = itemTable.getSelectedRow();
        if(selectedRow >= 0){
            UpdateSalesItem updateSalesItem = new UpdateSalesItem(this, selectedRow, productList);
            updateSalesItem.item.setText(salesTransaction.getItem(selectedRow).getName());
            updateSalesItem.metric.setText(salesTransaction.getItem(selectedRow).getQuantity());
            updateSalesItem.price.setText(String.valueOf(salesTransaction.getItem(selectedRow).getPrice()));
            updateSalesItem.description.setText(salesTransaction.getItem(selectedRow).getDescription());
            updateSalesItem.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select an item to edit!");
        }
    }//GEN-LAST:event_editItemActionPerformed

    private void removeItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeItemActionPerformed
        int selectedRow = itemTable.getSelectedRow();
        if(selectedRow >= 0){
            salesTransaction.removeItem(selectedRow);
            showItemTable();
            description.setText(itemDescription(salesTransaction.getItemList()));
            setTotalAmount(salesTransaction.getItemList());
            setBalance(total, amountPaidConverter(amountPaidChecker(amountPaid.getText())));
            totalAmount.setText(setDecimalFormat(this.total));
            balance.setText(setDecimalFormat(this.remainingBalance));
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select an item to be removed!");
        }
    }//GEN-LAST:event_removeItemActionPerformed

    private void removeAllItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAllItemsActionPerformed
        salesTransaction.flushAllItem();
        showItemTable();
        description.setText(itemDescription(salesTransaction.getItemList()));
        setTotalAmount(salesTransaction.getItemList());
        setBalance(total, amountPaidConverter(amountPaidChecker(amountPaid.getText())));
        totalAmount.setText(setDecimalFormat(this.total));
        balance.setText(setDecimalFormat(this.remainingBalance));
    }//GEN-LAST:event_removeAllItemsActionPerformed

    private void addCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCustomerActionPerformed
        AddCustomer addCustomer = new AddCustomer(this, true);
        addCustomer.setVisible(true);
    }//GEN-LAST:event_addCustomerActionPerformed

    private void selectCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectCustomerActionPerformed
        int selectedRow = customerTable.getSelectedRow();
        if(selectedRow >= 0){
            customer = customerList.get(selectedRow);
            customerName.setText(customer.getName());
            phoneNumber.setText(customer.getPhoneNumber());
            addSalesTabbedPane.setSelectedIndex(0);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a customer!");
        }
    }//GEN-LAST:event_selectCustomerActionPerformed

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        dispose();
    }//GEN-LAST:event_cancelActionPerformed

    private void addSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSalesActionPerformed
        if(date.getDate() != null){
            if(paymentMethod.getSelectedIndex() > 0){
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String formattedDate = dateFormat.format(date.getDate());
                int paymentIndex = paymentMethod.getSelectedIndex() - 1;
                int paymentCode = accountCodes.get(paymentIndex);

                if(customer.getName().equals(customerName.getText()) && customer.getPhoneNumber().equals(phoneNumber.getText()) 
                        && !customer.getId().isBlank()){
                    //if customer info is == to customer object, or it is in the customer list
                    if(salesTransaction.getItemList().isEmpty()){
                        JOptionPane.showMessageDialog(rootPane, "Please add at least 1 item or service!");
                    } else if (!isNumeric(amountPaid.getText())){
                        JOptionPane.showMessageDialog(rootPane, "Please enter a numeric amount!");
                    } else if(Double.parseDouble(amountPaid.getText()) > total){
                        JOptionPane.showMessageDialog(rootPane, "Amount to pay is more than the total amount!");
                    } else if (amountPaidConverter(amountPaidChecker(amountPaid.getText())) == 0.00){
                         JOptionPane.showMessageDialog(rootPane, "Please enter an amount paid!");
                    } else {
                        Cheque cheque = new Cheque();
                        boolean isCheque = false;

                        if(!chequeNumber.getText().isBlank() || dateIssued.getDate() != null || 
                                !payee.getText().isBlank() || !accountNumber.getText().isBlank() || !bankName.getText().isBlank()){
                            if(chequeNumber.getText().isBlank()){
                                JOptionPane.showMessageDialog(rootPane, "Please enter a cheque number!");
                                return;
                            } else if (payee.getText().isBlank()){
                                JOptionPane.showMessageDialog(rootPane, "Please enter the name of the payee!");
                                return;
                            } else if (accountNumber.getText().isBlank()){
                                JOptionPane.showMessageDialog(rootPane, "Please enter an account number!");
                                return;
                            } else if (bankName.getText().isBlank()){
                                JOptionPane.showMessageDialog(rootPane, "Please enter a bank name");
                                return;
                            } else {
                                String issuedDate = dateFormat.format(dateIssued.getDate());
                                cheque.setChequeNumber(chequeNumber.getText());
                                cheque.setPayee(payee.getText());
                                cheque.setAccountNumber(accountNumber.getText());
                                cheque.setBankName(bankName.getText());
                                cheque.setDateIssued(issuedDate);
                                isCheque = true;
                            }
                        }
                        
                        int n = JOptionPane.showConfirmDialog(rootPane, "Are you sure all of the information that you've entered are correct?");

                        if(n == 0){
                            if(isCheque){
                                new EntryDBController().addSales(false, formattedDate, customer.getId(), phoneNumber.getText(), 
                                            new Dashboard().stringEscape(description.getText()),currentUser.getId(), total, Double.parseDouble(amountPaid.getText()), this.remainingBalance, 
                                            paymentCode, remarks(),salesTransaction.getItemList(), cheque);
                            } else {
                                new EntryDBController().addSales(false, formattedDate, customer.getId(), phoneNumber.getText(), 
                                        new Dashboard().stringEscape(description.getText()),currentUser.getId(), total, Double.parseDouble(amountPaid.getText()), this.remainingBalance, 
                                        paymentCode, remarks(),salesTransaction.getItemList());
                            }
                            
                            JOptionPane.showMessageDialog(rootPane, "Transaction was successfully added to the database!");
                            listener.updateSalesTable();
                            dispose();
                        }
                    }
                } else {
                    //else if customer info is != to customer object, or not in the customer list
                    if(!customerName.getText().isBlank() && !phoneNumber.getText().isBlank()){
                        //if customer info are not blank
                        String customerName = this.customerName.getText();
                        String phoneNumber = this.phoneNumber.getText();
                        int n = JOptionPane.showConfirmDialog(rootPane, "Do you want to add " + customerName + 
                                " and his/her number " + phoneNumber + " into the customer list?");

                        if(n == 0){
                            //if Yes then adding of customer info to database and relate this transaction to the customer id
                            String[] splittedCustomerName = customerName.split(" ");
                            String firstName = "";
                            String lastName = "";;

                            if(splittedCustomerName.length >= 2){
                                firstName = splittedCustomerName[0];
                                lastName = splittedCustomerName[splittedCustomerName.length - 1];
                            } else if (splittedCustomerName.length == 1){
                                firstName = splittedCustomerName[0];
                            }

                            String customerID = new CustomerDBController().addAndGetCustomerID(firstName, lastName, phoneNumber);

                            //adding of sales transaction
                            if(salesTransaction.getItemList().isEmpty()){
                                JOptionPane.showMessageDialog(rootPane, "Please add at least 1 item or service!");
                            } else if (!isNumeric(amountPaid.getText())){
                                JOptionPane.showMessageDialog(rootPane, "Please enter a numeric amount!");
                            } else if (amountPaidConverter(amountPaidChecker(amountPaid.getText())) == 0.00){
                                JOptionPane.showMessageDialog(rootPane, "Please enter an amount paid!");
                            } else {
                                Cheque cheque = new Cheque();
                                boolean isCheque = false;

                                if(!chequeNumber.getText().isBlank() || dateIssued.getDate() != null || 
                                        !payee.getText().isBlank() || !accountNumber.getText().isBlank() || !bankName.getText().isBlank()){
                                    if(chequeNumber.getText().isBlank()){
                                        JOptionPane.showMessageDialog(rootPane, "Please enter a cheque number!");
                                        return;
                                    } else if (payee.getText().isBlank()){
                                        JOptionPane.showMessageDialog(rootPane, "Please enter the name of the payee!");
                                        return;
                                    } else if (accountNumber.getText().isBlank()){
                                        JOptionPane.showMessageDialog(rootPane, "Please enter an account number!");
                                        return;
                                    } else if (bankName.getText().isBlank()){
                                        JOptionPane.showMessageDialog(rootPane, "Please enter a bank name");
                                        return;
                                    } else {
                                        String issuedDate = dateFormat.format(dateIssued.getDate());
                                        cheque.setChequeNumber(chequeNumber.getText());
                                        cheque.setPayee(payee.getText());
                                        cheque.setAccountNumber(accountNumber.getText());
                                        cheque.setBankName(bankName.getText());
                                        cheque.setDateIssued(issuedDate);
                                        isCheque = true;
                                    }
                                }
                                
                                int confirm = JOptionPane.showConfirmDialog(rootPane, "Are you sure all of the information that you've entered are correct?");

                                if(confirm == 0){
                                    if(isCheque){
                                        new EntryDBController().addSales(false, formattedDate, customerID, phoneNumber, 
                                                    new Dashboard().stringEscape(description.getText()),currentUser.getId(), total, Double.parseDouble(amountPaid.getText()), this.remainingBalance, 
                                                    paymentCode, remarks(),salesTransaction.getItemList(), cheque);
                                    } else {
                                        new EntryDBController().addSales(false, formattedDate, customerID, phoneNumber, 
                                                new Dashboard().stringEscape(description.getText()),currentUser.getId(), total, Double.parseDouble(amountPaid.getText()), this.remainingBalance, 
                                                paymentCode, remarks(),salesTransaction.getItemList());
                                    }
                                    
                                    JOptionPane.showMessageDialog(rootPane, "Transaction was successfully added to the database!");
                                    listener.updateSalesTable();
                                    dispose();
                                }
                            }

                        } else {
                            //if No then just add the customer name directly
                            if(salesTransaction.getItemList().isEmpty()){
                                JOptionPane.showMessageDialog(rootPane, "Please add at least 1 item or service!");
                            } else if (!isNumeric(amountPaid.getText())){
                                JOptionPane.showMessageDialog(rootPane, "Please enter a numeric amount!");
                            } else if (amountPaidConverter(amountPaidChecker(amountPaid.getText())) == 0.00){
                                JOptionPane.showMessageDialog(rootPane, "Please enter an amount paid!");
                            } else {
                                Cheque cheque = new Cheque();
                                boolean isCheque = false;

                                if(!chequeNumber.getText().isBlank() || dateIssued.getDate() != null || 
                                        !payee.getText().isBlank() || !accountNumber.getText().isBlank() || !bankName.getText().isBlank()){
                                    if(chequeNumber.getText().isBlank()){
                                        JOptionPane.showMessageDialog(rootPane, "Please enter a cheque number!");
                                        return;
                                    } else if (payee.getText().isBlank()){
                                        JOptionPane.showMessageDialog(rootPane, "Please enter the name of the payee!");
                                        return;
                                    } else if (accountNumber.getText().isBlank()){
                                        JOptionPane.showMessageDialog(rootPane, "Please enter an account number!");
                                        return;
                                    } else if (bankName.getText().isBlank()){
                                        JOptionPane.showMessageDialog(rootPane, "Please enter a bank name");
                                        return;
                                    } else {
                                        String issuedDate = dateFormat.format(dateIssued.getDate());
                                        cheque.setChequeNumber(chequeNumber.getText());
                                        cheque.setPayee(payee.getText());
                                        cheque.setAccountNumber(accountNumber.getText());
                                        cheque.setBankName(bankName.getText());
                                        cheque.setDateIssued(issuedDate);
                                        isCheque = true;
                                    }
                                }
                                
                                int confirm = JOptionPane.showConfirmDialog(rootPane, "Are you sure all of the information that you've entered are correct?");

                                if(confirm == 0){
                                    if(isCheque){
                                        new EntryDBController().addSales(true, formattedDate, customerName, phoneNumber, 
                                                    new Dashboard().stringEscape(description.getText()), currentUser.getId(), total, Double.parseDouble(amountPaid.getText()),
                                                    this.remainingBalance,paymentCode, remarks(),salesTransaction.getItemList(), cheque);
                                    } else {
                                        new EntryDBController().addSales(true, formattedDate, customerName, phoneNumber, 
                                                new Dashboard().stringEscape(description.getText()), currentUser.getId(), total, Double.parseDouble(amountPaid.getText()),
                                                this.remainingBalance,paymentCode, remarks(),salesTransaction.getItemList());
                                    }
                                    
                                    JOptionPane.showMessageDialog(rootPane, "Transaction was successfully added to the database!");
                                    listener.updateSalesTable();
                                    dispose();
                                }
                            }
                        }
                    } else {
                        //If either one of the customer info is blank
                        if(salesTransaction.getItemList().isEmpty()){
                            JOptionPane.showMessageDialog(rootPane, "Please add at least 1 item or service!");
                        } else if (!isNumeric(amountPaid.getText())){
                            JOptionPane.showMessageDialog(rootPane, "Please enter a numeric amount!");
                        } else if (amountPaidConverter(amountPaidChecker(amountPaid.getText())) == 0.00){
                            JOptionPane.showMessageDialog(rootPane, "Please enter an amount paid!");
                        } else {
                            Cheque cheque = new Cheque();
                            boolean isCheque = false;

                            if(!chequeNumber.getText().isBlank() || dateIssued.getDate() != null || 
                                    !payee.getText().isBlank() || !accountNumber.getText().isBlank() || !bankName.getText().isBlank()){
                            if(chequeNumber.getText().isBlank()){
                                    JOptionPane.showMessageDialog(rootPane, "Please enter a cheque number!");
                                    return;
                            } else if (payee.getText().isBlank()){
                                    JOptionPane.showMessageDialog(rootPane, "Please enter the name of the payee!");
                                    return;
                            } else if (accountNumber.getText().isBlank()){
                                    JOptionPane.showMessageDialog(rootPane, "Please enter an account number!");
                                    return;
                            } else if (bankName.getText().isBlank()){
                                    JOptionPane.showMessageDialog(rootPane, "Please enter a bank name");
                                    return;
                            } else {
                                    String issuedDate = dateFormat.format(dateIssued.getDate());
                                    cheque.setChequeNumber(chequeNumber.getText());
                                    cheque.setPayee(payee.getText());
                                    cheque.setAccountNumber(accountNumber.getText());
                                    cheque.setBankName(bankName.getText());
                                    cheque.setDateIssued(issuedDate);
                                    isCheque = true;
                                }
                            }   
                            int confirm = JOptionPane.showConfirmDialog(rootPane, "Are you sure all of the information that you've entered are correct?");

                            if(confirm == 0){
                                String customerName = this.customerName.getText();
                                String phoneNumber = this.phoneNumber.getText();
                                
                                if(isCheque){
                                    new EntryDBController().addSales(true, formattedDate, customerName , phoneNumber, new Dashboard().stringEscape(description.getText()), 
                                                currentUser.getId(), total, Double.parseDouble(amountPaid.getText()) , this.remainingBalance,
                                                paymentCode, remarks(),salesTransaction.getItemList(), cheque);
                                } else {
                                    new EntryDBController().addSales(true, formattedDate, customerName , phoneNumber, new Dashboard().stringEscape(description.getText()), 
                                                currentUser.getId(), total, Double.parseDouble(amountPaid.getText()) , this.remainingBalance,
                                                paymentCode, remarks(),salesTransaction.getItemList());
                                }
                                
                                JOptionPane.showMessageDialog(rootPane, "Transaction was successfully added to the database!");
                                listener.updateSalesTable();
                                dispose();
                            }
                        }
                    }          
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "Please select a payment method!");
            } 
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please enter a date!");
        }       
    }//GEN-LAST:event_addSalesActionPerformed

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
            java.util.logging.Logger.getLogger(AddSales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddSales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddSales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddSales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddSales().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField accountNumber;
    private javax.swing.JButton addCustomer;
    private javax.swing.JButton addItem;
    private javax.swing.JButton addSales;
    private javax.swing.JTabbedPane addSalesTabbedPane;
    private javax.swing.JTextField amountPaid;
    private javax.swing.JLabel balance;
    private javax.swing.JTextField bankName;
    private javax.swing.JButton cancel;
    private javax.swing.JTextField chequeNumber;
    private javax.swing.JTextField customerName;
    private javax.swing.JTable customerTable;
    public com.toedter.calendar.JDateChooser date;
    private com.toedter.calendar.JDateChooser dateIssued;
    private javax.swing.JTextArea description;
    private javax.swing.JButton editItem;
    private javax.swing.JTable itemTable;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextField payee;
    private javax.swing.JComboBox<String> paymentMethod;
    private javax.swing.JTextField phoneNumber;
    private javax.swing.JButton removeAllItems;
    private javax.swing.JButton removeItem;
    private javax.swing.JLabel searchCustomer;
    private javax.swing.JTextField searchCustomerField;
    private javax.swing.JButton selectCustomer;
    private javax.swing.JLabel totalAmount;
    public javax.swing.JLabel username;
    // End of variables declaration//GEN-END:variables
}
