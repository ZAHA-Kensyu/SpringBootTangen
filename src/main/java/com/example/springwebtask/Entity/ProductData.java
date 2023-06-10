package com.example.springwebtask.Entity;
import java.sql.Timestamp;

public class ProductData {
    int id;
    String productId;
    int categoryId;
    String name;
    Integer price;
    String imagePath;

    String description;

    Timestamp createdAt;
    Timestamp updateAt;
}
