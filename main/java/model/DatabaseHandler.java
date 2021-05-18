/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {
    
    private static DatabaseHandler instance;

    public static void update() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private Connection connection;
    private Statement statement;
    
    private void init() throws SQLException{
        connection = DriverManager.getConnection("jdbc:derby://localhost:1527/SurveysDB", "app" , "app"); 
        connection.setAutoCommit(false);
        statement = connection.createStatement();
    }
 
    private DatabaseHandler() throws ClassNotFoundException, SQLException{
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        init();
    }
    
    public static ResultSet query(String sqlQuery) throws ClassNotFoundException, SQLException{
        if(instance == null){
            instance = new DatabaseHandler();
        }
        if(instance.connection.isClosed()){
            instance.init();
        }
        return instance.statement.executeQuery(sqlQuery);
    }
    
    public static int update(String sqlQuery) throws SQLException, ClassNotFoundException{
        if(instance == null){
            instance = new DatabaseHandler();
        }
        if(instance.connection.isClosed()){
            instance.init();
        }
        try{
            int ret = instance.statement.executeUpdate(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            instance.connection.commit();
            return ret;
        }catch(SQLException ex){
            instance.connection.rollback();
            throw ex;
        }
    }
}