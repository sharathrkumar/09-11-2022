package com.virtuallearn.Authentication.AdminPanel.service;

import com.virtuallearn.Authentication.AdminPanel.entity.Enrollment;
import com.virtuallearn.Authentication.AdminPanel.entity.User;
import com.virtuallearn.Authentication.AdminPanel.request.HomeHeaderRequest;
import com.virtuallearn.Authentication.AdminPanel.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // for home top header
    private String GET_OCCUPATION = "SELECT occupation FROM user WHERE userName = ?";
    private String HOME_TOP_BAR = "SELECT coursePhoto, courseName FROM course";
    private  String COURSE_EXISTANCE_IN_SUBCATEGORY = "SELECT coursePhoto, courseName FROM course WHERE subCategoryId= ?";
    private String GET_CATEGORY_FROM_SUBCATEGORY = "Select categoryId from subcategory WHERE subcategoryId = ?";
    private String GET_COURSES = "SELECT * FROM course WHERE categoryId = ?";

    // "All" in home page
    private String GET_ALL_COURSES = "SELECT coursePhoto, courseName,categoryId, chapterCount FROM course,overview WHERE course.courseId = overview.courseId";
   //"popular" in home page
    private String GET_ALL_ENROLLED_COURSES = "SELECT distinct courseId FROM enrollment";
    private String GET_ENROLLMENT_COUNT = "SELECT count(courseId) FROM enrollment WHERE courseId= ?";
    private String GET_ALL_POPULAR_COURSES = "SELECT c.coursePhoto, c.courseName,c.categoryId, o.chapterCount FROM course c,overview o WHERE c.courseId=? and c.courseId = o.courseId";



    // "newest" in home
    private String GET_ALL_NEW_COURSES = "SELECT course.courseId, coursePhoto, courseName,categoryId, chapterCount FROM course,overview WHERE course.courseId = overview.courseId";

    //if user occupation is student display all courses, if not, based on subcategory matching occupation display courses of subcategory ,if subcategory is not having any courses then tahke
    // category of the sub category and display courses under that category
    public List<HomeResponseTopHeader> HomePageTopBar(HomeHeaderRequest homeHeaderRequest)   // front end should send username when ever they call home api as a response
    {
        User user= jdbcTemplate.queryForObject(GET_OCCUPATION,(rs, rowNum) -> {
            return new User(rs.getInt("occupation"));
        }, homeHeaderRequest.getUserName());
        System.out.println(user.getOccupation());
        if(user.getOccupation() == 0)
        {
            List<HomeResponseTopHeader> courseListForStudent = jdbcTemplate.query(HOME_TOP_BAR,(rs, rowNum) -> {
                return new HomeResponseTopHeader(rs.getString("coursePhoto"), rs.getString("courseName"));
            });
            return courseListForStudent;
        }
        else
        {
            try
            {
                List<HomeResponseTopHeader> course = jdbcTemplate.query(COURSE_EXISTANCE_IN_SUBCATEGORY,(rs, rowNum) -> {
                    return new HomeResponseTopHeader(rs.getString("coursePhoto"), rs.getString("courseName"));
                }, user.getOccupation());
                if(course.size() !=0)
                {
                    return course;
                }
            }
            catch(NullPointerException e)
            {
                int categoryId = jdbcTemplate.queryForObject(GET_CATEGORY_FROM_SUBCATEGORY, new Object[] {user.getOccupation()}, Integer.class);
                List<HomeResponseTopHeader> courses = jdbcTemplate.query(GET_COURSES,(rs, rowNum) -> {
                    return new HomeResponseTopHeader(rs.getString("coursePhoto"), rs.getString("courseName"));
                }, categoryId);
                return courses;
            }
        }
        return null;
    }

    //"All" in home page display all the courses
    public List<HomeAllCourse> getAllCourses()
    {
        List<HomeAllCourse> allCourses = jdbcTemplate.query(GET_ALL_COURSES,(rs, rowNum) -> {
            return new HomeAllCourse(rs.getString("coursePhoto"), rs.getString("courseName"), rs.getInt("categoryId"),rs.getInt("chapterCount"));
        });
        return allCourses;
    }

    //"popular" in home page , filter all the courses with maximum enrollments more than 5
    public List<HomeAllCourse> getPopularCourses()
    {
        List<HomeAllCourse> popularCourseList = new ArrayList<>();
        List<Enrollment> allEnrolledCourses = jdbcTemplate.query(GET_ALL_ENROLLED_COURSES,(rs, rowNum) -> {
            return new Enrollment(rs.getInt("courseId"));
        });
        System.out.println(allEnrolledCourses);
        for(int i=0;i<allEnrolledCourses.size();i++)
        {
                int enrolmentCount = jdbcTemplate.queryForObject(GET_ENROLLMENT_COUNT, new Object[] {allEnrolledCourses.get(i).getCourseId()}, Integer.class);
                if(enrolmentCount >1)
                {
                    HomeAllCourse homeAllCourse= jdbcTemplate.queryForObject(GET_ALL_POPULAR_COURSES,(rs, rowNum) -> {
                        return new HomeAllCourse(rs.getString("coursePhoto"), rs.getString("courseName"), rs.getInt("categoryId"),rs.getInt("chapterCount"));
                    },allEnrolledCourses.get(i).getCourseId());
                    popularCourseList.add(homeAllCourse);
                }
        }
        System.out.println(popularCourseList);
     return popularCourseList;
     }


     public List<HomeAllCourse> getNewCourses()
     {
         List<HomeAllCourse> newCourseList = new ArrayList<>();
         List<HomeAllCourse> allNewCourses = jdbcTemplate.query(GET_ALL_NEW_COURSES,(rs, rowNum) -> {
             return new HomeAllCourse(rs.getInt("courseId"),rs.getString("coursePhoto"), rs.getString("courseName"), rs.getInt("categoryId"),rs.getInt("chapterCount"));
         });
         //System.out.println(allNewCourses.size());
         int size = allNewCourses.size()-1;
         for(int i=size;i>0;i--)
         {
             //System.out.println(allNewCourses.get(i).getCourseId());
             HomeAllCourse homeAllCourse= jdbcTemplate.queryForObject(GET_ALL_POPULAR_COURSES,(rs, rowNum) -> {
                 return new HomeAllCourse(rs.getString("coursePhoto"), rs.getString("courseName"), rs.getInt("categoryId"),rs.getInt("chapterCount"));
             },allNewCourses.get(i).getCourseId());
             newCourseList.add(homeAllCourse);
         }
         return newCourseList;
     }






