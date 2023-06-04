package com.example.springwebtask.Entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Categories {
    Integer id;
    String name;
    Timestamp created_at;
    Timestamp updated_at;
}
