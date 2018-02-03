package com.csye6225.spring2018;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "personID", unique=true, nullable = false)
    private int id;

    @Column(name="Email")
//    @NotEmpty
//    @Email
    private String email;

    @Column(name = "Password")

    @NotEmpty(message = "Please enter your password.")
//    @Size(min = 6, max = 15, message = "Your password must between 6 and 15 characters")
    private String password;

    public User(){

    }

    public int getId() {
        return id;
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
}
