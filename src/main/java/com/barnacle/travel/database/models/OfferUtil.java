package com.barnacle.travel.database.models;

import com.barnacle.travel.util.Formatter;

public class OfferUtil {
    private double discount;
    private long until;
    private Flight flight;

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public long getUntil() {
        return until;
    }

    public void setUntil(long until) {
        this.until = until;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public String finalAmt() {
        return Formatter.formatAsCurrency((1 - discount / 100) * flight.getAmt());
    }
}
