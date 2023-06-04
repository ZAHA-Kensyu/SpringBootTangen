package com.example.springwebtask.Entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DetailData {
    int id;
    @NotEmpty
    @Size(max = 20)
    String productId;

    @NotEmpty
    @Size(max = 255)
    //商品名
    String name;

    @NotEmpty
    @Size(max = 7)
    //商品単価
    String price;

    @NotEmpty
    //商品カテゴリ
    String categoryName;

    @Size(max = 2000)
    //商品テキスト
    String description;
}
