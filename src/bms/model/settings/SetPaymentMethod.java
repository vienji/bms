/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package bms.model.settings;

import bms.classes.Account;
import bms.dbcontroller.AccountDBController;
import bms.listeners.DashboardListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Vienji
 */
public class SetPaymentMethod extends javax.swing.JFrame {
    private DashboardListener listener;
    private ArrayList<Account> accountSelectionList = new AccountDBController().getAllAccounts();
    private ArrayList<Account> accountList = new ArrayList();
    private boolean isSales = false;
    /**
     * Creates new form SetPaymentMethod
     */
    public SetPaymentMethod() {
        initComponents();
    }

    public SetPaymentMethod(DashboardListener listener, ArrayList<Account> accountList, boolean isSales){
        initComponents();
        initAccountSelectionBox();
        this.accountList = accountList;
        this.listener = listener;
        this.isSales = isSales;
        showAccountTable();
    }
    
    private void initAccountSelectionBox(){            
        accountSelectionList.forEach((e) -> {
            accountSelectionBox.addItem(e.getAccountName());
        });
    }
    
    private void showAccountTable(){
        accountTable.setModel(new DefaultTableModel(null, new String[] {"Account", "Type", "Normally"}));
        DefaultTableModel populatedAccountTable = (DefaultTableModel) accountTable.getModel();
        
        Iterator i = (Iterator) accountList.iterator();
        
        while(i.hasNext()){
            Account account = (Account) i.next();
            
            String[] data = {account.getAccountName(), account.getAccountType(), account.getNormally()};
            populatedAccountTable.addRow(data);
        }
    }
    
    private boolean hasDuplicate(Account account, ArrayList<Account> accountList){
        Iterator i = (Iterator) accountList.iterator();
        while(i.hasNext()){
            Account accountMirror = (Account) i.next();
            if(account.getAccountName().equals(accountMirror.getAccountName()) 
                    && account.getCode() == accountMirror.getCode()){
                return true;
            }
        }
        return false;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        accountTable = new javax.swing.JTable();
        removeAccount = new javax.swing.JButton();
        removeAllAccount = new javax.swing.JButton();
        accountSelectionBox = new javax.swing.JComboBox<>();
        cancel = new javax.swing.JButton();
        ok = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Set Payment Method");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        accountTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Account", "Type", "Normally"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true
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

        removeAccount.setText("Remove");
        removeAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAccountActionPerformed(evt);
            }
        });

        removeAllAccount.setText("Remove All");
        removeAllAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAllAccountActionPerformed(evt);
            }
        });

        accountSelectionBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Account" }));
        accountSelectionBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accountSelectionBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(removeAccount, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                    .addComponent(removeAllAccount, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                    .addComponent(accountSelectionBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(accountSelectionBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeAccount)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeAllAccount)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        ok.setText("Ok");
        ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(ok)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancel)
                    .addComponent(ok))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void accountSelectionBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accountSelectionBoxActionPerformed
        Account account = accountSelectionList.get(accountSelectionBox.getSelectedIndex() - 1);
        if(!hasDuplicate(account, accountList)){
            accountList.add(account);
            showAccountTable();
            accountSelectionBox.setSelectedIndex(0);
        } else {
            JOptionPane.showMessageDialog(rootPane, "You have already added " + account.getAccountName() + " into the table!");
            accountSelectionBox.setSelectedIndex(0);
        }
    }//GEN-LAST:event_accountSelectionBoxActionPerformed

    private void removeAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAccountActionPerformed
        int selectedRow = accountTable.getSelectedRow();
        if(selectedRow >= 0){
            accountList.remove(selectedRow);
            showAccountTable();
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select an account to be removed!");
        }
    }//GEN-LAST:event_removeAccountActionPerformed

    private void removeAllAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAllAccountActionPerformed
        accountList.clear();
        showAccountTable();
    }//GEN-LAST:event_removeAllAccountActionPerformed

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        dispose();
    }//GEN-LAST:event_cancelActionPerformed

    private void okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okActionPerformed
        if(!accountList.isEmpty()){
            if(isSales){
                listener.updateSalesPaymentConfig(accountList);
            } else {
                listener.updatePurchasesPaymentConfig(accountList);
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please add at least 1 account!");
        }       
    }//GEN-LAST:event_okActionPerformed

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
            java.util.logging.Logger.getLogger(SetPaymentMethod.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SetPaymentMethod.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SetPaymentMethod.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SetPaymentMethod.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SetPaymentMethod().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> accountSelectionBox;
    private javax.swing.JTable accountTable;
    private javax.swing.JButton cancel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton ok;
    private javax.swing.JButton removeAccount;
    private javax.swing.JButton removeAllAccount;
    // End of variables declaration//GEN-END:variables
}
