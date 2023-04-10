package com.google.hemmah.domain.model;


public class WalkthroughItem {
    private int imageResourceId;
    private String title;
    private String description;

    public WalkthroughItem(int imageResourceId, String title, String description) {
        this.imageResourceId = imageResourceId;
        this.title = title;
        this.description = description;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
