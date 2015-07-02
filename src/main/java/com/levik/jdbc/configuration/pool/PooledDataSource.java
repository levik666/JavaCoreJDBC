package com.levik.jdbc.configuration.pool;

import com.levik.jdbc.configuration.exception.JDBCException;
import com.levik.jdbc.configuration.model.BasicDataSource;
import com.levik.jdbc.configuration.model.DataBaseType;
import com.levik.jdbc.configuration.template.AbstractTemplate;
import com.levik.jdbc.configuration.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PooledDataSource extends AbstractTemplate {

    private BlockingQueue<Connection> queue;
    private int capacity;

    public PooledDataSource(final DataBaseType dataBaseType, final BasicDataSource dataSource, final int capacity) {
        super(dataBaseType, dataSource);

        this.capacity = capacity;

        queue = new ArrayBlockingQueue<>(capacity);
    }

    protected void initDataSource() {
        for (int index = 0; index <= capacity; index++) {
            final Connection conn = JDBCUtils.getDBConnection(dataBaseType, dataSource);
            queue.offer(conn);
        }
    }

    public Connection getConnection() {
        if (queue.isEmpty()) {
            return null;
        }
        final Connection conn = queue.peek();

        if (isValid(conn)) {
            return conn;
        }

        return null;
    }

    protected boolean isValid(final Connection conn) {
        if (conn != null) {

            try {
                if (conn.isClosed()) {
                    conn.close();
                    return false;
                }

                return true;


            } catch (SQLException exe) {
                throw new JDBCException(exe.getMessage());
            }
        }

        return false;
    }

    public boolean released(final Connection conn) {
        boolean isAdded = false;
        if (isValid(conn)) {
            isAdded = queue.offer(conn);
        }
        return isAdded;
    }

    public void destroyAll(){
        for (final Connection conn : queue) {
            JDBCUtils.releaseConnection(conn);
        }
    }

}
