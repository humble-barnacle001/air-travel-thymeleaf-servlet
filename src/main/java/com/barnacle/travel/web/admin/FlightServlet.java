package com.barnacle.travel.web.admin;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.barnacle.travel.config.TemplateEngineUtil;
import com.barnacle.travel.database.models.Flight;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@WebServlet("/flight")
public class FlightServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext sc = req.getServletContext();

        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String company = req.getParameter("company");
        String amtString = req.getParameter("amt");

        try {
            int amt = Integer.parseInt(amtString);
            MongoDatabase db = (MongoDatabase) sc.getAttribute("db");
            MongoCollection<Flight> collection = db.getCollection("flights", Flight.class);
            Flight newFlight = new Flight()
                    .setFrom(from)
                    .setTo(to)
                    .setCompany(company)
                    .setAmt(amt);
            InsertOneResult newFlightInsertResult = collection.insertOne(newFlight);
            if (newFlightInsertResult.wasAcknowledged()) {
                TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(sc);
                WebContext context = new WebContext(req, resp, sc);
                context.setVariable("flight", newFlight);

                resp.setContentType("text/html;charset=UTF-8");
                engine.process("newflight", context, resp.getWriter());
            }
        } catch (Exception e) {
            System.out.println("Exception occured while POST in: " + this.getClass());
            System.err.println(e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
