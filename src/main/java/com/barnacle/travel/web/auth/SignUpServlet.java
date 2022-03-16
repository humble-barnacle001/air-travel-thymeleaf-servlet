package com.barnacle.travel.web.auth;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.barnacle.travel.database.models.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;

import org.springframework.security.crypto.bcrypt.BCrypt;

@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ServletContext sc = req.getServletContext();
        HttpSession session = req.getSession();
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("pass");

        MongoDatabase db = (MongoDatabase) sc.getAttribute("db");
        MongoCollection<User> collection = db.getCollection("users", User.class);
        long count = collection.countDocuments(Filters.eq("email", email));
        if (count != 0)
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email already registered!");
        else {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            long currentTime = new Date().getTime();
            User newUser = new User(email, name, hashedPassword, currentTime, false);
            InsertOneResult user = collection.insertOne(newUser);
            if (user.wasAcknowledged()) {
                session.setAttribute("userID", user.getInsertedId());
                session.setAttribute("isLoggedIn", Boolean.TRUE);

                resp.sendRedirect(req.getContextPath() + "/dashboard");
            } else {
                session.setAttribute("isLoggedIn", Boolean.FALSE);

                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }
}