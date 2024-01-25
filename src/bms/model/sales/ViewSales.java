/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package bms.model.sales;

import bms.classes.Account;
import bms.classes.Cheque;
import bms.classes.Item;
import bms.classes.SalesTransaction;
import bms.classes.User;
import bms.dbcontroller.AccountDBController;
import bms.dbcontroller.EntryDBController;
import bms.listeners.DashboardListener;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Vienji
 */
public class ViewSales extends javax.swing.JFrame {
    private DashboardListener dashboardListener;
    private SalesTransaction salesTransaction;
    private User user;
    private ArrayList<Integer> accountCodes = getAccountCodes();
    ArrayList<Account>  paymentHistory= new ArrayList();
    /**
     * Creates new form ViewSales
     */
    public ViewSales() {
        initComponents();
    }
    
    public ViewSales(SalesTransaction salesTransaction, DashboardListener dashboardListener, User user) {
        initComponents();
        this.salesTransaction = salesTransaction;
        this.dashboardListener = dashboardListener;
        this.user = user;
        if(this.salesTransaction.getRemainingBalance() == 0.00){
            amountToPay.setEnabled(false);
            addPayment.setEnabled(false);
            chequeNumber.setEnabled(false);
            dateIssued.setEnabled(false);
            payee.setEnabled(false);
            accountNumber.setEnabled(false);
            bankName.setEnabled(false);
        }
        initData();
        initPaymentMethods();
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
                paymentMethodSelectionBox.addItem(account.getAccountName());
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
    
    private void showPaymentHistory(){
        paymentHistoryTable.setModel(new DefaultTableModel(null, new String[]{"Date", "Method", "Remarks", "Amount"}));
        DefaultTableModel populatedPaymentHistory = (DefaultTableModel) paymentHistoryTable.getModel();
        paymentHistory = new EntryDBController().getSalesPaymentHistory(salesTransaction.getId());
        
        Iterator i = (Iterator) paymentHistory.iterator();
        
        while(i.hasNext()){
            Account account = (Account) i.next();
            
            String[] paymentHistoryData= {account.getDate(), new AccountDBController().getAccount(account.getCode()).getAccountName()
                    , account.getRemarks(), "₱ " + setDecimalFormat(account.getAmount())};
            populatedPaymentHistory.addRow(paymentHistoryData);
        }
    }
    
    private void initData(){
        showItemTable();
        showPaymentHistory();
        date.setText(salesTransaction.getDate());
        customerName.setText(salesTransaction.getCustomerName());
        phoneNumber.setText(salesTransaction.getCustomerContact());
        totalAmount.setText("₱ "+ setDecimalFormat(salesTransaction.getTotalAmount()));
        amountPaid.setText("₱ "+ setDecimalFormat(salesTransaction.getAmountPaid()));
        balance.setText("₱ "+ setDecimalFormat(salesTransaction.getRemainingBalance()));
        description.setText(salesTransaction.getDescription());
        salesPerson.setText(salesTransaction.getSalesPerson());
    }
    
    private String setDecimalFormat(Double amount){
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        
        return decimalFormat.format(amount);
    }
    
    private boolean isNumeric(String amount){       
        try{
            Double.parseDouble(amount);
            return true;
        } catch (NumberFormatException e){
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        close = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        description = new javax.swing.JTextArea();
        jLabel14 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        amountToPay = new javax.swing.JTextField();
        addPayment = new javax.swing.JButton();
        balance = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        amountPaid = new javax.swing.JLabel();
        totalAmount = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        itemTable = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        phoneNumber = new javax.swing.JTextField();
        customerName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        salesPerson = new javax.swing.JLabel();
        paymentMethodSelectionBox = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        chequeNumber = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        dateIssued = new com.toedter.calendar.JDateChooser();
        jLabel13 = new javax.swing.JLabel();
        payee = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        accountNumber = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        bankName = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        paymentHistoryTable = new javax.swing.JTable();
        chequeInfo = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("View Sales Transaction");
        setResizable(false);

        close.setText("Close");
        close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeActionPerformed(evt);
            }
        });

