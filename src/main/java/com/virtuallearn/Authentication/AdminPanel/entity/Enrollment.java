package com.virtuallearn.Authentication.AdminPanel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enrollment
{
    private String userName;
    private int courseId;
    private Date joinDate;
    private Date completedDate;
    private int courseScore;

    public Enrollment(int courseId)
    {
        this.courseId = courseId;
    }
}
