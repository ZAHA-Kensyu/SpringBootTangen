package com.example.springwebtask.Dao;

import com.example.springwebtask.Entity.*;

import java.util.List;

public interface ProductManagementSystemDao {
    public List<UserData> loginCheck(LoginData loginData);

    DetailData findById(int id);

    int  insert(FormData formData);

    int update(FormData formData,int id);
    List<Categories> getCategory();

    List<MenuData> getMenuData();

    List<MenuData> getSearchData(String keyword);

    int delete(String productId);


    ProductData productIdCheck(String productId);
}
