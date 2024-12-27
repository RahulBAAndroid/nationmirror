package com.netlink.newsapplication.models;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class YoutubePlaylistResponse {
    @SerializedName("items")
    private List<YoutubeModel> items;

    public List<YoutubeModel> getItems() {
        return items;
    }

    public void setItems(List<YoutubeModel> items) {
        this.items = items;
    }
}
