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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cart cart = (Cart) o;

        if (cartId != cart.cartId) return false;
        if (name != null ? !name.equals(cart.name) : cart.name != null) return false;
        return !(lastName != null ? !lastName.equals(cart.lastName) : cart.lastName != null);

    }

    @Override
    public int hashCode() {
        int result = cartId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
