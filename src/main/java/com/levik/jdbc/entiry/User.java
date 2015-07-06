package com.levik.jdbc.entiry;

import com.levik.jdbc.configuration.annotations.Entity;
import com.levik.jdbc.configuration.annotations.Id;
import com.levik.jdbc.configuration.annotations.JoinColumn;
import com.levik.jdbc.configuration.annotations.ManyToOne;

@Entity(name = "user")
public class User {

    @Id
    private int id;
    private String email;
    private String password;
    private String firstName;

    @ManyToOne
    @JoinColumn(name="id")
    private Blog blog;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }
}
