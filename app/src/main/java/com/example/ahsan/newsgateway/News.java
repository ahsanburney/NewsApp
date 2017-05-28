package com.example.ahsan.newsgateway;
import java.io.Serializable;

public class News implements Serializable {

    private static final String TAG = "news";
    private String id;
    private String name;
    private String url;
    private String category;

    public News(String id, String name, String url, String category) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.category = category;

    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getUrl() {
        return url;
    }
    public String getCategory() {
        return category;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
