package com.example.android.sportnewsapp;

public class News {
    //state
    private String mSection;
    private String mTitle;
    private String mUrl;
    private String mThumbnail;

    //Constructor to create 3 object instances
    public News(String section, String title, String url, String thumbnail) {
        mSection = section;
        mTitle = title;
        mUrl = url;
        mThumbnail = thumbnail;
    }

    //Methods
    public String getSection() {
        return mSection;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getThumbnail() {
        return mThumbnail;
    }
}
