/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bms.classes;

import bms.model.Dashboard;
import java.util.ArrayList;

/**
 *
 * @author Vienji
 */
public class AuditTrail {
    
    public String getId() {
        return id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getModule() {
        return module;
    }

    public String getEvent() {
        return event;
    }

    public String getUser() {
        return user;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAffectedAccounts() {
        return affectedAccounts;
    }

    public Double getBeforeValue() {
        return beforeValue;
    }

    public Double getAfterValue() {
        return afterValue;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setAffectedAccounts(String affectedAccounts) {
        this.affectedAccounts = affectedAccounts;
    }

    public void setBeforeValue(Double beforeValue) {
        this.beforeValue = beforeValue;
    }

    public void setAfterValue(Double afterValue) {
        this.afterValue = afterValue;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String accountsToString(ArrayList<Account> accountsList){
        String accounts = "";
        int n = 1;
        
        for(Account account: accountsList){
            String accountName = new Dashboard().stringEscape(account.getAccountName());
            
            if(accountsList.size() == 1){ return accounts + accountName;}
            
            if(n < accountsList.size()){
                
                accounts += accountName + ", ";
            } else {
                accounts += "and " + accountName;
            }
            
            n++;
        }
        return accounts;
    }
    
    private String id;
    private String timestamp;
    private String module;
    private String event;
    private String user;
    private String transactionId;
    private String affectedAccounts;
    private Double beforeValue;
    private Double afterValue;
    private Double totalAmount;
}
