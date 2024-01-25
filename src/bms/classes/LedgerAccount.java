/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bms.classes;

/**
 *
 * @author Vienji
 */
public class LedgerAccount {

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getAction() {
        return action;
    }

    public Double getAmount() {
        return amount;
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
    
    public void setDescription(String description, String remarks) {
        this.description = "(" + remarks + ") " + description;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    private String id;
    private String date;
    private String description;
    private String action;
    private Double amount;
}
