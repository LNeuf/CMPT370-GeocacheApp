package com.cmpt370_geocacheapp;

public class ListItem {
    private String id;
    private String itemName;
    private String difficulty;
    private String time;
    private String distance;


    public ListItem(String name, String description, String time, String id, String distance) {
        this.itemName = name;
        this.difficulty = description;
        this.time = time;
        this.id = id;
        this.distance = distance;

    }

    public String getItemName() {
        return this.itemName;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getTime() {
        return this.time;
    }

    public String getID() {
        return this.id;
    }

    public String getDistance() {
        return this.distance;
    }
}
