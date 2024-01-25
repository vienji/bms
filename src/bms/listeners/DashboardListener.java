/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package bms.listeners;

import bms.classes.Account;
import java.util.ArrayList;

/**
 *
 * @author Vienji
 */
public interface DashboardListener {
    void updateUserTable();
    void updateVendorTable();
    void updateCustomerTable();
    void updateChartOfAccountTable();
    void updateJournalTable();
    void updateSalesTable();
    void updateExpensesTable();
    void updatePurchasesTable();
    void enableTaxRateConfig();
    void reportDateRange(String fromDate, String toDate, int Sheet);
    void setSelectedReportSheet();
    void reportDateRangeForAccount(String fromDate, String toDate, Account account);
    void updateSalesPaymentConfig(ArrayList<Account> accountList);
    void updatePurchasesPaymentConfig(ArrayList<Account> accountList);
    void updateProductTable();
}
