/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bms.model.report;

import bms.classes.Account;
import bms.classes.BalanceSheetData;
import bms.classes.IncomeStatementData;
import bms.classes.LedgerAccount;
import bms.classes.ReportLedgerTransaction;
import bms.classes.TrialBalanceData;
import bms.dbcontroller.AccountDBController;
import bms.dbcontroller.EntryDBController;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Vienji
 */
public class ReportController {
    
    public void printBalanceSheet(String fromDate, String toDate){
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        LocalDate dateToday = LocalDate.now();
        String asOf = "As of " + dateToday.format(dateFormat);
        ArrayList<Account>  accountsList =  new AccountDBController().getAllAccounts(fromDate, toDate);
        List<BalanceSheetData> transactionsList = new ArrayList();
        Double totalAsset = 0.00;
        Double totalLiability = 0.00;
        Double totalEquity = 0.00;
        Double retainedEarnings = 0.00;
        Double revenue = 0.00;
        Double expenses = 0.00;
        
        //Assets
        BalanceSheetData assetHeader = new BalanceSheetData("ASSETS", "", "");
        transactionsList.add(assetHeader);
        for(int i = 0; i < accountsList.size(); i++){
            if(accountsList.get(i).getAccountType().equalsIgnoreCase("Assets") && accountsList.get(i).getBalance() > 0.00){
                BalanceSheetData data = new BalanceSheetData();
                
                data.setType("");
                data.setAccount(accountsList.get(i).getAccountName());
                data.setAmount("₱ " + setDecimalFormat(accountsList.get(i).getBalance()));
                transactionsList.add(data);
                totalAsset += accountsList.get(i).getBalance();
            }
        }
        BalanceSheetData totalAssetRow = new BalanceSheetData("", "Total Assets", "₱ " + setDecimalFormat(totalAsset));
        transactionsList.add(totalAssetRow);
        
        //Liabilities
        BalanceSheetData liabilityHeader = new BalanceSheetData("LIABILITIES", "", "");
        transactionsList.add(liabilityHeader);
        for(int i = 0; i < accountsList.size(); i++){
            if(accountsList.get(i).getAccountType().equalsIgnoreCase("Liabilities") && accountsList.get(i).getBalance() > 0.00){
                BalanceSheetData data = new BalanceSheetData();
                
                data.setType("");
                data.setAccount(accountsList.get(i).getAccountName());
                data.setAmount("₱ " + setDecimalFormat(accountsList.get(i).getBalance()));
                transactionsList.add(data);
                totalLiability += accountsList.get(i).getBalance();
            }
        }
        BalanceSheetData totalLiabilityRow = new BalanceSheetData("", "Total Liabilities", "₱ " + setDecimalFormat(totalLiability));
        transactionsList.add(totalLiabilityRow);
               
        //Equity
        BalanceSheetData equityHeader = new BalanceSheetData("EQUITY", "", "");
        transactionsList.add(equityHeader);
        for(int i = 0; i < accountsList.size(); i++){
            if(accountsList.get(i).getAccountType().equalsIgnoreCase("Equity") && accountsList.get(i).getBalance() > 0.00){
                BalanceSheetData data = new BalanceSheetData();
                
                data.setType("");
                data.setAccount(accountsList.get(i).getAccountName());
                data.setAmount("₱ " + setDecimalFormat(accountsList.get(i).getBalance()));
                transactionsList.add(data);
                totalEquity += accountsList.get(i).getBalance();
            }
        }
        
        for(int i = 0; i < accountsList.size(); i++){
            if(accountsList.get(i).getAccountType().equalsIgnoreCase("Revenue")){
                revenue += accountsList.get(i).getBalance();
            } else if (accountsList.get(i).getAccountType().equalsIgnoreCase("Expenses")){
                expenses += accountsList.get(i).getBalance();
            }
        }
        
        retainedEarnings = revenue - expenses;
        
        BalanceSheetData retainedEarningsRow = new BalanceSheetData("", "Retained Earnings", "₱ " + setDecimalFormat(retainedEarnings));
        transactionsList.add(retainedEarningsRow);
        
        totalEquity += retainedEarnings;
        BalanceSheetData totalEquityRow = new BalanceSheetData("", "Total Equity", "₱ " + setDecimalFormat(totalEquity));
        transactionsList.add(totalEquityRow);
        
        Double totalLiabilitiesAndEquity = totalLiability + totalEquity;
        BalanceSheetData totalLiabilitiesAndEquityRow = new BalanceSheetData("", "Total Liabilities And Equity", "₱ " + setDecimalFormat(totalLiabilitiesAndEquity));
        transactionsList.add(totalLiabilitiesAndEquityRow);
        
        //Printing
        try{
            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(transactionsList);
            
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("BalanceSheetJRBean", itemsJRBean);
            parameters.put("Date", asOf);
            
            InputStream input = new FileInputStream(new File("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Project\\BMS\\src\\bms\\reports\\Balance_Sheet.jrxml"));
            
            JasperDesign jasperDesign = JRXmlLoader.load(input);
            
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
            
            JasperViewer.viewReport(jasperPrint, false);
            
        } catch (Exception e){e.printStackTrace();}
    }
    
