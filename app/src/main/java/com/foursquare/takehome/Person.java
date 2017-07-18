package com.foursquare.takehome;

/**
 *  One change made in setArrivalTime and getArrivalTime methods, which was accepting int as argument
 *  Changed to long for type compatibility
 *
 */
final public class Person {
    private int id;
    private String name;
    private long arriveTime;
    private long leaveTime;

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

    public long getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(long arriveTime) {
        this.arriveTime = arriveTime;
    }

    public long getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(long leaveTime) {
        this.leaveTime = leaveTime;
    }
}