        description.setEditable(false);
        description.setColumns(20);
        description.setRows(5);
        jScrollPane2.setViewportView(description);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Description:");

        jLabel9.setText("Amount to pay:");

        addPayment.setText("Add Payment");
        addPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPaymentActionPerformed(evt);
            }
        });

        balance.setText("₱ 0.00");

        jLabel7.setText("Balance:");

        jLabel6.setText("Amount Paid:");

        amountPaid.setText("₱ 0.00");

        totalAmount.setText("₱ 0.00");

        jLabel5.setText("Total Amount:");

        itemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item/Service", "Qty.", "Description", "Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        itemTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(itemTable);
        if (itemTable.getColumnModel().getColumnCount() > 0) {
            itemTable.getColumnModel().getColumn(0).setResizable(false);
            itemTable.getColumnModel().getColumn(1).setResizable(false);
            itemTable.getColumnModel().getColumn(2).setResizable(false);
            itemTable.getColumnModel().getColumn(3).setResizable(false);
        }

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("List of Items and Services Sold");

        jLabel3.setText("Phone Number:");

        phoneNumber.setEditable(false);

        customerName.setEditable(false);

        jLabel2.setText("Customer Name:");

        jLabel1.setText("Date:");

        date.setText("00/00/0000");

        jLabel10.setText("Sales Person: ");

        salesPerson.setText("salesPerson");

        paymentMethodSelectionBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("For Cheques:");

        jLabel11.setText("Cheque Number:");

        jLabel12.setText("Date Issued:");

        dateIssued.setDateFormatString("MM/dd/yyyy");

        jLabel13.setText("Payee");

        jLabel15.setText("Account Number:");

        jLabel16.setText("Bank Name:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(date))
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(customerName, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(phoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(salesPerson)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(close, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(amountPaid)
                                    .addComponent(balance)
                                    .addComponent(totalAmount)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(amountToPay)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(paymentMethodSelectionBox, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(addPayment))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(chequeNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                            .addComponent(jLabel11)
                                            .addComponent(jLabel15))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(dateIssued, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING)))
                                    .addComponent(accountNumber))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(payee)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel16)
                                            .addComponent(jLabel13))
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(bankName)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGap(26, 26, 26))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(date))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(customerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(phoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(totalAmount))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(amountPaid))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(balance))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(amountToPay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addPayment)
                    .addComponent(paymentMethodSelectionBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chequeNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateIssued, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(payee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(accountNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bankName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(close)
                    .addComponent(jLabel10)
                    .addComponent(salesPerson))
                .addGap(0, 16, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Sales Transaction", jPanel1);

        paymentHistoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Method", "Remarks", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        paymentHistoryTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(paymentHistoryTable);
        if (paymentHistoryTable.getColumnModel().getColumnCount() > 0) {
            paymentHistoryTable.getColumnModel().getColumn(0).setResizable(false);
            paymentHistoryTable.getColumnModel().getColumn(1).setResizable(false);
            paymentHistoryTable.getColumnModel().getColumn(2).setResizable(false);
            paymentHistoryTable.getColumnModel().getColumn(3).setResizable(false);
        }

        chequeInfo.setText("View Cheque Info");
        chequeInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chequeInfoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(chequeInfo)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(39, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 577, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(chequeInfo)
                .addGap(41, 41, 41))
        );

        jTabbedPane1.addTab("Payment History", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 525, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeActionPerformed
        dispose();
    }//GEN-LAST:event_closeActionPerformed

    private void addPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPaymentActionPerformed
        int paymentIndex = paymentMethodSelectionBox.getSelectedIndex() - 1;
         
        if(!isNumeric(amountToPay.getText())){
            JOptionPane.showMessageDialog(rootPane, "Please enter a numeric amount!");
        } else if (paymentIndex < 0){
            JOptionPane.showMessageDialog(rootPane, "Please select a payment method!");
        } else if (Double.parseDouble(amountToPay.getText()) > new EntryDBController().getSalesBalance(salesTransaction.getId()) ){
            JOptionPane.showMessageDialog(rootPane, "Payment amount is more than the balance!");
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
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    String formattedDate = dateFormat.format(dateIssued.getDate());
                    cheque.setChequeNumber(chequeNumber.getText());
                    cheque.setPayee(payee.getText());
                    cheque.setAccountNumber(accountNumber.getText());
                    cheque.setBankName(bankName.getText());
                    cheque.setDateIssued(formattedDate);
                    isCheque = true;
                }
            }
            
            int n = JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to add this payment?");
            int paymentCode = accountCodes.get(paymentIndex);
            if(n == 0){
                String remarks = Double.parseDouble(amountToPay.getText()) >= new EntryDBController().getSalesBalance(salesTransaction.getId()) ? "Fully paid" : "Partially paid";
                
                if(isCheque){
                    new EntryDBController().paySalesTransaction(salesTransaction.getId(), Double.parseDouble(amountToPay.getText()), 
                            remarks, user.getId(), paymentCode, cheque);
                } else {
                    new EntryDBController().paySalesTransaction(salesTransaction.getId(), Double.parseDouble(amountToPay.getText()), 
                            remarks, user.getId(), paymentCode);
                }
                
                amountPaid.setText("₱ "+ setDecimalFormat(new EntryDBController().getSalesAmountPaid(salesTransaction.getId())));
                balance.setText("₱ "+ setDecimalFormat(new EntryDBController().getSalesBalance(salesTransaction.getId())));           
                showPaymentHistory();
                amountToPay.setText("");
                chequeNumber.setText("");
                payee.setText("");
                accountNumber.setText("");
                bankName.setText("");
                if(new EntryDBController().getSalesBalance(salesTransaction.getId()) == 0.00){
                    amountToPay.setEnabled(false);
                    addPayment.setEnabled(false);
                    chequeNumber.setEnabled(false);
                    dateIssued.setEnabled(false);
                    payee.setEnabled(false);
                    accountNumber.setEnabled(false);
                    bankName.setEnabled(false);
                }
                dashboardListener.updateSalesTable();
            }  
        }       
    }//GEN-LAST:event_addPaymentActionPerformed

    private void chequeInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chequeInfoActionPerformed
        int selectedRow = paymentHistoryTable.getSelectedRow();
        
        if(selectedRow > -1){
            if(new EntryDBController().hasChequeInfo(paymentHistory.get(selectedRow).getRefId())){
                Cheque cheque = new EntryDBController().getCheque(paymentHistory.get(selectedRow).getRefId());
                ChequeInfo info = new ChequeInfo(cheque);
                info.setVisible(true);
             } else {
                JOptionPane.showMessageDialog(rootPane, "Selected row is neither a cheque or has no cheque information recorded!");
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a row to be viewed!");
        }
    }//GEN-LAST:event_chequeInfoActionPerformed

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
            java.util.logging.Logger.getLogger(ViewSales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewSales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewSales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewSales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewSales().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField accountNumber;
    public javax.swing.JButton addPayment;
    private javax.swing.JLabel amountPaid;
    private javax.swing.JTextField amountToPay;
    private javax.swing.JLabel balance;
    private javax.swing.JTextField bankName;
    private javax.swing.JButton chequeInfo;
    private javax.swing.JTextField chequeNumber;
    private javax.swing.JButton close;
    private javax.swing.JTextField customerName;
    private javax.swing.JLabel date;
    private com.toedter.calendar.JDateChooser dateIssued;
    private javax.swing.JTextArea description;
    private javax.swing.JTable itemTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField payee;
    private javax.swing.JTable paymentHistoryTable;
    private javax.swing.JComboBox<String> paymentMethodSelectionBox;
    private javax.swing.JTextField phoneNumber;
    public javax.swing.JLabel salesPerson;
    private javax.swing.JLabel totalAmount;
    // End of variables declaration//GEN-END:variables
}
