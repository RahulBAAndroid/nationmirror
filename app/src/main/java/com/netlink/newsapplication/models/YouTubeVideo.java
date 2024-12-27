package com.netlink.newsapplication.models;

public class YouTubeVideo {
    private String title;
    private String description;
    private String thumbnailUrl;
    private String videoId;

    public YouTubeVideo(String title, String description, String thumbnailUrl, String videoId) {
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getVideoId() {
        return videoId;
    }
}
