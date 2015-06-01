package com.levik.jdbc.configuration.template.converter;

import java.util.Map;

public interface TypeConverter {

    Map<String, String> convertType(final Map<String, String> classParams);

}
