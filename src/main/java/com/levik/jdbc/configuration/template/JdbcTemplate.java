package com.levik.jdbc.configuration.template;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.levik.jdbc.configuration.pool.PooledDataSource;
import org.apache.log4j.Logger;

import com.levik.jdbc.configuration.exception.JDBCException;
import com.levik.jdbc.configuration.model.BasicDataSource;
import com.levik.jdbc.configuration.model.DDLAuto;
import com.levik.jdbc.configuration.model.DataBaseType;
import com.levik.jdbc.configuration.model.ValueType;
import com.levik.jdbc.configuration.utils.JDBCUtils;

public class JdbcTemplate extends PooledDataSource {
	private static final Logger LOGGER = Logger.getLogger(JdbcTemplate.class);

    private static final int DEFAULT_INIT_CAPACITY = 10;

    public JdbcTemplate(DataBaseType dataBaseType, BasicDataSource dataSource){
        this(dataBaseType, dataSource, DEFAULT_INIT_CAPACITY);
    }

    public JdbcTemplate(DataBaseType dataBaseType, BasicDataSource dataSource, int capacity) {
        super(dataBaseType, dataSource, capacity);

        initDataSource();
    }

    public void createTable(Object obj) {
    	LOGGER.info("Create table for class: "+ obj.getClass().getName());
        
    	Connection connection = null;
        final Map<String, ValueType> dataBaseMetaDate = objectProcessedBefore(obj);

        try {
            connection = getConnection();

            if (DDLAuto.CREATE == getDdlAuto()) {
                LOGGER.info("perform create table");
                final String query = super.createQuery(dataBaseMetaDate);
                LOGGER.info("query: "+ query);
                JDBCUtils.performStatement(connection, query);
            } else if (DDLAuto.CREATE_DROP == getDdlAuto()) {
                LOGGER.info("perform create drop table");
                final String dropQuery = super.dropQuery();
                LOGGER.info("dropQuery: " + dropQuery);
                final String query = super.createQuery(dataBaseMetaDate);
                LOGGER.info("query: " + query);

                try {
                    JDBCUtils.performStatement(connection, dropQuery);
                } catch (JDBCException exe) {
                    LOGGER.warn("Table is not exist " + exe.getMessage());
                }

                JDBCUtils.performStatement(connection, query);
            }
        } finally {
            released(connection);
        }
    }

    public void save(Object obj){
    	LOGGER.info("Save entity: " + obj.getClass().getName());
        
    	Connection connection = null;
        final Map<String, ValueType> dataBaseMetaDate = objectProcessedBefore(obj);

        final String query = super.saveQuery(dataBaseMetaDate);
        LOGGER.info("query: "+ query);

        try {
            connection = getConnection();
            JDBCUtils.performPrepareStatement(connection, query);
        } finally {
            released(connection);
        }
    }

    public void update(Object obj){
    	LOGGER.info("Update entity: "+ obj.getClass().getName());

    	Connection connection = null;
        final Map<String, ValueType> dataBaseMetaDate = objectProcessedBefore(obj);

        final String query = super.updateQuery(dataBaseMetaDate);
        LOGGER.info("query: "+ query);

        try {
            connection = getConnection();
            JDBCUtils.performPrepareStatement(connection, query);
        } finally {
            released(connection);
        }
    }

    public void delete(Object obj){
    	LOGGER.info("Delete entity: "+ obj.getClass().getName());
        
    	Connection connection = null;
        final Map<String, ValueType> dataBaseMetaDate = objectProcessedBefore(obj);

        final String query = super.deleteQuery(dataBaseMetaDate);
        LOGGER.info("query: "+ query);

        try {
            connection = getConnection();
            JDBCUtils.performPrepareStatement(connection, query);
        } finally {
            released(connection);
        }
    }
    
	public <T> T select(String query, Class<T> aClass) {
        LOGGER.info("Select entity: "+ aClass.getName());
		LOGGER.info("query: " + query);

		Connection connection = null;
		T obj = null;

		try {
			connection = getConnection();
            ResultSet resultSet = JDBCUtils.performPrepareStatementWithResult(connection,
					query);
			if (resultSet.next()) {
                obj = getObjectFromResultSet(
                        resultSet, aClass);
			}
		} catch (SQLException | IllegalArgumentException
				| IllegalAccessException | InstantiationException exe) {
            throw new JDBCException(exe.getMessage(), exe);
		} finally {
            released(connection);
		}
		return obj;
	}
	
	public <T> Set<T> selectSet(String query, Class<T> aClass) {
        LOGGER.info("Select entity: "+ aClass.getName());
        LOGGER.info("query: " + query);

		Connection connection = null;
		final Set<T> objSet = new HashSet<>();

		try {
			connection = getConnection();
            ResultSet resultSet = JDBCUtils.performPrepareStatementWithResult(connection,
					query);
			while (resultSet.next()) {
                T obj = getObjectFromResultSet(resultSet, aClass);
                objSet.add(obj);
			}
		} catch (SQLException | IllegalArgumentException
				| IllegalAccessException | InstantiationException exe) {
			throw new JDBCException(exe.getMessage(), exe);
		} finally {
            released(connection);
		}
        return objSet;
	}
}
