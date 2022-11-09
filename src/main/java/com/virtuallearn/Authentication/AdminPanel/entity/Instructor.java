package com.virtuallearn.Authentication.AdminPanel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Instructor
{
    private int instructorId;
    private String instructorName;
    private String url;
    private String description;
    private String profilePhoto;

}
