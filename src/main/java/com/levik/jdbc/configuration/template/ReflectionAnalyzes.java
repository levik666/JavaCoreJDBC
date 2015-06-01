package com.levik.jdbc.configuration.template;

import com.levik.jdbc.configuration.annotations.Entity;
import com.levik.jdbc.configuration.annotations.Id;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReflectionAnalyzes {

    protected static final String TABLE_NAME = "TABLE_NAME";
    protected static final String PK = "PK";

    protected Map<String , String> getMetaDateByObjectWithType(Object obj){
        final Map<String , String> metaDate = new LinkedHashMap<>();
        final Class<?> aClass = obj.getClass();

        if (aClass.isAnnotationPresent(Entity.class)){
            final Entity annotation = aClass.getAnnotation(Entity.class);
            final String name = annotation.name();
            metaDate.put(TABLE_NAME, name);
        }

        final Field[] declaredFields = aClass.getDeclaredFields();
        for(Field field : declaredFields){
            final String fieldName = field.getName();
            if (field.isAnnotationPresent(Id.class)){
                metaDate.put(PK, fieldName);
            }

            final String fieldType = field.getType().getName();
            metaDate.put(fieldName, fieldType);
        }

        return metaDate;
    }

    protected Map<String , Object> getMetaDateByObjectWithValues(Object obj){
        return getMetaDateByObjectWithValues(obj, false);
    }
    protected Map<String , Object> getMetaDateByObjectWithValuesAndPk(Object obj){
        return getMetaDateByObjectWithValues(obj, true);
    }

    private Map<String , Object> getMetaDateByObjectWithValues(Object obj, boolean withPk){
        final Map<String , Object> metaDate = new LinkedHashMap<>();
        final Class<?> aClass = obj.getClass();

        if (aClass.isAnnotationPresent(Entity.class)){
            final Entity annotation = aClass.getAnnotation(Entity.class);
            final String name = annotation.name();
            metaDate.put(TABLE_NAME, name);
        }

        final Field[] declaredFields = aClass.getDeclaredFields();
        for(Field field : declaredFields){
            field.setAccessible(true);
            final String fieldName = field.getName();

            if (withPk){
                if (field.isAnnotationPresent(Id.class)){
                    metaDate.put(PK, fieldName);
                }
            }

            Object fieldType;
            try {
                fieldType = field.get(obj);
            } catch (IllegalAccessException exe) {
                throw new RuntimeException("Can't get access to field " + field.getName() + "due to exe " + exe.getMessage());
            }
            metaDate.put(fieldName, fieldType);
        }

        return metaDate;
    }
}
