package com.netlink.newsapplication.models;

public class VideoModel {
    private String embedded;
    private String title;

    // Default constructor required for Firebase
    public VideoModel() {
    }

    public VideoModel(String embedded, String title) {
        this.embedded = embedded;
        this.title = title;
    }
    public String getEmbedded() {
        return embedded;
    }

    public void setEmbedded(String embedded) {
        this.embedded = embedded;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
