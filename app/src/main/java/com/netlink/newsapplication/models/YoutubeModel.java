package com.netlink.newsapplication.models;

import com.google.gson.annotations.SerializedName;
public class YoutubeModel {
    @SerializedName("snippet")
    private Snippet snippet;

    public Snippet getSnippet() {
        return snippet;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }

    public class Snippet {
        @SerializedName("thumbnails")
        private Thumbnails thumbnails;
        @SerializedName("title")
        private String title;

        @SerializedName("resourceId")
        private ResourceId resourceId;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public ResourceId getResourceId() {
            return resourceId;
        }

        public void setResourceId(ResourceId resourceId) {this.resourceId = resourceId;}

        public Thumbnails getThumbnails() {return thumbnails;}

        public void setThumbnails(Thumbnails thumbnails) {this.thumbnails = thumbnails;}
    }

    public class ResourceId {
        @SerializedName("videoId")
        private String videoId;

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }
    }
    public class Thumbnails {
        @SerializedName("high")
        private Thumbnail high;

        public Thumbnail getHigh() {
            return high;
        }

        public void setHigh(Thumbnail high) {
            this.high = high;
        }
    }

    public class Thumbnail {
        @SerializedName("url")
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
