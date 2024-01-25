/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package bms.model.purchases;

import bms.classes.Account;
import bms.classes.ImageManipulator;
import bms.classes.Item;
import bms.classes.PurchaseTransaction;
import bms.classes.User;
import bms.classes.Vendor;
import bms.dbcontroller.AccountDBController;
import bms.dbcontroller.EntryDBController;
import bms.dbcontroller.VendorDBController;
import bms.listeners.DashboardListener;
import bms.listeners.PurchasesListener;
import bms.model.Dashboard;
import bms.model.vendormanagement.AddVendor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Vienji
 */
public class AddPurchases extends javax.swing.JFrame implements PurchasesListener{
    private DashboardListener dashboardListener;
    private PurchaseTransaction purchasesTransaction = new PurchaseTransaction();
    private Vendor supplier = new Vendor();
    private Double total = 0.00;
    private Double remainingBalance = 0.00;
    private ArrayList<Vendor> supplierList = new VendorDBController().getVendorsList();
    private User user;
    private ArrayList<Integer> accountCodes = getAccountCodes();
    /**
     * Creates new form AddPurchases
     */
    public AddPurchases() {
        initComponents();
        amountPaidListener();
        showSupplierTable();
        
        new ImageManipulator().setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\search.png", searchSupplier);
    }
    
    public AddPurchases(DashboardListener dashboardListener, User user) {
        initComponents();
        initAssetAccount();
        amountPaidListener();
        showSupplierTable();
        this.user = user;
        this.dashboardListener = dashboardListener;
        new ImageManipulator().setIcons("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Resources\\Icons\\search.png", searchSupplier);
        initPaymentMethods();
    }

    private void initPaymentMethods(){
        try(InputStream input = new FileInputStream("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Project\\BMS\\src\\bms\\path\\to\\purchasespaymentconfig.properties")){
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
        try(InputStream input = new FileInputStream("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Project\\BMS\\src\\bms\\path\\to\\purchasespaymentconfig.properties")){
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
    
    private void initAssetAccount(){
        ArrayList<Account> assetList = new AccountDBController().getAllAssetAccount();
        
        assetList.forEach((e) -> {
            assetAccount.addItem(e.getAccountName());
        });
    }
    
    @Override
    public void addItem(Item item){
        purchasesTransaction.addItem(item);
        showItemTable();
        description.setText(itemDescription(purchasesTransaction.getItemList()));
        setTotalAmount(purchasesTransaction.getItemList());
        setBalance(total, amountPaidConverter(amountPaidChecker(amountPaid.getText())));
        totalAmount.setText("₱ " + setDecimalFormat(this.total));
        balance.setText("₱ " + setDecimalFormat(this.remainingBalance));
    }
    
    @Override
    public void updateItem(int itemIndex, Item item){
        purchasesTransaction.setItem(itemIndex, item);
        showItemTable();
        description.setText(itemDescription(purchasesTransaction.getItemList()));
        setTotalAmount(purchasesTransaction.getItemList());
        setBalance(total, amountPaidConverter(amountPaidChecker(amountPaid.getText())));
        totalAmount.setText("₱ " + setDecimalFormat(this.total));
        balance.setText("₱ " + setDecimalFormat(this.remainingBalance));
    }
    
    @Override
    public void updateSupplierTable(){
        supplierList = new VendorDBController().getAllVendors();
        showSupplierTable();
    }
    
    private void showItemTable(){
        itemTable.setModel(new DefaultTableModel(null, new String[]{"Item", "Qty.", "Description", "Price"}));
        DefaultTableModel populatedItemTable = (DefaultTableModel) itemTable.getModel();
        
        Iterator i = (Iterator) purchasesTransaction.getItemList().iterator();
        
        while(i.hasNext()){
            Item item = (Item) i.next();
            
            String[] itemData = {item.getName(), String.valueOf(item.getQuantity()), 
                item.getDescription(), " ₱ " + setDecimalFormat(item.getPrice())};
            populatedItemTable.addRow(itemData);
        }
    }
    
    private void showSupplierTable(){
        supplierTable.setModel(new DefaultTableModel(null, new String[]{"Name", "Phone Number"}));
        DefaultTableModel populatedSupplierTable = (DefaultTableModel) supplierTable.getModel();
        
        Iterator i = (Iterator) supplierList.iterator();
        
        while(i.hasNext()){
            Vendor supplier = (Vendor) i.next();
            
            String[] supplierData = {supplier.getName(), supplier.getPhoneNumber()};
            populatedSupplierTable.addRow(supplierData);
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
    
    private String setDecimalFormat(Double amount){
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        
        return decimalFormat.format(amount);
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
                balance.setText("₱ " + setDecimalFormat(remainingBalance));
            }
        });
    }
    
    private String itemDescription(ArrayList<Item> itemList){
        String description = "Bought ";
        int n = 1;
        for(Item item: itemList){
            if(itemList.size() == 1){ return description + item.getName() + " from a supplier.";}
            
            if(n < itemList.size()){
                description += item.getName() + ", ";
            } else {
                description += "and " + item.getName() + " from a supplier.";
            }
            
            n++;
        }
                
        return itemList.isEmpty() ? "" : description;
    }
    
    private String remarks(){
        return (remainingBalance == 0.0) ? "Fully paid" : "Partially paid";
    }
    
    private boolean isPathCorrect(String document){
        try{
            FileInputStream fileInputStream = new FileInputStream(document);
            System.out.println("true");
            return true;
        } catch (FileNotFoundException e){
            System.out.println("False");
            return false;
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

        addPurchasesTabbedPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        date = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        supplierName = new javax.swing.JTextField();
        phoneNumber = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        itemTable = new javax.swing.JTable();
        editItem = new javax.swing.JButton();
        removeAllItem = new javax.swing.JButton();
        removeItem = new javax.swing.JButton();
        addItem = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        totalAmount = new javax.swing.JLabel();
        balance = new javax.swing.JLabel();
        amountPaid = new javax.swing.JTextField();
        paymentMethod = new javax.swing.JComboBox<>();
        dueDate = new com.toedter.calendar.JDateChooser();
        isApplicable = new javax.swing.JCheckBox();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        description = new javax.swing.JTextArea();
        cancel = new javax.swing.JButton();
        addPurchases = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        purchaser = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        assetAccount = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        documentPath = new javax.swing.JTextField();
        choosePhoto = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        searchSupplier = new javax.swing.JLabel();
        searchSupplierField = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        supplierTable = new javax.swing.JTable();
        addSupplier = new javax.swing.JButton();
        selectSupplier = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add Purchases");
        setResizable(false);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Date:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(417, 36, -1, -1));

        date.setDateFormatString("MM/dd/yyyy");
        jPanel1.add(date, new org.netbeans.lib.awtextra.AbsoluteConstraints(456, 30, 101, -1));

        jLabel2.setText("Supplier Name:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(38, 69, -1, -1));

        jLabel3.setText("Phone Number:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 104, -1, -1));

        supplierName.setEditable(false);
        jPanel1.add(supplierName, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 66, 160, -1));

        phoneNumber.setEditable(false);
        jPanel1.add(phoneNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 101, 160, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("List of Items");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 141, -1, -1));

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        itemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item", "Qty.", "Description", "Price"
            }
        ));
        jScrollPane1.setViewportView(itemTable);

