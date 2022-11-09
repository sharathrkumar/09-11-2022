package com.virtuallearn.Authentication.AdminPanel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category
{
    private Integer categoryId;
    private String categoryName;


    public Category(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
