/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bms.classes;

/**
 *
 * @author Vienji
 */
public class ReportLedgerTransaction {
    
    public ReportLedgerTransaction(){}
    
    public ReportLedgerTransaction(String date, String description, String debit, String credit, String balance){
        this.date = date; 
        this.description = description;
        this.debit = debit;
        this.credit = credit;
        this.balance = balance;
    }
    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getDebit() {
        return debit;
    }

    public String getCredit() {
        return credit;
    }

    public String getBalance() {
        return balance;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
    
    private String date;
    private String description;
    private String debit;
    private String credit;
    private String balance;
}
