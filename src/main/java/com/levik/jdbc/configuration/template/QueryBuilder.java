package com.levik.jdbc.configuration.template;

import com.levik.jdbc.configuration.exception.JDBCException;
import com.levik.jdbc.configuration.template.converter.TypeConverter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class QueryBuilder<T> extends ReflectionAnalyzes{

    private static final int ROW_COUNT = 2;
    private static final int ROW_COUNT_UPDATE = 4;
    private static final int SIZE = 1;

    private static final String OPEN_BRACKETS = "(";
    private static final String CLOSE_BRACKETS = ")";
    private static final String COMMA = ", ";
    private static final String SEMICOLON = ";";
    private static final String QUOTES = "'";

    private static final String SET = " SET ";
    private static final String WHERE = " WHERE ";
    private static final String EQUALITY = " = ";

    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String DROP_TABLE = "DROP TABLE ";
    private static final String INSERT_INTO_TABLE = "INSERT INTO ";
    private static final String UPDATE_TABLE = "UPDATE ";
    private static final String DELETE_FROM_TABLE = "DELETE FROM ";
    private static final String VALUES = " VALUES ";

    private static final String PRIMARY_KEY = "PRIMARY KEY ";

    private final List<String> skipFields = Arrays.asList(TABLE_NAME, PK);
    protected TypeConverter typeConverter;

    public void setTypeConverter(final TypeConverter typeConverter) {
        this.typeConverter = typeConverter;
    }

    @Override
    protected Map<String, String> getMetaDateByObjectWithType(Object obj) {
        final Map<String, String> metaDateByObject = super.getMetaDateByObjectWithType(obj);
        return typeConverter.convertType(metaDateByObject);
    }

    protected String createQuery(final Map<String, String> dataBaseMetaDate){
        verifiedBeforeProcessing(dataBaseMetaDate);
        final StringBuilder query = new StringBuilder(CREATE_TABLE + dataBaseMetaDate.get(TABLE_NAME) + OPEN_BRACKETS);
        createHelper(query, dataBaseMetaDate);

        if (hasPk(dataBaseMetaDate)){
            addPrimaryKey(query, dataBaseMetaDate);
        }

        query.append(CLOSE_BRACKETS);
        return query.toString();
    }

    protected  String dropQuery(final Map<String, String> dataBaseMetaDate){
        return DROP_TABLE + dataBaseMetaDate.get(TABLE_NAME) + SEMICOLON;
    }

    public String saveQuery(final Map<String, Object> dataBaseMetaDate){
        final StringBuilder query = new StringBuilder(INSERT_INTO_TABLE + dataBaseMetaDate.get(TABLE_NAME) + OPEN_BRACKETS);
        insertHelper(query, dataBaseMetaDate);

        query.append(CLOSE_BRACKETS);
        return query.toString();
    }

    public String updateQuery(final Map<String, Object> dataBaseMetaDate){
        final StringBuilder query = new StringBuilder(UPDATE_TABLE + dataBaseMetaDate.get(TABLE_NAME) + SET);
        updateHelper(query, dataBaseMetaDate);

        return query.toString();
    }

    public String deleteQuery(final Map<String, Object> dataBaseMetaDate){
        Object value = null;
        final StringBuilder query = new StringBuilder(DELETE_FROM_TABLE + dataBaseMetaDate.get(TABLE_NAME));
        final String fieldName = (String) dataBaseMetaDate.get(PK);

        if (fieldName != null){
            value = dataBaseMetaDate.get(fieldName);
        }

        query.append(WHERE).append(fieldName).append(EQUALITY).append(value);
        return query.toString();
    }

    private void updateHelper(final StringBuilder query, final Map<String, Object> dataBaseMetaDate) {
        int size = dataBaseMetaDate.size();

        int index = ROW_COUNT_UPDATE;
        for (final Map.Entry<String, Object> next : dataBaseMetaDate.entrySet()) {
            final String key = next.getKey();

            if (skipFields.contains(key)){
                continue;
            }

            final String fieldName = (String) dataBaseMetaDate.get(PK);
            if (fieldName != null && fieldName.equals(key)){
                continue;
            }

            final Object value = next.getValue();

            query.append(key).append(EQUALITY);

            if (value instanceof String){
                query.append(QUOTES).append(value).append(QUOTES);
            } else {
                query.append(value);
            }

            if (index < size){
                query.append(COMMA);
            }

            index++;
        }

        final String fieldName = (String) dataBaseMetaDate.get(PK);

        Object value = null;
        if (fieldName != null){
            value = dataBaseMetaDate.get(fieldName);
        }

        query.append(WHERE).append(fieldName).append(EQUALITY).append(value);
    }

    private void insertHelper(final StringBuilder query, final Map<String, Object> dataBaseMetaDate){
        int size = dataBaseMetaDate.size();

        int index = ROW_COUNT;
        for (final Map.Entry<String, Object> next : dataBaseMetaDate.entrySet()) {
            final String key = next.getKey();

            if (skipFields.contains(key)){
                continue;
            }
            query.append(key);

            if (index < size){
                query.append(COMMA);
            }

            index++;
        }

        query.append(CLOSE_BRACKETS);

        query.append(VALUES).append(OPEN_BRACKETS);

        index = ROW_COUNT;
        for (final Map.Entry<String, Object> next : dataBaseMetaDate.entrySet()) {
            final String key = next.getKey();

            if (skipFields.contains(key)){
                continue;
            }
            final Object value = next.getValue();

            if (value instanceof String){
                query.append(QUOTES).append(value).append(QUOTES);
            } else {
                query.append(value);
            }

            if (index < size){
                query.append(COMMA);
            }

            index++;
        }
    }

    private void createHelper(final StringBuilder query, final Map<String, String> dataBaseMetaDate){
        int size = dataBaseMetaDate.size();

        int index = ROW_COUNT;
        for (final Map.Entry<String, String> next : dataBaseMetaDate.entrySet()) {
            final String key = next.getKey();

            if (skipFields.contains(key)){
                continue;
            }
            final String value = next.getValue();

            query.append(key).append(" ").append(value);

            if (index < size){
                query.append(COMMA);
            }

            index++;
        }
    }

    private void verifiedBeforeProcessing(final Map<String, String> dataBaseMetaDate){
        int size = dataBaseMetaDate.size();
        final String tableName = dataBaseMetaDate.get(TABLE_NAME);

        if (tableName == null){
            throw new JDBCException("Class should have annotation Entity");
        }

        if (size <= SIZE){
            throw new JDBCException("Table should have at least one field");
        }
    }

    private boolean hasPk(final Map<String, String> dataBaseMetaDate){
        final String fieldName = dataBaseMetaDate.get(PK);
        return fieldName != null;

    }

    private void addPrimaryKey(final StringBuilder query, final Map<String, String> dataBaseMetaDate){
        query.append(PRIMARY_KEY).append(OPEN_BRACKETS).append(dataBaseMetaDate.get(PK)).append(CLOSE_BRACKETS);
    }
}
