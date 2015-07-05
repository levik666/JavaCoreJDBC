package com.levik.jdbc.configuration.template;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.levik.jdbc.configuration.annotations.Entity;
import com.levik.jdbc.configuration.annotations.Id;
import com.levik.jdbc.configuration.annotations.JoinColumn;
import com.levik.jdbc.configuration.annotations.ManyToOne;
import com.levik.jdbc.configuration.model.Foreign;
import com.levik.jdbc.configuration.model.ValueType;

public class ReflectionAnalyzes {
    private String tableName;

    private List<String> primaryKeys;

    private List<Foreign> foreignKeys;

    public String getTableName() {
        return tableName;
    }

    public List<String> getPrimaryKeys() {
        return primaryKeys;
    }

    public List<Foreign> getForeignKeys() {
        return foreignKeys;
    }

    protected Map<String, ValueType> getMetaDateByObjectWithType(Object obj) {
        final Map<String, ValueType> metaDate = new ConcurrentHashMap<>();
        final Class<?> aClass = obj.getClass();
        primaryKeys = new CopyOnWriteArrayList<>();
        foreignKeys = new CopyOnWriteArrayList<>();

        tableName = getTableName(aClass);

        final Field[] declaredFields = aClass.getDeclaredFields();
        for (final Field field : declaredFields) {
            field.setAccessible(true);
            String fieldName = field.getName();

            if (field.isAnnotationPresent(Id.class)) {
                primaryKeys.add(fieldName);
            }

            if (field.isAnnotationPresent(JoinColumn.class) && field.isAnnotationPresent(ManyToOne.class)) {
                populateForeignKey(field, fieldName, obj, metaDate);
                continue;
            }

            final String fieldType = field.getType().getName();
            populateMetaData(fieldType, obj, metaDate, field, fieldName);
        }

        return metaDate;
    }

    private void populateForeignKey(final Field field, String fieldName, final Object obj, final Map<String, ValueType> metaDate){
            final Foreign foreign = new Foreign();
            Class<?> aClassInner;
            Field fieldByName;

            final JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
            final String name = joinColumn.name();

            foreign.setRefId(name);
            fieldName = fieldName + "_" + name;
            foreign.setKey(fieldName);


            final String className = field.getType().getName();

            try {
                aClassInner = Class.forName(className);
                fieldByName = aClassInner.getDeclaredField(name);
            } catch (Exception exe) {
                throw new RuntimeException("Can't load class or get declared field " + name + "due to exe " + exe.getMessage());
            }

            String tableNameInner = getTableName(aClassInner);
            foreign.setRefName(tableNameInner);

            if (fieldByName.isAnnotationPresent(Id.class)) {
                final String fieldType = fieldByName.getType().getName();
                populateMetaData(fieldType, obj, metaDate, field, fieldName );
            }

            foreignKeys.add(foreign);
    }

    private String getTableName(final Class<?> aClass) {
        if (aClass.isAnnotationPresent(Entity.class)) {
            final Entity annotation = aClass.getAnnotation(Entity.class);
            return annotation.name();
        }

        return null;
    }

    private void populateMetaData(String fieldType, final Object obj, final Map<String, ValueType> metaDate, final Field field, final String fieldName){
        Object fieldValue;
        try {
            fieldValue = field.get(obj);
        } catch (IllegalAccessException exe) {
            throw new RuntimeException("Can't get access to field " + field.getName() + "due to exe " + exe.getMessage());
        }

        final ValueType valueType = new ValueType(fieldType, fieldValue);
        metaDate.put(fieldName, valueType);
    }

    public <T> T getObjectFromResultSet(ResultSet rs, Class<T> aClass)
            throws SQLException, IllegalArgumentException,
            IllegalAccessException, InstantiationException {
        primaryKeys = new CopyOnWriteArrayList<>();

        getTableName(aClass);

        T obj = aClass.newInstance();
        final Field[] declaredFields = aClass.getDeclaredFields();
        for (final Field field : declaredFields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Id.class)) {
                primaryKeys.add(field.getName());
            }
            setField(rs, obj, field);
        }
        return obj;
    }

    private void setField(ResultSet rs, Object obj, final Field field)
            throws SQLException, IllegalAccessException {
        final String fieldName = field.getName();

        final String fieldType = field.getType().getName();

        switch (fieldType) {
            case "String":
                field.set(obj, rs.getString(fieldName));
                break;
            case "int":
                field.setInt(obj, rs.getInt(fieldName));
                break;
            case "boolean":
                field.setBoolean(obj, rs.getBoolean(fieldName));
                break;
            case "byte":
                field.setByte(obj, rs.getByte(fieldName));
                break;
            case "char":
                field.setChar(obj, rs.getString(fieldName).charAt(0));
                break;
            case "double":
                field.setDouble(obj, rs.getDouble(fieldName));
                break;
            case "float":
                field.setFloat(obj, rs.getFloat(fieldName));
                break;
            case "long":
                field.setLong(obj, rs.getLong(fieldName));
                break;
            case "short":
                field.setShort(obj, rs.getShort(fieldName));
                break;
            default:
                field.set(obj, rs.getObject(fieldName));
                break;
        }
    }

}
