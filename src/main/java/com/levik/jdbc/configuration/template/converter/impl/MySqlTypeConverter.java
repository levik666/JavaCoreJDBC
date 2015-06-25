package com.levik.jdbc.configuration.template.converter.impl;

import com.levik.jdbc.configuration.model.ValueType;
import com.levik.jdbc.configuration.template.converter.TypeConverter;

import java.util.HashMap;
import java.util.Map;

public class MySqlTypeConverter implements TypeConverter{

    private Map<String, String> dataBaseParams = new HashMap<>();

    {
        dataBaseParams.put("java.lang.String", "VARCHAR(255)");
    }

    public MySqlTypeConverter(){}

    public MySqlTypeConverter(final Map<String, String> dataBaseParams) {
        this.dataBaseParams = new HashMap<>();
        this.dataBaseParams.putAll(dataBaseParams);
    }

    public Map<String, ValueType> convertType(final Map<String, ValueType> classParams) {
        for (Map.Entry<String, ValueType> next : classParams.entrySet()) {
            final String key = next.getKey();
            final ValueType valueType = next.getValue();
            final String newValue = dataBaseParams.get(valueType.getType());
            if (newValue != null){
                valueType.setType(newValue);
                classParams.put(key, valueType);
            }
        }

        return classParams;
    }
}
