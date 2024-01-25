/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package bms.model.purchases;

import bms.classes.Account;
import bms.classes.Item;
import bms.classes.PurchaseTransaction;
import bms.classes.User;
import bms.dbcontroller.AccountDBController;
import bms.dbcontroller.EntryDBController;
import bms.listeners.DashboardListener;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Vienji
 */
public class ViewPurchases extends javax.swing.JFrame {
    private DashboardListener dashboardListener;
    private PurchaseTransaction purchaseTransaction;
    private User user;
    private ArrayList<Integer> accountCodes = getAccountCodes();
    /**
     * Creates new form ViewPurchases
     */
    public ViewPurchases() {
        initComponents();
    }
    
    public ViewPurchases(PurchaseTransaction purchaseTransaction, DashboardListener dashboardListener, User user) {
        initComponents();
        this.purchaseTransaction = purchaseTransaction;
        this.dashboardListener = dashboardListener;
        this.user = user;
        if(this.purchaseTransaction.getRemainingBalance() == 0.00){
            amountToPay.setEnabled(false);
            addPayment.setEnabled(false);
        }
        initData();
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
                paymentMethodSelectionBox.addItem(account.getAccountName());
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
    
    private void showItemTable(){
        itemTable.setModel(new DefaultTableModel(null, new String[]{"Item", "Qty.", "Description", "Price"}));
        DefaultTableModel populatedItemTable = (DefaultTableModel) itemTable.getModel();
        
        Iterator i = (Iterator) purchaseTransaction.getItemList().iterator();
        
        while(i.hasNext()){
            Item item = (Item) i.next();
            
            String[] itemData = {item.getName(), String.valueOf(item.getQuantity()), 
                item.getDescription(), " ₱ " + setDecimalFormat(item.getPrice())};
            populatedItemTable.addRow(itemData);
        }
    }
    
    private void showPaymentHistory(){
        paymentHistoryTable.setModel(new DefaultTableModel(null, new String[]{"Date", "Remarks", "Amount"}));
        DefaultTableModel populatedPaymentHistory = (DefaultTableModel) paymentHistoryTable.getModel();
        ArrayList<Account> paymentHistory = new EntryDBController().getPurchasesPaymentHistory(purchaseTransaction.getId());
        
        Iterator i = (Iterator) paymentHistory.iterator();
        
        while(i.hasNext()){
            Account account = (Account) i.next();
            
            String[] paymentHistoryData= {account.getDate(), account.getRemarks(), "₱ " + setDecimalFormat(account.getAmount())};
            populatedPaymentHistory.addRow(paymentHistoryData);
        }
    }
    
    private void initData(){
        showItemTable();
        showPaymentHistory();
        date.setText(purchaseTransaction.getDate());
        dueDate.setText(purchaseTransaction.getDueDate());
        supplierName.setText(purchaseTransaction.getSupplier());
        phoneNumber.setText(purchaseTransaction.getSupplierContact());
        totalAmount.setText("₱ " + setDecimalFormat(purchaseTransaction.getTotalAmount()));
        amountPaid.setText("₱ " + setDecimalFormat(purchaseTransaction.getAmountPaid()));
        balance.setText("₱ " + setDecimalFormat(purchaseTransaction.getRemainingBalance()));
        paymentMethod.setText(purchaseTransaction.getPaymentMethod().getAccountName());
        assetAccount.setText(purchaseTransaction.getAssetAccount().getAccountName());
        purchaser.setText(purchaseTransaction.getPurchaser());
        description.setText(purchaseTransaction.getDescription());
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
        date = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        phoneNumber = new javax.swing.JTextField();
        supplierName = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        dueDate = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        itemTable = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        totalAmount = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        amountPaid = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        balance = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        amountToPay = new javax.swing.JTextField();
        addPayment = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        paymentMethod = new javax.swing.JLabel();
        purchaser = new javax.swing.JLabel();
        assetAccount = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        description = new javax.swing.JTextArea();
        jLabel21 = new javax.swing.JLabel();
        close = new javax.swing.JButton();
        paymentMethodSelectionBox = new javax.swing.JComboBox<>();
        viewSupportingDocument = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        paymentHistoryTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("View Purchases");
        setResizable(false);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        date.setText("00/00/0000");
        jPanel1.add(date, new org.netbeans.lib.awtextra.AbsoluteConstraints(77, 21, -1, -1));

        jLabel1.setText("Date:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(38, 21, -1, -1));

        jLabel3.setText("Supplier Name:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 58, -1, -1));

        jLabel4.setText("Phone Number:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 98, -1, -1));

        phoneNumber.setEditable(false);
        jPanel1.add(phoneNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 95, 188, -1));

        supplierName.setEditable(false);
        jPanel1.add(supplierName, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 55, 188, -1));

        jLabel9.setText("Due Date:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(375, 21, -1, -1));

        dueDate.setText("00/00/0000");
        jPanel1.add(dueDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(438, 21, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("List of Items");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 147, -1, -1));

        itemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item", "Qty.", "Description", "Price"
            }
        ));
        jScrollPane1.setViewportView(itemTable);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 170, 474, 110));

        jLabel6.setText("Total Amount:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(38, 298, -1, -1));

        totalAmount.setText("₱ 0.00");
        jPanel1.add(totalAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(121, 298, -1, -1));

        jLabel7.setText("Amount Paid:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 332, -1, -1));

        amountPaid.setText("₱ 0.00");
        jPanel1.add(amountPaid, new org.netbeans.lib.awtextra.AbsoluteConstraints(121, 332, -1, -1));

        jLabel8.setText("Balance:");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 366, -1, -1));

        balance.setText("₱ 0.00");
        jPanel1.add(balance, new org.netbeans.lib.awtextra.AbsoluteConstraints(121, 366, -1, -1));

        jLabel16.setText("Amount to pay:");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 404, -1, -1));
        jPanel1.add(amountToPay, new org.netbeans.lib.awtextra.AbsoluteConstraints(121, 401, 146, -1));

        addPayment.setText("Add Payment");
        addPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPaymentActionPerformed(evt);
            }
        });
        jPanel1.add(addPayment, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 400, -1, -1));

        jLabel14.setText("Purchaser:");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(307, 366, -1, -1));

        jLabel19.setText("Asset Account:");
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(283, 332, -1, -1));

        jLabel18.setText("Payment Method:");
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(267, 298, -1, -1));

        paymentMethod.setText("method");
        jPanel1.add(paymentMethod, new org.netbeans.lib.awtextra.AbsoluteConstraints(369, 298, -1, -1));

        purchaser.setText("name");
        jPanel1.add(purchaser, new org.netbeans.lib.awtextra.AbsoluteConstraints(369, 366, -1, -1));

        assetAccount.setText("asset");
        jPanel1.add(assetAccount, new org.netbeans.lib.awtextra.AbsoluteConstraints(369, 332, -1, -1));

        description.setEditable(false);
        description.setColumns(20);
        description.setRows(5);
        jScrollPane2.setViewportView(description);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 485, 474, 101));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setText("Description:");
        jPanel1.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 462, -1, -1));

        close.setText("Close");
        close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeActionPerformed(evt);
            }
        });
        jPanel1.add(close, new org.netbeans.lib.awtextra.AbsoluteConstraints(425, 604, 80, -1));

        paymentMethodSelectionBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        jPanel1.add(paymentMethodSelectionBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(274, 401, 119, -1));

        viewSupportingDocument.setText("View Supporting Document");
        viewSupportingDocument.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewSupportingDocumentActionPerformed(evt);
            }
        });
        jPanel1.add(viewSupportingDocument, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 450, -1, -1));

        jTabbedPane1.addTab("Purchases Transaction", jPanel1);

        paymentHistoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Remarks", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        paymentHistoryTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(paymentHistoryTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 563, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Payment History", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 536, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        }  else if (paymentIndex < 0){
            JOptionPane.showMessageDialog(rootPane, "Please select a payment method!");
        }   else if(Double.parseDouble(amountToPay.getText()) > new EntryDBController().getPurchasesBalance(purchaseTransaction.getId()) ){
            JOptionPane.showMessageDialog(rootPane, "Payment amount is more than the balance!");
        } else {
            int n = JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to add this payment?");
            int paymentCode = accountCodes.get(paymentIndex);
            if(n == 0){
                String remarks = Double.parseDouble(amountToPay.getText()) >= new EntryDBController().getPurchasesBalance(purchaseTransaction.getId()) ? "Fully paid" : "Partially paid";
                
                new EntryDBController().payPurchasesTransaction(purchaseTransaction.getId(), Double.parseDouble(amountToPay.getText()), remarks, user.getId(), paymentCode);
                amountPaid.setText("₱ "+ setDecimalFormat(new EntryDBController().getPurchasesAmountPaid(purchaseTransaction.getId())));
                balance.setText("₱ "+ setDecimalFormat(new EntryDBController().getPurchasesBalance(purchaseTransaction.getId())));
                showPaymentHistory();
                if(new EntryDBController().getPurchasesBalance(purchaseTransaction.getId()) == 0.00){
                    amountToPay.setEnabled(false);
                    addPayment.setEnabled(false);
                }
                dashboardListener.updatePurchasesTable();
                amountToPay.setText("");
            }           
        }
    }//GEN-LAST:event_addPaymentActionPerformed

    private void viewSupportingDocumentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewSupportingDocumentActionPerformed
        try{
            ViewDocument viewDocument = new ViewDocument();
            viewDocument.document.setIcon(new ImageIcon(new EntryDBController().getDocument(
                    purchaseTransaction.getId()).getScaledInstance(viewDocument.document.getWidth(), 
                      viewDocument.document.getHeight(), Image.SCALE_SMOOTH)));
            viewDocument.setVisible(true);
        } catch (NullPointerException e){
            JOptionPane.showMessageDialog(rootPane, "No Document Found!");
        }  
    }//GEN-LAST:event_viewSupportingDocumentActionPerformed

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
            java.util.logging.Logger.getLogger(ViewPurchases.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewPurchases.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewPurchases.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewPurchases.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewPurchases().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addPayment;
    private javax.swing.JLabel amountPaid;
    private javax.swing.JTextField amountToPay;
    private javax.swing.JLabel assetAccount;
    private javax.swing.JLabel balance;
    private javax.swing.JButton close;
    private javax.swing.JLabel date;
    private javax.swing.JTextArea description;
    private javax.swing.JLabel dueDate;
    private javax.swing.JTable itemTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel21;
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
    private javax.swing.JTable paymentHistoryTable;
    private javax.swing.JLabel paymentMethod;
    private javax.swing.JComboBox<String> paymentMethodSelectionBox;
    private javax.swing.JTextField phoneNumber;
    private javax.swing.JLabel purchaser;
    private javax.swing.JTextField supplierName;
    private javax.swing.JLabel totalAmount;
    private javax.swing.JButton viewSupportingDocument;
    // End of variables declaration//GEN-END:variables
}
