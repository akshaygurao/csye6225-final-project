package com.csye6225.spring2018.DAO;

import com.csye6225.spring2018.pojo.User;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO extends DAO{

    public UserDAO() {
    }

    public User get(String username, String password) throws Exception {
        try {
            begin();
            Query q = getSession().createQuery("from User where username = :username and password = :password");
            q.setString("username", username);
            q.setString("password", password);
            User user = (User) q.uniqueResult();
            commit();
            return user;
        } catch (HibernateException e) {
            rollback();
            throw new Exception("Could not get user " + username, e);
        }
    }


    public User register(User u) throws Exception {
        try {
            begin();
            System.out.println("inside DAO");

            User user = new User();

            user.setEmail(u.getEmail());
            user.setPassword(u.getPassword());
            getSession().save(user);
            commit();
            return user;

        } catch (HibernateException e) {
            rollback();
            throw new Exception("Exception while creating user: " + e.getMessage());
        }
    }

    public void delete(User user) throws Exception {
        try {
            begin();
            getSession().delete(user);
            commit();
        } catch (HibernateException e) {
            rollback();
            throw new Exception("Could not delete user " + user.getEmail(), e);
        }
    }


}
