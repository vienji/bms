/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bms.dbcontroller;

import bms.classes.Cryptographer;
import bms.classes.User;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
/**
 *
 * @author Vienji
 */
public class UserDBController extends javax.swing.JFrame {
    private String query = "";
    
    public void add(String firstName, String lastName, String password, String username, String accessLevel){
        Connection connection = null;
        Statement statement = null;
        try {
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "INSERT INTO user (firstname, lastname, password, username, accesslevel, status) "
                    + "VALUES ('" + firstName + "', '" + lastName + "', '" + password + "', '" + username + "', '" + accessLevel +"', 'active')";
            
            statement.executeUpdate(query);
            
            query = "UPDATE user SET uid = CONCAT('UID', YEAR(CURDATE()), MONTH(CURDATE()), LPAD(LAST_INSERT_ID(), 3, '0')) WHERE id = LAST_INSERT_ID()";
               
            statement.executeUpdate(query);
            
        } catch (SQLException e){
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
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
    
    /*  Get credentials using username and password as a parameter to retrieve the user details including the password.
    *   Password is still encrypted after being retrieved.
    */
    public User getCredential(String username, String password){
        Cryptographer cryptographer = new Cryptographer();
        User user = new User();
        String encryptedPassword = "";
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        
        try {
            encryptedPassword = cryptographer.encrypt(password);
        } catch (Exception e){}
        
        try {
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT uid, firstname, lastname, password, username, accesslevel, status FROM user WHERE username = '" 
                    + username + "' AND password = '" + encryptedPassword + "'";
            result = statement.executeQuery(query);
            
            while(result.next()){
                user.setId(result.getString("uid"));
                user.setFirstName(result.getString("firstname"));
                user.setLastName(result.getString("lastname"));
                user.setPassword(result.getString("password"));
                user.setUsername(result.getString("username"));
                user.setAccessLevel(result.getString("accesslevel"));
                user.setStatus(result.getString("status"));
                user.setName(user.getFirstName(), user.getLastName());
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
        
        return user;
    }
    
    //Get credentials using uid as a parameter.
    public User getCredential(String uid){
        User user = new User();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        
        try {
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT firstname, lastname, password, username, accesslevel, status FROM user WHERE uid = '" 
                    + uid + "'";
            result = statement.executeQuery(query);
            
            while(result.next()){
                user.setFirstName(result.getString("firstname"));
                user.setLastName(result.getString("lastname"));
                user.setPassword(result.getString("password"));
                user.setUsername(result.getString("username"));
                user.setAccessLevel(result.getString("accesslevel"));
                user.setStatus(result.getString("status"));
                user.setName(user.getFirstName(), user.getLastName());
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
        
        return user;
    }
    
    public ArrayList<User> getAllUsers(){
        ArrayList<User> userList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try {
             connection = Driver.getConnection();
             statement = connection.createStatement();
             query = "SELECT * FROM user";
             result = statement.executeQuery(query);
         
             while (result.next()){
                User user = new User();
                
                user.setId(result.getString("uid"));
                user.setFirstName(result.getString("firstname"));
                user.setLastName(result.getString("lastname"));
                user.setPassword(result.getString("password"));
                user.setUsername(result.getString("username"));
                user.setAccessLevel(result.getString("accesslevel"));
                user.setStatus(result.getString("status"));
                user.setName(user.getFirstName(), user.getLastName());
                
                userList.add(user);
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
        
        return userList;
    }
    
    public ArrayList<User> sortUsers(String sortBy){
        ArrayList<User> userList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try {
             connection = Driver.getConnection();
             statement = connection.createStatement();
             query = "SELECT * FROM user ORDER BY " + sortBy;
             result = statement.executeQuery(query);
         
             while (result.next()){
                User user = new User();
                
                user.setId(result.getString("uid"));
                user.setFirstName(result.getString("firstname"));
                user.setLastName(result.getString("lastname"));
                user.setPassword(result.getString("password"));
                user.setUsername(result.getString("username"));
                user.setAccessLevel(result.getString("accesslevel"));
                user.setStatus(result.getString("status"));
                user.setName(user.getFirstName(), user.getLastName());
                
                userList.add(user);
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
        
        return userList;
    }
    
    public ArrayList<User> searchUser(String search){
        ArrayList<User> userList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        
        try {
            connection = Driver.getConnection();
             statement = connection.createStatement();
             query = "SELECT * FROM user "
                     + "WHERE uid LIKE '%" + search + "%' OR firstname LIKE '%" + search + "%' OR lastname LIKE '%" + search + "%' OR username LIKE '%" + search + "%'";
             result = statement.executeQuery(query);
         
             while (result.next()){
                User user = new User();
                
                user.setId(result.getString("uid"));
                user.setFirstName(result.getString("firstname"));
                user.setLastName(result.getString("lastname"));
                user.setPassword(result.getString("password"));
                user.setUsername(result.getString("username"));
                user.setAccessLevel(result.getString("accesslevel"));
                user.setStatus(result.getString("status"));
                user.setName(user.getFirstName(), user.getLastName());
                
                userList.add(user);
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
        
        return userList;
    }
    
    public void deactivate(String uid){
        Connection connection = null;
        Statement statement = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "UPDATE user SET status = 'deactivated' WHERE uid = '" + uid + "'";
            
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
    
    public void update(String uid, String firstName, String lastName, String password, String username, String accessLevel){
        Connection connection = null;
        Statement statement = null;
        try {
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "UPDATE user SET firstname = '" + firstName + "', lastname = '" + 
                    lastName + "', password = '" + password + "', username = '" + username + "', accesslevel = '" + accessLevel + "' WHERE uid = '" + uid + "'";
            
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
}
