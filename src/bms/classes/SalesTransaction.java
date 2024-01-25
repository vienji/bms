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
public class SalesTransaction {
    private ArrayList<Item> itemList = new ArrayList();
    
    public Account getPaymentMethod() {
        return paymentMethod;
    }

    public Account getRevenueAccount() {
        return revenueAccount;
    }

    public void setPaymentMethod(Account paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setRevenueAccount(Account revenueAccount) {
        this.revenueAccount = revenueAccount;
    }
    
    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public String getDescription() {
        return description;
    }

    public String getSalesPerson() {
        return salesPerson;
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

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSalesPerson(String salesPerson) {
        this.salesPerson = salesPerson;
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

    public void setItemList(ArrayList itemList){
        this.itemList = itemList;
    }
    
    public ArrayList<Item> getItemList(){
        return itemList;
    }
    
    public void addItem(Item item){
        this.itemList.add(item);
    }
    
    public Item getItem(int index){
        return itemList.get(index);
    }
    
    public void setItem(int index, Item item){
        this.itemList.set(index, item);
    }
    
    public void removeItem(int index){
        this.itemList.remove(index);
    }
    
    public void flushAllItem(){
        this.itemList.clear();
    }
    
    private String id;
    private String date;
    private String customerName;
    private String customerContact;
    private String description;
    private String salesPerson;
    private double totalAmount;
    private double amountPaid;
    private double remainingBalance;
    private Account paymentMethod;
    private Account revenueAccount;
}
