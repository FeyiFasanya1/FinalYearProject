package com.example.app.Domain;

public class CategoryDomain {
    private String title;
    private String CategoryId;
    private String picUrl;

    public CategoryDomain(String title, String CategoryId, String picUrl) {
        this.title = title;
        this.CategoryId = CategoryId;
        this.picUrl = picUrl;
    }

    public CategoryDomain() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String CategoryId) {
        this.CategoryId = CategoryId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
