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
public class PurchaseTransaction {
    private ArrayList<Item> itemList = new ArrayList();
    
    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getSupplierContact() {
        return supplierContact;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public Account getPaymentMethod() {
        return paymentMethod;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public double getRemainingBalance() {
        return remainingBalance;
    }

    public Account getAssetAccount() {
        return assetAccount;
    }

    public String getDescription() {
        return description;
    }

    public String getPurchaser() {
        return purchaser;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public void setSupplierContact(String supplierContact) {
        this.supplierContact = supplierContact;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setPaymentMethod(Account paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public void setRemainingBalance(double remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public void setAssetAccount(Account assetAccount) {
        this.assetAccount = assetAccount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPurchaser(String purchaser) {
        this.purchaser = purchaser;
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
    private String dueDate;
    private String supplier;
    private String supplierContact;
    private double totalAmount;
    private Account paymentMethod;
    private double amountPaid;
    private double remainingBalance;
    private Account assetAccount;
    private String description;
    private String purchaser;
}
