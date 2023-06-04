package com.example.springwebtask.Entity;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserData {
    Integer id;
    String loginId;
    String passWord;
    String name;
    Integer role;

    Timestamp created_at;
    Timestamp update_at;
}
