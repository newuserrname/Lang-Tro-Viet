package com.dmt.thanhtruong.langtroviet.Models;

public class Post {
    private int id, comments, count_view, price, area;
    private String title, description, address, phone, date, images;
    private User user;
    private boolean selfLike;

    public Post() {
        this.id = id;
        this.comments = comments;
        this.count_view = count_view;
        this.price = price;
        this.area = area;
        this.title = title;
        this.description = description;
        this.address = address;
        this.phone = phone;
        this.date = date;
        this.images = images;
        this.user = user;
        this.selfLike = selfLike;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getCount_view() {
        return count_view;
    }

    public void setCount_view(int count_view) {
        this.count_view = count_view;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isSelfLike() {
        return selfLike;
    }

    public void setSelfLike(boolean selfLike) {
        this.selfLike = selfLike;
    }
}
