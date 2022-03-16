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

@WebFilter({ "/login", "/signup", "/auth" })
public class NotLoggedFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();

        Optional<Boolean> isLoggedIn = Optional.ofNullable((Boolean) session.getAttribute("isLoggedIn"));
        if (isLoggedIn.orElse(false)) {
            resp.setHeader("Refresh", "5; URL=" + req.getContextPath() + "/dashboard");
            resp.sendError(
                    HttpServletResponse.SC_CONFLICT,
                    "Already logged in! Redirecting to dashboard in 5 seconds!!");
        } else {
            session.invalidate();
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}
