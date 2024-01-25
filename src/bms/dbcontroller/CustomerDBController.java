/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bms.dbcontroller;

import bms.classes.Account;
import bms.classes.AuditTrail;
import bms.classes.Customer;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author Vienji
 */
public class CustomerDBController {
    private String query = "";
    
    public void add(String firstName, String lastName, String phoneNumber, String organization, String position, String address){
        Connection connection = null;
        Statement statement = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "INSERT INTO customer (firstname, lastname, phonenumber, organization, position, address) "
                    + "VALUES ('" + firstName + "', '" + lastName + "', '" + phoneNumber + "', '" 
                    + organization + "', '" + position + "', '" + address + "')";
            
            statement.executeUpdate(query);
            
            query = "UPDATE customer SET cid = CONCAT('CID', YEAR(CURDATE()), MONTH(CURDATE()), LPAD(LAST_INSERT_ID(), 3, '0')) WHERE id = LAST_INSERT_ID()";
            
            statement.executeUpdate(query);
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
    }
    
    public String addAndGetCustomerID(String firstName, String lastName, String phoneNumber){
        add(firstName, lastName, phoneNumber, "", "", "");
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT cid FROM customer WHERE phonenumber = '" + phoneNumber + "'";
            result = statement.executeQuery(query);
            
            while(result.next()){
                return result.getString("cid");
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
        
        return "";
    }
    
    public Customer getCustomer(String cid){
        Customer customer = new Customer();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT firstname, lastname, phonenumber, organization, position, address FROM customer WHERE cid = '" + cid + "'";
            result = statement.executeQuery(query);
            
            while(result.next()){
                customer.setFirstName(result.getString("firstname"));
                customer.setLastName(result.getString("lastname"));
                customer.setName(customer.getFirstName() + " " + customer.getLastName());
                customer.setPhoneNumber(result.getString("phonenumber"));
                customer.setOrganization(result.getString("organization"));
                customer.setPosition(result.getString("position"));
                customer.setAddress(result.getString("address"));
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
        
        return customer;
    }
    
    public ArrayList<Customer> getAllCustomers(){
        ArrayList<Customer> customerList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        
        try {
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT * FROM customer";
            result = statement.executeQuery(query);
            
            while(result.next()){
                Customer customer = new Customer();
                
                customer.setId(result.getString("cid"));
                customer.setFirstName(result.getString("firstname"));
                customer.setLastName(result.getString("lastname"));
                customer.setPhoneNumber(result.getString("phonenumber"));
                customer.setName(customer.getFirstName() + " " + customer.getLastName());
                customer.setOrganization(result.getString("organization"));
                customer.setPosition(result.getString("position"));
                customer.setAddress(result.getString("address"));
                customer.setBalance(getCustomerBalance(customer.getId()));
                
                customerList.add(customer);
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
        
        return customerList;
    }
    
    public ArrayList<Customer> getCustomersList(){
        ArrayList<Customer> customerList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        
        try {
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT * FROM customer";
            result = statement.executeQuery(query);
            
            while(result.next()){
                Customer customer = new Customer();
                
                customer.setId(result.getString("cid"));
                customer.setFirstName(result.getString("firstname"));
                customer.setLastName(result.getString("lastname"));
                customer.setPhoneNumber(result.getString("phonenumber"));
                customer.setName(customer.getFirstName() + " " + customer.getLastName());
                customer.setOrganization(result.getString("organization"));
                customer.setPosition(result.getString("position"));
                customer.setAddress(result.getString("address"));
                
                customerList.add(customer);
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
        
        return customerList;
    }
    
    public ArrayList<Customer> searchCustomer(String search){
        ArrayList<Customer> customerList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        
        try {
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT * FROM customer "
                    + "WHERE cid LIKE '%" + search + "%' OR firstname LIKE '%" + search + "%' OR lastname LIKE '%" + search + "%' OR phonenumber LIKE '%" + search + "%'";
            result = statement.executeQuery(query);
            
            while(result.next()){
                Customer customer = new Customer();
                
                customer.setId(result.getString("cid"));
                customer.setFirstName(result.getString("firstname"));
                customer.setLastName(result.getString("lastname"));
                customer.setPhoneNumber(result.getString("phonenumber"));
                customer.setName(customer.getFirstName() + " " + customer.getLastName());
                customer.setOrganization(result.getString("organization"));
                customer.setPosition(result.getString("position"));
                customer.setAddress(result.getString("address"));
                customer.setBalance(getCustomerBalance(customer.getId()));
                
                customerList.add(customer);
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
        
        return customerList;
    }
    
    public ArrayList<Customer> sortCustomer(String sortBy){
        ArrayList<Customer> customerList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        
        try {
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT * FROM customer ORDER by " + sortBy;
            result = statement.executeQuery(query);
            
            while(result.next()){
                Customer customer = new Customer();
                
                customer.setId(result.getString("cid"));
                customer.setFirstName(result.getString("firstname"));
                customer.setLastName(result.getString("lastname"));
                customer.setPhoneNumber(result.getString("phonenumber"));
                customer.setName(customer.getFirstName() + " " + customer.getLastName());
                customer.setOrganization(result.getString("organization"));
                customer.setPosition(result.getString("position"));
                customer.setAddress(result.getString("address"));
                customer.setBalance(getCustomerBalance(customer.getId()));
                
                customerList.add(customer);
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
        
        return customerList;
    }
    
    public void update(String cid, String firstName, String lastName, String phoneNumber, String organization, String position, String address){
        Connection connection = null;
        Statement statement = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "UPDATE customer SET firstname = '" + firstName + "', lastname = '" + 
                    lastName + "', phonenumber = '" + phoneNumber + "', organization = '" + organization + 
                    "', position = '" + position + "', address = '" + address + "' WHERE cid = '" + cid + "'";
            
            statement.execute(query);
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
    }
    
    public Double getCustomerBalance(String id){
        Double balance = 0.00;
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT sid FROM customersalestransaction WHERE customer = '" + id + "'";
            result = statement.executeQuery(query); 
            
            while(result.next()){
                balance += new EntryDBController().getSalesBalance(result.getString("sid"));
            }
            
            query = "SELECT sid FROM guestsalestransaction WHERE customer = '" + id + "'";
            result = statement.executeQuery(query); 
            
            while(result.next()){
                balance += new EntryDBController().getSalesBalance(result.getString("sid"));
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
        
        return balance;
    }
    
    
    public void delete(String id, String user){
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            Double balance = getCustomerBalance(id);
            
            query = "SELECT accountname "
                    + "FROM bms.subentryaccountitems "
                    + "INNER JOIN customersalestransaction ON subentryaccountitems.id = customersalestransaction.sid "
                    + "WHERE customersalestransaction.customer = '" + id + "' "
                    + "GROUP BY accountname";
            result = statement.executeQuery(query);
            
            ArrayList<Account> accountList = new ArrayList();
            while(result.next()){
                Account account = new AccountDBController().getAccount(result.getInt("accountname"));
                accountList.add(account);
            }
            
            query = "SELECT sid FROM customersalestransaction WHERE customer = '" + id + "'";
            result = statement.executeQuery(query);
            
            while(result.next()){
                String sid = result.getString("sid");
                new EntryDBController().deleteSales(sid, user);
            }
            
            query = "DELETE FROM customer WHERE cid = '" + id + "'";
            statement.executeUpdate(query);
            
            //Log
            String affectedAccounts = new AuditTrail().accountsToString(accountList);
            
            new AuditTrailDBController().logTrail("Customer Management", "Deleted",user, " ", affectedAccounts, 0.00, 0.00, balance);
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
