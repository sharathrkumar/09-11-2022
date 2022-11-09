package com.virtuallearn.Authentication.AdminPanel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategory
{
    private Integer categoryId;
    private Integer subCategoryId;
    private String subCategoryName;
}
