package com.example.hiennguyen.firebaseexample.model;

public class User {

    protected String key;
    protected String gcm_id;

    public User() {
    }

    public User(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getGcm_id() {
        return gcm_id;
    }

    public void setGcm_id(String gcm_id) {
        this.gcm_id = gcm_id;
    }
}
