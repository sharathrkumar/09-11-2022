package com.virtuallearn.Authentication.AdminPanel.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonRequest
{
    private int lessonNumber;
    private int courseId;
    private int chapterId;
    private String lessonName;
    private Time lessonDuration;
    private MultipartFile videoLink;
}
