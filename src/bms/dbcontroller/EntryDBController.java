/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bms.dbcontroller;

import bms.classes.Account;
import bms.classes.AuditTrail;
import bms.classes.Cheque;
import bms.classes.Customer;
import bms.classes.ExpensesTransaction;
import bms.classes.Item;
import bms.classes.JournalEntry;
import bms.classes.LedgerAccount;
import bms.classes.PurchaseTransaction;
import bms.classes.SalesTransaction;
import bms.classes.Vendor;
import bms.model.Dashboard;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import javax.imageio.ImageIO;

/**
 *
 * @author Vienji
 */
public class EntryDBController {
    private String query = "";
    
    //General Journal
    public void addJournalEntry(String date, String description, String encoder, ArrayList<Account> accountList, String document){
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            String jid = "ID00001";
            LocalTime localTime = LocalTime.now();
            String time = String.valueOf(localTime);
            
            query = "INSERT INTO journalentry (date, description, encoder, time) "
                    + "VALUES ('" + date + "', '" + description + "', '" + encoder + "', '" + time + "')";
            
            statement.executeUpdate(query);
            
            query = "UPDATE journalentry SET jid = CONCAT('JID', YEAR(CURDATE()), MONTH(CURDATE()), LPAD(LAST_INSERT_ID(), 3, '0')) WHERE id = LAST_INSERT_ID()";
            
            statement.executeUpdate(query);
            
            query = "SELECT jid FROM journalentry WHERE date = '" + date + "' AND time = '" + time + "'";
            result = statement.executeQuery(query);
            
            while(result.next()){jid = result.getString("jid");}

            query = "INSERT INTO document (transactionid, document, filename) "
                    + "VALUES (?, ?, ?)";
            
            preparedStatement = connection.prepareStatement(query);
            
            FileInputStream fileInputStream = null;
            
            if(document != null){fileInputStream = new FileInputStream(document);}
            
            preparedStatement.setString(1, jid);
            preparedStatement.setBinaryStream(2, fileInputStream);
            preparedStatement.setString(3, filenameSplitter(document));
            preparedStatement.executeUpdate();
            
            if(fileInputStream != null){fileInputStream.close();}
            
            Iterator i = (Iterator) accountList.iterator();
            
            while(i.hasNext()){
                Account account = (Account) i.next();
                query = "INSERT INTO accountitems (id, accountname, amount, action) "
                        + "VALUES ('" + jid + "', " + account.getCode() + ", " + account.getAmount() + ", '" + account.getAction() +"')";
                statement.execute(query);
            }
            
            //Log
            String affectedAccounts = new AuditTrail().accountsToString(accountList);
            Double totalAmount = 0.00;
            
            for(int j = 0; j < accountList.size(); j++){
                if(accountList.get(j).getAction().equalsIgnoreCase("Debit")){
                    totalAmount += accountList.get(j).getAmount();
                }
            }
            
            new AuditTrailDBController().logTrail("Journal", "Added", encoder, jid, affectedAccounts, 0.00, 0.00, totalAmount);
        } catch (Exception e){
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
            if(preparedStatement != null){
                try{preparedStatement.close();}catch(SQLException e){}
            }
        }
    }
    
    public ArrayList<JournalEntry> getAllJournalEntry(){
        ArrayList<JournalEntry> entryList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT journalentry.jid, journalentry.date, journalentry.description, concat(user.firstname, ' ', user.lastname) AS encoder "
                    + "FROM journalentry "
                    + "INNER JOIN user ON journalentry.encoder = user.uid "
                    + "ORDER BY journalentry.date DESC, journalentry.time DESC";
            result = statement.executeQuery(query);
            
            while(result.next()){
                JournalEntry entry = new JournalEntry();
                
                entry.setId(result.getString("jid"));
                entry.setDate(result.getString("date"));
                entry.setDescription(result.getString("description"));             
                entry.setEncoder(result.getString("encoder"));
                entry.setAccountList(getAccountItems(entry.getId()));
                
                entryList.add(entry);                           
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
        
        return entryList;
    }
    
    public JournalEntry getJournalEntry(String id){
        JournalEntry entry = new JournalEntry();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT journalentry.jid, journalentry.date, journalentry.description, concat(user.firstname, ' ', user.lastname) AS encoder "
                    + "FROM journalentry "
                    + "INNER JOIN user ON journalentry.encoder = user.uid "
                    + "WHERE journalentry.jid = '" + id + "'";
            result = statement.executeQuery(query);
            
            while(result.next()){
                entry.setId(result.getString("jid"));
                entry.setDate(result.getString("date"));
                entry.setDescription(result.getString("description"));             
                entry.setEncoder(result.getString("encoder"));
                entry.setAccountList(getAccountItems(entry.getId()));                                       
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
        return entry;
    }
    
    public ArrayList<JournalEntry> getAllJournalEntry(String fromDate, String toDate){
        ArrayList<JournalEntry> entryList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT journalentry.jid, journalentry.date, journalentry.description, concat(user.firstname, ' ', user.lastname) AS encoder "
                    + "FROM journalentry "
                    + "INNER JOIN user ON journalentry.encoder = user.uid "
                    + "WHERE journalentry.date BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + "ORDER BY journalentry.date DESC, journalentry.time DESC";
            result = statement.executeQuery(query);
            
            while(result.next()){
                JournalEntry entry = new JournalEntry();
                
                entry.setId(result.getString("jid"));
                entry.setDate(result.getString("date"));
                entry.setDescription(result.getString("description"));             
                entry.setEncoder(result.getString("encoder"));
                entry.setAccountList(getAccountItems(entry.getId()));
                
                entryList.add(entry);                           
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
        
        return entryList;
    }
    
    public ArrayList<JournalEntry> searchJournalEntry(String search){
        ArrayList<JournalEntry> entryList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT journalentry.jid, journalentry.date, journalentry.description, concat(user.firstname, ' ', user.lastname) AS encoder "
                    + "FROM journalentry "
                    + "INNER JOIN user ON journalentry.encoder = user.uid "
                    + "WHERE jid LIKE '%" + search + "%' OR date LIKE '%" + search + "%' OR description LIKE '%" + 
                    search + "%' OR user.firstname LIKE '%" + search + "%' OR user.lastname LIKE '%" + search + "%'";
            result = statement.executeQuery(query);
            
            while(result.next()){
                JournalEntry entry = new JournalEntry();
                
                entry.setId(result.getString("jid"));
                entry.setDate(result.getString("date"));
                entry.setDescription(result.getString("description"));             
                entry.setEncoder(result.getString("encoder"));
                entry.setAccountList(getAccountItems(entry.getId()));
                
                entryList.add(entry);                           
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
        
        return entryList;
    }
    
    public ArrayList<JournalEntry> sortJournalEntry(String sortBy){
        ArrayList<JournalEntry> entryList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT journalentry.jid, journalentry.date, journalentry.description, concat(user.firstname, ' ', user.lastname) AS encoder "
                    + "FROM journalentry "
                    + "INNER JOIN user ON journalentry.encoder = user.uid "
                    + "ORDER BY " + sortBy;
            result = statement.executeQuery(query);
            
            while(result.next()){
                JournalEntry entry = new JournalEntry();
                
                entry.setId(result.getString("jid"));
                entry.setDate(result.getString("date"));
                entry.setDescription(result.getString("description"));             
                entry.setEncoder(result.getString("encoder"));
                entry.setAccountList(getAccountItems(entry.getId()));
                
                entryList.add(entry);                           
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
        
        return entryList;
    }
    
    public void deleteEntry(String id, String user){
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try {
            connection = Driver.getConnection();
            statement = connection.createStatement();
            
            query = "SELECT DISTINCT accountname FROM accountitems WHERE id = '" + id + "'";
            result = statement.executeQuery(query);
                
            ArrayList<Account> accountList = new ArrayList();
            Double totalAmount = getEntryTotalAmount(id);
            while(result.next()){
                Account account = new AccountDBController().getAccount(result.getInt("accountname"));
                accountList.add(account);                
            }
                
            query = "DELETE FROM journalentry WHERE jid = '" + id + "' ";
            statement.executeUpdate(query);
                
            query = "DELETE FROM accountitems WHERE id = '" + id + "' ";
            statement.executeUpdate(query);
                
            //Log
            String affectedAccounts = new AuditTrail().accountsToString(accountList);
                
            new AuditTrailDBController().logTrail("Journal", "Deleted",user, id, affectedAccounts, 0.00, 0.00, totalAmount);
            
        } catch (SQLException e) {
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
    
    public void updateEntry(String id, String date, String description, String encoder, ArrayList<Account> accountList){
        Connection connection = null;
        Statement statement = null;
        try{
            Double beforeValue = getEntryTotalAmount(id);
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "UPDATE journalentry SET date = '" + date + "', description = '" + description + "' WHERE jid = '" + id + "'";
            statement.execute(query);
            
            query = "DELETE FROM accountitems WHERE id = '" + id + "' ";
            statement.executeUpdate(query);
            
            Iterator i = (Iterator) accountList.iterator();
            
            while(i.hasNext()){
                Account account = (Account) i.next();
                query = "INSERT INTO accountitems (id, accountname, amount, action) "
                        + "VALUES ('" + id + "', " + account.getCode() + ", " + account.getAmount() + ", '" + account.getAction() +"')";
                statement.execute(query);
            }
            Double afterValue = getEntryTotalAmount(id);
            
            //Log
            String affectedAccounts = new AuditTrail().accountsToString(accountList);
                
            new AuditTrailDBController().logTrail("Journal", "Edited",encoder, id, affectedAccounts, beforeValue, afterValue, afterValue);
            
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
    
    public Double getEntryTotalAmount(String id){
        Double totalAmount = 0.00;
        Connection connection = null;
        Statement statement = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT * FROM accountitems WHERE id = '" + id + "' ";
            ResultSet result = statement.executeQuery(query);
                
            while(result.next()){
                if(result.getString("action").equalsIgnoreCase("Debit")){
                    totalAmount += result.getDouble("amount");
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }  finally {
            if(connection != null){
                try{connection.close();}catch(SQLException e){}
            }
            if(statement != null){
                try{statement.close();}catch(SQLException e){}
            }
        }
        
        return totalAmount;
    }
    
    //Sales
    private int salesRemainingBalanceAccount = new AccountDBController().getAccountCode(new Dashboard().getAccountConfig("salesconfig.properties", "sales_remaining_balance_account"));
    private int salesRevenueAccount = new AccountDBController().getAccountCode(new Dashboard().getAccountConfig("salesconfig.properties", "sales_revenue_account"));
    
    public void addSales(boolean isGuest, String date, String customer, String phoneNumber, String description, String salesPerson, 
            Double totalAmount, Double amountPaid, Double balance, int paymentMethod, String remarks, ArrayList<Item> itemList, Cheque cheque){
            String table = isGuest ? "guestsalestransaction" : "customersalestransaction";
            String idCode = isGuest ? "GSID" : "CSID";
            Connection connection = null;
            Statement statement = null;
            ResultSet result = null;
            
            try {
                connection = Driver.getConnection();
                statement = connection.createStatement();
                String sid = "ID00001";
                LocalTime localTime = LocalTime.now();
                String time = String.valueOf(localTime);               
                
                if(isGuest){
                    query = "INSERT INTO "+ table +" (date, customer, phonenumber, description, salesperson, totalamount, paymentmethod, time) "
                            + "VALUES ('" + date + "', '" + customer + "', '" + phoneNumber + "', '" + description + "', '" + salesPerson + "', " + totalAmount + ", " + 
                            paymentMethod + ", '"+ time +"')";
                } else {
                    query = "INSERT INTO "+ table +" (date, customer, description, salesperson, totalamount, paymentmethod, time) "
                            + "VALUES ('" + date + "', '" + customer + "', '" + description + "', '" + salesPerson + "', " + totalAmount + ", " 
                            + paymentMethod + ", '"+ time +"')";
                }

                statement.executeUpdate(query);
                
                query = "UPDATE "+ table +" SET sid = CONCAT('"+ idCode +"', YEAR(CURDATE()), MONTH(CURDATE()), LPAD(LAST_INSERT_ID(), 3, '0')) WHERE id = LAST_INSERT_ID()";
                
                statement.executeUpdate(query);
                
                query = "SELECT sid FROM " + table + " WHERE date = '" + date + "' AND time = '" + time + "'";
                result = statement.executeQuery(query);
            
                while(result.next()){sid = result.getString("sid");}
                
                Iterator i = (Iterator) itemList.iterator();
                
                while(i.hasNext()){
                    Item item = (Item) i.next();
                    
                    query = "INSERT INTO item (id, name, description, quantity, price) "
                            + "VALUES ('" + sid + "', '" + item.getName() + "', '" + item.getDescription() 
                            + "', '" + item.getQuantity() + "', " + item.getPrice() + ")";
                    statement.executeUpdate(query);
                }
                
                ArrayList<Account> accountList = new ArrayList();
                
                //Payment method
                if(amountPaid == 0){
                    //direct credit
                    query = "INSERT subentryaccountitems (id, date, accountname, amount, action) "
                            + "VALUES ('" + sid + "', '" + date + "', " + salesRemainingBalanceAccount + ", " + balance + ", 'Debit')";
                    statement.executeUpdate(query);
                    
                    Account account = new AccountDBController().getAccount(salesRemainingBalanceAccount);
                    accountList.add(account);  
                } else {
                    query = "INSERT subentryaccountitems (id, date, accountname, amount, action, remarks) "
                            + "VALUES ('" + sid + "', '" + date + "', " + paymentMethod + ", " + amountPaid + ", 'Debit', '" + remarks + "')";
                    statement.executeUpdate(query);
                    
                    //Cheque info  
                    int refId = 0;
                    query = "SELECT refid FROM subentryaccountitems WHERE refid = LAST_INSERT_ID()";
                    result = statement.executeQuery(query);

                    while(result.next()){
                        refId = result.getInt("refid");
                    }

                    addCheque(cheque, refId);
                    
                    Account account = new AccountDBController().getAccount(paymentMethod);
                    accountList.add(account);
                    
                    if(balance > 0){
                        query = "INSERT subentryaccountitems (id, date, accountname, amount, action) "
                            + "VALUES ('" + sid + "', '" + date + "', " + salesRemainingBalanceAccount + ", " + balance + ", 'Debit')";
                        statement.executeUpdate(query);
                        
                        Account account1 = new AccountDBController().getAccount(salesRemainingBalanceAccount);
                        accountList.add(account1);
                    }
                }
                
                //Revenue
                query = "INSERT subentryaccountitems (id, date, accountname, amount, action) "
                            + "VALUES ('" + sid + "', '" + date + "', " + salesRevenueAccount + ", " + totalAmount + ", 'Credit')";
                statement.executeUpdate(query);
                
                Account account = new AccountDBController().getAccount(salesRevenueAccount);
                accountList.add(account);
                
                //Log
                String affectedAccounts = new AuditTrail().accountsToString(accountList);
               
                new AuditTrailDBController().logTrail("Sales", "Added",salesPerson, sid, affectedAccounts, 0.00, 0.00, totalAmount);
                
            } catch(SQLException e){
                e.printStackTrace();
            }  finally {
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
    
    public void addSales(boolean isGuest, String date, String customer, String phoneNumber, String description, String salesPerson, 
            Double totalAmount, Double amountPaid, Double balance, int paymentMethod, String remarks, ArrayList<Item> itemList){
            String table = isGuest ? "guestsalestransaction" : "customersalestransaction";
            String idCode = isGuest ? "GSID" : "CSID";
            Connection connection = null;
            Statement statement = null;
            ResultSet result = null;
            
            try {
                connection = Driver.getConnection();
                statement = connection.createStatement();
                String sid = "ID00001";
                LocalTime localTime = LocalTime.now();
                String time = String.valueOf(localTime);               
                
                if(isGuest){
                    query = "INSERT INTO "+ table +" (date, customer, phonenumber, description, salesperson, totalamount, paymentmethod, time) "
                            + "VALUES ('" + date + "', '" + customer + "', '" + phoneNumber + "', '" + description + "', '" + salesPerson + "', " + totalAmount + ", " + 
                            paymentMethod + ", '"+ time +"')";
                } else {
                    query = "INSERT INTO "+ table +" (date, customer, description, salesperson, totalamount, paymentmethod, time) "
                            + "VALUES ('" + date + "', '" + customer + "', '" + description + "', '" + salesPerson + "', " + totalAmount + ", " 
                            + paymentMethod + ", '"+ time +"')";
                }

                statement.executeUpdate(query);
                
                query = "UPDATE "+ table +" SET sid = CONCAT('"+ idCode +"', YEAR(CURDATE()), MONTH(CURDATE()), LPAD(LAST_INSERT_ID(), 3, '0')) WHERE id = LAST_INSERT_ID()";
                
                statement.executeUpdate(query);
                
                query = "SELECT sid FROM " + table + " WHERE date = '" + date + "' AND time = '" + time + "'";
                result = statement.executeQuery(query);
            
                while(result.next()){sid = result.getString("sid");}
                
                Iterator i = (Iterator) itemList.iterator();
                
                while(i.hasNext()){
                    Item item = (Item) i.next();
                    
                    query = "INSERT INTO item (id, name, description, quantity, price) "
                            + "VALUES ('" + sid + "', '" + item.getName() + "', '" + item.getDescription() 
                            + "', '" + item.getQuantity() + "', " + item.getPrice() + ")";
                    statement.executeUpdate(query);
                }
                
                ArrayList<Account> accountList = new ArrayList();
                
                //Payment method
                if(amountPaid == 0){
                    //direct credit
                    query = "INSERT subentryaccountitems (id, date, accountname, amount, action) "
                            + "VALUES ('" + sid + "', '" + date + "', " + salesRemainingBalanceAccount + ", " + balance + ", 'Debit')";
                    statement.executeUpdate(query);
                    
                    Account account = new AccountDBController().getAccount(salesRemainingBalanceAccount);
                    accountList.add(account);  
                } else {
                    query = "INSERT subentryaccountitems (id, date, accountname, amount, action, remarks) "
                            + "VALUES ('" + sid + "', '" + date + "', " + paymentMethod + ", " + amountPaid + ", 'Debit', '" + remarks + "')";
                    statement.executeUpdate(query);
                    
                    Account account = new AccountDBController().getAccount(paymentMethod);
                    accountList.add(account);
                    
                    if(balance > 0){
                        query = "INSERT subentryaccountitems (id, date, accountname, amount, action) "
                            + "VALUES ('" + sid + "', '" + date + "', " + salesRemainingBalanceAccount + ", " + balance + ", 'Debit')";
                        statement.executeUpdate(query);
                        
                        Account account1 = new AccountDBController().getAccount(salesRemainingBalanceAccount);
                        accountList.add(account1);
                    }
                }
                
                //Revenue
                query = "INSERT subentryaccountitems (id, date, accountname, amount, action) "
                            + "VALUES ('" + sid + "', '" + date + "', " + salesRevenueAccount + ", " + totalAmount + ", 'Credit')";
                statement.executeUpdate(query);
                
                Account account = new AccountDBController().getAccount(salesRevenueAccount);
                accountList.add(account);
                
                //Log
                String affectedAccounts = new AuditTrail().accountsToString(accountList);
               
                new AuditTrailDBController().logTrail("Sales", "Added",salesPerson, sid, affectedAccounts, 0.00, 0.00, totalAmount);
                
            } catch(SQLException e){
                e.printStackTrace();
            }  finally {
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
    
    public SalesTransaction getSaleTransaction(String id){
        SalesTransaction sales = new SalesTransaction();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            ArrayList<Item> itemList = new ArrayList();
            
            if(id.contains("CSID")){
                query = "SELECT * FROM customersalestransaction WHERE sid = '" + id + "'";
                result = statement.executeQuery(query);            

                while(result.next()){     
                    sales.setId(result.getString("sid"));
                    sales.setDate(result.getString("date"));

                    Customer customer = new CustomerDBController().getCustomer(result.getString("customer"));

                    sales.setCustomerName(customer.getName());
                    sales.setCustomerContact(customer.getPhoneNumber());
                    sales.setDescription(result.getString("description"));
                    sales.setSalesPerson(new UserDBController().getCredential(result.getString("salesperson")).getName());
                    sales.setTotalAmount(result.getDouble("totalamount"));
                    sales.setAmountPaid(getSalesAmountPaid(sales.getId()));
                    sales.setRemainingBalance(getSalesBalance(sales.getId()));
                    Account account = new AccountDBController().getAccount(result.getInt("paymentmethod"));                    
                    
                    if(account.getCode() == 103){ account.setAccountName("Credit");}
                    sales.setPaymentMethod(account);                               
                }
            } else {
                query = "SELECT * FROM guestsalestransaction WHERE sid = '" + id + "'";
                result = statement.executeQuery(query);

                while(result.next()){
                    sales.setId(result.getString("sid"));
                    sales.setDate(result.getString("date"));
                    sales.setCustomerName(result.getString("customer"));
                    sales.setCustomerContact(result.getString("phonenumber"));
                    sales.setDescription(result.getString("description"));
                    sales.setSalesPerson(new UserDBController().getCredential(result.getString("salesperson")).getName());
                    sales.setTotalAmount(result.getDouble("totalamount"));
                    sales.setAmountPaid(getSalesAmountPaid(sales.getId()));
                    sales.setRemainingBalance(getSalesBalance(sales.getId()));
                    Account account = new AccountDBController().getAccount(result.getInt("paymentmethod"));                    
                    
                    if(account.getCode() == 103){ account.setAccountName("Credit");}
                    sales.setPaymentMethod(account); 
                }
            }  
            
            query = "SELECT * FROM item WHERE id = '" + id + "'";
            result = statement.executeQuery(query);
                    
            while(result.next()){
                Item item = new Item();
                        
                item.setName(result.getString("name"));
                item.setDescription(result.getString("description"));
                item.setQuantity(result.getString("quantity"));
                item.setPrice(result.getDouble("price"));
                        
                itemList.add(item);
            }                   
            
            sales.setItemList(itemList);
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
        
        return sales;
    }
    
    public ArrayList<SalesTransaction> getAllSales(){
        ArrayList<SalesTransaction> salesTransactionList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT * FROM customersalestransaction "
                    + "ORDER BY date DESC, time DESC";
            result = statement.executeQuery(query);

            while(result.next()){
                SalesTransaction sales = new SalesTransaction();
                
                sales.setId(result.getString("sid"));
                sales.setDate(result.getString("date"));
                
                Customer customer = new CustomerDBController().getCustomer(result.getString("customer"));

                sales.setCustomerName(customer.getName());
                sales.setCustomerContact(customer.getPhoneNumber());
                sales.setDescription(result.getString("description"));
                sales.setSalesPerson(new UserDBController().getCredential(result.getString("salesperson")).getName());
                sales.setTotalAmount(result.getDouble("totalamount"));
                sales.setAmountPaid(getSalesAmountPaid(sales.getId()));
                sales.setRemainingBalance(getSalesBalance(sales.getId()));
                sales.setPaymentMethod(new AccountDBController().getAccount(result.getInt("paymentmethod")));
                
                salesTransactionList.add(sales);
            }
            
            query = "SELECT * FROM guestsalestransaction "
                    + "ORDER BY date DESC, time DESC";
            result = statement.executeQuery(query);

            while(result.next()){
                SalesTransaction sales = new SalesTransaction();
                
                sales.setId(result.getString("sid"));
                sales.setDate(result.getString("date"));
                sales.setCustomerName(result.getString("customer"));
                sales.setCustomerContact(result.getString("phonenumber"));
                sales.setDescription(result.getString("description"));
                sales.setSalesPerson(new UserDBController().getCredential(result.getString("salesperson")).getName());
                sales.setTotalAmount(result.getDouble("totalamount"));
                sales.setAmountPaid(getSalesAmountPaid(sales.getId()));
                sales.setRemainingBalance(getSalesBalance(sales.getId()));
                sales.setPaymentMethod(new AccountDBController().getAccount(result.getInt("paymentmethod")));
                
                salesTransactionList.add(sales);
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

        return salesTransactionList;
    }
    
    public ArrayList<SalesTransaction> getAllSales(String fromDate, String toDate){
        ArrayList<SalesTransaction> salesTransactionList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT * FROM customersalestransaction "
                    + "WHERE date BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + "ORDER BY date DESC, time DESC";
            result = statement.executeQuery(query);

            while(result.next()){
                SalesTransaction sales = new SalesTransaction();
                
                sales.setId(result.getString("sid"));
                sales.setDate(result.getString("date"));
                
                Customer customer = new CustomerDBController().getCustomer(result.getString("customer"));

                sales.setCustomerName(customer.getName());
                sales.setCustomerContact(customer.getPhoneNumber());
                sales.setDescription(result.getString("description"));
                sales.setSalesPerson(new UserDBController().getCredential(result.getString("salesperson")).getName());
                sales.setTotalAmount(result.getDouble("totalamount"));
                sales.setAmountPaid(getSalesAmountPaid(sales.getId()));
                sales.setRemainingBalance(getSalesBalance(sales.getId()));
                sales.setPaymentMethod(new AccountDBController().getAccount(result.getInt("paymentmethod")));
                
                salesTransactionList.add(sales);
            }
            
            query = "SELECT * FROM guestsalestransaction "
                    + "WHERE date BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + "ORDER BY date DESC, time DESC";
            result = statement.executeQuery(query);

            while(result.next()){
                SalesTransaction sales = new SalesTransaction();
                
                sales.setId(result.getString("sid"));
                sales.setDate(result.getString("date"));
                sales.setCustomerName(result.getString("customer"));
                sales.setCustomerContact(result.getString("phonenumber"));
                sales.setDescription(result.getString("description"));
                sales.setSalesPerson(new UserDBController().getCredential(result.getString("salesperson")).getName());
                sales.setTotalAmount(result.getDouble("totalamount"));
                sales.setAmountPaid(getSalesAmountPaid(sales.getId()));
                sales.setRemainingBalance(getSalesBalance(sales.getId()));
                sales.setPaymentMethod(new AccountDBController().getAccount(result.getInt("paymentmethod")));
                
                salesTransactionList.add(sales);
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

        return salesTransactionList;
    }
    
    public ArrayList<SalesTransaction> searchSales(String search){
        ArrayList<SalesTransaction> salesTransactionList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT customersalestransaction.sid, customersalestransaction.date, concat(customer.firstname, ' ', customer.lastname) AS customer, "
                    + "customer.phonenumber, customersalestransaction.description, concat(user.firstname, ' ', user.lastname) AS salesperson, "
                    + "customersalestransaction.totalamount, customersalestransaction.paymentmethod "
                    + "FROM customersalestransaction "
                    + "INNER JOIN customer ON customersalestransaction.customer = customer.cid "
                    + "INNER JOIN user ON customersalestransaction.salesperson = user.uid "
                    + "WHERE sid LIKE '%" + search + "%' OR date LIKE '%" + search + "%' OR customer.firstname LIKE '%" + search + "%' OR description LIKE '%" + search + "%' "
                    + "OR customer.lastname LIKE '%" + search + "%'";
            result = statement.executeQuery(query);
            
            while(result.next()){
                SalesTransaction sales = new SalesTransaction();
                
                sales.setId(result.getString("sid"));
                sales.setDate(result.getString("date"));
                sales.setCustomerName(result.getString("customer"));
                sales.setCustomerContact(result.getString("customer.phonenumber"));
                sales.setDescription(result.getString("description"));
                sales.setSalesPerson(result.getString("salesperson"));
                sales.setTotalAmount(result.getDouble("totalamount"));
                sales.setAmountPaid(getSalesAmountPaid(sales.getId()));
                sales.setRemainingBalance(getSalesBalance(sales.getId()));
                sales.setPaymentMethod(new AccountDBController().getAccount(result.getInt("paymentmethod")));
                
                salesTransactionList.add(sales);
            }
            
            query = "SELECT * FROM guestsalestransaction "
                    + "WHERE sid LIKE '%" + search + "%' OR date LIKE '%" + search + "%' OR customer LIKE '%" + search + "%' OR description LIKE '%" + search + "%'";
            result = statement.executeQuery(query);
            
            while(result.next()){
                SalesTransaction sales = new SalesTransaction();
                
                sales.setId(result.getString("sid"));
                sales.setDate(result.getString("date"));
                sales.setCustomerName(result.getString("customer"));
                sales.setCustomerContact(result.getString("phonenumber"));
                sales.setDescription(result.getString("description"));
                sales.setSalesPerson(new UserDBController().getCredential(result.getString("salesperson")).getName());
                sales.setTotalAmount(result.getDouble("totalamount"));
                sales.setAmountPaid(getSalesAmountPaid(sales.getId()));
                sales.setRemainingBalance(getSalesBalance(sales.getId()));
                sales.setPaymentMethod(new AccountDBController().getAccount(result.getInt("paymentmethod")));
                
                salesTransactionList.add(sales);
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
          
        return salesTransactionList;
    }
    
    public ArrayList<SalesTransaction> sortSales(String sortBy1, String sortBy2){
        ArrayList<SalesTransaction> salesTransactionList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT * FROM customersalestransaction "
                    + "INNER JOIN customer ON customersalestransaction.customer = customer.cid "
                    + "ORDER BY " + sortBy1;
            result = statement.executeQuery(query);
            
            while(result.next()){
                SalesTransaction sales = new SalesTransaction();
                
                sales.setId(result.getString("sid"));
                sales.setDate(result.getString("date"));
                
                Customer customer = new CustomerDBController().getCustomer(result.getString("customer"));

                sales.setCustomerName(customer.getName());
                sales.setCustomerContact(customer.getPhoneNumber());
                sales.setDescription(result.getString("description"));
                sales.setSalesPerson(new UserDBController().getCredential(result.getString("salesperson")).getName());
                sales.setTotalAmount(result.getDouble("totalamount"));
                sales.setAmountPaid(getSalesAmountPaid(sales.getId()));
                sales.setRemainingBalance(getSalesBalance(sales.getId()));
                sales.setPaymentMethod(new AccountDBController().getAccount(result.getInt("paymentmethod")));
                
                salesTransactionList.add(sales);
            }
            
            query = "SELECT * FROM guestsalestransaction "
                    + "ORDER BY " + sortBy2;
            result = statement.executeQuery(query);
            
            while(result.next()){
                SalesTransaction sales = new SalesTransaction();
                
                sales.setId(result.getString("sid"));
                sales.setDate(result.getString("date"));
                sales.setCustomerName(result.getString("customer"));
                sales.setCustomerContact(result.getString("phonenumber"));
                sales.setDescription(result.getString("description"));
                sales.setSalesPerson(new UserDBController().getCredential(result.getString("salesperson")).getName());
                sales.setTotalAmount(result.getDouble("totalamount"));
                sales.setAmountPaid(getSalesAmountPaid(sales.getId()));
                sales.setRemainingBalance(getSalesBalance(sales.getId()));
                sales.setPaymentMethod(new AccountDBController().getAccount(result.getInt("paymentmethod")));
                
                salesTransactionList.add(sales);
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
        
        return salesTransactionList;
    }
    
    public void paySalesTransaction(String id, Double amountPaid, String remarks, String salesPerson, int paymentCode, Cheque cheque){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String date = dateFormat.format(Calendar.getInstance().getTime());
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            //payment
            query = "INSERT INTO subentryaccountitems (id, date, accountname, amount, action, remarks) "
                    + "VALUES ('" + id + "', '" + date + "', " + paymentCode + ", " + amountPaid + ", 'Debit', '" + remarks + "')";
            
            statement.executeUpdate(query);
            
            //Cheque info  
            int refId = 0;
            query = "SELECT refid FROM subentryaccountitems WHERE refid = LAST_INSERT_ID()";
            result = statement.executeQuery(query);
            
            while(result.next()){
                refId = result.getInt("refid");
            }
                    
            addCheque(cheque, refId);
            
            //balance
            query = "INSERT INTO subentryaccountitems (id, date, accountname, amount, action) "
                    + "VALUES ('" + id + "', '" + date + "', " + salesRemainingBalanceAccount + ", " + amountPaid + ", 'Credit')";
            
            statement.executeUpdate(query);     
            
            ArrayList<Account> accountList = new ArrayList();
            Account account = new AccountDBController().getAccount(paymentCode);
            accountList.add(account);
            account = new AccountDBController().getAccount(salesRemainingBalanceAccount);
            accountList.add(account);
            
            String affectedAccounts = new AuditTrail().accountsToString(accountList);
               
            new AuditTrailDBController().logTrail("Sales", "Added Payment",salesPerson, id, affectedAccounts, 0.00, 0.00, amountPaid);
            
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
    
    public void paySalesTransaction(String id, Double amountPaid, String remarks, String salesPerson, int paymentCode){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String date = dateFormat.format(Calendar.getInstance().getTime());
        Connection connection = null;
        Statement statement = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            //payment
            query = "INSERT INTO subentryaccountitems (id, date, accountname, amount, action, remarks) "
                    + "VALUES ('" + id + "', '" + date + "', " + paymentCode + ", " + amountPaid + ", 'Debit', '" + remarks + "')";
            
            statement.executeUpdate(query);
            
            //balance
            query = "INSERT INTO subentryaccountitems (id, date, accountname, amount, action) "
                    + "VALUES ('" + id + "', '" + date + "', " + salesRemainingBalanceAccount + ", " + amountPaid + ", 'Credit')";
            
            statement.executeUpdate(query);     
            
            ArrayList<Account> accountList = new ArrayList();
            Account account = new AccountDBController().getAccount(paymentCode);
            accountList.add(account);
            account = new AccountDBController().getAccount(salesRemainingBalanceAccount);
            accountList.add(account);
            
            String affectedAccounts = new AuditTrail().accountsToString(accountList);
               
            new AuditTrailDBController().logTrail("Sales", "Added Payment",salesPerson, id, affectedAccounts, 0.00, 0.00, amountPaid);
            
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
    
    public void addCheque(Cheque cheque, int refId){
        Connection connection = null;
        Statement statement = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            
            query = "INSERT INTO cheque (chequenumber, dateissued, payee, accountnumber, bankname, refid) VALUES "
                    + "('" + cheque.getChequeNumber() + "', '" + cheque.getDateIssued() + "', '" + cheque.getPayee() + "', '" + cheque.getAccountNumber() 
                    + "', '" + cheque.getBankName() + "', " + refId + ")";
            
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
    
    public boolean hasChequeInfo(int refId){
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        int count = 0;
        
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            
            
            query = "SELECT count(refid) as count FROM bms.cheque WHERE refid = " + refId;
            result = statement.executeQuery(query);

            while(result.next()){
                count = result.getInt("count");
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
        
        if(count > 0){
                return true;
        } else {
            return false;
        }
    }
    
    public Cheque getCheque(int refId){
        Cheque cheque = new Cheque();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            
            
            query = "SELECT * FROM cheque WHERE refid = " + refId;
            result = statement.executeQuery(query);

            while(result.next()){
                cheque.setChequeNumber(result.getString("chequenumber"));
                cheque.setDateIssued(result.getString("dateissued"));
                cheque.setPayee(result.getString("payee"));
                cheque.setAccountNumber(result.getString("accountnumber"));
                cheque.setBankName(result.getString("bankname"));
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
        
        return cheque;
    }
    
    public Double getSalesAmountPaid(String id){
        Double totalAmount = 0.00;
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT SUM(amount) AS amount FROM subentryaccountitems "
                    + "INNER JOIN account ON subentryaccountitems.accountname = account.code "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                    + "WHERE subentryaccountitems.id = '" + id + "' AND accounttype.normally = 'Debit' AND subentryaccountitems.action = 'Debit' AND subentryaccountitems.accountname != " + salesRemainingBalanceAccount;
            result = statement.executeQuery(query);
            
            while(result.next()){
                totalAmount += result.getDouble("amount");
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
        
        return totalAmount;
    }
    
    public ArrayList<Account> getSalesPaymentHistory(String id){
        ArrayList<Account> paymentHistory = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT * FROM subentryaccountitems "
                    + "INNER JOIN account ON subentryaccountitems.accountname = account.code "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                    + "WHERE subentryaccountitems.id = '" + id + "' AND accounttype.normally = 'Debit' AND subentryaccountitems.action = 'Debit' AND subentryaccountitems.accountname != " + salesRemainingBalanceAccount
                    + " ORDER BY subentryaccountitems.date DESC";
            result = statement .executeQuery(query);
            
            while(result.next()){
                Account account = new Account();
                
                account.setRefId(result.getInt("refid"));
                account.setDate(result.getString("date"));
                account.setCode(result.getInt("accountname"));
                account.setRemarks(result.getString("remarks"));
                account.setAmount(result.getDouble("amount"));
                account.setAction(result.getString("action"));
                
                paymentHistory.add(account);
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
        
        return paymentHistory;
    }
    
    public Double getSalesBalance(String id){
        Double balance = 0.00;
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT amount, action FROM subentryaccountitems WHERE id = '" + id + "' AND accountname = " + salesRemainingBalanceAccount;
            result = statement.executeQuery(query);
            
            while(result.next()){
                Account account = new Account();
                
                account.setAmount(result.getDouble("amount"));
                account.setAction(result.getString("action"));
                
                if(account.getAction().equals("Debit")){
                    balance += account.getAmount();
                } else {
                    balance -= account.getAmount();
                }              
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
        
        return balance ;
    }
    
    public void deleteSales(String id, String user){
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            
            if(id.contains("CSID")){
                query = "SELECT DISTINCT accountname FROM subentryaccountitems WHERE id = '" + id + "'";
                result = statement.executeQuery(query);
                
                ArrayList<Account> accountList = new ArrayList();
                Double totalAmount = 0.00;
                while(result.next()){
                    Account account = new AccountDBController().getAccount(result.getInt("accountname"));
                    accountList.add(account);                
                }
                
                query = "SELECT totalamount FROM customersalestransaction WHERE sid = '" + id + "'";
                result = statement.executeQuery(query);
                
                while(result.next()){
                    totalAmount = result.getDouble("totalamount");
                }
                
                query = "DELETE FROM customersalestransaction WHERE sid = '" + id + "' ";
                statement.executeUpdate(query);
                
                query = "DELETE FROM item WHERE id = '" + id + "' ";
                statement.executeUpdate(query);
                
                query = "DELETE FROM subentryaccountitems WHERE id = '" + id + "' ";
                statement.executeUpdate(query);
                
                String affectedAccounts = new AuditTrail().accountsToString(accountList);
                
                new AuditTrailDBController().logTrail("Sales", "Deleted",user, id, affectedAccounts, 0.00, 0.00, totalAmount);
            } else {
                query = "SELECT DISTINCT accountname FROM subentryaccountitems WHERE id = '" + id + "'";
                result = statement.executeQuery(query);
                
                ArrayList<Account> accountList = new ArrayList();
                Double totalAmount = 0.00;
                while(result.next()){
                    Account account = new AccountDBController().getAccount(result.getInt("accountname"));
                    accountList.add(account);                
                }
                
                query = "SELECT totalamount FROM guestsalestransaction WHERE sid = '" + id + "'";
                result = statement.executeQuery(query);
                
                while(result.next()){
                    totalAmount = result.getDouble("totalamount");
                }
                
                query = "DELETE FROM guestsalestransaction WHERE sid = '" + id + "' ";
                statement.executeUpdate(query);
                
                query = "DELETE FROM item WHERE id = '" + id + "' ";
                statement.executeUpdate(query);
                
                query = "DELETE FROM subentryaccountitems WHERE id = '" + id + "' ";
                statement.executeUpdate(query);
                
                String affectedAccounts = new AuditTrail().accountsToString(accountList);
                
                new AuditTrailDBController().logTrail("Sales", "Deleted",user, id, affectedAccounts, 0.00, 0.00, totalAmount);
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
    }
    
    //ExpensesEntry
    public void addExpensesEntry(String date, String description, String encoder, ArrayList<Account> accountList, String document){
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            String eid = "ID00001";
            LocalTime localTime = LocalTime.now();
            String time = String.valueOf(localTime);
            
            query = "INSERT INTO expensestransaction (date, description, encoder, time) "
                    + "VALUES ('" + date + "', '" + description + "', '" + encoder + "', '" + time + "')";
            
            statement.executeUpdate(query);
            
            query = "UPDATE expensestransaction SET eid = CONCAT('EID', YEAR(CURDATE()), MONTH(CURDATE()), LPAD(LAST_INSERT_ID(), 3, '0')) WHERE id = LAST_INSERT_ID()";
            
            statement.executeUpdate(query);
            
            query = "SELECT eid FROM expensestransaction WHERE date = '" + date + "' AND time = '" + time + "'";
            result = statement.executeQuery(query);
            
            while(result.next()){eid = result.getString("eid");}

            query = "INSERT INTO document (transactionid, document, filename) "
                    + "VALUES (?, ?, ?)";
            
            preparedStatement = connection.prepareStatement(query);
            
            FileInputStream fileInputStream = null;
            
            if(document != null){fileInputStream = new FileInputStream(document);}
            
            preparedStatement.setString(1, eid);
            preparedStatement.setBinaryStream(2, fileInputStream);
            preparedStatement.setString(3, filenameSplitter(document));
            preparedStatement.executeUpdate();
            
            if(fileInputStream != null){fileInputStream.close();}

            Iterator i = (Iterator) accountList.iterator();
            
            while(i.hasNext()){
                Account account = (Account) i.next();
                query = "INSERT INTO accountitems (id, accountname, amount, action) "
                        + "VALUES ('" + eid + "', " + account.getCode() + ", " + account.getAmount() + ", '" + account.getAction() +"')";
                statement.execute(query);
            }
            
            //Log
            String affectedAccounts = new AuditTrail().accountsToString(accountList);
            Double totalAmount = 0.00;
            
            for(int j = 0; j < accountList.size(); j++){
                if(accountList.get(j).getAction().equalsIgnoreCase("Debit")){
                    totalAmount += accountList.get(j).getAmount();
                }
            }
            
            new AuditTrailDBController().logTrail("Expenses", "Added", encoder, eid, affectedAccounts, 0.00, 0.00, totalAmount);
        } catch (Exception e){
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
            if(preparedStatement != null){
                try{preparedStatement.close();}catch(SQLException e){}
            }
        }
    }
    
    public ArrayList<ExpensesTransaction> getAllExpensesEntry(){
        ArrayList<ExpensesTransaction> entryList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT expensestransaction.eid, expensestransaction.date, expensestransaction.description, concat(user.firstname, ' ', user.lastname) AS encoder "
                    + "FROM expensestransaction "
                    + "INNER JOIN user ON expensestransaction.encoder = user.uid "
                    + "ORDER BY expensestransaction.date DESC, expensestransaction.time DESC";
            result = statement.executeQuery(query);
            
            while(result.next()){
                ExpensesTransaction entry = new ExpensesTransaction();
                
                entry.setId(result.getString("eid"));
                entry.setDate(result.getString("date"));
                entry.setDescription(result.getString("description"));             
                entry.setEncoder(result.getString("encoder"));
                entry.setAccountList(getAccountItems(entry.getId()));
                
                entryList.add(entry);                           
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
        
        return entryList;
    }
    
    public ArrayList<ExpensesTransaction> getAllExpensesEntry(String fromDate, String toDate){
        ArrayList<ExpensesTransaction> entryList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT expensestransaction.eid, expensestransaction.date, expensestransaction.description, concat(user.firstname, ' ', user.lastname) AS encoder "
                    + "FROM expensestransaction "
                    + "INNER JOIN user ON expensestransaction.encoder = user.uid "
                    + "WHERE expensestransaction.date BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + "ORDER BY expensestransaction.date DESC, expensestransaction.time DESC";
            result = statement.executeQuery(query);
            
            while(result.next()){
                ExpensesTransaction entry = new ExpensesTransaction();
                
                entry.setId(result.getString("eid"));
                entry.setDate(result.getString("date"));
                entry.setDescription(result.getString("description"));             
                entry.setEncoder(result.getString("encoder"));
                entry.setAccountList(getAccountItems(entry.getId()));
                
                entryList.add(entry);                           
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
        
        return entryList;
    }
    
    public ArrayList<ExpensesTransaction> searchExpensesEntry(String search){
        ArrayList<ExpensesTransaction> entryList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT expensestransaction.eid, expensestransaction.date, expensestransaction.description, concat(user.firstname, ' ', user.lastname) AS encoder "
                    + "FROM expensestransaction "
                    + "INNER JOIN user ON expensestransaction.encoder = user.uid "
                    + "WHERE eid LIKE '%" + search + "%' OR date LIKE '%" + search + "%' OR description LIKE '%" + 
                    search + "%' OR user.firstname LIKE '%" + search + "%' OR user.lastname LIKE '%" + search + "%'";
            result = statement.executeQuery(query);
            
            while(result.next()){
                ExpensesTransaction entry = new ExpensesTransaction();
                
                entry.setId(result.getString("eid"));
                entry.setDate(result.getString("date"));
                entry.setDescription(result.getString("description"));             
                entry.setEncoder(result.getString("encoder"));
                entry.setAccountList(getAccountItems(entry.getId()));
                
                entryList.add(entry);                           
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
        
        return entryList;
    }
    
    public ArrayList<ExpensesTransaction> sortExpensesEntry(String sortBy){
        ArrayList<ExpensesTransaction> entryList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT expensestransaction.eid, expensestransaction.date, expensestransaction.description, concat(user.firstname, ' ', user.lastname) AS encoder "
                    + "FROM expensestransaction "
                    + "INNER JOIN user ON expensestransaction.encoder = user.uid "
                    + "ORDER BY " + sortBy;
            result = statement.executeQuery(query);
            
            while(result.next()){
                ExpensesTransaction entry = new ExpensesTransaction();
                
                entry.setId(result.getString("eid"));
                entry.setDate(result.getString("date"));
                entry.setDescription(result.getString("description"));             
                entry.setEncoder(result.getString("encoder"));
                entry.setAccountList(getAccountItems(entry.getId()));
                
                entryList.add(entry);                           
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
        
        return entryList;
    }
    
    public ExpensesTransaction getExpensesTransaction(String id){
        ExpensesTransaction entry = new ExpensesTransaction();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT expensestransaction.eid, expensestransaction.date, expensestransaction.description, concat(user.firstname, ' ', user.lastname) AS encoder "
                    + "FROM expensestransaction "
                    + "INNER JOIN user ON expensestransaction.encoder = user.uid "
                    + "WHERE expensestransaction.eid = '" + id + "'";
            result = statement.executeQuery(query);
            
            while(result.next()){
                entry.setId(result.getString("eid"));
                entry.setDate(result.getString("date"));
                entry.setDescription(result.getString("description"));             
                entry.setEncoder(result.getString("encoder"));
                entry.setAccountList(getAccountItems(entry.getId()));                                       
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
        
        return entry;
    }
    
    public void deleteExpensesEntry(String id, String user){
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try {
            connection = Driver.getConnection();
            statement = connection.createStatement();
            
            query = "SELECT DISTINCT accountname FROM accountitems WHERE id = '" + id + "'";
            result = statement.executeQuery(query);
                
            ArrayList<Account> accountList = new ArrayList();
            Double totalAmount = getExpensesEntryTotalAmount(id);
            while(result.next()){
                Account account = new AccountDBController().getAccount(result.getInt("accountname"));
                accountList.add(account);                
            }
                
            query = "DELETE FROM expensestransaction WHERE eid = '" + id + "' ";
            statement.executeUpdate(query);
                
            query = "DELETE FROM accountitems WHERE id = '" + id + "' ";
            statement.executeUpdate(query);
                
            String affectedAccounts = new AuditTrail().accountsToString(accountList);
                
            new AuditTrailDBController().logTrail("Expenses", "Deleted",user, id, affectedAccounts, 0.00, 0.00, totalAmount);
            
        } catch (SQLException e) {
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
    
    public void updateExpensesEntry(String id, String date, String description, String encoder, ArrayList<Account> accountList){
        Connection connection = null;
        Statement statement = null;
        try{
            Double beforeValue = getExpensesEntryTotalAmount(id);
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "UPDATE expensestransaction SET date = '" + date + "', description = '" + description + "' WHERE eid = '" + id + "'";
            statement.execute(query);
            
            query = "DELETE FROM accountitems WHERE id = '" + id + "' ";
            statement.executeUpdate(query);
            
            Iterator i = (Iterator) accountList.iterator();
            
            while(i.hasNext()){
                Account account = (Account) i.next();
                query = "INSERT INTO accountitems (id, accountname, amount, action) "
                        + "VALUES ('" + id + "', " + account.getCode() + ", " + account.getAmount() + ", '" + account.getAction() +"')";
                statement.execute(query);
            }
            Double afterValue = getExpensesEntryTotalAmount(id);
            
            //Log
            String affectedAccounts = new AuditTrail().accountsToString(accountList);
                
            new AuditTrailDBController().logTrail("Expenses", "Edited",encoder, id, affectedAccounts, beforeValue, afterValue, afterValue);
            
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
    
    public Double getExpensesEntryTotalAmount(String id){
        Double totalAmount = 0.00;
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
                connection = Driver.getConnection();
                statement = connection.createStatement();
                query = "SELECT * FROM accountitems WHERE id = '" + id + "' ";
                result = statement.executeQuery(query);
                
                while(result.next()){
                    if(result.getString("action").equalsIgnoreCase("Debit")){
                        totalAmount += result.getDouble("amount");
                    }
                }
            } catch(SQLException e){
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
        return totalAmount;
    }  

    //Purchases
    private int purchasesRemainingBalanceAccount = new AccountDBController().getAccountCode(new Dashboard().getAccountConfig("purchasesconfig.properties", "purchases_remaining_balance_account"));

    public void addPurchases(String date, String dueDate, String supplier, Double totalAmount, Double amountPaid, Double balance, 
            int paymentMethod, int assetAccount, String purchaser, String description, String remarks, ArrayList<Item> itemList, String encoder, String document){
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            String pid = "ID00001";
            LocalTime localTime = LocalTime.now();
            String time = String.valueOf(localTime); 
            
            /*
            query = "INSERT INTO purchasestransaction (date, duedate, supplier, totalamount, assetaccount, paymentmethod, description, purchaser, time) "
                    + "VALUES ('" + date + "', '" + dueDate + "', '" + supplier + "', " + totalAmount + ", " + assetAccount + ", " + paymentMethod + ", '"
                    + description + "', '" + purchaser + "', '" + time + "')";
            
            statement.executeUpdate(query);*/
            
            query = "INSERT INTO purchasestransaction (date, duedate, supplier, totalamount, assetaccount, paymentmethod, description, purchaser, time, document) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            preparedStatement = connection.prepareStatement(query);
            
            FileInputStream fileInputStream = null;
            
            if(!document.isBlank()){fileInputStream = new FileInputStream(document);}                       
            
            preparedStatement.setString(1, date);
            preparedStatement.setString(2, dueDate);
            preparedStatement.setString(3, supplier);
            preparedStatement.setDouble(4, totalAmount);
            preparedStatement.setInt(5, assetAccount);
            preparedStatement.setInt(6, paymentMethod);
            preparedStatement.setString(7, description);
            preparedStatement.setString(8, purchaser);
            preparedStatement.setString(9, time);
            preparedStatement.setBinaryStream(10, fileInputStream);
            preparedStatement.executeUpdate();
            
            if(fileInputStream != null){fileInputStream.close();}
            
            query = "UPDATE purchasestransaction SET pid = CONCAT('PID', YEAR(CURDATE()), MONTH(CURDATE()), LPAD(LAST_INSERT_ID(), 3, '0')) WHERE id = LAST_INSERT_ID()";
            
            statement.executeUpdate(query);
            
            query = "SELECT pid FROM purchasestransaction WHERE date = '" + date + "' AND time = '" + time + "'";
            result = statement.executeQuery(query);
            
            while(result.next()){pid = result.getString("pid");}
            
            Iterator i = (Iterator) itemList.iterator();
                
                while(i.hasNext()){
                    Item item = (Item) i.next();
                    
                    query = "INSERT INTO item (id, name, description, quantity, price) "
                            + "VALUES ('" + pid + "', '" + item.getName() + "', '" + item.getDescription() 
                            + "', '" + item.getQuantity() + "', " + item.getPrice() + ")";
                    statement.executeUpdate(query);
                }
                
                ArrayList<Account> accountList = new ArrayList();
                Account account = new Account();
                
                //Payment method
                if(amountPaid == 0){
                    //direct credit
                    query = "INSERT subentryaccountitems (id, date, accountname, amount, action) "
                            + "VALUES ('" + pid + "', '" + date + "', " + purchasesRemainingBalanceAccount + ", " + balance + ", 'Credit')";
                    statement.executeUpdate(query);
                    
                    account = new AccountDBController().getAccount(401);
                    accountList.add(account);
                } else {
                    query = "INSERT subentryaccountitems (id, date, accountname, amount, action, remarks) "
                            + "VALUES ('" + pid + "', '" + date + "', " + paymentMethod + ", " + amountPaid + ", 'Credit', '" +  remarks + "')";
                    statement.executeUpdate(query);
                    
                    account = new AccountDBController().getAccount(paymentMethod);
                    accountList.add(account);
                    
                    if(balance > 0){
                        query = "INSERT subentryaccountitems (id, date, accountname, amount, action) "
                            + "VALUES ('" + pid + "', '" + date + "', " + purchasesRemainingBalanceAccount + ", " + balance + ", 'Credit')";
                        statement.executeUpdate(query);
                        
                        account = new AccountDBController().getAccount(401);
                        accountList.add(account);
                    }
                }
                
                //Asset
                query = "INSERT subentryaccountitems (id, date, accountname, amount, action) "
                            + "VALUES ('" + pid + "', '" + date + "', " + assetAccount + ", " + totalAmount + ", 'Debit')";
                statement.executeUpdate(query);
            
                account = new AccountDBController().getAccount(assetAccount);
                accountList.add(account);
                
                String affectedAccounts = new AuditTrail().accountsToString(accountList);
               
                new AuditTrailDBController().logTrail("Purchases", "Added",encoder, pid, affectedAccounts, 0.00, 0.00, totalAmount);
        } catch (Exception e){
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
            if(preparedStatement != null){
                try{preparedStatement.close();}catch(SQLException e){}
            }
        }      
    }
    
    public Image getDocument(String id){
        Image image = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            
            query = "SELECT document FROM purchasestransaction WHERE pid = '" + id + "'";
            result = statement.executeQuery(query);
                
            if(result.next()){
                InputStream inputStream = result.getBinaryStream("document");
                
                BufferedImage bufferedImage = convertBlobToImage(inputStream);
                
                image = bufferedImage;
            }
        } catch(Exception e){
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
        return image;
    }
    
    public BufferedImage convertBlobToImage(InputStream inputStream) throws IOException {
        return ImageIO.read(inputStream);
    }
    
    public PurchaseTransaction getPurchaseTransaction(String id){
        PurchaseTransaction purchases = new PurchaseTransaction();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
            try{
                connection = Driver.getConnection();
                statement = connection.createStatement();
                ArrayList<Item> itemList = new ArrayList();
                
                query = "SELECT * FROM purchasestransaction WHERE pid = '" + id + "'";
                result = statement.executeQuery(query);
                
                while(result.next()){
                    purchases.setId(result.getString("pid"));
                    purchases.setDate(result.getString("date"));
                    purchases.setDueDate(result.getString("duedate"));
                    
                    Vendor supplier = new VendorDBController().getVendor(result.getString("supplier"));
                    
                    purchases.setSupplier(supplier.getName());
                    purchases.setSupplierContact(supplier.getPhoneNumber());
                    purchases.setTotalAmount(result.getDouble("totalamount"));
                    purchases.setAmountPaid(getPurchasesAmountPaid(purchases.getId()));
                    purchases.setRemainingBalance(getPurchasesBalance(purchases.getId()));
                    purchases.setPurchaser(result.getString("purchaser"));
                    purchases.setDescription(result.getString("description"));
                    purchases.setAssetAccount(new AccountDBController().getAccount(result.getInt("assetaccount")));
                    Account account = new AccountDBController().getAccount(result.getInt("paymentmethod"));                    
                    
                    if(account.getCode() == 401){ account.setAccountName("Credit");}
                    
                    purchases.setPaymentMethod(account);
                }
                
                query = "SELECT * FROM item WHERE id = '" + id + "'";
                ResultSet itemResult = statement.executeQuery(query);

                while(itemResult.next()){
                    Item item = new Item();

                    item.setName(itemResult.getString("name"));
                    item.setDescription(itemResult.getString("description"));
                    item.setQuantity(itemResult.getString("quantity"));
                    item.setPrice(itemResult.getDouble("price"));

                    itemList.add(item);
                }                   

                purchases.setItemList(itemList);
                
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
        return purchases;
    }
    
    public ArrayList<PurchaseTransaction> getAllPurchases(){
        ArrayList<PurchaseTransaction> purchasesList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        
            try{
                connection = Driver.getConnection();
                statement = connection.createStatement();
                query = "SELECT purchasestransaction.pid, purchasestransaction.date, vendor.name, purchasestransaction.description, purchasestransaction.totalamount, account.accountname "
                        + "FROM purchasestransaction "
                        + "INNER JOIN vendor ON purchasestransaction.supplier = vendor.vid "
                        + "INNER JOIN account ON purchasestransaction.assetaccount = account.code "
                        + "ORDER BY date DESC, time DESC";
                result = statement.executeQuery(query);
                
                while(result.next()){
                    PurchaseTransaction purchases = new PurchaseTransaction();
                    
                    purchases.setId(result.getString("purchasestransaction.pid"));
                    purchases.setDate(result.getString("purchasestransaction.date"));
                    purchases.setSupplier(result.getString("vendor.name"));
                    purchases.setDescription(result.getString("purchasestransaction.description"));
                    purchases.setTotalAmount(result.getDouble("purchasestransaction.totalamount"));
                    purchases.setAmountPaid(getPurchasesAmountPaid(purchases.getId()));
                    purchases.setRemainingBalance(getPurchasesBalance(purchases.getId()));
                    Account account = new Account();
                    account.setAccountName(result.getString("account.accountname"));
                    purchases.setAssetAccount(account);
                    
                    purchasesList.add(purchases);
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
        return purchasesList;
    }
    
    public ArrayList<PurchaseTransaction> getAllPurchases(String fromDate, String toDate){
        ArrayList<PurchaseTransaction> purchasesList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
            try{
                connection = Driver.getConnection();
                statement = connection.createStatement();
                query = "SELECT purchasestransaction.pid, purchasestransaction.date, vendor.name, purchasestransaction.description, purchasestransaction.totalamount, account.accountname "
                        + "FROM purchasestransaction "
                        + "INNER JOIN vendor ON purchasestransaction.supplier = vendor.vid "
                        + "INNER JOIN account ON purchasestransaction.assetaccount = account.code "
                        + "WHERE purchasestransaction.date BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                        + "ORDER BY date DESC, time DESC";
                result = statement.executeQuery(query);
                
                while(result.next()){
                    PurchaseTransaction purchases = new PurchaseTransaction();
                    
                    purchases.setId(result.getString("purchasestransaction.pid"));
                    purchases.setDate(result.getString("purchasestransaction.date"));
                    purchases.setSupplier(result.getString("vendor.name"));
                    purchases.setDescription(result.getString("purchasestransaction.description"));
                    purchases.setTotalAmount(result.getDouble("purchasestransaction.totalamount"));
                    purchases.setAmountPaid(getPurchasesAmountPaid(purchases.getId()));
                    purchases.setRemainingBalance(getPurchasesBalance(purchases.getId()));
                    Account account = new Account();
                    account.setAccountName(result.getString("account.accountname"));
                    purchases.setAssetAccount(account);
                    
                    purchasesList.add(purchases);
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
        return purchasesList;
    }
    
    public ArrayList<PurchaseTransaction> searchPurchases(String search){
        ArrayList<PurchaseTransaction> purchasesList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
            try{
                connection = Driver.getConnection();
                statement = connection.createStatement();
                query = "SELECT purchasestransaction.pid, purchasestransaction.date, vendor.name, purchasestransaction.description, purchasestransaction.totalamount, account.accountname "
                        + "FROM purchasestransaction "
                        + "INNER JOIN vendor ON purchasestransaction.supplier = vendor.vid "
                        + "INNER JOIN account ON purchasestransaction.assetaccount = account.code "
                        + "WHERE pid LIKE '%" + search + "%' OR date LIKE '%" + search + "%' OR vendor.name LIKE '%" + search + "%' OR description LIKE '%" + search + "%' OR account.accountname LIKE '%" + search + "%'";
                result = statement.executeQuery(query);
                
                while(result.next()){
                    PurchaseTransaction purchases = new PurchaseTransaction();
                    
                    purchases.setId(result.getString("purchasestransaction.pid"));
                    purchases.setDate(result.getString("purchasestransaction.date"));
                    purchases.setSupplier(result.getString("vendor.name"));
                    purchases.setDescription(result.getString("purchasestransaction.description"));
                    purchases.setTotalAmount(result.getDouble("purchasestransaction.totalamount"));
                    purchases.setAmountPaid(getPurchasesAmountPaid(purchases.getId()));
                    purchases.setRemainingBalance(getPurchasesBalance(purchases.getId()));
                    Account account = new Account();
                    account.setAccountName(result.getString("account.accountname"));
                    purchases.setAssetAccount(account);
                    
                    purchasesList.add(purchases);
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
        return purchasesList;
    }
    
    public ArrayList<PurchaseTransaction> sortPurchases(String sortBy){
        ArrayList<PurchaseTransaction> purchasesList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
            try{
                connection = Driver.getConnection();
                statement = connection.createStatement();
                query = "SELECT purchasestransaction.pid, purchasestransaction.date, vendor.name, purchasestransaction.description, purchasestransaction.totalamount, account.accountname "
                        + "FROM purchasestransaction "
                        + "INNER JOIN vendor ON purchasestransaction.supplier = vendor.vid "
                        + "INNER JOIN account ON purchasestransaction.assetaccount = account.code "
                        + "ORDER BY " + sortBy;
                result = statement.executeQuery(query);
                
                while(result.next()){
                    PurchaseTransaction purchases = new PurchaseTransaction();
                    
                    purchases.setId(result.getString("purchasestransaction.pid"));
                    purchases.setDate(result.getString("purchasestransaction.date"));
                    purchases.setSupplier(result.getString("vendor.name"));
                    purchases.setDescription(result.getString("purchasestransaction.description"));
                    purchases.setTotalAmount(result.getDouble("purchasestransaction.totalamount"));
                    purchases.setAmountPaid(getPurchasesAmountPaid(purchases.getId()));
                    purchases.setRemainingBalance(getPurchasesBalance(purchases.getId()));
                    Account account = new Account();
                    account.setAccountName(result.getString("account.accountname"));
                    purchases.setAssetAccount(account);
                    
                    purchasesList.add(purchases);
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
        return purchasesList;
    }
     
    public ArrayList<Account> getPurchasesPaymentHistory(String id){
        ArrayList<Account> paymentHistory = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT * FROM subentryaccountitems "
                    + "INNER JOIN account ON subentryaccountitems.accountname = account.code "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                    + "WHERE subentryaccountitems.id = '" + id + "' AND accounttype.normally = 'Debit' AND subentryaccountitems.action = 'Credit' AND subentryaccountitems.accountname != " + purchasesRemainingBalanceAccount
                    + " ORDER BY subentryaccountitems.date DESC";
            result = statement .executeQuery(query);
            
            while(result.next()){
                Account account = new Account();
                
                account.setDate(result.getString("date"));
                account.setRemarks(result.getString("remarks"));
                account.setAmount(result.getDouble("amount"));
                account.setAction(result.getString("action"));
                
                paymentHistory.add(account);
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
        return paymentHistory;
    }
    
    public void payPurchasesTransaction(String id, Double amountPaid, String remarks, String user, int paymentCode){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String date = dateFormat.format(Calendar.getInstance().getTime());
        Connection connection = null;
        Statement statement = null;

        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "INSERT INTO subentryaccountitems (id, date, accountname, amount, action, remarks) "
                    + "VALUES ('" + id + "', '" + date + "', " + paymentCode + ", " + amountPaid + ", 'Credit', '" + remarks + "')";
            
            statement.executeUpdate(query);
            
            query = "INSERT INTO subentryaccountitems (id, date, accountname, amount, action) "
                    + "VALUES ('" + id + "', '" + date + "', " + purchasesRemainingBalanceAccount + ", " + amountPaid + ", 'Debit')";
            
            statement.executeUpdate(query);    
            
            ArrayList<Account> accountList = new ArrayList();
            Account account = new AccountDBController().getAccount(paymentCode);
            accountList.add(account);
            account = new AccountDBController().getAccount(purchasesRemainingBalanceAccount);
            accountList.add(account);
            
            String affectedAccounts = new AuditTrail().accountsToString(accountList);
               
            new AuditTrailDBController().logTrail("Purchases", "Added Payment",user, id, affectedAccounts, 0.00, 0.00, amountPaid);
            
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
    
    public Double getPurchasesAmountPaid(String pid){
        Double amountPaid = 0.00;
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
            try{
                connection = Driver.getConnection();
                statement = connection.createStatement();
                query = "SELECT SUM(amount) AS amount FROM subentryaccountitems "
                    + "INNER JOIN account ON subentryaccountitems.accountname = account.code "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                    + "WHERE subentryaccountitems.id = '" + pid + "' AND accounttype.normally = 'Debit' AND subentryaccountitems.action = 'Credit' AND subentryaccountitems.accountname != " + purchasesRemainingBalanceAccount;
                
                result = statement.executeQuery(query);
                
                while(result.next()){
                    amountPaid = result.getDouble("amount");
                }
            } catch(SQLException e){
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
        return amountPaid;
    }
    
    public Double getPurchasesBalance(String id){
        Double balance = 0.00;
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT * FROM subentryaccountitems WHERE id = '" + id + "' AND accountname = " + purchasesRemainingBalanceAccount;
            result = statement.executeQuery(query);
            
            while(result.next()){
                Account account = new Account();
                
                account.setAmount(result.getDouble("amount"));
                account.setAction(result.getString("action"));
                
                if(account.getAction().equals("Credit")){
                    balance += account.getAmount();
                } else {
                    balance -= account.getAmount();
                }              
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
        return balance ;
    }
    
    public void deletePurchases(String id, String user){
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            
            query = "SELECT DISTINCT accountname FROM subentryaccountitems WHERE id = '" + id + "'";
                result = statement.executeQuery(query);
                
                ArrayList<Account> accountList = new ArrayList();
                Double totalAmount = 0.00;
                while(result.next()){
                    Account account = new AccountDBController().getAccount(result.getInt("accountname"));
                    accountList.add(account);                
                }
                
                query = "SELECT totalamount FROM purchasestransaction WHERE pid = '" + id + "'";
                result = statement.executeQuery(query);
                
                while(result.next()){
                    totalAmount = result.getDouble("totalamount");
                }
                
                query = "DELETE FROM purchasestransaction WHERE pid = '" + id + "' ";
                statement.executeUpdate(query);
                
                query = "DELETE FROM item WHERE id = '" + id + "' ";
                statement.executeUpdate(query);
                
                query = "DELETE FROM subentryaccountitems WHERE id = '" + id + "' ";
                statement.executeUpdate(query);
                
                String affectedAccounts = new AuditTrail().accountsToString(accountList);
                
                new AuditTrailDBController().logTrail("Purchases", "Deleted",user, id, affectedAccounts, 0.00, 0.00, totalAmount);
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
    
    //Ledger
    public ArrayList<LedgerAccount> getPostedEntries(int code, String fromDate, String toDate){
        ArrayList<LedgerAccount> postedEntries = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT accountitems.id, journalentry.date, '' AS remarks, journalentry.description, accountitems.action, accountitems.amount "
                    + "FROM accountitems "
                    + "INNER JOIN journalentry ON accountitems.id = journalentry.jid "
                    + "WHERE accountname = " + code + " AND journalentry.date BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + " UNION "
                    + "SELECT accountitems.id, expensestransaction.date, '' AS remarks, expensestransaction.description, accountitems.action, accountitems.amount "
                    + "FROM accountitems "
                    + "INNER JOIN expensestransaction ON accountitems.id = expensestransaction.eid "
                    + "WHERE accountname = " + code + " AND expensestransaction.date BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + " UNION "
                    + "SELECT subentryaccountitems.id, subentryaccountitems.date, subentryaccountitems.remarks, purchasestransaction.description , subentryaccountitems.action, subentryaccountitems.amount "
                    + "FROM subentryaccountitems "
                    + "INNER JOIN purchasestransaction ON subentryaccountitems.id = purchasestransaction.pid "
                    + "WHERE accountname = " + code + " AND subentryaccountitems.date BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + " UNION "
                    + "SELECT subentryaccountitems.id, subentryaccountitems.date, subentryaccountitems.remarks, customersalestransaction.description, subentryaccountitems.action, subentryaccountitems.amount "
                    + "FROM subentryaccountitems "
                    + "INNER JOIN customersalestransaction ON subentryaccountitems.id = customersalestransaction.sid "
                    + "WHERE accountname = " + code + " AND subentryaccountitems.date BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + " UNION "
                    + "SELECT subentryaccountitems.id, subentryaccountitems.date, subentryaccountitems.remarks, guestsalestransaction.description, subentryaccountitems.action, subentryaccountitems.amount "
                    + "FROM subentryaccountitems "
                    + "INNER JOIN guestsalestransaction ON subentryaccountitems.id = guestsalestransaction.sid "
                    + "WHERE accountname = " + code + " AND subentryaccountitems.date BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + " ORDER BY date";

            result = statement.executeQuery(query);
            
            while(result.next()){
                LedgerAccount ledgerAccount = new LedgerAccount();
                
                ledgerAccount.setId(result.getString("id"));
                ledgerAccount.setDate(result.getString("date"));
                
                if(result.getString("remarks") == null || result.getString("remarks").isBlank()){
                    ledgerAccount.setDescription(result.getString("description"));
                } else { ledgerAccount.setDescription(result.getString("description"), result.getString("remarks"));}
                
                ledgerAccount.setAction(result.getString("action"));
                ledgerAccount.setAmount(result.getDouble("amount"));

                postedEntries.add(ledgerAccount);
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
        
        return postedEntries;
    }
    
    public ArrayList<LedgerAccount> getTruePostedEntries(int code, String openingBalance, String toDate){
        ArrayList<LedgerAccount> postedEntries = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT accountitems.id, journalentry.date, '' AS remarks, journalentry.description, accountitems.action, accountitems.amount "
                    + "FROM accountitems "
                    + "INNER JOIN journalentry ON accountitems.id = journalentry.jid "
                    + "WHERE accountname = " + code + " AND journalentry.date BETWEEN '" + openingBalance + "' AND '" + toDate + "' "
                    + " UNION "
                    + "SELECT accountitems.id, expensestransaction.date, '' AS remarks, expensestransaction.description, accountitems.action, accountitems.amount "
                    + "FROM accountitems "
                    + "INNER JOIN expensestransaction ON accountitems.id = expensestransaction.eid "
                    + "WHERE accountname = " + code + " AND expensestransaction.date BETWEEN '" + openingBalance + "' AND '" + toDate + "' "
                    + " UNION "
                    + "SELECT subentryaccountitems.id, subentryaccountitems.date, subentryaccountitems.remarks, purchasestransaction.description , subentryaccountitems.action, subentryaccountitems.amount "
                    + "FROM subentryaccountitems "
                    + "INNER JOIN purchasestransaction ON subentryaccountitems.id = purchasestransaction.pid "
                    + "WHERE accountname = " + code + " AND subentryaccountitems.date BETWEEN '" + openingBalance + "' AND '" + toDate + "' "
                    + " UNION "
                    + "SELECT subentryaccountitems.id, subentryaccountitems.date, subentryaccountitems.remarks, customersalestransaction.description, subentryaccountitems.action, subentryaccountitems.amount "
                    + "FROM subentryaccountitems "
                    + "INNER JOIN customersalestransaction ON subentryaccountitems.id = customersalestransaction.sid "
                    + "WHERE accountname = " + code + " AND subentryaccountitems.date BETWEEN '" + openingBalance + "' AND '" + toDate + "' "
                    + " UNION "
                    + "SELECT subentryaccountitems.id, subentryaccountitems.date, subentryaccountitems.remarks, guestsalestransaction.description, subentryaccountitems.action, subentryaccountitems.amount "
                    + "FROM subentryaccountitems "
                    + "INNER JOIN guestsalestransaction ON subentryaccountitems.id = guestsalestransaction.sid "
                    + "WHERE accountname = " + code + " AND subentryaccountitems.date BETWEEN '" + openingBalance + "' AND '" + toDate + "' "
                    + " ORDER BY date";

            result = statement.executeQuery(query);
            
            while(result.next()){
                LedgerAccount ledgerAccount = new LedgerAccount();
                
                ledgerAccount.setId(result.getString("id"));
                ledgerAccount.setDate(result.getString("date"));
                
                if(result.getString("remarks") == null || result.getString("remarks").isBlank()){
                    ledgerAccount.setDescription(result.getString("description"));
                } else { ledgerAccount.setDescription(result.getString("description"), result.getString("remarks"));}
                
                ledgerAccount.setAction(result.getString("action"));
                ledgerAccount.setAmount(result.getDouble("amount"));

                postedEntries.add(ledgerAccount);
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
        
        return postedEntries;
    }
    
    //Summary
    public Double getTotalRevenue(String fromDate, String toDate){
        Double revenue = 0.00;
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT SUM(amount) AS totalrevenue "
                    + "FROM subentryaccountitems "
                    + "INNER JOIN account ON subentryaccountitems.accountname = account.code "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                    + "WHERE accounttype.type = 'Revenue' AND subentryaccountitems.date BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + "UNION "
                    + "SELECT SUM(amount) AS totalrevenue "
                    + "FROM accountitems "
                    + "INNER JOIN account ON accountitems.accountname = account.code "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                    + "INNER JOIN journalentry ON accountitems.id = journalentry.jid "
                    + "WHERE accounttype.type = 'Revenue' AND journalentry.date BETWEEN '" + fromDate + "' AND '" + toDate + "' ";
            result = statement.executeQuery(query);
            
            while(result.next()){
                revenue += result.getDouble("totalrevenue");
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
        
        return revenue;
    }
    
    public Double getTotalExpenses(String fromDate, String toDate){
        Double expenses = 0.00;
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
         try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT SUM(amount) AS totalexpenses "
                    + "FROM accountitems "
                    + "INNER JOIN account ON accountitems.accountname = account.code "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                    + "INNER JOIN expensestransaction ON accountitems.id = expensestransaction.eid "
                    + "WHERE accounttype.type = 'Expenses' AND expensestransaction.date BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + "UNION "
                    + "SELECT SUM(amount) AS totalexpenses "
                    + "FROM accountitems "
                    + "INNER JOIN account ON accountitems.accountname = account.code "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                    + "INNER JOIN journalentry ON accountitems.id = journalentry.jid "
                    + "WHERE accounttype.type = 'Expenses' AND journalentry.date BETWEEN '" + fromDate + "' AND '" + toDate + "' ";
            result = statement.executeQuery(query);
            
            while(result.next()){
                expenses += result.getDouble("totalexpenses");
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
        
        return expenses;
    }
    
    public ArrayList<LedgerAccount> getRecentTransactions(String fromDate, String toDate){
        ArrayList<LedgerAccount> recentTransactions = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT DISTINCT accountitems.id, journalentry.date, '' AS remarks, journalentry.description, accountitems.amount "
                    + "FROM accountitems "
                    + "INNER JOIN journalentry ON accountitems.id = journalentry.jid "
                    + "WHERE journalentry.date BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + " UNION "
                    + "SELECT DISTINCT accountitems.id, expensestransaction.date, '' AS remarks, expensestransaction.description, accountitems.amount "
                    + "FROM accountitems "
                    + "INNER JOIN expensestransaction ON accountitems.id = expensestransaction.eid "
                    + "WHERE expensestransaction.date BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + " UNION "
                    + "SELECT subentryaccountitems.id, subentryaccountitems.date, subentryaccountitems.remarks, purchasestransaction.description , subentryaccountitems.amount "
                    + "FROM subentryaccountitems "
                    + "INNER JOIN purchasestransaction ON subentryaccountitems.id = purchasestransaction.pid "
                    + "WHERE subentryaccountitems.accountname != '103' AND subentryaccountitems.accountname != '201' AND subentryaccountitems.date BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + " UNION "
                    + "SELECT subentryaccountitems.id, subentryaccountitems.date, subentryaccountitems.remarks, customersalestransaction.description, subentryaccountitems.amount "
                    + "FROM subentryaccountitems "
                    + "INNER JOIN customersalestransaction ON subentryaccountitems.id = customersalestransaction.sid "
                    + "WHERE subentryaccountitems.accountname != '103' AND subentryaccountitems.accountname != '201' AND subentryaccountitems.date BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + " UNION "
                    + "SELECT subentryaccountitems.id, subentryaccountitems.date, subentryaccountitems.remarks, guestsalestransaction.description, subentryaccountitems.amount "
                    + "FROM subentryaccountitems "
                    + "INNER JOIN guestsalestransaction ON subentryaccountitems.id = guestsalestransaction.sid "
                    + "WHERE subentryaccountitems.accountname != '103' AND subentryaccountitems.accountname != '201' AND subentryaccountitems.date BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + " ORDER BY date DESC";
            
            result = statement.executeQuery(query);
            
            while(result.next()){
                LedgerAccount ledgerAccount = new LedgerAccount();
                
                ledgerAccount.setId(result.getString("id"));
                ledgerAccount.setDate(result.getString("date"));
                
                if(result.getString("remarks") == null || result.getString("remarks").isBlank()){
                    ledgerAccount.setDescription(result.getString("description"));
                } else { ledgerAccount.setDescription(result.getString("description"), result.getString("remarks"));}
                
                ledgerAccount.setAmount(result.getDouble("amount"));

                recentTransactions.add(ledgerAccount);
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
        
        
        return recentTransactions;
    }
    
    //Other functions
    public ArrayList<Account> getAccountItems(String id){
        ArrayList<Account> accountItems = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
            try{
                connection = Driver.getConnection();
                statement = connection.createStatement();
                query = "SELECT account.code, account.accountname, accounttype.type, accounttype.normally, accountitems.amount, accountitems.action "
                        + "FROM accountitems "
                        + "INNER JOIN account ON accountitems.accountname = account.code "
                        + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                        + "WHERE accountitems.id = '" + id + "'";
                result = statement.executeQuery(query);
                
                while(result.next()){
                    Account account = new Account();
                    
                    account.setCode(result.getInt("account.code"));
                    account.setAccountName(result.getString("account.accountname"));
                    account.setAccountType(result.getString("accounttype.type"));
                    account.setNormally(result.getString("accounttype.normally"));
                    account.setAmount(result.getDouble("accountitems.amount"));
                    account.setAction(result.getString("action"));
                    accountItems.add(account);
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
        return accountItems;
    }
    
    public Double getTotalGrossIncome(String fromDate, String toDate){
        Double grossIncome = 0.00;
        Double sales = 0.00;
        Double expenses = 0.00;
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT SUM(amount) AS totalrevenue "
                    + "FROM subentryaccountitems "
                    + "INNER JOIN account ON subentryaccountitems.accountname = account.code "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                    + "WHERE accounttype.type = 'Revenue' AND subentryaccountitems.date BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + "UNION "
                    + "SELECT SUM(amount) AS totalrevenue "
                    + "FROM accountitems "
                    + "INNER JOIN account ON accountitems.accountname = account.code "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                    + "INNER JOIN journalentry ON accountitems.id = journalentry.jid "
                    + "WHERE accounttype.type = 'Revenue' AND journalentry.date BETWEEN '" + fromDate + "' AND '" + toDate + "' ";
            result = statement.executeQuery(query);
            
            while(result.next()){
                sales += result.getDouble("totalrevenue");
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
        
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT SUM(amount) AS totalexpenses "
                    + "FROM accountitems "
                    + "INNER JOIN account ON accountitems.accountname = account.code "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                    + "INNER JOIN expensestransaction ON accountitems.id = expensestransaction.eid "
                    + "WHERE accounttype.type = 'Expenses' AND expensestransaction.date BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + "UNION "
                    + "SELECT SUM(amount) AS totalexpenses "
                    + "FROM accountitems "
                    + "INNER JOIN account ON accountitems.accountname = account.code "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                    + "INNER JOIN journalentry ON accountitems.id = journalentry.jid "
                    + "WHERE accounttype.type = 'Expenses' AND journalentry.date BETWEEN '" + fromDate + "' AND '" + toDate + "' ";
            result = statement.executeQuery(query);
            
            while(result.next()){
                expenses += result.getDouble("totalexpenses");
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
        
        grossIncome = sales - expenses;
        
        return grossIncome;
    }
    
    public String filenameSplitter(String path){
        String filename = "";
        String temp = "";
        int size = 0;
        
        if(path != null){
            size = path.length();
            out:
            for(int i = size - 1; i >= 0; i--){
                if(path.charAt(i) != '\\'){
                    temp += path.charAt(i);
                } else {
                    break out;
                }
            }

            for(int i = temp.length() - 1; i >= 0; i--){
                filename += temp.charAt(i);
            }  
        }

        return filename;            
    }  
    
    public Image getPhotoDocument(String id){
        Image image = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            
            query = "SELECT document FROM document WHERE transactionid = '" + id + "'";
            result = statement.executeQuery(query);
                
            if(result.next()){
                InputStream inputStream = result.getBinaryStream("document");
                
                BufferedImage bufferedImage = convertBlobToImage(inputStream);
                
                image = bufferedImage;
            }
        } catch(Exception e){
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
        return image;
    }
}
