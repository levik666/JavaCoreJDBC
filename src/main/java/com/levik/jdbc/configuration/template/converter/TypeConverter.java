package com.levik.jdbc.configuration.template.converter;

import com.levik.jdbc.configuration.model.ValueType;

import java.util.Map;

public interface TypeConverter {

    Map<String, ValueType> convertType(final Map<String, ValueType> classParams);

}
