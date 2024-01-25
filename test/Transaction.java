
import bms.classes.Cryptographer;
import java.util.Scanner;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Vienji
 */
public class Transaction {

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Transaction(){}
    
    public Transaction(String id, String date, String description, String amount){
        this.description = description;
        this.id = id;
        this.date = date;
        this.amount = amount;
    }
    
    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    
    private String id;
    private String date;
    private String amount;
    private String description;
    
    public static void main(String[] args) throws Exception {
        Cryptographer crypt = new Cryptographer();
        Scanner input = new Scanner(System.in);
        System.out.print("Encrypted password: ");
        String password = input.nextLine();
        
        System.out.println("Decrypted Password: " +crypt.decrypt(password));
    }
}
