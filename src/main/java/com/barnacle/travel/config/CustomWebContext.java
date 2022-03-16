package com.barnacle.travel.config;

import java.util.Calendar;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.context.WebContext;

public class CustomWebContext {

    public static WebContext generateContext(
            final HttpServletRequest req,
            final HttpServletResponse resp,
            final ServletContext sc) {

        final WebContext context = new WebContext(req, resp, sc);

        context.setVariable("date", Calendar.getInstance().get(Calendar.YEAR));
        context.setVariable("isLoggedIn", req.getSession().getAttribute("isLoggedIn"));

        return context;
    }
}
