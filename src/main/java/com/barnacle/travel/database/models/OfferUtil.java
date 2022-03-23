package com.barnacle.travel.database.models;

import com.barnacle.travel.util.Formatter;

import org.bson.types.ObjectId;

public class OfferUtil {
    private ObjectId id;
    private double discount;
    private long until;
    private Flight flight;

    public ObjectId getId() {
        return id;
    }

    public OfferUtil setId(ObjectId id) {
        this.id = id;
        return this;
    }

    public double getDiscount() {
        return discount;
    }

    public OfferUtil setDiscount(double discount) {
        this.discount = discount;
        return this;
    }

    public long getUntil() {
        return until;
    }

    public OfferUtil setUntil(long until) {
        this.until = until;
        return this;
    }

    public Flight getFlight() {
        return flight;
    }

    public OfferUtil setFlight(Flight flight) {
        this.flight = flight;
        return this;
    }

    public String finalAmt() {
        return Formatter.formatAsCurrency((1 - discount / 100) * flight.getAmt());
    }
}
