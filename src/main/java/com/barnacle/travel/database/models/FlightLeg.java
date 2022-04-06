package com.barnacle.travel.database.models;

public class FlightLeg extends Flight implements Comparable<FlightLeg> {
    private int depth;

    public int getDepth() {
        return depth;
    }

    public FlightLeg setDepth(int depth) {
        this.depth = depth;
        return this;
    }

    @Override
    public int compareTo(FlightLeg o) {
        return this.depth - o.depth;
    }

    @Override
    public String toString() {
        return "{Company: " + getCompany() +
                ", From: " + getFrom() +
                ", To: " + getTo() +
                ", Depth: " + depth +
                ", Initial Amount: " + getAmt() +
                "}";
    }
}
