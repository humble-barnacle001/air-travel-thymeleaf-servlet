package com.barnacle.travel.database.models;

import org.bson.types.ObjectId;

public class Offer {

    private ObjectId id;
    private ObjectId flightID;
    private long until;
    private double discount;

    public Offer() {
    }

    public Offer(ObjectId flightID, long until, double discount) {
        this.flightID = flightID;
        this.until = until;
        this.discount = discount;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setFlightID(ObjectId flightID) {
        this.flightID = flightID;
    }

    public ObjectId getFlightID() {
        return this.flightID;
    }

    public void setUntil(long until) {
        this.until = until;
    }

    public long getUntil() {
        return until;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
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