        editItem.setText("Edit Item");
        editItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editItemActionPerformed(evt);
            }
        });

        removeAllItem.setText("Remove All");
        removeAllItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAllItemActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(addItem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(removeItem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(removeAllItem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(editItem)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editItem)
                    .addComponent(removeAllItem)
                    .addComponent(removeItem)
                    .addComponent(addItem))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 164, 522, -1));

        jLabel5.setText("Total Amount:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(63, 384, -1, -1));

        jLabel6.setText("Amount Paid:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(63, 416, -1, -1));

        jLabel7.setText("Balance:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(92, 448, -1, -1));

        jLabel8.setText("Payment Method:");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 416, -1, -1));

        jLabel9.setText("Due Date:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 480, -1, -1));

        totalAmount.setText("₱ 0.00");
        jPanel1.add(totalAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(151, 384, -1, -1));

        balance.setText("₱ 0.00");
        jPanel1.add(balance, new org.netbeans.lib.awtextra.AbsoluteConstraints(148, 448, -1, -1));
        jPanel1.add(amountPaid, new org.netbeans.lib.awtextra.AbsoluteConstraints(148, 413, 140, -1));

        paymentMethod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        jPanel1.add(paymentMethod, new org.netbeans.lib.awtextra.AbsoluteConstraints(407, 413, 150, -1));

        dueDate.setDateFormatString("MM/dd/yyyy");
        jPanel1.add(dueDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(139, 477, 140, -1));

        isApplicable.setText("Not Applicable");
        jPanel1.add(isApplicable, new org.netbeans.lib.awtextra.AbsoluteConstraints(291, 474, -1, -1));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Description:");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 620, -1, -1));

        description.setColumns(20);
        description.setRows(5);
        jScrollPane2.setViewportView(description);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 643, 516, 81));

        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });
        jPanel1.add(cancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(488, 737, -1, -1));

        addPurchases.setText("Add");
        addPurchases.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPurchasesActionPerformed(evt);
            }
        });
        jPanel1.add(addPurchases, new org.netbeans.lib.awtextra.AbsoluteConstraints(405, 737, 71, -1));

        jLabel13.setText("Purchaser:");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 520, -1, -1));
        jPanel1.add(purchaser, new org.netbeans.lib.awtextra.AbsoluteConstraints(139, 517, 140, -1));

        jLabel10.setText("Asset Account:");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(48, 560, -1, -1));

        assetAccount.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        jPanel1.add(assetAccount, new org.netbeans.lib.awtextra.AbsoluteConstraints(139, 557, 140, -1));

        jLabel15.setText("Document:");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 597, -1, -1));
        jPanel1.add(documentPath, new org.netbeans.lib.awtextra.AbsoluteConstraints(139, 591, 140, -1));

        choosePhoto.setText("Choose Photo");
        choosePhoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                choosePhotoActionPerformed(evt);
            }
        });
        jPanel1.add(choosePhoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(291, 588, -1, -1));

        addPurchasesTabbedPane.addTab("Add Purchases", jPanel1);

        searchSupplier.setMaximumSize(new java.awt.Dimension(30, 30));
        searchSupplier.setPreferredSize(new java.awt.Dimension(30, 30));

        supplierTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Supplier", "Phone Number"
            }
        ));
        jScrollPane3.setViewportView(supplierTable);

        addSupplier.setText("Add Supplier");
        addSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSupplierActionPerformed(evt);
            }
        });

        selectSupplier.setText("Select");
        selectSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectSupplierActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(selectSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addSupplier))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(searchSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(searchSupplierField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(searchSupplierField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 616, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addSupplier)
                    .addComponent(selectSupplier))
                .addGap(27, 27, 27))
        );

        addPurchasesTabbedPane.addTab("Suppliers List", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(addPurchasesTabbedPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(addPurchasesTabbedPane)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void addItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addItemActionPerformed
        AddPurchasesItem addPurchasesItem = new AddPurchasesItem(this);
        addPurchasesItem.setVisible(true);
    }//GEN-LAST:event_addItemActionPerformed

    private void editItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editItemActionPerformed
        int selectedRow = itemTable.getSelectedRow();
        if(selectedRow >= 0){
            UpdatePurchasesItem updatePurchasesItem = new UpdatePurchasesItem(this, selectedRow);
            updatePurchasesItem.item.setText(purchasesTransaction.getItem(selectedRow).getName());
            updatePurchasesItem.quantity.setValue(Integer.parseInt(purchasesTransaction.getItem(selectedRow).getQuantity()));
            updatePurchasesItem.price.setText(String.valueOf(purchasesTransaction.getItem(selectedRow).getPrice()));
            updatePurchasesItem.description.setText(purchasesTransaction.getItem(selectedRow).getDescription());
            updatePurchasesItem.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select an item to edit!");
        }   
    }//GEN-LAST:event_editItemActionPerformed

    private void removeItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeItemActionPerformed
        int selectedRow = itemTable.getSelectedRow();
        if(selectedRow >= 0){
            purchasesTransaction.removeItem(selectedRow);
            showItemTable();
            description.setText(itemDescription(purchasesTransaction.getItemList()));
            setTotalAmount(purchasesTransaction.getItemList());
            setBalance(total, amountPaidConverter(amountPaidChecker(amountPaid.getText())));
            totalAmount.setText("₱ " + setDecimalFormat(this.total));
            balance.setText("₱ " + setDecimalFormat(this.remainingBalance));
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select an item to be removed!");
        }
    }//GEN-LAST:event_removeItemActionPerformed

    private void removeAllItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAllItemActionPerformed
        purchasesTransaction.flushAllItem();
        showItemTable();
        description.setText(itemDescription(purchasesTransaction.getItemList()));
        setTotalAmount(purchasesTransaction.getItemList());
        setBalance(total, amountPaidConverter(amountPaidChecker(amountPaid.getText())));
        totalAmount.setText("₱ " + setDecimalFormat(this.total));
        balance.setText("₱ " + setDecimalFormat(this.remainingBalance));
    }//GEN-LAST:event_removeAllItemActionPerformed

    private void addSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSupplierActionPerformed
        AddVendor addVendor = new AddVendor(this, true);
        addVendor.setVisible(true);
    }//GEN-LAST:event_addSupplierActionPerformed

    private void selectSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectSupplierActionPerformed
        int selectedRow = supplierTable.getSelectedRow();
        if(selectedRow >= 0){
            supplier = supplierList.get(selectedRow);
            supplierName.setText(supplier.getName());
            phoneNumber.setText(supplier.getPhoneNumber());
            addPurchasesTabbedPane.setSelectedIndex(0);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a customer!");
        }
    }//GEN-LAST:event_selectSupplierActionPerformed

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
       dispose();
    }//GEN-LAST:event_cancelActionPerformed

    private void addPurchasesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPurchasesActionPerformed
        if(date.getDate() != null){
            if(paymentMethod.getSelectedIndex() > 0){
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String formattedDate = dateFormat.format(date.getDate());
                String formattedDueDate = "Not Applicable"; 
                if(dueDate.getDate() != null){formattedDueDate = dateFormat.format(dueDate.getDate());}
                ArrayList<Account> assetList = new AccountDBController().getAllAssetAccount();
                Account account = assetList.get(assetAccount.getSelectedIndex() - 1);
                int paymentIndex = paymentMethod.getSelectedIndex() - 1;
                int paymentCode = accountCodes.get(paymentIndex);

                if(supplierName.getText().isBlank()){
                    JOptionPane.showMessageDialog(rootPane, "Please select a supplier!");
                } else if (purchasesTransaction.getItemList().isEmpty()){
                    JOptionPane.showMessageDialog(rootPane, "Please add at least 1 item or service!");
                } else if (!isNumeric(amountPaid.getText())){
                    JOptionPane.showMessageDialog(rootPane, "Please enter a numeric amount!");
                } else if(Double.parseDouble(amountPaid.getText()) > total){
                        JOptionPane.showMessageDialog(rootPane, "Amount to pay is more than the total amount!");
                } else if (amountPaidConverter(amountPaidChecker(amountPaid.getText())) == 0.00){
                    JOptionPane.showMessageDialog(rootPane, "Please enter an amount paid!");
                } else if (!isApplicable.isSelected() && dueDate.getDate() == null){
                    JOptionPane.showMessageDialog(rootPane, "Please enter a due date or click the Not Applicable checkbox!");
                } else if (purchaser.getText().isBlank()){
                    JOptionPane.showMessageDialog(rootPane, "Please indicate who is the purchaser!");
                } else if (assetAccount.getSelectedIndex() == 0){
                    JOptionPane.showMessageDialog(rootPane, "Please select an asset account!");
                } else if (!isPathCorrect(documentPath.getText())){
                    JOptionPane.showMessageDialog(rootPane, "Incorrect Path!");
                } else {
                    int n = JOptionPane.showConfirmDialog(rootPane, "Are you sure all of the information that you've entered are correct?");

                    if(n == 0){
                            new EntryDBController().addPurchases(formattedDate, formattedDueDate, supplier.getId(), total, 
                                    Double.parseDouble(amountPaid.getText()), remainingBalance, paymentCode, account.getCode(), 
                                    purchaser.getText(), new Dashboard().stringEscape(description.getText()), 
                                    remarks(),purchasesTransaction.getItemList(), user.getId(), documentPath.getText());
                            JOptionPane.showMessageDialog(rootPane, "Transaction was successfully added to the database!");
                            dashboardListener.updatePurchasesTable();
                            dispose();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "Please select a payment method!");
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please enter a date!");
        }
    }//GEN-LAST:event_addPurchasesActionPerformed

    private void choosePhotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_choosePhotoActionPerformed
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.showOpenDialog(null);
            File file = chooser.getSelectedFile();
            if(file != null){
                String filePath = file.getAbsolutePath();
                documentPath.setText(filePath);
            }
        } catch (NullPointerException e) {}
    }//GEN-LAST:event_choosePhotoActionPerformed

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
            java.util.logging.Logger.getLogger(AddPurchases.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddPurchases.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddPurchases.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddPurchases.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddPurchases().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addItem;
    private javax.swing.JButton addPurchases;
    private javax.swing.JTabbedPane addPurchasesTabbedPane;
    private javax.swing.JButton addSupplier;
    private javax.swing.JTextField amountPaid;
    private javax.swing.JComboBox<String> assetAccount;
    private javax.swing.JLabel balance;
    private javax.swing.JButton cancel;
    private javax.swing.JButton choosePhoto;
    public com.toedter.calendar.JDateChooser date;
    private javax.swing.JTextArea description;
    private javax.swing.JTextField documentPath;
    private com.toedter.calendar.JDateChooser dueDate;
    private javax.swing.JButton editItem;
    private javax.swing.JCheckBox isApplicable;
    private javax.swing.JTable itemTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JComboBox<String> paymentMethod;
    private javax.swing.JTextField phoneNumber;
    private javax.swing.JTextField purchaser;
    private javax.swing.JButton removeAllItem;
    private javax.swing.JButton removeItem;
    private javax.swing.JLabel searchSupplier;
    private javax.swing.JTextField searchSupplierField;
    private javax.swing.JButton selectSupplier;
    private javax.swing.JTextField supplierName;
    private javax.swing.JTable supplierTable;
    private javax.swing.JLabel totalAmount;
    // End of variables declaration//GEN-END:variables
}
