package com.example.auth1;
import java.io.Serializable;

public class Product extends Offer implements Serializable {
    private String productId;
    private String productName;
    private String productDescription;
    private String contactInfo;
    private String imageUrl;
    private String userId;

    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(Product.class)
    }

    public Product(String productId, String productName, String productDescription, String contactInfo, String imageUrl, String userId) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.contactInfo = contactInfo;
        this.imageUrl = imageUrl;
        this.userId = userId;
    }

    public Product(String productId, String productName, String productDescription, String contactInfo, String imageUrl, String uid, String selectedCurrency) {
    }


    @Override
    public String getName() {
        return productName;
    }

    @Override
    public String getDescription() {
        return productDescription;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
