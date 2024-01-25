/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bms.classes;

/**
 *
 * @author Vienji
 */
public class AccountType {

    public String getNormally() {
        return normally;
    }

    public void setNormally(String normally) {
        this.normally = normally;
    }

    public int getId() {
        return id;
    }

    public String getAccountTypeName() {
        return accountTypeName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAccountTypeName(String accountTypeName) {
        this.accountTypeName = accountTypeName;
    }
    
    private int id;
    private String accountTypeName;
    private String normally;
}
