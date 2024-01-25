/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bms.dbcontroller;

import bms.classes.Account;
import bms.classes.AccountType;
import bms.model.Dashboard;
import java.util.*;
import java.sql.*;

/**
 *
 * @author Vienji
 */
public class AccountDBController {
    private String query = "";
    
    //Account Controller
    
    public void add(int code, String accountName, int accountTypeID){
        Connection connection = null;
        Statement statement = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "INSERT INTO account (code, accountname, accounttype) "
                    + "VALUES (" + code + ", '" + accountName + "', " + accountTypeID + ")";
            
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
    
    public ArrayList<Account> getAllAccounts(){
        ArrayList<Account> accountList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT account.code, account.accountname, accounttype.type, accounttype.normally "  
                    + "FROM account "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id ";
            
            result = statement.executeQuery(query);
            
            while(result.next()){
                Account account = new Account();
                
                account.setCode(result.getInt("code"));
                account.setAccountName(result.getString("accountname"));
                account.setAccountType(result.getString("accounttype.type"));
                account.setNormally(result.getString("accounttype.normally"));
                account.setBalance(getAccountBalance(account.getCode()));
                
                accountList.add(account);
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
        
        return accountList;
    }  
    
    public ArrayList<Account> getAllAccounts(String fromDate, String toDate){
        ArrayList<Account> accountList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT account.code, account.accountname, accounttype.type, accounttype.normally "  
                    + "FROM account "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id";
            
            result = statement.executeQuery(query);
            
            while(result.next()){
                Account account = new Account();
                
                account.setCode(result.getInt("code"));
                account.setAccountName(result.getString("accountname"));
                account.setAccountType(result.getString("accounttype.type"));
                account.setNormally(result.getString("accounttype.normally"));
                account.setBalance(getAccountBalance(account.getCode(), fromDate, toDate));
                
                accountList.add(account);
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
        
        return accountList;
    }  
    
    public ArrayList<Account> searchAccount(String search, String fromDate, String toDate){
        ArrayList<Account> accountList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT account.code, account.accountname, accounttype.type, accounttype.normally "  
                    + "FROM account "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                    + "WHERE account.code LIKE '%" + search + "%' OR account.accountname LIKE '%" + search + "%' OR accounttype.type LIKE '%" + search + "%'";
            
            result = statement.executeQuery(query);
            
            while(result.next()){
                Account account = new Account();
                
                account.setCode(result.getInt("code"));
                account.setAccountName(result.getString("accountname"));
                account.setAccountType(result.getString("accounttype.type"));
                account.setNormally(result.getString("accounttype.normally"));
                account.setBalance(getAccountBalance(account.getCode(), fromDate, toDate));
                
                accountList.add(account);
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
        
        return accountList;
    }
    
    public ArrayList<Account> sortAccount(String sortBy, String fromDate, String toDate){
        ArrayList<Account> accountList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT account.code, account.accountname, accounttype.type, accounttype.normally "  
                    + "FROM account "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                    + "ORDER BY " + sortBy;
            
            result = statement.executeQuery(query);
            
            while(result.next()){
                Account account = new Account();
                
                account.setCode(result.getInt("code"));
                account.setAccountName(result.getString("accountname"));
                account.setAccountType(result.getString("accounttype.type"));
                account.setNormally(result.getString("accounttype.normally"));
                account.setBalance(getAccountBalance(account.getCode(), fromDate, toDate));
                
                accountList.add(account);
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
        return accountList;
    }
    
    public Account getAccount(int code){
        Account account = new Account();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT accountname, accounttype FROM account WHERE code = " + code;
            result = statement.executeQuery(query);
            
            while(result.next()){
                account.setAccountName(result.getString("accountname"));
                account.setAccountTypeID(result.getInt("accounttype"));
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
        
        return account;
    }
    
    public ArrayList<Account> getAccountList(ArrayList<String> accountCodes){
        ArrayList<Account> accountList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            
            for(String code : accountCodes){
                query = "SELECT account.code, account.accountname, accounttype.type, accounttype.normally "  
                    + "FROM account "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                    + "WHERE code = " + code;
                result = statement.executeQuery(query);

                Account account = new Account();
                
                while(result.next()){
                    account.setCode(result.getInt("code"));
                    account.setAccountName(result.getString("accountname"));
                    account.setAccountType(result.getString("accounttype.type"));
                    account.setNormally(result.getString("accounttype.normally"));
                }
                
                accountList.add(account);
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
        
        return accountList;
    }
    
    public int getAccountCode(String accountName){
        int code = 0;
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT code FROM account WHERE accountname = '" + accountName + "'";
            result = statement.executeQuery(query);
            
            while(result.next()){
               code = result.getInt("code");
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
        
        return code;
    }
    
    public ArrayList<Account> getAllExpensesAccount(){
        ArrayList<Account> expensesAccountList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
            try{
                connection = Driver.getConnection();
                statement = connection.createStatement();
                query = "SELECT account.code, account.accountname, accounttype.type, accounttype.normally "
                        + "FROM account "
                        + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                        + "WHERE account.accounttype = 2";
                result = statement.executeQuery(query);
                
                while(result.next()){
                    Account account = new Account();
                    
                    account.setCode(result.getInt("code"));
                    account.setAccountName(result.getString("accountname"));
                    account.setAccountType(result.getString("accounttype.type"));
                    account.setNormally(result.getString("accounttype.normally"));
                    account.setBalance(getAccountBalance(account.getCode()));

                    expensesAccountList.add(account);
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
        return expensesAccountList;
    }
    
    public ArrayList<Account> getAllAssetAccount(){
        ArrayList<Account> expensesAccountList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
            try{
                connection = Driver.getConnection();
                statement = connection.createStatement();
                query = "SELECT account.code, account.accountname, accounttype.type, accounttype.normally "
                        + "FROM account "
                        + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                        + "WHERE account.accounttype = 1";
                result = statement.executeQuery(query);
                
                while(result.next()){
                    Account account = new Account();
                    
                    account.setCode(result.getInt("code"));
                    account.setAccountName(result.getString("accountname"));
                    account.setAccountType(result.getString("accounttype.type"));
                    account.setNormally(result.getString("accounttype.normally"));
                    account.setBalance(getAccountBalance(account.getCode()));

                    expensesAccountList.add(account);
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
        return expensesAccountList;
    }
    
    public void update(int code, String accountName, int accountTypeID){
        Connection connection = null;
        Statement statement = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "UPDATE account SET accountname = '" + accountName + "', accounttype = " + accountTypeID + " WHERE code = " + code;
            
            statement.execute(query);
        } catch(SQLException e){
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
    
    //Account Type Controller
    public ArrayList<AccountType> getAllAccountTypes(){
        ArrayList<AccountType> accountTypeList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT * FROM accounttype";
            result = statement.executeQuery(query);
            
            while(result.next()){
                AccountType accountType = new AccountType();
                
                accountType.setId(result.getInt("id"));
                accountType.setAccountTypeName(result.getString("type"));
                accountType.setNormally(result.getString("normally"));
                
                accountTypeList.add(accountType);
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
        
        return accountTypeList;
    }     
    
    public Double getAccountBalance(int code){
        Double balance = 0.00;
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();

            query = "SELECT accountitems.id, journalentry.date, accountitems.action, accountitems.amount, accounttype.normally "
                    + "FROM accountitems "
                    + "INNER JOIN journalentry ON accountitems.id = journalentry.jid "
                    + "INNER JOIN account ON accountitems.accountname = account.code "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                    + "WHERE accountitems.accountname = " + code 
                    + " UNION "
                    + "SELECT accountitems.id, expensestransaction.date, accountitems.action, accountitems.amount, accounttype.normally "
                    + "FROM accountitems "
                    + "INNER JOIN expensestransaction ON accountitems.id = expensestransaction.eid "
                    + "INNER JOIN account ON accountitems.accountname = account.code "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                    + "WHERE accountitems.accountname = " + code 
                    + " UNION "
                    + "SELECT subentryaccountitems.id, subentryaccountitems.date, subentryaccountitems.action, subentryaccountitems.amount, accounttype.normally "
                    + "FROM subentryaccountitems "
                    + "INNER JOIN purchasestransaction ON subentryaccountitems.id = purchasestransaction.pid "
                    + "INNER JOIN account ON subentryaccountitems.accountname = account.code "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                    + "WHERE subentryaccountitems.accountname = " + code 
                    + " UNION "
                    + "SELECT subentryaccountitems.id, subentryaccountitems.date, subentryaccountitems.action, subentryaccountitems.amount, accounttype.normally "
                    + "FROM subentryaccountitems "
                    + "INNER JOIN customersalestransaction ON subentryaccountitems.id = customersalestransaction.sid "
                    + "INNER JOIN account ON subentryaccountitems.accountname = account.code "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                    + "WHERE subentryaccountitems.accountname = " + code 
                    + " UNION "
                    + "SELECT subentryaccountitems.id, subentryaccountitems.date, subentryaccountitems.action, subentryaccountitems.amount, accounttype.normally "
                    + "FROM subentryaccountitems "
                    + "INNER JOIN guestsalestransaction ON subentryaccountitems.id = guestsalestransaction.sid "
                    + "INNER JOIN account ON subentryaccountitems.accountname = account.code "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                    + "WHERE subentryaccountitems.accountname = " + code 
                    + " ORDER BY date";
            
            result = statement.executeQuery(query);
            
            while(result.next()){
                Account account = new Account();
                
                account.setAmount(result.getDouble("amount"));
                account.setAction(result.getString("action"));
                account.setNormally(result.getString("normally"));             
                
                if(account.getNormally().equalsIgnoreCase("Debit")){
                    if(account.getAction().equalsIgnoreCase("Debit")){
                        balance += account.getAmount();
                    } else {
                        balance -= account.getAmount();
                    }
                } else {
                    if(account.getAction().equalsIgnoreCase("Debit")){
                        balance -= account.getAmount();
                    } else {
                        balance += account.getAmount();
                    }
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
        
        return balance;
    }
    
    public Double getAccountBalance(int code, String fromDate, String toDate){
        Double balance = 0.00;
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();

            query = "SELECT accountitems.id, journalentry.date, accountitems.action, accountitems.amount, accounttype.normally "
                    + "FROM accountitems "
                    + "INNER JOIN journalentry ON accountitems.id = journalentry.jid "
                    + "INNER JOIN account ON accountitems.accountname = account.code "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                    + "WHERE accountitems.accountname = " + code + " AND journalentry.date BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + " UNION "
                    + "SELECT accountitems.id, expensestransaction.date, accountitems.action, accountitems.amount, accounttype.normally "
                    + "FROM accountitems "
                    + "INNER JOIN expensestransaction ON accountitems.id = expensestransaction.eid "
                    + "INNER JOIN account ON accountitems.accountname = account.code "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                    + "WHERE accountitems.accountname = " + code + " AND expensestransaction.date BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + " UNION "
                    + "SELECT subentryaccountitems.id, subentryaccountitems.date, subentryaccountitems.action, subentryaccountitems.amount, accounttype.normally "
                    + "FROM subentryaccountitems "
                    + "INNER JOIN purchasestransaction ON subentryaccountitems.id = purchasestransaction.pid "
                    + "INNER JOIN account ON subentryaccountitems.accountname = account.code "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                    + "WHERE subentryaccountitems.accountname = " + code + " AND subentryaccountitems.date BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + " UNION "
                    + "SELECT subentryaccountitems.id, subentryaccountitems.date, subentryaccountitems.action, subentryaccountitems.amount, accounttype.normally "
                    + "FROM subentryaccountitems "
                    + "INNER JOIN customersalestransaction ON subentryaccountitems.id = customersalestransaction.sid "
                    + "INNER JOIN account ON subentryaccountitems.accountname = account.code "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                    + "WHERE subentryaccountitems.accountname = " + code + " AND subentryaccountitems.date BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + " UNION "
                    + "SELECT subentryaccountitems.id, subentryaccountitems.date, subentryaccountitems.action, subentryaccountitems.amount, accounttype.normally "
                    + "FROM subentryaccountitems "
                    + "INNER JOIN guestsalestransaction ON subentryaccountitems.id = guestsalestransaction.sid "
                    + "INNER JOIN account ON subentryaccountitems.accountname = account.code "
                    + "INNER JOIN accounttype ON account.accounttype = accounttype.id "
                    + "WHERE subentryaccountitems.accountname = " + code + " AND subentryaccountitems.date BETWEEN '" + fromDate + "' AND '" + toDate + "' "
                    + " ORDER BY date";
            
            result = statement.executeQuery(query);
            
            while(result.next()){
                Account account = new Account();
                
                account.setAmount(result.getDouble("amount"));
                account.setAction(result.getString("action"));
                account.setNormally(result.getString("normally"));             
                
                if(account.getNormally().equalsIgnoreCase("Debit")){
                    if(account.getAction().equalsIgnoreCase("Debit")){
                        balance += account.getAmount();
                    } else {
                        balance -= account.getAmount();
                    }
                } else {
                    if(account.getAction().equalsIgnoreCase("Debit")){
                        balance -= account.getAmount();
                    } else {
                        balance += account.getAmount();
                    }
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
        return balance;
    }
    
    public void delete(int code, String accountName, String user, Double totalAmount){
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        EntryDBController entryDB = new EntryDBController();
        
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
        
            query = "SELECT id FROM accountitems "
                    + "WHERE accountname = " + code
                    + " UNION "
                    + "SELECT id FROM subentryaccountitems "
                    + "WHERE accountname = " + code;
            result = statement.executeQuery(query);
            
            while(result.next()){
                String id = result.getString("id");
                
                if(id.contains("CSID")){
                    entryDB.deleteSales(id, user); 
                } else if (id.contains("GSID")){
                    entryDB.deleteSales(id, user); 
                } else if (id.contains("EID")){
                    entryDB.deleteExpensesEntry(id, user);
                } else if (id.contains("PID")){
                    entryDB.deletePurchases(id, user);
                } else if (id.contains("JID")){
                    entryDB.deleteEntry(id, user);
                } 
            }
            
            query = "DELETE FROM account WHERE code = " + code;
            statement.executeUpdate(query);
            
            new AuditTrailDBController().logTrail("Chart Of Accounts", "Deleted",user, " ", new Dashboard().stringEscape(accountName), 0.00, 0.00, totalAmount);
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
    
    public int countRelatedTransactions(String code){
        int count = 0;
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try {
            connection = Driver.getConnection();
            statement = connection.createStatement();
            
            query = "SELECT COUNT(DISTINCT id) as total FROM accountitems WHERE accountname = " + code
                    + " UNION "
                    + "SELECT COUNT(DISTINCT id) as total FROM subentryaccountitems WHERE accountname = " + code;
            result = statement.executeQuery(query);
                
            while(result.next()){
                count += result.getInt("total");
            }
            
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
        return count;
    }
    
    public boolean isDuplicate(String code){
        int count = 0;
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try {
            connection = Driver.getConnection();
            statement = connection.createStatement();
            
            query = "SELECT COUNT(code) as total FROM account WHERE code = " + code;
            result = statement.executeQuery(query);
                
            while(result.next()){
                count += result.getInt("total");
            }
            
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
        
        if(count == 0){
            return false;
        } else {
            return true;
        }
    }
}
