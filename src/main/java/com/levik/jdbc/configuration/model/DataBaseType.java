package com.levik.jdbc.configuration.model;

public enum DataBaseType {

    MY_SQL("com.mysql.jdbc.Driver", "jdbc:mysql://"),
    POSTGRES_SQL("org.postgresql.Driver", "jdbc:postgresql://"),
    ORACLE_SQL("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@");

    private String driver;
    private String connectionPrefix;

    DataBaseType(String driver, String connectionPrefix) {
        this.driver = driver;
        this.connectionPrefix = connectionPrefix;
    }

    public String getDriver() {
        return driver;
    }

    public String getConnectionPrefix() {
        return connectionPrefix;
    }
}
