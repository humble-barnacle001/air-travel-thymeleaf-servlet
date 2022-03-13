package com.barnacle.travel.web;

import com.barnacle.travel.config.TemplateEngineUtil;
import com.barnacle.travel.database.ConnectionUtil;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

import org.bson.Document;
import org.javatuples.Quartet;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

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
        MongoDatabase db = ConnectionUtil.getDatabase();
        MongoCollection<Document> collection = db.getCollection("offers");
        long currentTime = new Date().getTime();

        MongoIterable<Quartet<String, String, Boolean, String>> t = collection.find()
                .map(doc -> new Quartet<String, String, Boolean, String>(
                        (String) doc.get("from"), (String) doc.get("to"),
                        (Boolean) ((Long) doc.get("until") >= currentTime), (String) doc.get("amt")));

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        WebContext context = new WebContext(request, response, request.getServletContext());
        context.setVariable("offers", t);
        context.setVariable("date", Calendar.getInstance().get(Calendar.YEAR));
        response.setContentType("text/html;charset=UTF-8");
        engine.process("index.html", context, response.getWriter());
    }

}
