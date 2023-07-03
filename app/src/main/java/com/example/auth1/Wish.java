package com.example.auth1;

public class Wish {
    private String id;
    private String wish;
    private String contactInfo;
    private String userId;


    public Wish() {
        // Default constructor required for calls to DataSnapshot.getValue(Wish.class)
    }

    public Wish(String id, String wish, String contactInfo, String userId) {
        this.id = id;
        this.wish = wish;
        this.contactInfo = contactInfo;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public String getWish() {
        return wish;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
