package com.example.ahsan.newsgateway;
import java.io.Serializable;

public class NewsDetails implements Serializable{
    private static final String TAG = "newsDetail";
    private String newsAuthor;
    private String newsTitle;
    private String newsDescription;
    private String newsImage;
    private String newsDate;
    private String newsData;

    public NewsDetails(String newsAuthor, String newsTitle, String newsDescription, String newsImage, String newsDate, String newsData) {
        this.newsAuthor = newsAuthor;
        this.newsTitle = newsTitle;
        this.newsImage = newsImage;
        this.newsDescription = newsDescription;
        this.newsDate = newsDate;
        this.newsData = newsData;

    }

    public String getNewsAuthor() {
        return newsAuthor;
    }
    public String getNewsTitle() {
        return newsTitle;
    }
    public String getNewsDescription() {
        return newsDescription;
    }
    public String getNewsImage() {
        return newsImage;
    }
    public String getNewsDate(){return newsDate;}
    public String getNewsData(){return newsData;}

    public void setNewsAuthor(String newsAuthor) {
        this.newsAuthor = newsAuthor;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
    }

    public void setNewsImage(String newsImage) {
        this.newsImage = newsImage;
    }

    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }

    public void setNewsData(String newsData) {
        this.newsData = newsData;
    }
}
