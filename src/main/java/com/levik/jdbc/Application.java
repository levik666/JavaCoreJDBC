package com.levik.jdbc;

import com.levik.jdbc.configuration.model.BasicDataSource;
import com.levik.jdbc.configuration.model.DDLAuto;
import com.levik.jdbc.configuration.model.DataBaseType;
import com.levik.jdbc.configuration.template.JdbcTemplate;
import com.levik.jdbc.configuration.template.converter.impl.MySqlTypeConverter;
import com.levik.jdbc.entiry.Cart;

public class Application {

    public static void main(String[] args) {
        final BasicDataSource basicDataSource = new BasicDataSource("localhost", "3306", "simple", "root", "root");
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataBaseType.MY_SQL, basicDataSource);
        jdbcTemplate.setTypeConverter(new MySqlTypeConverter());
        jdbcTemplate.setDdlAuto(DDLAuto.CREATE_DROP);

        Cart cart = new Cart();
        cart.setCartId(2);
        cart.setName("test2");
        cart.setLastName("test2");

        jdbcTemplate.createTable(cart);
        jdbcTemplate.save(cart);
        jdbcTemplate.update(cart);
        jdbcTemplate.delete(cart);

    }
}