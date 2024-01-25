/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package bms.model.generaljournal;

import bms.classes.Account;
import bms.classes.User;
import bms.dbcontroller.EntryDBController;
import bms.listeners.DashboardListener;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import java.util.Iterator;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;
import bms.listeners.GeneralJournalListener;
import bms.model.Dashboard;
import java.io.File;
import javax.swing.JFileChooser;


/**
 *
 * @author Vienji
 */
public class AddEntry extends javax.swing.JFrame implements GeneralJournalListener{
    private DashboardListener listener;
    private User currentUser;
    private String documentPath;
    private ArrayList<Account> accountList = new ArrayList();
    /**
     * Creates new form AddEntry
     */
    public AddEntry() {
        initComponents();
    }
    
    public AddEntry(User user, DashboardListener listener){
        initComponents();
        this.currentUser = user;
        this.listener = listener;
    }
    
    @Override
    public void addEntryAccount(Account account){
        if(hasDuplicate(account, accountList)){
            addAccountAmount(getIndexOf(account), account.getAmount());
        } else {
            accountList.add(account);
        }
        showAccountTable();
    }
    
    @Override
    public void updateEntryAccount(Account account, int accountIndex){
        if(hasDuplicate(account, accountList)){
            updateAccountAmount(getIndexOf(account), account.getAmount());
        } else {
            accountList.set(accountIndex, account);
        }
        showAccountTable();
    }
    
    private void showAccountTable(){
        accountList = sortAccount(accountList);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        accountTable.setModel(new DefaultTableModel(null, new String[]{"Account", "Debit", "Credit"}));
        DefaultTableModel populatedAccountTable = (DefaultTableModel) accountTable.getModel();
        Double debit = 0.00, credit = 0.00;

        Iterator i = (Iterator) accountList.iterator();
        
        while(i.hasNext()){
            Account account = (Account) i.next();
            if(account.getAction().equalsIgnoreCase("Debit")){
                String[] accountData = {account.getAccountName(), " ₱ " + decimalFormat.format(account.getAmount()), " "};              
                populatedAccountTable.addRow(accountData);
                debit += account.getAmount();
            } else {
                String[] accountData = {account.getAccountName(), " ", " ₱ " + decimalFormat.format(account.getAmount())};
                populatedAccountTable.addRow(accountData);
                credit += account.getAmount();
            }
        }
        
        if(!accountList.isEmpty()){
            String[] total = {"Total", " ₱ " + decimalFormat.format(debit), " ₱ " + decimalFormat.format(credit)};
            populatedAccountTable.addRow(total);
        }      
    }
    
    //Account List management
    private boolean hasDuplicate(Account account, ArrayList<Account> accountList){
        Iterator i = (Iterator) accountList.iterator();
        while(i.hasNext()){
            Account accountMirror = (Account) i.next();
            if(account.getAccountName().equals(accountMirror.getAccountName()) 
                    && account.getCode() == accountMirror.getCode() && account.getAction().equals(accountMirror.getAction())){
                return true;
            }
        }
        return false;
    }
    
    private void addAccountAmount(int index, double amount){
        Account account = accountList.get(index);
        account.setAmount(account.getAmount() + amount);
        accountList.set(index, account);        
    }
    
    private void updateAccountAmount(int index, double amount){
        Account account = accountList.get(index);
        account.setAmount(amount);
        accountList.set(index, account);
    }
    
    private int getIndexOf(Account account){
        int n = 0;
        for(Account accountMirror : accountList){
            if(account.getAccountName().equals(accountMirror.getAccountName()) 
                    && account.getAction().equalsIgnoreCase(accountMirror.getAction())){
                return n;
            } else {
                n++;
            }
        }
        return n;
    }
    
    private ArrayList<Account> sortAccount(ArrayList<Account> accountList){
        ArrayList<Account> sortedAccountList = new ArrayList();
        accountList.forEach((e) -> {if(e.getAction().equals("Debit")){sortedAccountList.add(e);}});
        accountList.forEach((e) -> {if(e.getAction().equals("Credit")){sortedAccountList.add(e);}});
        return sortedAccountList;
    }
    
