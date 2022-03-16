package com.barnacle.travel.web.auth;

import java.io.IOException;

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

import org.springframework.security.crypto.bcrypt.BCrypt;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext sc = req.getServletContext();
        HttpSession session = req.getSession();

        String email = req.getParameter("email");
        String password = req.getParameter("pass");

        MongoDatabase db = (MongoDatabase) sc.getAttribute("db");
        MongoCollection<User> collection = db.getCollection("users", User.class);
        User user = collection.find(Filters.eq("email", email)).first();

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            session.setAttribute("userID", user.getId());
            session.setAttribute("isLoggedIn", Boolean.TRUE);

            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } else {
            session.setAttribute("isLoggedIn", Boolean.FALSE);

            resp.setHeader("Refresh", "2; URL=" + req.getContextPath() + "/auth");
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid Credentials");
        }
    }
}
