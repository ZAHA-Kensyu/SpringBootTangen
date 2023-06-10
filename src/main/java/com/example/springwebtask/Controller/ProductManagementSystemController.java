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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

        var loginLists = pmsService.loginCheck(loginData);

        if(!loginLists.isEmpty()){
            //セッションIDを保存
            httpSession.setAttribute("userSession",loginLists);
            return "redirect:/menu";
        }else{
            model.addAttribute("isLoginCheck",true);
            return "/index";
        }
    }

    @GetMapping("/menu")
    String menu(@RequestParam(value="keyword",required = false)String keyword, Model model,HttpSession session){
        if(session.getAttribute("userSession") == null){
            return "redirect:/index";
        }

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
    String postProductInsert(@Validated @ModelAttribute FormData formData, BindingResult bindingResult,Model model){

        var categoryLists = pmsService.getCategory();
        model.addAttribute("categoryLists",categoryLists);

        if( pmsService.productIdCheck(formData.getProductId()) != null){
            model.addAttribute("isProductIdCheck",true);
            return "/insert";
        }

        if(bindingResult.hasErrors()){
            return "/insert";
        }else {
            pmsService.insert(formData);
            return "redirect:/menu";
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
    String getProductUpdate(@ModelAttribute FormData formData,@PathVariable int id,Model model,HttpSession session){
        if(session.getAttribute("userSession") == null){
            return "redirect:/index";
        }

        var detailData = pmsService.findById(id);//詳細画面のデータ取得
        model.addAttribute("detailData",detailData);
        formData.setProductId(detailData.getProductId());
        formData.setName(detailData.getName());
        formData.setPrice(detailData.getPrice());
        formData.setDescription(detailData.getDescription());
        model.addAttribute("formData",formData);
        //カテゴリ表示
        var category = pmsService.getCategory();//カテゴリ取得
        model.addAttribute("categoryLists",category);

        //POSTに飛ばす時にidを渡す。
        model.addAttribute("id",id);

        return "updateInput";
    }

    @PostMapping("updateInput/{id}")
    String postProductUpdate(RedirectAttributes redirectAttributes, @Validated @ModelAttribute FormData formData, BindingResult bindingResult, @PathVariable int id, Model model){

        System.out.println("入力された" + formData.getProductId());

        System.out.println("POST送信受け取り");
        var detailData = pmsService.findById(id);//詳細画面のデータ取得
        //ここに移動
        var category = pmsService.getCategory();//カテゴリ取得
        model.addAttribute("categoryLists", category);
        model.addAttribute("detailData", detailData);

        //詳細データのIDと入力されたIDが同じじゃない場合重複しているか確認
        if(!detailData.getProductId().equals(formData.getProductId())) {
            //入力されたIDが重複していないか調べる。
            System.out.println("重複チェック。");
            if (pmsService.productIdCheck(formData.getProductId()) != null) {
                model.addAttribute("isProductIdCheck", true);
                System.out.println("エラー 重複項目がありました。");
                return "/updateInput";
            }
        }

        if(bindingResult.hasErrors()){
            System.out.println("更新エラー。");
            for (var error : bindingResult.getFieldErrors()) {
                System.out.println("フィールド: " + error.getField());
                System.out.println(error.getDefaultMessage());
            }
            return "/updateInput";
        }
        else{
            System.out.println("更新しました。");
            //商品更新
            pmsService.update(formData,id);
            redirectAttributes.addFlashAttribute("message","更新が成功しました。");
            return "redirect:/menu";
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
