package com.barnacle.travel.web.auth;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logOut")
public class LogOutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<HttpSession> session = Optional.ofNullable(req.getSession());
        session.ifPresent(HttpSession::invalidate);

        req.getSession().setAttribute("isLoggedIn", Boolean.FALSE);

        resp.sendRedirect(req.getContextPath() + "/auth");
    }
}
