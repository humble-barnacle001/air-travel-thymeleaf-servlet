package com.barnacle.travel.database.models;

import com.barnacle.travel.util.Formatter;

import org.bson.types.ObjectId;

public class Flight {
    private ObjectId id;
    private String from;
    private String to;
    private int amt;
    private String company;

    public ObjectId getId() {
        return id;
    }

    public Flight setId(ObjectId id) {
        this.id = id;
        return this;
    }

    public Flight setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public Flight setTo(String to) {
        this.to = to;
        return this;
    }

    public String getTo() {
        return to;
    }

    public Flight setAmt(int amt) {
        this.amt = amt;
        return this;
    }

    public int getAmt() {
        return amt;
    }

    public String formattedAmtString() {
        return Formatter.formatAsCurrency(amt);
    }

    public String formattedTravelPlanString() {
        return from + " to " + to;
    }

    public Flight setCompany(String company) {
        this.company = company;
        return this;
    }

    public String getCompany() {
        return company;
    }

    @Override
    public String toString() {
        return "Company: " + company +
                "\nFrom: " + from +
                "\nTo: " + to +
                "\nInitial Amount: " + amt;
    }
}
