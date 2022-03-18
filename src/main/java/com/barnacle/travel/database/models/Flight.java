package com.barnacle.travel.database.models;

import com.barnacle.travel.util.Formatter;

import org.bson.types.ObjectId;

public class Flight {
    private ObjectId id;
    private String from;
    private String to;
    private int amt;
    private String company;

    public Flight() {
    }

    public Flight(String from, String to, int amt, String company) {
        this.from = from;
        this.to = to;
        this.amt = amt;
        this.company = company;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public void setAmt(int amt) {
        this.amt = amt;
    }

    public int getAmt() {
        return amt;
    }

    public String formattedAmtString() {
        return Formatter.formatAsCurrency(amt);
    }

    public void setCompany(String company) {
        this.company = company;
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
