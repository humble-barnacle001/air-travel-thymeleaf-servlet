package com.barnacle.travel.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.barnacle.travel.database.models.User;

@WebServlet("/dashboard")
public class DashServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        Optional<Boolean> isLoggedIn = Optional.ofNullable((Boolean) session.getAttribute("isLoggedIn"));
        if (!isLoggedIn.orElse(false))
            resp.sendRedirect(req.getContextPath() + "/auth");
        else {
            PrintWriter pw = resp.getWriter();
            resp.setContentType("text/html");
            pw.print((((User) session.getAttribute("user")).getIsManager()
                    ? "Admin"
                    : "User") + " ");
            pw.println("DASHBOARD");

            pw.println("<form action='/logOut' method='get'>");
            pw.println("<input type='submit' value='Log Out' />");
            pw.println("</form>");
        }
    }
}
