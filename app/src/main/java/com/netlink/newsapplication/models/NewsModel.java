package com.netlink.newsapplication.models;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsModel {
    @SerializedName("id")
    private int id;

    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }

    @SerializedName("categories")
    private List<Integer> categories;

    @SerializedName("title")
    private Title title;

    @SerializedName("content")
    private Content content;


    @SerializedName("yoast_head_json")
    private YoastHeadJson yoastHeadJson;

    @SerializedName("excerpt")
    private Excerpt excerpt;



    public void setDate(String date) {
        this.date = date;
    }



    public String getDate() {
        return date;
    }



    @SerializedName("date")
    private String date;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @SerializedName("link")
    private String link;
    // Add getters for the fields

    public int getId() {
        return id;
    }
    public YoastHeadJson getYoastHeadJson() {return yoastHeadJson;}
    public Title getTitle() {
        return title;
    }

    public Content getContent() {
        return content;
    }
    public Excerpt getExcerpt() {
        return excerpt;
    }

    //All setters for the fields
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public void setYoastHeadJson(YoastHeadJson yoastHeadJson) {
        this.yoastHeadJson = yoastHeadJson;
    }

    public void setExcerpt(Excerpt excerpt) {
        this.excerpt = excerpt;
    }

    // Inner classes for title and content
    public static class Title {
        public void setRendered(String rendered) {
            this.rendered = rendered;
        }

        @SerializedName("rendered")
        private String rendered;

        public String getRendered() {
            return rendered;
        }


    }

    public static class Content {
        public void setRendered(String rendered) {
            this.rendered = rendered;
        }

        @SerializedName("rendered")
        private String rendered;

        public String getRendered() {
            return rendered;
        }
    }
    public class Excerpt {
        public void setRendered(String rendered) {
            this.rendered = rendered;
        }

        @SerializedName("rendered")
        private String rendered;

        public String getRendered() {
            return rendered;
        }
    }
    public static class YoastHeadJson {
        public void setOgImage(List<OgImage> ogImage) {
            this.ogImage = ogImage;
        }

        @SerializedName("og_image")
        private List<OgImage> ogImage;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        @SerializedName("author")
        private String author;
        public List<OgImage> getOgImage() {
            return ogImage;
        }
    }

    public static class OgImage {
        public void setUrl(String url) {
            this.url = url;
        }

        @SerializedName("url")
        private String url;

        public String getUrl() {
            return url;
        }
    }
}
