package com.barnacle.travel.web;

import com.barnacle.travel.config.TemplateEngineUtil;
import com.barnacle.travel.database.models.Offer;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@WebServlet("")
public class IndexServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletContext sc = request.getServletContext();

        MongoDatabase db = (MongoDatabase) sc.getAttribute("db");
        MongoCollection<Offer> collection = db.getCollection("offers", Offer.class);
        MongoIterable<Offer> offerList = collection.find();

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(sc);
        WebContext context = new WebContext(request, response, sc);
        long currentTime = new Date().getTime();

        context.setVariable("offers", offerList);
        context.setVariable("currentTime", currentTime);
        context.setVariable("date", Calendar.getInstance().get(Calendar.YEAR));
        response.setContentType("text/html;charset=UTF-8");
        engine.process("index.html", context, response.getWriter());
    }

}
