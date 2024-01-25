/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bms.classes;

import bms.dbcontroller.Driver;
import bms.dbcontroller.UserDBController;
import bms.listeners.LoadScreenListener;
import bms.model.Dashboard;
import bms.model.LoadScreen;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
/**
 *
 * @author Vienji
 */
public class User extends javax.swing.JFrame{

    public User(String id, String firstName, String lastName, String username, String password, String accessLevel, String status){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.accessLevel = accessLevel;
        this.status = status;
    }
    
    public User(){
        this.id = "";
        this.firstName = "";
        this.lastName = "";
        this.username = "";
        this.password = "";
        this.accessLevel = "";
        this.status = "";
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
    
    public String getName() {
        return name;
    }
    
    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public String getStatus() {
        return status;
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

    public void setName(String firstName, String lastName) {
        this.name = firstName + " " + lastName;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public void setStatus(String status) {
        this.status = status;
    }
 
    //Functions
    
    public boolean isAccessGranted(String username, String password){
        Connection connection = Driver.getConnection();
        boolean isLogin = false;
        if(connection != null){
            User user = new UserDBController().getCredential(username, password);
            
            if(user.getUsername().isBlank() == false && user.getPassword().isBlank() == false && user.getAccessLevel().isBlank() == false && user.getStatus().isBlank() == false ){
                
                if(user.getStatus().equals("active")){
                    String level = user.getAccessLevel();
                    
                    switch(level){
                        case "Administrator":
                            isLogin = true;
                            showLoadingScreen(user, true);
                            break;
                        case "Cashier":
                            isLogin = true;
                            showLoadingScreen(user, false);
                            break;    
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "User is already deactivated!");
                }              
            } else {
                JOptionPane.showMessageDialog(rootPane, "Invalid Credentials! Please check and try again.");
            }      
        } else {
            JOptionPane.showMessageDialog(rootPane, "Can't connect to the server! Please check your network and try again.");
        }
        return isLogin;
    }
    
    private void showLoadingScreen(User user, boolean isAdmin){
        LoadScreen loadScreen = new LoadScreen();
        loadScreen.welcomeUsername.setText("Welcome " + user.getFirstName() + "!");
        loadScreen.setVisible(true);
        listener = loadScreen;
        Dashboard dashboard = new Dashboard(user);
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
            @Override
            protected Void doInBackground() throws Exception {
                
                listener.updateLoadingModule("Initializing Components...");
                dashboard.initIcons();
                dashboard.initDates();
                listener.updateLoadingPercentage(10);
                listener.updateLoadingBar(10);
                listener.updateLoadingModule("Initializing Ledger...");
                
                dashboard.initLedger();
                listener.updateLoadingPercentage(25);
                listener.updateLoadingBar(25);
                listener.updateLoadingModule("Initializing Configurations...");
                
                listener.updateLoadingModule("Network Settings...");
                dashboard.initNetworkSettings();
                listener.updateLoadingPercentage(30);
                listener.updateLoadingBar(30);
                listener.updateLoadingModule("Accounting Configurations...");
                
                dashboard.initAccountingConfigurations();
                listener.updateLoadingPercentage(35);
                listener.updateLoadingBar(35);
                listener.updateLoadingModule("Sales Configurations...");
                
                dashboard.initSalesConfiguration();
                listener.updateLoadingPercentage(40);
                listener.updateLoadingBar(40);
                listener.updateLoadingModule("Puchases Configurations...");
                
                dashboard.initPurchasesConfiguration();
                listener.updateLoadingPercentage(50);
                listener.updateLoadingBar(50);
                listener.updateLoadingModule("Loading Products Data...");
                
                dashboard.initProductsData();
                listener.updateLoadingPercentage(75);
                listener.updateLoadingBar(75);
                listener.updateLoadingModule("Loading Summary Module...");
                
                dashboard.initSummary();
                dashboard.initAccessLevel(isAdmin);
                listener.updateLoadingPercentage(100);
                listener.updateLoadingBar(100);
                
                return null;
            }
            
            @Override
            protected void done(){
                loadScreen.dispose();
                SwingUtilities.invokeLater(() -> {     
                    dashboard.setVisible(true);
                 });
            }
        };
        
        worker.execute();
    }
    
    //Data fields
    private String id;
    private String firstName;
    private String lastName;
    private String name;
    private String password;
    private String username;
    private String accessLevel;
    private String status;
    private LoadScreenListener listener;
}
