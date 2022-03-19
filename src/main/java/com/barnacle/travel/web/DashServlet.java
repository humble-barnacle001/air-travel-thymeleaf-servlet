package com.barnacle.travel.web;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.barnacle.travel.config.CustomWebContext;
import com.barnacle.travel.config.TemplateEngineUtil;
import com.barnacle.travel.database.models.User;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@WebServlet("/dashboard")
public class DashServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        Optional<Boolean> isLoggedIn = Optional.ofNullable(
                (Boolean) session.getAttribute("isLoggedIn"));
        if (!isLoggedIn.orElse(false))
            resp.sendRedirect(req.getContextPath() + "/auth");
        else {
            Optional<User> user = Optional.ofNullable((User) session.getAttribute("user"));
            if (user.isPresent()) {
                if (user.get().getIsManager()) {
                    ServletContext sc = req.getServletContext();
                    TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(sc);
                    WebContext context = CustomWebContext.generateContext(req, resp, sc);
                    resp.setContentType("text/html;charset=UTF-8");
                    engine.process("dashboard", context, resp.getWriter());
                } else
                    resp.sendRedirect(req.getContextPath() + "/search");
            } else {
                resp.sendRedirect(req.getContextPath() + "/auth");
            }
        }
    }
}
