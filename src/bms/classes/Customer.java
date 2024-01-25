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
public class Customer {

    private ArrayList<SalesTransaction> transactionList;
    
    public Customer(){
        this.id = "";
        this.firstName = "";
        this.lastName = "";
        this.name = "";
        this.phoneNumber = "";
        this.balance = 0.00;
        this.transactionList = new ArrayList();
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public String getOrganization() {
        return organization;
    }

    public String getPosition() {
        return position;
    }

    public String getAddress() {
        return address;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    //Functions
    
    public void setTransactionHistory(ArrayList transactionList){
        this.transactionList = transactionList;
    }
    
    public ArrayList<SalesTransaction> getTransactionHistory(){
        return transactionList;
    }
    
    //Data Fields
    private String id;
    private String firstName;
    private String lastName;
    private String name;
    private String phoneNumber;
    private String organization;
    private String position;
    private String address;
    private double balance;  
}
