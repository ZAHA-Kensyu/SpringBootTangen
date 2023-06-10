package com.example.springwebtask.Service;

import com.example.springwebtask.Entity.*;

import java.util.List;

public interface ProductManagementSystemService {
    public List<UserData> loginCheck(LoginData loginData);

    DetailData findById(int id);

    int  insert(FormData formData);
    int update(FormData formData,int id);
    int delete(String productId);

    List<Categories> getCategory();

    List<MenuData> getMenuData();

    List<MenuData> getSearchData(String keyword);

    public ProductData productIdCheck(String productId);
}
