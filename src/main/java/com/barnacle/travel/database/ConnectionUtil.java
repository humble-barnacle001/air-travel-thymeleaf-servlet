package com.barnacle.travel.database;

import com.mongodb.client.MongoDatabase;

public class ConnectionUtil {
    private static MongoDatabase database;

    public static void setDatabase(MongoDatabase db) {
        database = db;
    }

    public static MongoDatabase getDatabase() {
        return database;
    }
}
