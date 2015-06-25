package com.levik.jdbc.configuration.template;

import com.levik.jdbc.configuration.model.BasicDataSource;
import com.levik.jdbc.configuration.model.DDLAuto;
import com.levik.jdbc.configuration.model.DataBaseType;
import com.levik.jdbc.configuration.model.ValueType;
import com.levik.jdbc.configuration.utils.JDBCUtils;

import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AbstractTemplate extends QueryBuilder {

    protected final DataBaseType dataBaseType;
    protected final BasicDataSource dataSource;

    protected Map<Object, Map<String, ValueType>> bucket = new ConcurrentHashMap<>();

    protected DDLAuto ddlAuto = DDLAuto.CREATE;

    public AbstractTemplate(final DataBaseType dataBaseType, final BasicDataSource dataSource) {
        this.dataBaseType = dataBaseType;
        this.dataSource = dataSource;

        JDBCUtils.loadDriver(dataBaseType);
    }

    protected Connection getConnection() {
        return JDBCUtils.getDBConnection(dataBaseType, dataSource);
    }

    public DDLAuto getDdlAuto() {
        return ddlAuto;
    }

    public void setDdlAuto(DDLAuto ddlAuto) {
        this.ddlAuto = ddlAuto;
    }

    protected Map<String, ValueType> objectProcessedBefore(final Object obj){
        Map<String, ValueType> dataBaseMetaDate = null;
        if (bucket.containsKey(obj)){
            return bucket.get(obj);
        }

        dataBaseMetaDate = getMetaDateByObjectWithType(obj);
        bucket.put(obj, dataBaseMetaDate);
        return dataBaseMetaDate;
    }

}
