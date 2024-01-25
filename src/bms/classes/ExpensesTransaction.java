/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bms.classes;

import java.util.ArrayList;

/**
 *
 * @author Vienji
 */
public class ExpensesTransaction {

    public ArrayList<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(ArrayList<Account> accountList) {
        this.accountList = accountList;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Account getPaymentMethod() {
        return paymentMethod;
    }

    public Account getExpenseAccount() {
        return expenseAccount;
    }

    public String getEncoder() {
        return encoder;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public double getRemainingBalance() {
        return remainingBalance;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPaymentMethod(Account paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setExpenseAccount(Account expenseAccount) {
        this.expenseAccount = expenseAccount;
    }

    public void setEncoder(String encoder) {
        this.encoder = encoder;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public void setRemainingBalance(double remainingBalance) {
        this.remainingBalance = remainingBalance;
    }
    
    public void addAccount(Account account){
        accountList.add(account);
    }
    
    private String id;
    private String date;
    private String description;
    private Account paymentMethod;
    private Account expenseAccount;
    private String encoder;
    private double totalAmount;
    private double amountPaid;
    private double remainingBalance;
    private ArrayList<Account> accountList; 
}
