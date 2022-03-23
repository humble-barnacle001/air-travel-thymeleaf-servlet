package com.barnacle.travel.util;

import java.util.Arrays;

import com.barnacle.travel.database.models.Offer;
import com.barnacle.travel.database.models.OfferUtil;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UnwindOptions;

import org.bson.types.ObjectId;

public class DataFetcher {
    public static AggregateIterable<OfferUtil> fetchAllOffers(MongoDatabase db) {
        MongoCollection<Offer> offerCollection = db.getCollection("offers", Offer.class);
        return offerCollection.aggregate(
                Arrays.asList(
                        Aggregates.lookup("flights", "flightID", "_id", "flight"),
                        Aggregates.unwind(
                                "$flight",
                                new UnwindOptions().preserveNullAndEmptyArrays(true))),
                OfferUtil.class);
    }

    public static OfferUtil fetchOfferById(MongoDatabase db, ObjectId id) {
        MongoCollection<Offer> offerCollection = db.getCollection("offers", Offer.class);
        return offerCollection.aggregate(
                Arrays.asList(
                        Aggregates.match(Filters.eq("_id", id)),
                        Aggregates.lookup("flights", "flightID", "_id", "flight"),
                        Aggregates.unwind(
                                "$flight",
                                new UnwindOptions().preserveNullAndEmptyArrays(true))),
                OfferUtil.class)
                .first();
    }
}
