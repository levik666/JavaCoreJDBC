package com.levik.jdbc.configuration.template;

import com.levik.jdbc.configuration.annotations.Entity;
import com.levik.jdbc.configuration.annotations.Id;
import com.levik.jdbc.configuration.model.ValueType;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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
        primaryKeys = new CopyOnWriteArrayList<>();
        final Class<?> aClass = obj.getClass();

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
}
