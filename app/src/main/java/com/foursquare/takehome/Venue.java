package com.foursquare.takehome;

import java.util.List;

final public class Venue {
    private int id;
    private String name;
    private long openTime;
    private long closeTime;
    private List<Person> visitors;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOpenTime() {
        return openTime;
    }

    public void setOpenTime(long openTime) {
        this.openTime = openTime;
    }

    public long getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(long closeTime) {
        this.closeTime = closeTime;
    }

    public List<Person> getVisitors() {
        return visitors;
    }

    public void setVisitors(List<Person> visitors) {
        this.visitors = visitors;
    }
}
