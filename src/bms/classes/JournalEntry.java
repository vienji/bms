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
public class JournalEntry {   

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    private ArrayList<Account> accountList; 
    
    public ArrayList<Account> getAccountList(){
        return accountList;
    }
    
    public void setAccountList(ArrayList<Account> accountList){
        this.accountList = accountList;
    }
    
    public void addAccount(Account account){
        accountList.add(account);
    }
    
    public String getEncoder() {
        return encoder;
    }

    public void setEncoder(String encoder) {
        this.encoder = encoder;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    private String id;
    private String date;
    private String description;
    private String encoder;
    private Double totalAmount;
}
