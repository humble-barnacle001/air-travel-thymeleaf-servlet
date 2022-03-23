package com.barnacle.travel.database.models;

import org.bson.types.ObjectId;

public class Offer {

    private ObjectId id;
    private ObjectId flightID;
    private long until;
    private double discount;

    public ObjectId getId() {
        return id;
    }

    public Offer setId(ObjectId id) {
        this.id = id;
        return this;
    }

    public Offer setFlightID(ObjectId flightID) {
        this.flightID = flightID;
        return this;
    }

    public ObjectId getFlightID() {
        return this.flightID;
    }

    public Offer setUntil(long until) {
        this.until = until;
        return this;
    }

    public long getUntil() {
        return until;
    }

    public Offer setDiscount(double discount) {
        this.discount = discount;
        return this;
    }

    public double getDiscount() {
        return this.discount;
    }

    @Override
    public String toString() {
        return "ObjectId: " + flightID +
                "\nUntil: " + until +
                "\nDiscount: " + discount;
    }
}
