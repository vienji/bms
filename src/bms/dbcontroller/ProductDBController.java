/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bms.dbcontroller;

import bms.classes.Item;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Vienji
 */
public class ProductDBController {
    private String query = "";
    
    public void add(String name, String description, String metricUnit, Double price){
        Connection connection = null;
        Statement statement = null;
        try {
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "INSERT INTO product (name, description, metricunit, price) "
                    + "VALUES ('" + name + "', '" + description + "', '" + metricUnit + "', " + price + " )";
            
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
    
    public ArrayList<Item> getAllProducts(){
        ArrayList<Item> productList = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try {
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "SELECT * FROM product";
            result = statement.executeQuery(query);
            
            while(result.next()){
                Item item = new Item();
                
                item.setId(result.getInt("id"));
                item.setName(result.getString("name"));
                item.setDescription(result.getString("description"));
                item.setQuantity(result.getString("metricunit"));
                item.setPrice(result.getDouble("price"));
                
                productList.add(item);
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
        
        return productList;
    }
    
    public void update(int id, String name, String quantity, String description, Double price){
        Connection connection = null;
        Statement statement = null;
        try {
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "UPDATE product SET name = '" + name + "', description = '" + description + "', metricunit = '" + quantity + "', price = " + price 
                    + " WHERE id = " + id;
            
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
    
    public void delete(int id){
        Connection connection = null;
        Statement statement = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "DELETE FROM product WHERE id = " + id ;
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
    
    public void deleteAll(){
        Connection connection = null;
        Statement statement = null;
        try{
            connection = Driver.getConnection();
            statement = connection.createStatement();
            query = "DELETE FROM product";
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
}
