package com.levik.jdbc.configuration.template.converter.impl;

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

    public Map<String, String> convertType(final Map<String, String> classParams) {
        final Map<String, String> convertedMap = new HashMap<>();
        for (Map.Entry<String, String> next : classParams.entrySet()) {
            final String key = next.getKey();
            final String value = next.getValue();
            final String newValue = dataBaseParams.get(value);
            if (newValue != null){
                convertedMap.put(key, newValue);
            } else {
                convertedMap.put(key, value);
            }
        }

        return convertedMap;
    }
}
