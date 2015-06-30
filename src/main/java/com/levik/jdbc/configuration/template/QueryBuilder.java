package com.levik.jdbc.configuration.template;

import java.util.Map;

import com.levik.jdbc.configuration.exception.JDBCException;
import com.levik.jdbc.configuration.model.ValueType;
import com.levik.jdbc.configuration.template.converter.TypeConverter;

public class QueryBuilder<T> extends ReflectionAnalyzes{

    private static final int DEFAULT_ROW_COUNT = 0;
    private static final int SIZE = 1;

    private static final String SPACE = " ";
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

    protected TypeConverter typeConverter;

    public void setTypeConverter(final TypeConverter typeConverter) {
        this.typeConverter = typeConverter;
    }

    @Override
    protected Map<String, ValueType> getMetaDateByObjectWithType(Object obj) {
        final Map<String, ValueType> metaDateByObject = super.getMetaDateByObjectWithType(obj);
        return typeConverter.convertType(metaDateByObject);
    }

    protected String createQuery(final Map<String, ValueType> dataBaseMetaDate){
        verifiedBeforeProcessing(dataBaseMetaDate);
        final StringBuilder query = new StringBuilder(CREATE_TABLE + getTableName() + OPEN_BRACKETS);
        createHelper(query, dataBaseMetaDate);

        if (!getPrimaryKeys().isEmpty()){
            addPrimaryKey(query);
        }

        query.append(CLOSE_BRACKETS);
        return query.toString();
    }

    protected  String dropQuery(){
        return DROP_TABLE + getTableName() + SEMICOLON;
    }

    public String saveQuery(final Map<String, ValueType> dataBaseMetaDate){
        final StringBuilder query = new StringBuilder(INSERT_INTO_TABLE + getTableName() + OPEN_BRACKETS);
        insertHelper(query, dataBaseMetaDate);

        query.append(CLOSE_BRACKETS);
        return query.toString();
    }

    public String updateQuery(final Map<String, ValueType> dataBaseMetaDate){
        final StringBuilder query = new StringBuilder(UPDATE_TABLE + getTableName() + SET);
        updateHelper(query, dataBaseMetaDate);

        return query.toString();
    }

    public String deleteQuery(final Map<String, ValueType> dataBaseMetaDate){
        final StringBuilder query = new StringBuilder(DELETE_FROM_TABLE + getTableName());

        for(final String key : getPrimaryKeys()){
            query.append(WHERE).append(key).append(EQUALITY);

            final ValueType valueType = dataBaseMetaDate.get(key);
            final Object value = valueType.getValue();

            addValue(value, query);
        }

        return query.toString();
    }
    
    private void updateHelper(final StringBuilder query, final Map<String, ValueType> dataBaseMetaDate) {
        int size = dataBaseMetaDate.size();
        int index = DEFAULT_ROW_COUNT;

        for (final Map.Entry<String, ValueType> next : dataBaseMetaDate.entrySet()) {
            final String key = next.getKey();
            final ValueType valueType = next.getValue();
            final Object value = valueType.getValue();

            query.append(key).append(EQUALITY);

            addValue(value, query);

            index++;
            if (isThisNotLastElement(index, size)){
                query.append(COMMA);
            }


        }

        for(final String key : getPrimaryKeys()){
            final ValueType valueType = dataBaseMetaDate.get(key);
            final Object value = valueType.getValue();

            query.append(WHERE).append(key).append(EQUALITY);

            addValue(value, query);
        }
    }

    private void insertHelper(final StringBuilder query, final Map<String, ValueType> dataBaseMetaDate){
        int size = dataBaseMetaDate.size();
        int index = DEFAULT_ROW_COUNT;

        final StringBuilder keyBuilder = new StringBuilder();
        final StringBuilder valueBuilder = new StringBuilder(VALUES + OPEN_BRACKETS);

        for (final Map.Entry<String, ValueType> entry : dataBaseMetaDate.entrySet()) {
            final String key = entry.getKey();
            final ValueType valueType = entry.getValue();
            final Object value = valueType.getValue();

            keyBuilder.append(key);

            addValue(value, valueBuilder);

            index++;
            if (isThisNotLastElement(index, size)){
                keyBuilder.append(COMMA);
                valueBuilder.append(COMMA);
            }
        }

        keyBuilder.append(CLOSE_BRACKETS);

        query.append(keyBuilder).append(valueBuilder);
    }

    private void createHelper(final StringBuilder query, final Map<String, ValueType> dataBaseMetaDate){
        int index = DEFAULT_ROW_COUNT;
        int size = dataBaseMetaDate.size();

        for (final Map.Entry<String, ValueType> next : dataBaseMetaDate.entrySet()) {
            final String key = next.getKey();
            final ValueType valueType = next.getValue();

            query.append(key).append(SPACE).append(valueType.getType());
            index++;

            if (isThisNotLastElement(index, size)){
                query.append(COMMA);
            }
        }
    }

    private boolean isThisNotLastElement(int index, int size){
        return index < size;
    }

    private void verifiedBeforeProcessing(final Map<String, ValueType> dataBaseMetaDate){
        int size = dataBaseMetaDate.size();
        final String name = getTableName();

        if (name == null){
            throw new JDBCException("Class should have annotation Entity");
        }

        if (size <= SIZE){
            throw new JDBCException("Table should have at least one field");
        }
    }

    private void addPrimaryKey(final StringBuilder query){
        int index = DEFAULT_ROW_COUNT;
        int size = getPrimaryKeys().size();

        if (isCommaAbsent(query)){
            query.append(COMMA).append(SPACE);
        }

        for (final String key : getPrimaryKeys()){
            query.append(PRIMARY_KEY).append(OPEN_BRACKETS).append(key).append(CLOSE_BRACKETS);
            index++;

            if (isThisNotLastElement(index, size)){
                query.append(COMMA);
            }
        }
    }

    private void addValue(final Object value, final StringBuilder query){
        if (value instanceof String){
            query.append(QUOTES).append(value).append(QUOTES);
        } else {
            query.append(value);
        }
    }

    private boolean isCommaAbsent(final StringBuilder query){
        int length = query.length();
        final String lastElement = query.substring(length - 1, length);
        return !COMMA.equals(lastElement);
    }
    
}
