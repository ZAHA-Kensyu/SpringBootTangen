package com.example.springwebtask.Controller;

import com.example.springwebtask.Entity.FormData;
import com.example.springwebtask.Entity.LoginData;
import com.example.springwebtask.Service.PMSService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductManagementSystemController {

    @Autowired
    PMSService pmsService;

    @Autowired
    private HttpSession httpSession;

    @GetMapping("/index")
    String login(@ModelAttribute("loginData") LoginData loginData){
        System.out.println("accessしました");
        return "index";
    }

    @PostMapping("/index")
    String loginCheck(@Validated @ModelAttribute("loginData") LoginData loginData, BindingResult bindingResult,Model model){
        if(bindingResult.hasErrors()) {
            return "index";
        }

        System.out.println("indexにアクセスしました。");
        System.out.println("Id"+loginData.getId());
        System.out.println("PassWord"+loginData.getPassWord());
        var loginLists = pmsService.loginCheck(loginData);

        for(var loginList : loginLists){
            System.out.println("ログインチェックID"+loginList.getLoginId());
            System.out.println("ログインチェックloginID"+loginList.getLoginId());
            System.out.println("ログインチェックpassWord"+loginList.getPassWord());
            System.out.println("ログインチェックname"+loginList.getName());
            System.out.println("ログインチェックrole"+loginList.getRole());
            System.out.println("ログインチェックcreated_at"+loginList.getCreated_at());
            System.out.println("ログインチェックupdate_at"+loginList.getUpdate_at());
        }

        if(!loginLists.isEmpty()){
            //セッションIDを保存
            httpSession.setAttribute("userSession",loginLists);
            System.out.println("session "+httpSession.getAttribute("userSession"));
            return "redirect:/menu";
        }else{
            System.out.println("ログインできません。");
            model.addAttribute("isLoginCheck",true);
            return "/index";
        }
    }

    @GetMapping("/menu")
    String menu(@RequestParam(value="keyword",required = false)String keyword, Model model,HttpSession session){
        if(session.getAttribute("userSession") == null){
            return "redirect:/index";
        }
        System.out.println("menuにアクセスしました");
        if(keyword == null){
            System.out.println("何もパラメーター入っていません");
            var menuDataList =  pmsService.getMenuData();
            model.addAttribute("menuDataList",menuDataList);
            model.addAttribute("number",menuDataList.size());
        }else{
            System.out.println("パラメーター" + keyword);
            var searchDataList = pmsService.getSearchData(keyword);
            model.addAttribute("menuDataList",searchDataList);
            model.addAttribute("number",searchDataList.size());
        }
        return "menu";
    }

    @PostMapping("/insert")
    String postProductInsert(@Validated @ModelAttribute FormData formData, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println("エラー");
            return "/insert";
        }else {
            System.out.println("postInsertにアクセスしました");
            pmsService.insert(formData);
            return "/menu";
        }
    }
    @GetMapping("/insert")
    String getProductInsert(@ModelAttribute FormData formData,Model model,HttpSession session){
        if(session.getAttribute("userSession") == null){
            return "redirect:/index";
        }
        System.out.println("insertにアクセスしました");
        var categoryLists = pmsService.getCategory();
        model.addAttribute("categoryLists",categoryLists);
        return "insert";
    }

    @GetMapping("updateInput/{id}")
    String getProductUpdate(@PathVariable int id,Model model,HttpSession session){
        if(session.getAttribute("userSession") == null){
            return "redirect:/index";
        }

        var detailData = pmsService.findById(id);
        if(detailData != null){
            System.out.println("idをupdateに送ります。"+id);
            model.addAttribute("id",id);
            model.addAttribute("detailData",detailData);
        }else{
            model.addAttribute("id",id);
        }
        return "updateInput";
    }

    @PostMapping("updateInput/{id}")
    String postProductUpdate(@RequestBody FormData formData,@PathVariable int id){
        System.out.println("postUpdate");
        var number = pmsService.update(formData,id);
        if(number!=0){
            System.out.println("成功した");
            return "menu";
        }else{
            return "updateInput";
        }

    }

    @GetMapping("detail/{id}")
    String data(@PathVariable int id,Model model){

        var detailData = pmsService.findById(id);
        System.out.println(id);
        if(detailData != null) {
            detailData.setId(id);
            model.addAttribute("detailData",detailData);
        }
        return "detail";
    }

    @DeleteMapping("delete/{productId}")
    String delete(@PathVariable String productId){
        System.out.println("deleteリクエスト 削除ID"+productId);
        pmsService.delete(productId);
        return "menu";
    }

    @GetMapping("/logout")
    String logout(HttpSession session){
        session.invalidate();
        return "logout";
    }
}
