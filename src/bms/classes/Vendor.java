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
public class Vendor {
    private ArrayList<PurchaseTransaction> transactionList;
    
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public double getBalance() {
        return balance;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    //Functions
    public void makePurchase(PurchaseTransaction transaction){
    
    }
    
    public void makePayment(PurchaseTransaction transaction){
    
    }
    
    public void setTransactionHistory(ArrayList transactionList){
        this.transactionList = transactionList;
    }
    
    public ArrayList<PurchaseTransaction> getTransactionHistory(){
        return transactionList;
    }
    
    //Data field
    private String id;
    private String name;
    private String phoneNumber;
    private String address;
    private String email;
    private double balance;   
}
