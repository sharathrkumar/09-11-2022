package com.virtuallearn.Authentication.AdminPanel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Overview
{
   private int courseId;
   private String courseTagLine;
   private String description;
   private int chapterCount;
   private int lessonCount;
   private int courseMaterialId;
   private int  testCount;
   private String  learningOutCome;
   private String requirements;
   private int   instructorId;
   private String difficultyLevel;

    public Overview(int courseId, String courseTagLine, String description, String learningOutCome, String requirements, int instructorId, String difficultyLevel)
    {
        this.courseId = courseId;
        this.courseTagLine = courseTagLine;
        this.description = description;
        this.learningOutCome = learningOutCome;
        this.requirements = requirements;
        this.instructorId = instructorId;
        this.difficultyLevel = difficultyLevel;
    }
}
