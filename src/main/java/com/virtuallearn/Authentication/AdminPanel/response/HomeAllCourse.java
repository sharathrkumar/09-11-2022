package com.virtuallearn.Authentication.AdminPanel.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeAllCourse
{
    private int courseId;
    private String coursePhoto;
    private String courseName;
    private int categoryId;
    private int chapterCount;

    public HomeAllCourse(String coursePhoto, String courseName, int categoryId, int chapterCount)
    {
        this.coursePhoto = coursePhoto;
        this.courseName = courseName;
        this.categoryId = categoryId;
        this.chapterCount = chapterCount;
    }

    
}
