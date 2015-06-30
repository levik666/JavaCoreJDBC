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
import com.levik.jdbc.configuration.model.ValueType;

public class ReflectionAnalyzes {
    private String tableName;

    private List<String> primaryKeys;

    public String getTableName() {
        return tableName;
    }

    public List<String> getPrimaryKeys() {
        return primaryKeys;
    }

    protected Map<String, ValueType> getMetaDateByObjectWithType(Object obj) {
        final Map<String, ValueType> metaDate = new ConcurrentHashMap<>();
        final Class<?> aClass = obj.getClass();
        primaryKeys = new CopyOnWriteArrayList<>();

        if (aClass.isAnnotationPresent(Entity.class)) {
            final Entity annotation = aClass.getAnnotation(Entity.class);
            tableName = annotation.name();
        }

        final Field[] declaredFields = aClass.getDeclaredFields();
        for (final Field field : declaredFields) {
            field.setAccessible(true);
            final String fieldName = field.getName();

            if (field.isAnnotationPresent(Id.class)) {
                primaryKeys.add(fieldName);
            }

            final String fieldType = field.getType().getName();
            Object fieldValue;
            try {
                fieldValue = field.get(obj);
            } catch (IllegalAccessException exe) {
                throw new RuntimeException("Can't get access to field " + field.getName() + "due to exe " + exe.getMessage());
            }

            final ValueType valueType = new ValueType(fieldType, fieldValue);
            metaDate.put(fieldName, valueType);
        }

        return metaDate;
    }
    
    public <T> T getObjectFromResultSet(ResultSet rs, Class<T> aClass)
			throws SQLException, IllegalArgumentException,
			IllegalAccessException, InstantiationException {
    	primaryKeys = new CopyOnWriteArrayList<>();
    	
    	if (aClass.isAnnotationPresent(Entity.class)) {
            final Entity annotation = aClass.getAnnotation(Entity.class);
            tableName = annotation.name();
        }
    	
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
