package com.levik.jdbc.configuration.template;

import com.levik.jdbc.configuration.exception.JDBCException;
import com.levik.jdbc.configuration.model.BasicDataSource;
import com.levik.jdbc.configuration.model.DDLAuto;
import com.levik.jdbc.configuration.model.DataBaseType;
import com.levik.jdbc.configuration.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class JdbcTemplate extends AbstractTemplate{

    public JdbcTemplate(DataBaseType dataBaseType, BasicDataSource dataSource) {
        super(dataBaseType, dataSource);
    }

    public void createTable(Object obj) {
        Connection connection = null;
        final Map<String, String> dataBaseMetaDate = getMetaDateByObjectWithType(obj);

        try {
            connection = getConnection();

            if (DDLAuto.CREATE == ddlAuto) {
                final String createQuery = super.createQuery(dataBaseMetaDate);
                JDBCUtils.performStatement(connection, createQuery);
            } else if (DDLAuto.CREATE_DROP == ddlAuto) {
                final String dropQuery = super.dropQuery(dataBaseMetaDate);
                final String createQuery = super.createQuery(dataBaseMetaDate);

                try {
                    JDBCUtils.performStatement(connection, dropQuery);
                } catch (JDBCException exe) {
                    System.err.println("Can't drop table " + exe.getMessage());
                }

                JDBCUtils.performStatement(connection, createQuery);
            }
        } finally {
            JDBCUtils.releaseConnection(connection);
        }
    }

    public void save(Object obj){
        Connection connection = null;
        final Map<String, Object> dataBaseMetaDate = getMetaDateByObjectWithValues(obj);

        final String saveQuery = super.saveQuery(dataBaseMetaDate);

        try {
            connection = getConnection();
            JDBCUtils.performPrepareStatement(connection, saveQuery);

        } finally {
            JDBCUtils.releaseConnection(connection);
        }
    }

    public void update(Object obj){
        Connection connection = null;
        final Map<String, Object> dataBaseMetaDate = getMetaDateByObjectWithValuesAndPk(obj);

        final String saveQuery = super.updateQuery(dataBaseMetaDate);

        try {
            connection = getConnection();
            JDBCUtils.performPrepareStatement(connection, saveQuery);

        } finally {
            JDBCUtils.releaseConnection(connection);
        }
    }

    public void delete(Object obj){
        Connection connection = null;
        final Map<String, Object> dataBaseMetaDate = getMetaDateByObjectWithValuesAndPk(obj);

        final String saveQuery = super.deleteQuery(dataBaseMetaDate);

        try {
            connection = getConnection();
            JDBCUtils.performPrepareStatement(connection, saveQuery);

        } finally {
            JDBCUtils.releaseConnection(connection);
        }
    }
}
