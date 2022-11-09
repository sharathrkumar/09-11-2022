package com.virtuallearn.Authentication.AdminPanel.controller;

import com.virtuallearn.Authentication.AdminPanel.entity.*;
import com.virtuallearn.Authentication.AdminPanel.model.Policy;
import com.virtuallearn.Authentication.AdminPanel.request.CourseRequest;
import com.virtuallearn.Authentication.AdminPanel.request.InstructorRequest;
import com.virtuallearn.Authentication.AdminPanel.request.LessonRequest;
import com.virtuallearn.Authentication.AdminPanel.request.TestRequest;
import com.virtuallearn.Authentication.AdminPanel.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@RestController
public class AdminController
{
    @Autowired
    private AdminService adminService;

    @PostMapping("/course")
    public ResponseEntity<String> saveCourse(@ModelAttribute CourseRequest courseRequest) throws IOException
    {
        String courseResponse = adminService.addCourse(courseRequest);
        if(courseResponse == null)
        {
            return new ResponseEntity<String>("course addition is unsuccessful", HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<String>(courseResponse, HttpStatus.OK);
    }

    @PostMapping("/lesson")
    public ResponseEntity<String> saveLesson(@ModelAttribute LessonRequest lessonRequest) throws IOException, ParseException {
        String lessonResponse= adminService.addLesson(lessonRequest);
        if(lessonResponse == null)
        {
            return new ResponseEntity<String>("lesson addition is unsuccessful", HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>(lessonResponse, HttpStatus.OK);
    }

    @PostMapping("/test")
    public ResponseEntity<String> saveTest(@RequestBody TestRequest testRequest)
    {
        String testResponse = adminService.addTest(testRequest);
        if(testResponse == null)
        {
            return new ResponseEntity<String>("test addition is unsuccessful", HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>(testResponse,HttpStatus.OK);
    }

    @PostMapping("/question")
    public ResponseEntity<String> saveQuestion(@RequestBody Question question)
    {
        String questionResponse = adminService.addQuestion(question);
        if(questionResponse == null)
        {
            return new ResponseEntity<String>("question addition is unsuccessful", HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>(questionResponse,HttpStatus.OK);
    }
    @PostMapping("/instructor")
    public ResponseEntity<String> saveInstructor(@ModelAttribute InstructorRequest instructor) throws IOException {
        String instructorResponse = adminService.addInstructor(instructor);
        if(instructorResponse == null)
        {
            return new ResponseEntity<String>("instructor addition is unsuccessful", HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>(instructorResponse,HttpStatus.OK);
    }




//    ************************************Sharath*****************************


    @PostMapping("/Category")
    public ResponseEntity<String> addCategory(@RequestBody Category category)
    {
        int change = adminService.addCategory(category);

        if(change > 0)
            return ResponseEntity.of(Optional.of("Category " + category.getCategoryName()+ " has been Added SuccessFully"));
        else
            return new ResponseEntity<>("Category Type Already Exists",HttpStatus.ALREADY_REPORTED);
    }

    @GetMapping("/Categories")
    public ResponseEntity<List<Category>> getCategories()
    {
        List<Category> categories = adminService.getCategories();

        if((categories) != null)
            return ResponseEntity.of(Optional.of(categories));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/SubCategory")
    public ResponseEntity<String> addSubCategory(@RequestBody SubCategory subcategory)
    {
        int change = adminService.addSubCategory(subcategory);

        if(change > 0)
            return ResponseEntity.of(Optional.of("SubCategory " + subcategory.getSubCategoryName()+ " has been Added SuccessFully"));
        else
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
    }

    @GetMapping("/SubCategories")
    public ResponseEntity<List<SubCategory>> getSubcategory(@RequestBody Category category)
    {
        List<SubCategory> subCategories = adminService.getSubCategories(category);

        if((subCategories) != null)
            return ResponseEntity.of(Optional.of(subCategories));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/Chapter")
    public ResponseEntity<String> addChapter(@RequestBody Chapter chapter)
    {
        int change = adminService.addChapter(chapter);

        if(change > 0)
            return ResponseEntity.of(Optional.of("Chapter " + chapter.getChapterName()+ " has been Added SuccessFully"));
        else
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
    }


    @PostMapping("/Overview")
    public ResponseEntity<String> addOverview(@RequestBody Overview overview)
    {
        if(adminService.addOverView(overview) != 0)
            return ResponseEntity.of(Optional.of("OverView for the Course" + overview.getCourseId() + " has been Added SuccessFully"));
        else
            return new ResponseEntity<>("Overview For the Course "+overview.getCourseId()+"has already is Already Present",HttpStatus.ALREADY_REPORTED);
    }

    @PostMapping("/Policy")
    public ResponseEntity<String> addPolicy(@RequestBody Policy policy){
        if(adminService.addPolicy(policy) != 0){
            return new ResponseEntity<>("Privacy Policy and Terms and Condition Updated",HttpStatus.OK);
        }
        return new ResponseEntity<>("You Didn't changed the Privacy Policy and Terms and Conditions or Failed to Update the Privacy Policy and Terms and Condition",HttpStatus.ALREADY_REPORTED);
    }



}