    public void printIncomeStatement(String fromDate, String toDate){
        String dateFor = "For " + fromDate + " to " + toDate;
        ArrayList<Account>  accountsList =  new AccountDBController().getAllAccounts(fromDate, toDate);
        List<IncomeStatementData> transactionsList = new ArrayList();
        Double totalRevenue = 0.00;
        Double totalExpenses = 0.00;
        
        //Revenue
        IncomeStatementData revenueHeader = new IncomeStatementData("REVENUE", "" ,"");
        transactionsList.add(revenueHeader);
        for(int i = 0; i < accountsList.size(); i++){
            if(accountsList.get(i).getAccountType().equalsIgnoreCase("Revenue") && accountsList.get(i).getBalance() > 0.00){
                IncomeStatementData data = new IncomeStatementData();
                
                data.setType("");
                data.setAccount(accountsList.get(i).getAccountName());
                data.setAmount("₱ " + setDecimalFormat(accountsList.get(i).getBalance()));
                transactionsList.add(data);
                
                totalRevenue += accountsList.get(i).getBalance();
            }
        }
        IncomeStatementData totalRevenueRow = new IncomeStatementData("", "Total Revenue", "₱ " + setDecimalFormat(totalRevenue));
        transactionsList.add(totalRevenueRow);
        
        //Expenses
        IncomeStatementData expensesHeader = new IncomeStatementData("EXPENSES", "" ,"");
        transactionsList.add(expensesHeader);
        for(int i = 0; i < accountsList.size(); i++){
            if(accountsList.get(i).getAccountType().equalsIgnoreCase("Expenses") && accountsList.get(i).getBalance() > 0.00){
                IncomeStatementData data = new IncomeStatementData();
                
                data.setType("");
                data.setAccount(accountsList.get(i).getAccountName());
                data.setAmount("₱ " + setDecimalFormat(accountsList.get(i).getBalance()));
                transactionsList.add(data);
                
                totalExpenses += accountsList.get(i).getBalance();
            }
        }
        IncomeStatementData totalExpensesRow = new IncomeStatementData("", "Total Expenses", "₱ " + setDecimalFormat(totalExpenses));
        transactionsList.add(totalExpensesRow);
        
        Double netIncome = totalRevenue - totalExpenses;
        
        IncomeStatementData netIncomeRow = new IncomeStatementData("NET INCOME", "( Total Revenue - Total Expenses )", "₱ " + setDecimalFormat(netIncome));
        transactionsList.add(netIncomeRow);
        
        //Printing
        try{
            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(transactionsList);
            
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("IncomeStatementJRBean", itemsJRBean);
            parameters.put("Date", dateFor);
            
            InputStream input = new FileInputStream(new File("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Project\\BMS\\src\\bms\\reports\\Income_Statement.jrxml"));
            
            JasperDesign jasperDesign = JRXmlLoader.load(input);
            
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
            
            JasperViewer.viewReport(jasperPrint, false);
            
        } catch (Exception e){e.printStackTrace();}
    }
    