    private boolean isEntryBalance(ArrayList<Account> accountList){
        double debit = 0, credit = 0;
        
        for(Account account : accountList){
            if(account.getAction().equals("Debit")){
                debit += account.getAmount();
            } else {
                credit += account.getAmount();
            }
        }
        
        return debit == credit;
    }
    
    private boolean hasBothDebitAndCredit(ArrayList<Account> accountList){
        int debit = 0, credit = 0;
        
        for(Account account : accountList){
            if(account.getAction().equals("Debit")){
                debit++;
            } else {
                credit++;
            }
        }
        
        return credit > 0 && debit > 0;
    }
    
    private boolean isAmbigous(){
        for(int i = 0; i < accountList.size(); i++){
            Account account = accountList.get(i);
            for(int j = 0; j < accountList.size(); j++){
                Account accountMirror = accountList.get(j);
                
                if(account.getAccountName().equals(accountMirror.getAccountName()) 
                    && account.getCode() == accountMirror.getCode() && !account.getAction().equals(accountMirror.getAction())){
                    return true;
                }
            }
        }

        return false;
    }
    
    private String ambiguityReport(){
        String report = "";

        for(int i = 0; i < accountList.size(); i++){
            Account account = accountList.get(i);
            for(int j = 0; j < accountList.size(); j++){
                Account accountMirror = accountList.get(j);
                
                if(account.getAccountName().equals(accountMirror.getAccountName()) 
                    && account.getCode() == accountMirror.getCode() && !account.getAction().equals(accountMirror.getAction())){
                    return account.getAccountName() + " account is ambigous in row " + (i + 1 ) + " and " + (j + 1)+ ". Please check your entries.";
                }
            }
        }

        return report;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        encoder = new javax.swing.JTextField();
        date = new com.toedter.calendar.JDateChooser();
        description = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        accountTable = new javax.swing.JTable();
        editAccount = new javax.swing.JButton();
        removeAllAccounts = new javax.swing.JButton();
        removeAccount = new javax.swing.JButton();
        addAccount = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        addEntry = new javax.swing.JButton();
        addDocument = new javax.swing.JButton();
        isDocumentAdded = new javax.swing.JLabel();

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add Entry");
        setResizable(false);

        jLabel1.setText("Encoder");

        jLabel2.setText("Date");

        jLabel3.setText("Description");

        encoder.setEditable(false);

        date.setDateFormatString("MM/dd/yyyy");

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        accountTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Account", "Debit", "Credit"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        accountTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(accountTable);
        if (accountTable.getColumnModel().getColumnCount() > 0) {
            accountTable.getColumnModel().getColumn(0).setResizable(false);
            accountTable.getColumnModel().getColumn(1).setResizable(false);
            accountTable.getColumnModel().getColumn(2).setResizable(false);
        }

        editAccount.setText("Edit Account");
        editAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editAccountActionPerformed(evt);
            }
        });

        removeAllAccounts.setText("Remove All ");
        removeAllAccounts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAllAccountsActionPerformed(evt);
            }
        });

        removeAccount.setText("Remove");
        removeAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAccountActionPerformed(evt);
            }
        });

        addAccount.setText("Add Account");
        addAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAccountActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addAccount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(removeAccount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(removeAllAccounts, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editAccount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(addAccount)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(removeAccount)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(removeAllAccounts)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(editAccount)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        addEntry.setText("Add");
        addEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEntryActionPerformed(evt);
            }
        });

        addDocument.setText("Add Document");
        addDocument.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDocumentActionPerformed(evt);
            }
        });

        isDocumentAdded.setText("No Document Added");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addDocument)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(isDocumentAdded, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cancel))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(encoder, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(description, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(encoder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(description, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(addEntry)
                            .addComponent(cancel)
                            .addComponent(addDocument))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(isDocumentAdded, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        dispose();
    }//GEN-LAST:event_cancelActionPerformed

    private void addEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEntryActionPerformed
        if(date.getDate() != null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String formattedDate = dateFormat.format(date.getDate());
            
            if(accountList.isEmpty() || !hasBothDebitAndCredit(accountList)){
                JOptionPane.showMessageDialog(rootPane, "Please add at least 1 debit and credit account!");
            } else if (isAmbigous()){
                JOptionPane.showMessageDialog(rootPane, ambiguityReport());
            } else if(description.getText().isBlank()){
                JOptionPane.showMessageDialog(rootPane, "Please write a description!");
            } else if(!isEntryBalance(accountList)){
                JOptionPane.showMessageDialog(rootPane, "Please make sure that debit and credit are equal!");
            } else {

                int n = JOptionPane.showConfirmDialog(rootPane, "Are you sure that all of the data that you've entered are correct?");

                if(n == 0){
                    new EntryDBController().addJournalEntry(formattedDate, 
                            new Dashboard().stringEscape(description.getText()), currentUser.getId(), accountList, documentPath);
                    JOptionPane.showMessageDialog(rootPane, "Journal entry was successfully added!");                                      
                    listener.updateJournalTable();
                    dispose();
                }          
            } 
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please enter a date!");
        }          
    }//GEN-LAST:event_addEntryActionPerformed

    private void addAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAccountActionPerformed
       AddEntryAccount addEntryAccount = new AddEntryAccount(this);
       addEntryAccount.setVisible(true);
    }//GEN-LAST:event_addAccountActionPerformed

    private void removeAllAccountsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAllAccountsActionPerformed
        accountList.clear();
        showAccountTable();
    }//GEN-LAST:event_removeAllAccountsActionPerformed

    private void removeAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAccountActionPerformed
        int selectedRow = accountTable.getSelectedRow();
        if(selectedRow >= 0 && selectedRow < accountTable.getRowCount() - 1){
            accountList.remove(selectedRow);
            showAccountTable();
        } else if(selectedRow == accountTable.getRowCount() - 1){
            JOptionPane.showMessageDialog(rootPane, "Please select an account to be removed!");
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select an account to be removed!");
        }
    }//GEN-LAST:event_removeAccountActionPerformed

    private void editAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editAccountActionPerformed
        int selectedRow = accountTable.getSelectedRow();
        if(selectedRow >= 0 && selectedRow < accountTable.getRowCount() - 1){
            UpdateEntryAccount updateEntryAccount = new UpdateEntryAccount(this, selectedRow);
            updateEntryAccount.accountSelectionBox.setSelectedItem(accountList.get(selectedRow).getAccountName());
            updateEntryAccount.amount.setText(String.valueOf(accountList.get(selectedRow).getAmount()));
            updateEntryAccount.action.setSelectedItem(accountList.get(selectedRow).getAction());
            updateEntryAccount.setVisible(true);
        } else if(selectedRow == accountTable.getRowCount() - 1){
            JOptionPane.showMessageDialog(rootPane, "Please select an account to edit!");
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select an account to edit!");
        }
    }//GEN-LAST:event_editAccountActionPerformed

    private void addDocumentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDocumentActionPerformed
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.showOpenDialog(null);
            File file = chooser.getSelectedFile();
            if(file != null){
                documentPath = file.getAbsolutePath();
                if(!documentPath.isBlank()){
                    isDocumentAdded.setText("Document Added");
                } else {
                    isDocumentAdded.setText("No Document Added");
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_addDocumentActionPerformed

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
            java.util.logging.Logger.getLogger(AddEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddEntry().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable accountTable;
    private javax.swing.JButton addAccount;
    private javax.swing.JButton addDocument;
    private javax.swing.JButton addEntry;
    private javax.swing.JButton cancel;
    public com.toedter.calendar.JDateChooser date;
    private javax.swing.JTextField description;
    private javax.swing.JButton editAccount;
    public javax.swing.JTextField encoder;
    private javax.swing.JLabel isDocumentAdded;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton removeAccount;
    private javax.swing.JButton removeAllAccounts;
    // End of variables declaration//GEN-END:variables
}
