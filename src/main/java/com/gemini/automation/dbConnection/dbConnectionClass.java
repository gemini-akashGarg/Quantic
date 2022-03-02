package com.gemini.automation.dbConnection;

import com.gemini.automation.prism.APIRequest;
import com.gemini.automation.prism.DataInserted;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import groovyjarjarantlr4.v4.runtime.atn.OrderedATNConfigSet;
import oracle.jdbc.OracleConnection;
import oracle.net.ano.AnoComm;
import oracle.net.ano.AnoServices;
import org.bouncycastle.jcajce.provider.asymmetric.ec.KeyFactorySpi;

import java.sql.*;
import java.util.Properties;


public class dbConnectionClass {
    private static final String username = "";
    private static final String user_password = "";
    private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<Connection>();
    private static final String oracleProtocol = "jdbc:oracle:thin:@";
    public static final String DB_url = oracleProtocol + "";

    private String dbFullUrl;
    private String userName;
    private String password;
    private boolean isKerberosConnection;

    public void setKerberosConnection(boolean isKerberosConnection) {
        this.isKerberosConnection = isKerberosConnection;
    }

    public void OracleDBConnection(final String dbFullUrl, final String userName, final String password) {
        this.setKerberosConnection(false);
        this.userName = userName;
        this.password = password;
        this.openConnection(this.dbFullUrl, this.userName, this.password);

    }

    public void OraccleDBConnection(final boolean isKerberosConnection, final String dbFullUrl) {
        this.setKerberosConnection(isKerberosConnection);
        this.dbFullUrl = dbFullUrl;
        openConnection(this.dbFullUrl);
    }

    public void openConnection(final String dbFullUrl, final String userName, final String password) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            if (connectionThreadLocal.get() == null) {
                connectionThreadLocal.set(DriverManager.getConnection(dbFullUrl, userName, password));
            } else {
                closeConnection();
                connectionThreadLocal.set(DriverManager.getConnection(dbFullUrl, userName, password));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void openConnection(final String dbFullUrl) {
        this.setKerberosConnection(true);
        Properties properties=new Properties();
        properties.setProperty(OracleConnection.CONNECTION_PROPERTY_THIN_NET_AUTHENTICATION_SERVICES,"("+ AnoServices.AUTHENTICATION_KERBEROS5+")");
        properties.setProperty(OracleConnection.CONNECTION_PROPERTY_THIN_NET_AUTHENTICATION_KRB5_MUTUAL, "true");
        try{
            connectionThreadLocal.set(DriverManager.getConnection(dbFullUrl,properties));

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public ResultSet getResultSet(final String query){
        try {
            PreparedStatement statement;
            statement = connectionThreadLocal.get().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            return resultSet;
        }
        catch (Exception e){
            System.out.println("Error occur during DataBase Connection ");
            throw new RuntimeException("Database access error:"+e);
        }
    }

    public void closeConnection(){
        try{
            connectionThreadLocal.get().close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static JsonArray convertResultSetInJsonFormat(ResultSet resultSet){
        try{
            JsonArray array= new JsonArray();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int numOfColumn = metaData.getColumnCount();
            while(resultSet.next()){
                JsonObject jsonObject = new JsonObject();
                for (int i = 0;i<=numOfColumn;i++){
                    jsonObject.addProperty(metaData.getColumnName(i),resultSet.getString(i));
                }
                array.add(jsonObject);
            }
            return array;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean isKerberosConnection(){
        return this.isKerberosConnection;
    }

    //Class dbConnection return ResultSet
    //ResultSet to Json
}
