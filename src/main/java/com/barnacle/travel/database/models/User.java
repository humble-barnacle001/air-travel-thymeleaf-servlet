package com.barnacle.travel.database.models;

import org.bson.types.ObjectId;

public class User {
    private ObjectId id;
    private String email;
    private String name;
    private String password;
    private long since;
    private boolean isManager;

    public ObjectId getId() {
        return id;
    }

    public User setId(ObjectId id) {
        this.id = id;
        return this;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setSince(long since) {
        this.since = since;
        return this;
    }

    public long getSince() {
        return since;
    }

    public User setIsManager(boolean ismanager) {
        this.isManager = ismanager;
        return this;
    }

    public boolean getIsManager() {
        return isManager;
    }

    @Override
    public String toString() {
        return "Name: " + name +
                "\nEmail: " + email +
                "\nPassword: " + password +
                "\nSince: " + since +
                "\nIS MANAGER: " + isManager;
    }

}
