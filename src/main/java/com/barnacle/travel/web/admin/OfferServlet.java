package com.barnacle.travel.web.admin;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.barnacle.travel.config.TemplateEngineUtil;
import com.barnacle.travel.database.models.Flight;
import com.barnacle.travel.database.models.Offer;
import com.barnacle.travel.database.models.OfferUtil;
import com.barnacle.travel.util.DataFetcher;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;

import org.bson.types.ObjectId;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@WebServlet("/offer")
public class OfferServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext sc = req.getServletContext();

        String flightIDString = req.getParameter("flightID");
        String discountString = req.getParameter("discount");
        String untilString = req.getParameter("until");

        try {
            ObjectId flightID = new ObjectId(flightIDString);
            double discount = Double.parseDouble(discountString);
            long until = new SimpleDateFormat("dd/MM/yyyy").parse(untilString).getTime();
            MongoDatabase db = (MongoDatabase) sc.getAttribute("db");
            MongoCollection<Flight> collection = db.getCollection("flights", Flight.class);
            MongoCollection<Offer> offerCollection = db.getCollection("offers", Offer.class);
            long count = collection.countDocuments(Filters.eq("_id", flightID));
            if (count == 1) {
                InsertOneResult offer = offerCollection.insertOne(
                        new Offer()
                                .setFlightID(flightID)
                                .setDiscount(discount)
                                .setUntil(until));
                if (offer.wasAcknowledged()) {
                    OfferUtil oUtil = DataFetcher.fetchOfferById(
                            db,
                            offer.getInsertedId().asObjectId().getValue());
                    TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(sc);
                    WebContext context = new WebContext(req, resp, sc);
                    context.setVariable("offer", oUtil);
                    context.setVariable("isAdmin", Boolean.TRUE);

                    resp.setContentType("text/html;charset=UTF-8");
                    engine.process("newoffer", context, resp.getWriter());
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occured while POST in: " + this.getClass());
            System.err.println(e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext sc = req.getServletContext();

        String offerIdString = req.getParameter("id");

        try {
            ObjectId offerId = new ObjectId(offerIdString);
            MongoDatabase db = (MongoDatabase) sc.getAttribute("db");
            MongoCollection<Offer> offerCollection = db.getCollection("offers", Offer.class);

            DeleteResult oldOffer = offerCollection.deleteOne(Filters.eq("_id", offerId));
            if (oldOffer.wasAcknowledged()) {
                resp.sendError(HttpServletResponse.SC_NO_CONTENT);
            }
        } catch (Exception e) {
            System.out.println("Exception occured while DELETE in: " + this.getClass());
            System.err.println(e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
