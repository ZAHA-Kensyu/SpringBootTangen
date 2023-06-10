package com.example.springwebtask.Service;

import com.example.springwebtask.Dao.PMSDao;
import com.example.springwebtask.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PMSService implements ProductManagementSystemService{
    @Autowired
    PMSDao pmsDao;
    public List<UserData> loginCheck(LoginData loginData){
        return pmsDao.loginCheck(loginData);
    }

    public DetailData findById(int id){return pmsDao.findById(id);}

    @Override
    public int  insert(FormData formData){
        return pmsDao.insert(formData);
    }


    @Override
    public int update(FormData formData,int id){return pmsDao.update(formData,id);}

    public int delete(String productId){return pmsDao.delete(productId);}
    @Override
    public List<Categories> getCategory(){
        return pmsDao.getCategory();
    }

    @Override
    public ProductData productIdCheck(String productId){return pmsDao.productIdCheck(productId);}

    @Override
    public List<MenuData> getMenuData(){return pmsDao.getMenuData();}

    public List<MenuData> getSearchData(String keyword){System.out.println("PMSService"+keyword);return pmsDao.getSearchData(keyword);}
}
