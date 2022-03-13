package com.barnacle.travel.database;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.conversions.Bson;

@WebListener
public class Connection implements ServletContextListener {

    private static final String MONGO_STRING;
    private static MongoClient client;
    private static MongoDatabase db;

    static {
        MONGO_STRING = System.getenv("MONGO_URI");
        if (MONGO_STRING == null) {
            System.out.println("MongoDB Connection String is not assigned");
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        client = MongoClients.create(MONGO_STRING);
        db = client.getDatabase("y_3_s_e_it_a3");
        try {
            Bson command = new BsonDocument("ping", new BsonInt64(1));
            db.runCommand(command);
            System.out.println("Connected to MongoDB database: " + db.getName());
            ConnectionUtil.setDatabase(db);
        } catch (MongoException me) {
            System.err.println("An error occurred while attempting to run a command: " + me);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        client.close();

        System.out.println("Destroyed connection to MongoDB Server");
    }
}
