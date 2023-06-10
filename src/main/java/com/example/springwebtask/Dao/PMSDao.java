package com.example.springwebtask.Dao;

import com.example.springwebtask.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PMSDao implements ProductManagementSystemDao{
    @Autowired
    //private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Override
    public List<UserData> loginCheck(LoginData loginData){
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id",loginData.getId());
        param.addValue("passWord",loginData.getPassWord());
        return namedParameterJdbcTemplate.query("SELECT * FROM users WHERE login_id = :id and password = :passWord",param,new DataClassRowMapper<>(UserData.class));
    }

    @Override
    public int insert(FormData formData){
        Integer price = Integer.parseInt(formData.getPrice());
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("product_id", formData.getProductId());
        param.addValue("category_id", formData.getCategoryId());
        param.addValue("name", formData.getName());
        param.addValue("price",price);
        param.addValue("description", formData.getDescription());
        return namedParameterJdbcTemplate.update("INSERT INTO products (product_id,category_id,name,price,description) VALUES (:product_id,:category_id,:name,:price,:description)",param);
    }

    @Override
    public ProductData productIdCheck(String productId){
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("product_id", productId);
        var list = namedParameterJdbcTemplate.query("SELECT * FROM products WHERE product_id = :product_id",param,new DataClassRowMapper<>(ProductData.class));
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Categories> getCategory(){
        var categoriesList = namedParameterJdbcTemplate.query("SELECT * FROM categories",new DataClassRowMapper<>(Categories.class));
        return categoriesList;
    }

    @Override
    public int update(FormData formData,int id){
        MapSqlParameterSource param = new MapSqlParameterSource();
        var price = Integer.parseInt(formData.getPrice());
        param.addValue("product_id", formData.getProductId());
        param.addValue("category_id", formData.getCategoryId());
        param.addValue("name", formData.getName());
        param.addValue("price",price);
        param.addValue("description", formData.getDescription());
        param.addValue("id",id);

        return namedParameterJdbcTemplate.update("update products SET product_id = :product_id, category_id = :category_id ,name = :name,price = :price,description = :description WHERE id = :id ",param);
    }

    @Override
    public List<MenuData> getMenuData(){
        var menuDataList = namedParameterJdbcTemplate.query(
                "SELECT product.id,product.product_id,product.name,product.price,category.name AS category_name" +
                " FROM products product" +
                " INNER JOIN categories category" +
                " ON product.category_id = category.id",new DataClassRowMapper<>(MenuData.class));
        return menuDataList;
    }

    @Override
    public List<MenuData> getSearchData(String keyword){
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("keyword",keyword);
        var searchDataList = namedParameterJdbcTemplate.query(
                "SELECT product.id,product.product_id,product.name,product.price,category.name AS category_name" +
                " FROM products product" +
                " INNER JOIN categories category" +
                " ON product.category_id = category.id" +
                " WHERE product.name LIKE '%' || :keyword || '%'",param,new DataClassRowMapper<>(MenuData.class));
        return searchDataList;
    }

    @Override
    public DetailData findById(int id){
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id",id);
        var data = namedParameterJdbcTemplate.query(
                "SELECT product.product_id,product.name,product.price,category.name AS category_name,product.description" +
                " FROM products product" +
                " INNER JOIN categories category" +
                " ON product.category_id = category.id" +
                " WHERE product.id = :id",param,new DataClassRowMapper<>(DetailData.class));
        return data.isEmpty() ? null : data.get(0);
    }

    @Override
    public int delete(String productId){
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("productId",productId);

        return namedParameterJdbcTemplate.update("DELETE FROM products"+ " WHERE product_id = :productId",param);
    }

}
