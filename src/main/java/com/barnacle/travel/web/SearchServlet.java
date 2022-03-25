package com.barnacle.travel.web;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.barnacle.travel.config.CustomWebContext;
import com.barnacle.travel.config.TemplateEngineUtil;
import com.barnacle.travel.database.models.Flight;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext sc = req.getServletContext();
        Optional<String> optionalFrom = Optional.ofNullable(req.getParameter("from"));
        Optional<String> optionalTo = Optional.ofNullable(req.getParameter("to"));
        MongoDatabase db = (MongoDatabase) sc.getAttribute("db");
        MongoCollection<Flight> collection = db.getCollection("flights", Flight.class);

        try {
            TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(sc);
            WebContext context = CustomWebContext.generateContext(req, resp, sc);
            if (optionalFrom.orElse("") != "" && optionalTo.orElse("") != "") {
                String from = optionalFrom.get();
                String to = optionalTo.get();
                context.setVariable("isSearch", Boolean.TRUE);
                context.setVariable("from", from);
                context.setVariable("to", to);

                // TODO: Implement search functionality
                // TODO: Enable via string in flightcard.html
                context.setVariable("flights", collection.find());
            } else
                context.setVariable("isSearch", Boolean.FALSE);
            resp.setContentType("text/html;charset=UTF-8");
            engine.process("search", context, resp.getWriter());
        } catch (Exception e) {
            System.out.println("Exception occured while GET in: " + this.getClass());
            System.err.println(e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
