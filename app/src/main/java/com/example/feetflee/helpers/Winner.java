package com.example.feetflee.helpers;


public class Winner implements Comparable {

    private long time = 0;
    private int score = 0;
    private double lat = 0.0;
    private double lon = 0.0;
    private String name = "";

    public Winner() { }

    public long getTime() {
        return time;
    }

    public Winner setTime(long time) {
        this.time = time;
        return this;
    }

    public int getScore() {
        return score;
    }

    public Winner setScore(int score) {
        this.score = score;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public Winner setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLon() {
        return lon;
    }

    public Winner setLon(double lon) {
        this.lon = lon;
        return this;
    }

    public String getName() {
        return name;
    }

    public Winner setName(String name) {
        this.name = name;
        return this;
    }



    @Override
    public int compareTo(Object o) {
        Winner other = (Winner) o;
        if (this.score> other.score)
            return -1;
        return 1;
    }
}