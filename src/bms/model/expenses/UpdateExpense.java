/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package bms.model.expenses;

import bms.model.generaljournal.*;
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


/**
 *
 * @author Vienji
 */
public class UpdateExpense extends javax.swing.JFrame implements GeneralJournalListener{
    private DashboardListener listener;
    private User currentUser;
    private ArrayList<Account> accountList = new ArrayList();
    private String id;
    /**
     * Creates new form AddEntry
     */
    public UpdateExpense() {
        initComponents();
    }
    
    public UpdateExpense(User user, DashboardListener listener, ArrayList<Account> accountList, String id){
        initComponents();
        this.currentUser = user;
        this.listener = listener;
        this.accountList = accountList;
        this.id = id;
        showAccountTable();
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
        save = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Update Expense");
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

        save.setText("Save");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(save, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cancel))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
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
                                    .addComponent(description, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE))))))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(save)
                    .addComponent(cancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        dispose();
    }//GEN-LAST:event_cancelActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        if(date.getDate() != null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String formattedDate = dateFormat.format(date.getDate());
            
            if(accountList.isEmpty() || !hasBothDebitAndCredit(accountList)){
                JOptionPane.showMessageDialog(rootPane, "Please add at least 1 debit and credit account!");
            } else if(description.getText().isBlank()){
                JOptionPane.showMessageDialog(rootPane, "Please write a description!");
            } else if (isAmbigous()){
                JOptionPane.showMessageDialog(rootPane, ambiguityReport());
            } else if(!isEntryBalance(accountList)){
                JOptionPane.showMessageDialog(rootPane, "Please make sure that debit and credit are equal!");
            } else {

                int n = JOptionPane.showConfirmDialog(rootPane, "Are you sure that you want to save these changes?");

                if(n == 0){
                    new EntryDBController().updateExpensesEntry(id, formattedDate, 
                            new Dashboard().stringEscape(description.getText()), currentUser.getId(), accountList);
                    JOptionPane.showMessageDialog(rootPane, "Expenses transaction was successfully updated!");                                      
                    listener.updateJournalTable();
                    dispose();
                }          
            } 
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please enter a date!");
        }          
    }//GEN-LAST:event_saveActionPerformed

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
            java.util.logging.Logger.getLogger(UpdateExpense.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UpdateExpense.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UpdateExpense.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UpdateExpense.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UpdateExpense().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable accountTable;
    private javax.swing.JButton addAccount;
    private javax.swing.JButton cancel;
    public com.toedter.calendar.JDateChooser date;
    public javax.swing.JTextField description;
    private javax.swing.JButton editAccount;
    public javax.swing.JTextField encoder;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton removeAccount;
    private javax.swing.JButton removeAllAccounts;
    private javax.swing.JButton save;
    // End of variables declaration//GEN-END:variables
}
