package com.levik.jdbc.configuration.template;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.levik.jdbc.configuration.exception.JDBCException;
import com.levik.jdbc.configuration.model.BasicDataSource;
import com.levik.jdbc.configuration.model.DDLAuto;
import com.levik.jdbc.configuration.model.DataBaseType;
import com.levik.jdbc.configuration.model.ValueType;
import com.levik.jdbc.configuration.utils.JDBCUtils;
import com.levik.jdbc.entiry.Cart;

public class JdbcTemplate extends AbstractTemplate{
	static final Logger LOGGER = Logger.getLogger(JdbcTemplate.class);

    public JdbcTemplate(DataBaseType dataBaseType, BasicDataSource dataSource) {
        super(dataBaseType, dataSource);
    }

    public void createTable(Object obj) {
    	LOGGER.info("Create table for class: "+ obj.getClass().getName());
        
    	Connection connection = null;
        final Map<String, ValueType> dataBaseMetaDate = objectProcessedBefore(obj);

        try {
            connection = getConnection();

            if (DDLAuto.CREATE == getDdlAuto()) {
                final String createQuery = super.createQuery(dataBaseMetaDate);
                JDBCUtils.performStatement(connection, createQuery);
            } else if (DDLAuto.CREATE_DROP == getDdlAuto()) {
                final String dropQuery = super.dropQuery();
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
    	LOGGER.info("Save entity: "+ obj.getClass().getName());
        
    	Connection connection = null;
        final Map<String, ValueType> dataBaseMetaDate = objectProcessedBefore(obj);

        final String saveQuery = super.saveQuery(dataBaseMetaDate);

        try {
            connection = getConnection();
            JDBCUtils.performPrepareStatement(connection, saveQuery);

        } finally {
            JDBCUtils.releaseConnection(connection);
        }
    }

    public void update(Object obj){
    	LOGGER.info("Update entity: "+ obj.getClass().getName());

    	Connection connection = null;
        final Map<String, ValueType> dataBaseMetaDate = objectProcessedBefore(obj);

        final String saveQuery = super.updateQuery(dataBaseMetaDate);

        try {
            connection = getConnection();
            JDBCUtils.performPrepareStatement(connection, saveQuery);

        } finally {
            JDBCUtils.releaseConnection(connection);
        }
    }

    public void delete(Object obj){
    	LOGGER.info("Delete entity: "+ obj.getClass().getName());
        
    	Connection connection = null;
        final Map<String, ValueType> dataBaseMetaDate = objectProcessedBefore(obj);

        final String saveQuery = super.deleteQuery(dataBaseMetaDate);

        try {
            connection = getConnection();
            JDBCUtils.performPrepareStatement(connection, saveQuery);

        } finally {
            JDBCUtils.releaseConnection(connection);
        }
    }
    
	public Object select(String query, Class aclass) {
		LOGGER.info("Select from Db: " + query);

		Connection connection = null;
		ResultSet resultSet = null;

		Object newCart = null;

		try {
			connection = getConnection();
			resultSet = JDBCUtils.performPrepareStatementWithResult(connection,
					query);

			resultSet.next();

			newCart = ResultSetAnalizer.getObjectFromResultSet(resultSet,
					Cart.class);

		} catch (SQLException | IllegalArgumentException
				| IllegalAccessException | InstantiationException e1) {
			e1.printStackTrace();
		} finally {
			JDBCUtils.releaseConnection(connection);
		}
		return newCart;
	}
}