//     *******************************************Sharath*******************************




    public OverviewResponse getOverviewOfCourse(int courseId)
    {
        try {
            return jdbcTemplate.queryForObject("SELECT courseName,coursePhoto,categoryName,chapterCount,lessonCount,courseTagLine,previewVideo,overview.description,testCount,courseMaterialId,courseDuration,learningOutCome,requirements,instructorName,url,profilePhoto,instructor.description AS instructorDescription FROM overview INNER JOIN instructor ON overview.instructorId = instructor.instructorId  INNER JOIN course ON overview.courseId = course.courseId AND course.courseId = ? INNER JOIN category ON course.categoryId = category.categoryId",new BeanPropertyRowMapper<>(OverviewResponse.class),courseId);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public List<CourseResponse> getBasicCourses(int categoryId)
    {
        return jdbcTemplate.query("SELECT courseName,previewVideo,chapterCount,courseDuration FROM overView INNER JOIN course ON course.courseId = overview.courseId WHERE categoryId = "+categoryId+" AND difficultyLevel = 'Beginner'",new BeanPropertyRowMapper<>(CourseResponse.class));
    }

    public List<CourseResponse> getAdvanceCourses(int categoryId)
    {
        return jdbcTemplate.query("SELECT courseName,previewVideo,chapterCount,courseDuration FROM overView INNER JOIN course ON course.courseId = overview.courseId WHERE categoryId = "+categoryId+" AND difficultyLevel = 'Advanced'",new BeanPropertyRowMapper<>(CourseResponse.class));
    }

    public List<AllCoursesResponse> getAllCoursesOf(int categoryId)
    {
        return jdbcTemplate.query("SELECT coursePhoto,courseName,chapterCount,categoryName FROM overView INNER JOIN course ON course.courseId = overview.courseId INNER JOIN category ON category.categoryId = course.categoryId WHERE category.categoryId = "+categoryId+" ",new BeanPropertyRowMapper<>(AllCoursesResponse.class));
    }

    public String getPolicy()
    {
        return jdbcTemplate.queryForObject("SELECT privacyPolicy FROM policy WHERE policyId=(SELECT max(policyId) FROM policy)",String.class);
    }

    public String getTermsAndConditions()
    {
        return jdbcTemplate.queryForObject("SELECT termsAndConditions FROM policy WHERE policyId=(SELECT max(policyId) FROM policy)",String.class);
    }

}
