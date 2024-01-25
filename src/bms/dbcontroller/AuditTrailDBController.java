/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bms.dbcontroller;

import bms.classes.AuditTrail;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * @author Vienji
 */
public class AuditTrailDBController {
    private String query = "";
    
    public void logTrail(String module, String event, String user, String transactionId, String affectedAccounts, Double beforeValue, Double afterValue, Double totalAmount){
        Connection connection = null;
        Statement statement = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            String atid = "";
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
            LocalDate dateToday = LocalDate.now();
            LocalTime timeNow = LocalTime.now();
            String timeStamp = dateToday.format(dateFormat) + " " + timeNow.format(timeFormat);
            
            query = "INSERT INTO audittrail (timestamp, module, event, user, transactionid, affectedaccounts, beforevalue, aftervalue, transactionamount) "
                    + "VALUES ('" + timeStamp + "', '" + module + "', '" + event + "', '" + user + "', '" + transactionId + "', '"+ affectedAccounts + "', " + beforeValue + ", "
                    + afterValue + ", " + totalAmount + ")";
            
            statement.executeUpdate(query);
            
            query = "UPDATE audittrail SET atid = CONCAT('ATID', YEAR(CURDATE()), MONTH(CURDATE()), LPAD(LAST_INSERT_ID(), 3, '0')) WHERE id = LAST_INSERT_ID()";
            
            statement.executeUpdate(query);
            
        } catch (SQLException e){
            System.out.println("Error logging event trail!");
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
    
    public ArrayList<AuditTrail> getAuditTrails(String fromDate, String toDate){
        ArrayList<AuditTrail> trails = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;    
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "Select * FROM audittrail "
                    + "WHERE timestamp BETWEEN '" + fromDate + " 00:00' AND '" + toDate + " 24:00' "
                    + "ORDER BY timestamp DESC";
            result = statement.executeQuery(query);
            
            while(result.next()){
                  AuditTrail trail = new AuditTrail();
                  
                  trail.setId(result.getString("atid"));
                  trail.setTimestamp(result.getString("timestamp"));
                  trail.setModule(result.getString("module"));
                  trail.setEvent(result.getString("event"));
                  trail.setUser(new UserDBController().getCredential(result.getString("user")).getName());
                  trail.setTransactionId(result.getString("transactionid"));
                  trail.setAffectedAccounts(result.getString("affectedaccounts"));
                  trail.setBeforeValue(result.getDouble("beforevalue"));
                  trail.setAfterValue(result.getDouble("aftervalue"));
                  trail.setTotalAmount(result.getDouble("transactionamount"));
                  
                  trails.add(trail);
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
        
        return trails;
    }
    
    public ArrayList<AuditTrail> getAuditTrails(){
        ArrayList<AuditTrail> trails = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;    
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "Select * FROM audittrail "
                    + "ORDER BY timestamp DESC";
            result = statement.executeQuery(query);
            
            while(result.next()){
                  AuditTrail trail = new AuditTrail();
                  
                  trail.setId(result.getString("atid"));
                  trail.setTimestamp(result.getString("timestamp"));
                  trail.setModule(result.getString("module"));
                  trail.setEvent(result.getString("event"));
                  trail.setUser(new UserDBController().getCredential(result.getString("user")).getName());
                  trail.setTransactionId(result.getString("transactionid"));
                  trail.setAffectedAccounts(result.getString("affectedaccounts"));
                  trail.setBeforeValue(result.getDouble("beforevalue"));
                  trail.setAfterValue(result.getDouble("aftervalue"));
                  trail.setTotalAmount(result.getDouble("transactionamount"));
                  
                  trails.add(trail);
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
        
        return trails;
    }
    
    public ArrayList<AuditTrail> searchAuditTrail(String search){
        ArrayList<AuditTrail> trails = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;  
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "Select * FROM audittrail "
                    + "INNER JOIN user ON audittrail.user = user.uid "
                    + "WHERE audittrail.atid LIKE '%" + search + "%' OR audittrail.timestamp LIKE '%" + search + "%' OR audittrail.module LIKE '%" + search + "%' OR "
                    + "audittrail.event LIKE '%" + search + "%' OR user.firstname LIKE '%" + search + "%' OR audittrail.transactionid LIKE '%" + search + "%' OR audittrail.affectedaccounts LIKE '%" + search + "%'";
            result = statement.executeQuery(query);
            
            while(result.next()){
                  AuditTrail trail = new AuditTrail();
                  
                  trail.setId(result.getString("atid"));
                  trail.setTimestamp(result.getString("timestamp"));
                  trail.setModule(result.getString("module"));
                  trail.setEvent(result.getString("event"));
                  trail.setUser(new UserDBController().getCredential(result.getString("user")).getName());
                  trail.setTransactionId(result.getString("transactionid"));
                  trail.setAffectedAccounts(result.getString("affectedaccounts"));
                  trail.setBeforeValue(result.getDouble("beforevalue"));
                  trail.setAfterValue(result.getDouble("aftervalue"));
                  trail.setTotalAmount(result.getDouble("transactionamount"));
                  
                  trails.add(trail);
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
        
        return trails;
    }
}
