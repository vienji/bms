/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bms.classes;

/**
 *
 * @author Vienji
 */
public class TrialBalanceData {

    public TrialBalanceData(String account, String debit, String credit){
        this.account = account;
        this.debit = debit;
        this.credit = credit;
    }
    
    public String getAccount() {
        return account;
    }

    public String getDebit() {
        return debit;
    }

    public String getCredit() {
        return credit;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }
   
    private String account;
    private String debit;
    private String credit;
}
