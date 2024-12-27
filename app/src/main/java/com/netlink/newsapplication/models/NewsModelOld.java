package com.netlink.newsapplication.models;

public class NewsModelOld {
    public NewsModelOld(String category, String content, String imageUrl, long timestamp, String title) {
        this.category = category;
        this.content = content;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
        this.title = title;
    }

    private String category;
    private String content;

    public void setCategory(String category) {
        this.category = category;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String imageUrl;

    public String getCategory() {
        return category;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getTitle() {
        return title;
    }

    private long timestamp; // Unix timestamp
    private String title;

    public NewsModelOld() {
    }
}
