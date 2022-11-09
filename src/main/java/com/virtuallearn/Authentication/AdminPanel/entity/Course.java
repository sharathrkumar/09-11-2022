package com.virtuallearn.Authentication.AdminPanel.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Course
{
    private int courseId;
    private String coursePhoto;
    private String courseName;
    private String previewVideo;
    private int categoryId;
    private int subCategoryId;
    private String courseDuration;

    public Course(int courseId, String coursePhoto, String courseName, String previewVideo, int categoryId, int subCategoryId)
    {
        this.courseId = courseId;
        this.coursePhoto = coursePhoto;
        this.courseName = courseName;
        this.previewVideo = previewVideo;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
    }

    public Course(String courseDuration)
    {
        this.courseDuration = courseDuration;
    }

    public Course(String coursePhoto, String courseName) {
        this.coursePhoto = coursePhoto;
        this.courseName = courseName;
    }
    public Course(Integer courseId) {
        this.courseId = courseId;}
}
