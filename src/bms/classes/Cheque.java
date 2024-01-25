/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bms.classes;

/**
 *
 * @author Vienji
 */
public class Cheque {

    public String getChequeNumber() {
        return chequeNumber;
    }

    public String getDateIssued() {
        return dateIssued;
    }

    public String getPayee() {
        return payee;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public void setDateIssued(String dateIssued) {
        this.dateIssued = dateIssued;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    
    private String chequeNumber;
    private String dateIssued;
    private String payee;
    private String accountNumber;
    private String bankName;
}
