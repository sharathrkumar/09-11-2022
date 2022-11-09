package com.virtuallearn.Authentication.AdminPanel.controller;

import com.virtuallearn.Authentication.AdminPanel.entity.Category;
import com.virtuallearn.Authentication.AdminPanel.entity.Course;
import com.virtuallearn.Authentication.AdminPanel.request.HomeHeaderRequest;
import com.virtuallearn.Authentication.AdminPanel.response.*;
import com.virtuallearn.Authentication.AdminPanel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController
{
    @Autowired
    private UserService userService;

    @GetMapping("/home/course")
    public ResponseEntity<?> homeTopBarData(@RequestBody HomeHeaderRequest homeHeaderRequest)
    {
        List<HomeResponseTopHeader> coursesList= userService.HomePageTopBar(homeHeaderRequest);
        if(coursesList.size() ==0)
        {
            return new ResponseEntity<String>("courses are not available", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(coursesList,HttpStatus.OK);
    }


    @GetMapping("/home/course/all")
    public ResponseEntity<?> homeAllCourses()
    {
        List<HomeAllCourse> allCourses= userService.getAllCourses();
        if(allCourses.size() ==0)
        {
            return new ResponseEntity<String>("courses are not available", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(allCourses,HttpStatus.OK);
    }


    @GetMapping("/home/course/popular")
    public ResponseEntity<?> homePopularCourses()
    {
        List<HomeAllCourse> popularCourses= userService.getPopularCourses();
        if(popularCourses.size() ==0)
        {
            return new ResponseEntity<String>("courses are not available", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(popularCourses,HttpStatus.OK);
    }

    @GetMapping("/home/course/newest")
    public ResponseEntity<?> homeNewestCourses()
    {
        List<HomeAllCourse> newestCourses= userService.getNewCourses();
        if(newestCourses.size() ==0)
        {
            return new ResponseEntity<String>("courses are not available", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(newestCourses,HttpStatus.OK);
    }











//    *************************************Sharath********************************



    @GetMapping("/CourseOverView")
    public ResponseEntity<OverviewResponse> getCourseOverview(@RequestBody Course course) {
        try {
            OverviewResponse overviewResponse = userService.getOverviewOfCourse(course.getCourseId());
            if (overviewResponse != null) {
                return ResponseEntity.of(Optional.of(overviewResponse));
            }
            return new ResponseEntity("Overview For the Course is not Available", HttpStatus.NOT_FOUND);
        }catch (Exception e)
        {
            return new ResponseEntity("Invalid Input", HttpStatus.NOT_FOUND);

        }
    }

    @GetMapping("/BasicCourses")
    public ResponseEntity<List<CourseResponse>> getBeginnerCourses(@RequestBody Category category)
    {
        try {
            List<CourseResponse> courseResponses = userService.getBasicCourses(category.getCategoryId());

            if(courseResponses.isEmpty())
            {
                return new ResponseEntity("Currently No Courses Avaialable in this Category",HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.of(Optional.of(courseResponses));

        }catch (Exception e)
        {
            return new ResponseEntity("Invalid Input",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/AdvanceCourses")
    public ResponseEntity<List<CourseResponse>> getAdvancedCourses(@RequestBody Category category)
    {
        try {
            List<CourseResponse> courseResponses = userService.getAdvanceCourses(category.getCategoryId());

            if(courseResponses.isEmpty())
            {
                return new ResponseEntity("Currently No Courses Avaialable in this Category",HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.of(Optional.of(courseResponses));

        }catch (Exception e)
        {
            return new ResponseEntity("Invalid Input",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/AllCoursesOfCategory")
    public ResponseEntity<List<AllCoursesResponse>> getAllCourses(@RequestBody Category category)
    {
        try {
            List<AllCoursesResponse> allCourseResponses = userService.getAllCoursesOf(category.getCategoryId());

            if(allCourseResponses.isEmpty())
            {
                return new ResponseEntity("Currently No Courses Avaialable in this Category",HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.of(Optional.of(allCourseResponses));

        }catch (Exception e)
        {
            return new ResponseEntity("Invalid Input",HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/privacyPolicy")
    public ResponseEntity<String> getPrivacyPolicy() {
        try {
            String privacyPolicy = userService.getPolicy();
            return ResponseEntity.of(Optional.of(privacyPolicy));
        }catch (Exception e)
        {
            return new ResponseEntity("Privacy Policy Not Found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/TermsAndConditions")
    public ResponseEntity<String> getTermsAndConditions() {
        try {
            String termsAndConditions = userService.getTermsAndConditions();
            return ResponseEntity.of(Optional.of(termsAndConditions));
        }catch (Exception e)
        {
            return new ResponseEntity("Terms and Conditions Not Found", HttpStatus.NOT_FOUND);
        }
    }
}
