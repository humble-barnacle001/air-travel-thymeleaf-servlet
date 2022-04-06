package com.barnacle.travel.database.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.barnacle.travel.util.Formatter;

import org.bson.types.ObjectId;

public class FlightUtil {
    private ObjectId id;
    private String from;
    private String to;
    private int amt;
    private String company;
    private boolean isDirect;
    private List<FlightLeg> legs;

    public FlightUtil() {
        legs = new ArrayList<>();
        this.isDirect = false;
    }

    public ObjectId getId() {
        return id;
    }

    public FlightUtil setId(ObjectId id) {
        this.id = id;
        return this;
    }

    public FlightUtil setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public FlightUtil setTo(String to) {
        this.to = to;
        return this;
    }

    public String getTo() {
        return to;
    }

    public FlightUtil setAmt(int amt) {
        this.amt = amt;
        return this;
    }

    public int getAmt() {
        return amt;
    }

    public String formattedAmtString() {
        return Formatter.formatAsCurrency(
                amt + legs.stream().mapToLong(FlightLeg::getAmt).sum());
    }

    public FlightUtil setCompany(String company) {
        this.company = company;
        return this;
    }

    public String getCompany() {
        return company;
    }

    public boolean isDirect() {
        return isDirect;
    }

    public FlightUtil setAsDirect() {
        this.isDirect = true;
        return this;
    }

    public List<FlightLeg> getLegs() {
        return legs;
    }

    public FlightUtil setLegs(List<FlightLeg> legs) {
        this.legs = new ArrayList<FlightLeg>(legs);
        return this;
    }

    public String formattedTravelPlanString() {
        return from + " to " + legs.stream().sorted().map(FlightLeg::getTo).reduce(to, (f, s) -> s);
    }

    public String viaString() {
        String viaString = legs.size() > 0 ? "Via" : "";
        for (FlightLeg leg : legs.stream().sorted().collect(Collectors.toList())) {
            viaString += "-" + leg.getFrom();
        }
        return viaString;
    }

    @Override
    public FlightUtil clone() {
        return new FlightUtil()
                .setId(id)
                .setAmt(amt)
                .setCompany(company)
                .setFrom(from)
                .setTo(to);
    }

    @Override
    public String toString() {
        return "{Company: " + company +
                "\nFrom: " + from +
                "\nTo: " + to +
                "\nLegs: " + legs.toString() +
                "\nInitial Amount: " + amt +
                "}";
    }
}
