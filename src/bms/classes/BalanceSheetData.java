/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bms.classes;

/**
 *
 * @author Vienji
 */
public class BalanceSheetData {

    public BalanceSheetData(){
        this.type = "";
        this.account = "";
        this.amount = "";
    }
    
    public BalanceSheetData(String type, String account, String amount){
        this.type = type;
        this.account = account;
        this.amount = amount;
    }
    
    public String getType() {
        return type;
    }

    public String getAccount() {
        return account;
    }

    public String getAmount() {
        return amount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    private String type;
    private String account;
    private String amount;
}
