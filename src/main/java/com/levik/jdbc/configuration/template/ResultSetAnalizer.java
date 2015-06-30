package com.levik.jdbc.configuration.template;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetAnalizer {

	public static Object getObjectFromResultSet(ResultSet rs, Class<?> aClass)
			throws SQLException, IllegalArgumentException,
			IllegalAccessException, InstantiationException {
		Object obj = aClass.newInstance();
		final Field[] declaredFields = aClass.getDeclaredFields();
		for (final Field field : declaredFields) {
			field.setAccessible(true);
			final String fieldName = field.getName();

			final String fieldType = field.getType().getName();

			String fieldValue = rs.getString(fieldName);

			switch (fieldType) {
			case "String":
				field.set(obj, new String(fieldValue));
				break;
			case "int":
				field.setInt(obj, Integer.parseInt(fieldValue));
				break;
			case "boolean":
				field.setBoolean(obj, Boolean.parseBoolean(fieldValue));
				break;
			case "byte":
				field.setByte(obj, Byte.parseByte(fieldValue));
				break;
			case "char":
				field.setChar(obj, fieldValue.charAt(0));
				break;
			case "double":
				field.setDouble(obj, Double.parseDouble(fieldValue));
				break;
			case "float":
				field.setFloat(obj, Float.parseFloat(fieldValue));
				break;
			case "long":
				field.setLong(obj, Long.parseLong(fieldValue));
				break;
			case "short":
				field.setShort(obj, Short.parseShort(fieldValue));
				break;
			default:
				field.set(obj, null);
				break;
			}
		}
		return obj;
	}
}
