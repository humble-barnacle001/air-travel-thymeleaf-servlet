package com.barnacle.travel.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.barnacle.travel.database.models.Flight;
import com.barnacle.travel.database.models.FlightLeg;
import com.barnacle.travel.database.models.FlightUtil;
import com.barnacle.travel.database.models.Offer;
import com.barnacle.travel.database.models.OfferUtil;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.GraphLookupOptions;
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

    public static List<FlightUtil> searchFlights(MongoDatabase db, String source, String destination) {
        MongoCollection<Flight> collection = db.getCollection("flights", Flight.class);
        AggregateIterable<FlightUtil> allFlights = collection.aggregate(
                Arrays.asList(
                        Aggregates.match(Filters.eq("from", source)),
                        Aggregates.graphLookup(
                                "flights",
                                "$to",
                                "to",
                                "from",
                                "legs",
                                new GraphLookupOptions()
                                        .maxDepth(5)
                                        .depthField("depth")
                                        .restrictSearchWithMatch(
                                                Filters.and(
                                                        Filters.ne("from", destination),
                                                        Filters.ne("to", source))))),
                FlightUtil.class);
        List<FlightUtil> allPaths = new ArrayList<FlightUtil>();
        allFlights.forEach(futil -> allPaths.addAll(DataFetcher.generatePath(futil, destination)));
        // allPaths.forEach(p -> System.out.println(p.viaString()));
        return allPaths;
    }

    private static List<FlightUtil> generatePath(FlightUtil futil, String destination) {
        TreeMap<Integer, List<FlightLeg>> gf = futil.getLegs()
                .stream()
                .collect(Collectors.groupingBy(
                        FlightLeg::getDepth,
                        () -> new TreeMap<>(Collections.reverseOrder()),
                        Collectors.toList()));

        Set<String> matchList = new HashSet<String>(Arrays.asList(new String[] { destination }));
        ArrayList<ArrayList<FlightLeg>> paths = new ArrayList<ArrayList<FlightLeg>>();

        int maxDepth = gf.keySet().stream().reduce(-1, (acc, key) -> {
            return gf.get(key).stream().anyMatch(dn -> dn.getTo().equals(destination)) ? key : acc;
        });

        for (int depth = maxDepth; depth >= 0; depth--) {
            Set<String> matchList2 = new HashSet<String>();
            for (FlightLeg leg : gf.get(depth)) {
                if (matchList.contains(leg.getTo())) {
                    matchList2.add(leg.getFrom());
                    if (depth == maxDepth) {
                        ArrayList<FlightLeg> tpath = new ArrayList<>();
                        tpath.add(leg);
                        paths.add(tpath);

                        // System.out.println(paths);
                    } else {
                        paths.stream()
                                .filter(path -> path.get(maxDepth - leg.getDepth() - 1).getFrom().equals(leg.getTo()))
                                .forEach(path -> path.add(leg));
                    }
                }
            }

            ArrayList<ArrayList<FlightLeg>> tpaths = new ArrayList<ArrayList<FlightLeg>>();
            final int cdepth = depth;
            paths.stream()
                    .filter(path -> path.size() > maxDepth - cdepth + 1)
                    .forEach(path -> {
                        List<FlightLeg> t = path.subList(0, maxDepth - cdepth + 1 - 1);
                        path.subList(maxDepth - cdepth + 1 - 1, path.size())
                                .forEach(leg -> {
                                    ArrayList<FlightLeg> temp = new ArrayList<FlightLeg>(t);
                                    temp.add(leg);
                                    tpaths.add(temp);
                                });
                    });

            paths.addAll(tpaths);
            paths.removeIf(path -> path.size() != maxDepth - cdepth + 1);

            if (matchList2.isEmpty())
                break;
            matchList = new HashSet<String>(matchList2);
            // System.out.println(matchList.toString());
        }

        List<FlightUtil> flightPath = new ArrayList<FlightUtil>();
        paths.forEach(path -> flightPath.add(futil.clone().setLegs(path)));
        return flightPath;
    }

    public static List<FlightUtil> searchDirectFlights(MongoDatabase db, String source,
            String destination) {
        MongoCollection<FlightUtil> collection = db.getCollection("flights", FlightUtil.class);
        FindIterable<FlightUtil> directFlights = collection.find(
                Filters.and(
                        Filters.eq("from", source),
                        Filters.eq("to", destination)));

        List<FlightUtil> allDirectPaths = new ArrayList<FlightUtil>();
        directFlights.forEach(f -> {
            f.setAsDirect();
            allDirectPaths.add(f);
        });

        return allDirectPaths;
    }

}
