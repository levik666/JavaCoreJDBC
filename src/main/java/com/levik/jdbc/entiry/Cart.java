package com.levik.jdbc.entiry;

import com.levik.jdbc.configuration.annotations.Entity;
import com.levik.jdbc.configuration.annotations.Id;

@Entity(name = "Cart")
public class Cart {

    @Id
    private int cartId;

    private String name;

    private String lastName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
}
