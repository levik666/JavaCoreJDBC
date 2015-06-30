package com.levik.jdbc;

import java.util.Set;

import org.apache.log4j.Logger;

import com.levik.jdbc.configuration.model.BasicDataSource;
import com.levik.jdbc.configuration.model.DDLAuto;
import com.levik.jdbc.configuration.model.DataBaseType;
import com.levik.jdbc.configuration.template.JdbcTemplate;
import com.levik.jdbc.configuration.template.converter.impl.MySqlTypeConverter;
import com.levik.jdbc.entiry.Cart;

public class Application {
    static final Logger LOGGER = Logger.getLogger(Application.class); 

    public static void main(String[] args) {
        LOGGER.info("Run \"main\" method");
        final BasicDataSource basicDataSource = new BasicDataSource("localhost", "3306", "simple", "root", "root");
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataBaseType.MY_SQL, basicDataSource);
        jdbcTemplate.setTypeConverter(new MySqlTypeConverter());
        jdbcTemplate.setDdlAuto(DDLAuto.CREATE_DROP);

        Cart cart = new Cart();
        cart.setCartId(2);
        cart.setName("test2");
        cart.setLastName("test2");
        
        Cart cart2 = new Cart();
        cart2.setCartId(3);
        cart2.setName("test3");
        cart2.setLastName("test3");
        
        jdbcTemplate.createTable(cart);
        jdbcTemplate.save(cart);
        jdbcTemplate.update(cart);
        jdbcTemplate.save(cart2);
        Cart rs = jdbcTemplate.select("Select * From Cart Where cartId = 3", Cart.class);
        System.out.println(rs);
        
        Set<Cart> carts = jdbcTemplate.selectSet("Select * From Cart", Cart.class);
        System.out.println(carts);
        
        jdbcTemplate.delete(cart);
        jdbcTemplate.delete(cart2);

    }
}
