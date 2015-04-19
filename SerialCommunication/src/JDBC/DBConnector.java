/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Felipe
 */
public class DBConnector {
    private static String driver = "com.mysql.jdbc.Driver";
    private static String serverName = "p3plcpnl0206.prod.phx3.secureserver.net"; 
    private static String user = "apoo_endo";
    private static String password = "Aquaticchinchila1";
    private static Boolean status = false;

    
    public DBConnector() {
        
    }
    
    public static Connection getDataBaseConnection() throws ClassNotFoundException, SQLException{
        Connection connector = null;
        try{
            Class.forName(driver);
            String url = "jdbc:mysql://"+serverName+"/apoo";
            connector = DriverManager.getConnection(url, user, password);
            status = true;
            System.out.println("Conectou-se à base de dados com sucesso.");
            return connector;
        }catch(ClassNotFoundException e){
            System.out.println("Não foi possível localizar o driver: "+e.getMessage());
            status = false;
            return null;
        }catch(SQLException e){
            status = false;
            System.out.println("Não foi possível conectar-se ao servidor: "+e.getMessage());
            return null;
        }
        
        
    }
    
    public boolean isConnected(){
        return status;
    }
    
    public static boolean closeConnection() throws ClassNotFoundException{
        try{
            DBConnector.getDataBaseConnection().close();
            return true;
        }catch(SQLException e){
            System.out.println("Falha ao fechar conexão");
            return false;
        }
    }
    
    public static Connection refreshConnection() throws ClassNotFoundException, SQLException{
        DBConnector.closeConnection();
        return DBConnector.getDataBaseConnection();
    }
    
    
    
}