    public void printAccountTransactions(String fromDate, String toDate, Account account, String openingBalance){
        ArrayList<ReportLedgerTransaction> transactionList = new ArrayList();
        String accountName = account.getAccountName();
        String dateFrom = "From " + fromDate + " to " + toDate;
        
        ArrayList<Double> trueBalance = new ArrayList();
        ArrayList<LedgerAccount> rawBalance = new EntryDBController().getTruePostedEntries(account.getCode(), 
                openingBalance, toDate);
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
                    ReportLedgerTransaction transaction = new ReportLedgerTransaction(ledgerAccount.getDate(), ledgerAccount.getDescription(), 
                        " ₱ " + setDecimalFormat(ledgerAccount.getAmount()), "", " ₱ " + setDecimalFormat(trueBalance.get(position)));
                    transactionList.add(transaction);
                } else {
                    ReportLedgerTransaction transaction = new ReportLedgerTransaction(ledgerAccount.getDate(), ledgerAccount.getDescription(), "",
                        " ₱ " + setDecimalFormat(ledgerAccount.getAmount()), " ₱ " + setDecimalFormat(trueBalance.get(position)));
                    transactionList.add(transaction);
                }
            } else {
                if(ledgerAccount.getAction().equalsIgnoreCase("Debit")){
                    ReportLedgerTransaction transaction = new ReportLedgerTransaction(ledgerAccount.getDate(), ledgerAccount.getDescription(), 
                        " ₱ " + setDecimalFormat(ledgerAccount.getAmount()), "", " ₱ " + setDecimalFormat(trueBalance.get(position)));
                    transactionList.add(transaction);
                } else {
                    ReportLedgerTransaction transaction = new ReportLedgerTransaction(ledgerAccount.getDate(), ledgerAccount.getDescription(), "",
                        " ₱ " + setDecimalFormat(ledgerAccount.getAmount()), " ₱ " + setDecimalFormat(trueBalance.get(position)));
                    transactionList.add(transaction);
                }    
            }  

            position++;
        }
        
        //Printing
        try{
            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(transactionList);
            
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("AccountTransactionJRBean", itemsJRBean);
            parameters.put("Date", dateFrom);
            parameters.put("AccountName", accountName);
            
            InputStream input = new FileInputStream(new File("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Project\\BMS\\src\\bms\\reports\\Ledger_Account.jrxml"));
            
            JasperDesign jasperDesign = JRXmlLoader.load(input);
            
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
            
            JasperViewer.viewReport(jasperPrint, false);
            
        } catch (Exception e){e.printStackTrace();}    
    }
    
    public void printTrialBalance(String fromDate, String toDate){
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        LocalDate dateToday = LocalDate.now();
        String asOf = "As of " + dateToday.format(dateFormat);
        ArrayList<Account>  accountsList =  new AccountDBController().getAllAccounts(fromDate, toDate);
        List<TrialBalanceData> transactionsList = new ArrayList();
        Double totalDebit = 0.00;
        Double totalCredit = 0.00;
        
        Iterator i = (Iterator) accountsList.iterator();
        
        while(i.hasNext()){
            Account account = (Account) i.next();
            
            if(account.getBalance() > 0){
                if(account.getNormally().equalsIgnoreCase("Debit")){
                    TrialBalanceData data = new TrialBalanceData(account.getAccountName(), "₱ " + setDecimalFormat(account.getBalance()), " ");
                    transactionsList.add(data);
                    totalDebit += account.getBalance();
                } else {
                    TrialBalanceData data = new TrialBalanceData(account.getAccountName(), " ","₱ " + setDecimalFormat(account.getBalance()));
                    transactionsList.add(data);
                    totalCredit += account.getBalance();
                }
            }
        }
        
        TrialBalanceData  total = new TrialBalanceData("Total", "₱ " + setDecimalFormat(totalDebit), "₱ " + setDecimalFormat(totalCredit));
        transactionsList.add(total);
        //Printing
        try{
            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(transactionsList);
            
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("TrialBalanceJRBean", itemsJRBean);
            parameters.put("Date", asOf);
            
            InputStream input = new FileInputStream(new File("C:\\Users\\userpc\\Desktop\\Cabinet\\2324-1STSEM\\Capstone 2\\Project\\BMS\\src\\bms\\reports\\Trial_Balance.jrxml"));
            
            JasperDesign jasperDesign = JRXmlLoader.load(input);
            
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
            
            JasperViewer.viewReport(jasperPrint, false);
            
        } catch (Exception e){e.printStackTrace();}
    }
    
    private String setDecimalFormat(Double amount){
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        
        return decimalFormat.format(amount);
    }
}
