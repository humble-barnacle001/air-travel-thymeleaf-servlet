package com.barnacle.travel.web.auth.filters;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.barnacle.travel.database.models.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

@WebFilter({ "/dashboard", "/offer" })
public class LoggedFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();

        Optional<Boolean> isLoggedIn = Optional.ofNullable(
                (Boolean) session.getAttribute("isLoggedIn"));

        MongoDatabase db = (MongoDatabase) req.getServletContext().getAttribute("db");
        MongoCollection<User> collection = db.getCollection("users", User.class);
        User user = collection.find(Filters.eq("_id", session.getAttribute("userID"))).first();

        if (isLoggedIn.orElse(false) && user != null) {
            session.setAttribute("user", user);

            if (user.getIsManager())
                chain.doFilter(request, response);
            else
                resp.sendRedirect(req.getContextPath() + "/search");
        } else {
            resp.setHeader("Refresh", "5; URL=" + req.getContextPath() + "/auth");
            resp.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Admin login Required! Redirecting to login page in 5 seconds!!");
        }
    }

    @Override
    public void destroy() {
    }
}
