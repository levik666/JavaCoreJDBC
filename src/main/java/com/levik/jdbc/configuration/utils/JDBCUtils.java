package com.levik.jdbc.configuration.utils;

import com.levik.jdbc.configuration.exception.JDBCException;
import com.levik.jdbc.configuration.model.BasicDataSource;
import com.levik.jdbc.configuration.model.DataBaseType;

import java.sql.*;

public class JDBCUtils {

    public static void loadDriver(final DataBaseType dataBaseType){
        try {
            Class.forName(dataBaseType.getDriver());
        } catch (ClassNotFoundException exe) {
            throw new JDBCException("Can't find driver " + dataBaseType.getDriver()  + " throw exe " + exe.getMessage());
        }
    }

    public static Connection getDBConnection(final DataBaseType dataBaseType, final BasicDataSource dataSource){
        String url = dataBaseType.getConnectionPrefix() + dataSource.getHost() + ":" + dataSource.getPost();

        if (DataBaseType.ORACLE_SQL == dataBaseType){
            url += ":" + dataSource.getSid();
        } else {
            url += "/" + dataSource.getSid();
        }

        try {
            return DriverManager.getConnection(url, dataSource.getLogin(), dataSource.getPassword());
        } catch (SQLException exe) {
            throw new JDBCException("Can't get connection, Url " + url  + " throw exe " + exe.getMessage());
        }
    }

    public static void performStatement(final Connection connection, final String query){
        try{
            final Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException exe) {
            throw new JDBCException("Can't perform query " + query + " due to exe " + exe);
        }
    }

    public static void performPrepareStatement(final Connection connection, final String query){
        try{
            final PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute(query);
        } catch (SQLException exe) {
            throw new JDBCException("Can't perform query " + query + " due to exe " + exe);
        }
    }

    public static ResultSet performPrepareStatementWithResult(final Connection connection, final String query){
        try{
            final PreparedStatement preparedStatement = connection.prepareStatement(query);
            return preparedStatement.executeQuery();
        } catch (SQLException exe) {
            throw new JDBCException("Can't perform query " + query + " due to exe " + exe);
        }
    }

    public static void releaseConnection(final Connection connection){
        if (connection != null){
            try {
                connection.close();
            } catch (SQLException exe) {
                throw new JDBCException("Can't close connection due to exe " + exe);
            }
        }
    }


}
