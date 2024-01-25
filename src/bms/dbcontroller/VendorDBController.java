/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bms.dbcontroller;

import bms.classes.Account;
import bms.classes.AuditTrail;
import bms.classes.Vendor;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Vienji
 */
public class VendorDBController {
    private String query = "";
    
    //Adding vendor details with email
    public void add(String name, String phoneNumber, String email, String address){
        Connection connection = null;
        Statement statement = null;
        try {
            connection = Driver.getConnection();
            statement = connection.createStatement();
         
            query = "INSERT INTO vendor (name, phonenumber, email, address) "
                    + "VALUES ('" + name + "', '" + phoneNumber + "', '" + email + "', '" + address + "')";

            statement.executeUpdate(query);

            query = "UPDATE vendor SET vid = CONCAT('VID', YEAR(CURDATE()), MONTH(CURDATE()), LPAD(LAST_INSERT_ID(), 3, '0')) WHERE id = LAST_INSERT_ID()";

            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null){
                try{connection.close();}catch(SQLException e){}
            }
            if(statement != null){
                try{statement.close();}catch(SQLException e){}
            }
        }
    }
    
    //Adding vendor details without email 
    public void add(String name, String phoneNumber, String address){
        Connection connection = null;
        Statement statement = null;
        try {
            connection = Driver.getConnection();
            statement = connection.createStatement();
         
            query = "INSERT INTO vendor (name, phonenumber, address) "
                    + "VALUES ('" + name + "', '" + phoneNumber + "', '" + address + "')";

            statement.executeUpdate(query);

            query = "UPDATE vendor SET vid = CONCAT('VID', YEAR(CURDATE()), MONTH(CURDATE()), LPAD(LAST_INSERT_ID(), 3, '0')) WHERE id = LAST_INSERT_ID()";

            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null){
                try{connection.close();}catch(SQLException e){}
            }
            if(statement != null){
                try{statement.close();}catch(SQLException e){}
            }
        }
    }
    
    public Vendor getVendor(String vid){
        Vendor vendor = new Vendor();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT name, phonenumber, email, address FROM vendor WHERE vid = '" + vid + "'";
            result = statement.executeQuery(query);
            
            while(result.next()){
                vendor.setName(result.getString("name"));
                vendor.setPhoneNumber(result.getString("phonenumber"));
                vendor.setEmail(result.getString("email"));
                vendor.setAddress(result.getString("address"));
            }
        } catch (SQLException e){ 
            e.printStackTrace();
        } finally {
            if(connection != null){
                try{connection.close();}catch(SQLException e){}
            }
            if(statement != null){
                try{statement.close();}catch(SQLException e){}
            }
            if(result != null){
                try{result.close();}catch(SQLException e){}
            }
        }
        
        return vendor;
    }
    
    public ArrayList<Vendor> getAllVendors(){
        ArrayList<Vendor> vendorList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        
        try {
                connection = Driver.getConnection();
                statement = connection.createStatement();
                query = "SELECT * FROM vendor";
                result = statement.executeQuery(query);

                while (result.next()){
                   Vendor vendor = new Vendor();

                   vendor.setId(result.getString("vid"));
                   vendor.setName(result.getString("name"));
                   vendor.setPhoneNumber(result.getString("phonenumber"));
                   vendor.setAddress(result.getString("address"));
                   vendor.setEmail(result.getString("email"));
                   vendor.setBalance(getVendorBalance(vendor.getId()));

                   vendorList.add(vendor);
                }
         } catch (SQLException e){
             e.printStackTrace();
         } finally {
            if(connection != null){
                try{connection.close();}catch(SQLException e){}
            }
            if(statement != null){
                try{statement.close();}catch(SQLException e){}
            }
            if(result != null){
                try{result.close();}catch(SQLException e){}
            }
        }
        
        return vendorList;
    }
    
    public ArrayList<Vendor> getVendorsList(){
        ArrayList<Vendor> vendorList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        
        try {
                connection = Driver.getConnection();
                statement = connection.createStatement();
                query = "SELECT * FROM vendor";
                result = statement.executeQuery(query);

                while (result.next()){
                   Vendor vendor = new Vendor();

                   vendor.setId(result.getString("vid"));
                   vendor.setName(result.getString("name"));
                   vendor.setPhoneNumber(result.getString("phonenumber"));
                   vendor.setAddress(result.getString("address"));
                   vendor.setEmail(result.getString("email"));

                   vendorList.add(vendor);
                }
         } catch (SQLException e){
             e.printStackTrace();
         } finally {
            if(connection != null){
                try{connection.close();}catch(SQLException e){}
            }
            if(statement != null){
                try{statement.close();}catch(SQLException e){}
            }
            if(result != null){
                try{result.close();}catch(SQLException e){}
            }
        }
        
        return vendorList;
    }
    
    public ArrayList<Vendor> searchVendor(String search){
        ArrayList<Vendor> vendorList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try {
             connection = Driver.getConnection();
             statement = connection.createStatement();
             query = "SELECT * FROM vendor "
                     + "WHERE vid LIKE '%" + search + "%' OR name LIKE '%" + search + "%' OR phonenumber LIKE '%" + search 
                     + "%' OR email LIKE '%" + search + "%' OR address LIKE '%" + search + "%'";
             result = statement.executeQuery(query);
         
             while (result.next()){
                Vendor vendor = new Vendor();
                
                vendor.setId(result.getString("vid"));
                vendor.setName(result.getString("name"));
                vendor.setPhoneNumber(result.getString("phonenumber"));
                vendor.setAddress(result.getString("address"));
                vendor.setEmail(result.getString("email"));
                vendor.setBalance(getVendorBalance(vendor.getId()));
                
                vendorList.add(vendor);
             }
         }
         catch (SQLException e){
             e.printStackTrace();
         } finally {
            if(connection != null){
                try{connection.close();}catch(SQLException e){}
            }
            if(statement != null){
                try{statement.close();}catch(SQLException e){}
            }
            if(result != null){
                try{result.close();}catch(SQLException e){}
            }
        }
        
        return vendorList;
    }
    
    public ArrayList<Vendor> sortVendor(String sortBy){
        ArrayList<Vendor> vendorList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try {
             connection = Driver.getConnection();
             statement = connection.createStatement();
             query = "SELECT * FROM vendor ORDER BY " + sortBy;                    
             result = statement.executeQuery(query);
         
             while (result.next()){
                Vendor vendor = new Vendor();
                
                vendor.setId(result.getString("vid"));
                vendor.setName(result.getString("name"));
                vendor.setPhoneNumber(result.getString("phonenumber"));
                vendor.setAddress(result.getString("address"));
                vendor.setEmail(result.getString("email"));
                vendor.setBalance(getVendorBalance(vendor.getId()));
                
                vendorList.add(vendor);
             }
         }
         catch (SQLException e){
             e.printStackTrace();
         } finally {
            if(connection != null){
                try{connection.close();}catch(SQLException e){}
            }
            if(statement != null){
                try{statement.close();}catch(SQLException e){}
            }
            if(result != null){
                try{result.close();}catch(SQLException e){}
            }
        }
        
        return vendorList;
    }
     
    public void update(String vid, String name, String phoneNumber, String email, String address){
        Connection connection = null;
        Statement statement = null;

        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "UPDATE vendor SET name = '" + name + "', phoneNumber = '" + 
                    phoneNumber + "', email = '" + email + "', address = '" + address + "' WHERE vid = '" + vid + "'";
            
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null){
                try{connection.close();}catch(SQLException e){}
            }
            if(statement != null){
                try{statement.close();}catch(SQLException e){}
            }
        }
    }
    
    public Double getVendorBalance(String id){
        Double balance = 0.00;
        Connection connection = null;
        Statement statement = null;
        
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT * FROM purchasestransaction WHERE supplier = '" + id + "'";
            ResultSet result = statement.executeQuery(query); 
            
            while(result.next()){
                balance += new EntryDBController().getPurchasesBalance(result.getString("pid"));
            }
    
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if(connection != null){
                try{connection.close();}catch(SQLException e){}
            }
            if(statement != null){
                try{statement.close();}catch(SQLException e){}
            }
        }
        
        return balance;
    }
    
    public void delete(String id, String user){
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            Double balance = getVendorBalance(id);
            
            query = "SELECT accountname "
                    + "FROM bms.subentryaccountitems "
                    + "INNER JOIN purchasestransaction ON subentryaccountitems.id = purchasestransaction.pid "
                    + "WHERE purchasestransaction.supplier = '" + id + "' "
                    + "GROUP BY accountname";
            result = statement.executeQuery(query);
            
            ArrayList<Account> accountList = new ArrayList();
            while(result.next()){
                Account account = new AccountDBController().getAccount(result.getInt("accountname"));
                accountList.add(account);
            }
            
            query = "SELECT pid FROM purchasestransaction WHERE supplier = '" + id + "'";
            result = statement.executeQuery(query);
            
            while(result.next()){
                String pid = result.getString("pid");
                new EntryDBController().deletePurchases(pid, user);
            }
            
            query = "DELETE FROM vendor WHERE vid = '" + id + "'";
            statement.executeUpdate(query);
            
            //Log
            String affectedAccounts = new AuditTrail().accountsToString(accountList);
            
            new AuditTrailDBController().logTrail("Vendor Management", "Deleted",user, " ", affectedAccounts, 0.00, 0.00, balance);
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if(connection != null){
                try{connection.close();}catch(SQLException e){}
            }
            if(statement != null){
                try{statement.close();}catch(SQLException e){}
            }
            if(result != null){
                try{result.close();}catch(SQLException e){}
            }
        }
    } 
}
