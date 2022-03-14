package com.barnacle.travel.database.models;

import java.text.NumberFormat;
import java.util.Locale;

public class Offer {

    private String from;
    private String to;
    private long until;
    private String amt;

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

    public void setUntil(long until) {
        this.until = until;
    }

    public long getUntil() {
        return until;
    }

    public void setAmt(String amt) {
        try {
            NumberFormat cf = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
            cf.setMaximumFractionDigits(0);
            this.amt = cf.format(Long.valueOf(amt));
        } catch (Exception e) {
            // TODO: Deny value on exception
            System.out.println(e);
            System.out.println(amt);
            System.out.println("CANNOT FORMAT");
            this.amt = amt;
        }
    }

    public String getAmt() {
        return amt;
    }

    @Override
    public String toString() {
        return "";
    }

}
