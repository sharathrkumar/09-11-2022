package com.virtuallearn.Authentication.AdminPanel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chapter
{
    private Integer chapterId;
    private Integer courseId;
    private Integer chapterNumber;
    private String chapterName;
    private Boolean chapterCompletedStatus;
    private Integer chapterTestPercentage;
    private String chapterDuration;

    public Chapter(String chapterDuration)
    {
        this.chapterDuration = chapterDuration;
    }
}
