/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bms.classes;

/**
 *
 * @author Vienji
 */
public class Account {

    public int getRefId() {
        return refId;
    }

    public void setRefId(int refId) {
        this.refId = refId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Account(){
        this.code = 0;
        this.accountName = "";
        this.accountType = "";
        this.accountTypeID = 0;
        this.action = "";
        this.amount = 0.00;
        this.balance = 0.00;
        this.normally = "";
        this.remarks = "";
    }
    
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getNormally() {
        return normally;
    }

    public void setNormally(String normally) {
        this.normally = normally;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getCode() {
        return code;
    }

    public String getAccountName() {
        return accountName;
    }

    public int getAccountTypeID() {
        return accountTypeID;
    }

    public double getBalance() {
        return balance;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAccountTypeID(int accountTypeID) {
        this.accountTypeID = accountTypeID;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    private int refId;
    private int code;
    private String date;
    private String accountName;
    private int accountTypeID;
    private String accountType;
    private double amount;
    private String normally;
    private double balance;
    private String action;
    private String remarks;
}
