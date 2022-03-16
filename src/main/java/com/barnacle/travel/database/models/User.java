package com.barnacle.travel.database.models;

import org.bson.types.ObjectId;

public class User {
    private ObjectId id;
    private String email;
    private String name;
    private String password;
    private long since;
    private boolean isManager;

    public User() {
    }

    public User(String email, String name, String password, long currentTime, boolean ismanager) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.since = currentTime;
        this.isManager = ismanager;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setSince(long since) {
        this.since = since;
    }

    public long getSince() {
        return since;
    }

    public void setIsManager(boolean ismanager) {
        this.isManager = ismanager;
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
