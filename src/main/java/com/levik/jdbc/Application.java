package com.levik.jdbc;

import java.util.Calendar;
import java.util.Set;

import com.levik.jdbc.entiry.Blog;
import com.levik.jdbc.entiry.User;
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

        Blog blog = new Blog();
        blog.setId(1);
        blog.setName("test");
        blog.setCreated(Calendar.getInstance().getTime());

        User u1 = new User();
        u1.setId(1);
        u1.setPassword("test");
        u1.setEmail("test@i.ua");
        u1.setFirstName("test");
        u1.setBlog(blog);

        jdbcTemplate.createTable(blog);
        jdbcTemplate.createTable(u1);

        final Cart cart = new Cart();
        cart.setCartId(2);
        cart.setName("test2");
        cart.setLastName("test2");
        
        final Cart cart2 = new Cart();
        cart2.setCartId(3);
        cart2.setName("test3");
        cart2.setLastName("test3");
        
        jdbcTemplate.createTable(cart);
        jdbcTemplate.save(cart);
        jdbcTemplate.update(cart);
        jdbcTemplate.save(cart2);
        final Cart rs = jdbcTemplate.select("Select * From Cart Where cartId = 3", Cart.class);
        LOGGER.info(rs);

        Set<Cart> carts = jdbcTemplate.selectSet("Select * From Cart", Cart.class);
        LOGGER.info(carts);

        jdbcTemplate.delete(cart);
        jdbcTemplate.delete(cart2);

        jdbcTemplate.destroyAll();

    }
}
