package com.barnacle.travel.web;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.barnacle.travel.config.CustomWebContext;
import com.barnacle.travel.config.TemplateEngineUtil;
import com.barnacle.travel.database.models.Flight;
import com.barnacle.travel.util.DataFetcher;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@WebServlet("/dashboard")
public class DashServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ServletContext sc = req.getServletContext();
        MongoDatabase db = (MongoDatabase) sc.getAttribute("db");
        MongoCollection<Flight> collection = db.getCollection("flights", Flight.class);

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(sc);
        WebContext context = CustomWebContext.generateContext(req, resp, sc);
        context.setVariable("flights", collection.find());
        context.setVariable("offers", DataFetcher.fetchAllOffers(db));
        context.setVariable("isAdmin", Boolean.TRUE);

        resp.setContentType("text/html;charset=UTF-8");
        engine.process("dashboard", context, resp.getWriter());
    }

}
