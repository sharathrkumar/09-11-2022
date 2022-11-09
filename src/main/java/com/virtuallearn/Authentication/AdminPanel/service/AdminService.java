package com.virtuallearn.Authentication.AdminPanel.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.virtuallearn.Authentication.AdminPanel.entity.*;
import com.virtuallearn.Authentication.AdminPanel.model.Policy;
import com.virtuallearn.Authentication.AdminPanel.request.CourseRequest;
import com.virtuallearn.Authentication.AdminPanel.request.InstructorRequest;
import com.virtuallearn.Authentication.AdminPanel.request.LessonRequest;
import com.virtuallearn.Authentication.AdminPanel.request.TestRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.virtuallearn.Authentication.AdminPanel.common.Constants.*;

@Service
public class AdminService
{
    @Autowired
    JdbcTemplate jdbcTemplate;
    // to add course
    private String ADD_COURSE = "INSERT INTO course(courseId,coursePhoto,courseName,previewVideo,categoryId,subCategoryId) VALUES(?,?,?,?,?,?)";
    // to add lesson
    private String ADD_LESSON = "INSERT INTO lesson(lessonNumber,courseId,chapterId,lessonName,lessonDuration,videoLink) VALUES(?,?,?,?,?,?)";
    private String GET_CHAPTER_DURATION = "SELECT chapterDuration FROM chapter WHERE chapterId = ?";
    private String UPDATE_CHAPTER_DURATION = "UPDATE chapter SET chapterDuration = ? WHERE chapterId = ?";
    private String GET_COURSE_DURATION = "SELECT courseDuration FROM course WHERE courseId = ?";
    private String GET_COURSES_UNDER_A_CHAPTER = "SELECT chapterDuration FROM chapter WHERE courseId= ?";
    private String UPDATE_COURSE_DURATION = "UPDATE course SET courseDuration = ? WHERE courseId= ?";
    //add test
    private String ADD_TEST = "INSERT INTO test(testId,testName,chapterId,testDuration,passingGrade) values(?,?,?,?,?)";
    private String GET_EXISTING_QUESTIONS_COUNT = "SELECT questionsCount FROM test WHERE testId = ?";
    private String ADD_QUESTION = "INSERT INTO question(questionId,questionName,testId, option_1, option_2,option_3,option_4,correctAnswer) values(?,?,?,?,?,?,?,?)";
    private String UPDATE_QUESTIONS_COUNT = "UPDATE test SET questionsCount = ? where testId = ?";
    private String SET_COURSE_DURATION_TO_ZERO = "UPDATE course SET courseDuration= ? WHERE courseId = ?";
    //add instructor
    private String ADD_INSTRUCTOR = "INSERT INTO instructor(instructorId, instructorName,url,description,profilePhoto) values(?,?,?,?,?)";

    private File convertMultiPartToFile(MultipartFile file) throws IOException
    {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }

    private String generateFileName(MultipartFile multiPart)
    {
        return new Date().getTime() + "-" + Objects.requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
    }

    public String getFileUrl(MultipartFile multipartFile) throws IOException
    {
        String objectName = generateFileName(multipartFile);
        FileInputStream serviceAccount = new FileInputStream(FIREBASE_SDK_JSON);

        File file = convertMultiPartToFile(multipartFile);
        Path filePath = file.toPath();

        Storage storage = StorageOptions.newBuilder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).setProjectId(FIREBASE_PROJECT_ID).build().getService();
        BlobId blobId = BlobId.of(FIREBASE_BUCKET, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(multipartFile.getContentType()).build();
        storage.create(blobInfo, Files.readAllBytes(filePath));
        Blob blob = storage.create(blobInfo, Files.readAllBytes(filePath));
        System.out.println(String.format(DOWNLOAD_URL, URLEncoder.encode(objectName)));
        return String.format(DOWNLOAD_URL, URLEncoder.encode(objectName));
    }
    // To add courses
    public String addCourse(CourseRequest courseRequest) throws IOException
    {
            String coursePhotoLink = getFileUrl(courseRequest.getCoursePhoto());
            String courseVideoLink = getFileUrl(courseRequest.getPreviewVideo());
            jdbcTemplate.update(ADD_COURSE, courseRequest.getCourseId(), coursePhotoLink,courseRequest.getCourseName(),courseVideoLink,courseRequest.getCategoryId(), courseRequest.getSubCategoryId());
            return "Course Added SuccessFully";
    }
    //registering lessons under chapters
    public String addLesson(LessonRequest lessonRequest) throws IOException, ParseException {
        String lessonVideoLink = getFileUrl(lessonRequest.getVideoLink());
        String lessonTime = lessonRequest.getLessonDuration().toString();
        jdbcTemplate.update(ADD_LESSON,lessonRequest.getLessonNumber(), lessonRequest.getCourseId(), lessonRequest.getChapterId(),lessonRequest.getLessonName(), lessonTime,lessonVideoLink);

        // after adding lessons get the chapter duration add ,update it
       String chapterTime = jdbcTemplate.queryForObject(GET_CHAPTER_DURATION, new Object[]{lessonRequest.getChapterId()},String.class);
       String lessonTimeInfo = String.valueOf(lessonRequest.getLessonDuration());
       SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
       timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
       Date date1 = timeFormat.parse(chapterTime);
       Date date2 = timeFormat.parse(lessonTimeInfo);
       long sumOfDurations = date1.getTime() + date2.getTime();
       String courseDuration = timeFormat.format(new Date(sumOfDurations));
       jdbcTemplate.update(UPDATE_CHAPTER_DURATION, new Object[]{courseDuration,lessonRequest.getChapterId()});

       //after adding chapter duration get the course duration, add al;l chapters duration under that course , update course duration
        long durationsSum=0;
        String finalDuration="";
        Date durationDate1 = null;
        Date durationDate2;
        jdbcTemplate.update(SET_COURSE_DURATION_TO_ZERO, new Object[] {"00:00:00", lessonRequest.getCourseId()});
        String courseDurationInfo = jdbcTemplate.queryForObject(GET_COURSE_DURATION, new Object[] {lessonRequest.getCourseId()}, String.class);

        List<Chapter> chapterList = jdbcTemplate.query(GET_COURSES_UNDER_A_CHAPTER,(rs, rowNum) -> {
           return new Chapter(rs.getString("chapterDuration"));
        }, lessonRequest.getCourseId());
        System.out.println(chapterList);

        for(int i=0;i<chapterList.size();i++)
        {
            String chapterDurationInfo = chapterList.get(i).getChapterDuration();
            SimpleDateFormat timeFormatInfo = new SimpleDateFormat("HH:mm:ss");
            timeFormatInfo.setTimeZone(TimeZone.getTimeZone("UTC"));
            durationDate1 = timeFormatInfo.parse(courseDurationInfo);
            durationDate2 = timeFormatInfo.parse(chapterDurationInfo);
            durationsSum = durationDate1.getTime() + durationDate2.getTime();
            finalDuration = timeFormat.format(new Date(durationsSum));
            courseDurationInfo = finalDuration;
        }
        jdbcTemplate.update(UPDATE_COURSE_DURATION, new Object[] {finalDuration,lessonRequest.getCourseId()});
       return "Lesson Added SuccessFully";
    }
    // adding test
    public String addTest(TestRequest test)
    {
        String testDuration = test.getTestDuration().toString();
          jdbcTemplate.update(ADD_TEST, test.getTestId(), test.getTestName(), test.getChapterId(),testDuration, test.getPassingGrade());
          return "Test Added Successfully";
    }
    //adding questions under each test
    public String addQuestion(Question question)
    {
         int questionsCount = jdbcTemplate.queryForObject(GET_EXISTING_QUESTIONS_COUNT, new Object[] {question.getTestId()}, Integer.class);
         jdbcTemplate.update(ADD_QUESTION, question.getQuestionId(), question.getQuestionName(), question.getTestId(), question.getOption_1(), question.getOption_2(), question.getOption_3(),question.getOption_4(),question.getCorrectAnswer());
         jdbcTemplate.update(UPDATE_QUESTIONS_COUNT, new Object[] {questionsCount+1, question.getTestId()});
         return "Question added successfully";
    }
  //adding instructor for each course
    public String addInstructor(InstructorRequest instructorRequest) throws IOException
    {
        String profilePhotoLink = getFileUrl(instructorRequest.getProfilePhoto());
        jdbcTemplate.update(ADD_INSTRUCTOR,instructorRequest.getInstructorId(),instructorRequest.getInstructorName(), instructorRequest.getUrl(),instructorRequest.getDescription(),profilePhotoLink);
        return "Instructor added successfully";
    }




//    ****************************************Sharath*********************************


    public int addCategory(Category category)
    {
        try {
            jdbcTemplate.queryForObject("SELECT * FROM category WHERE categoryName = ?", new BeanPropertyRowMapper<>(Category.class),category.getCategoryName());
            return 0;
        }
        catch (Exception e)
        {
            if(category.getCategoryName().isEmpty())
                throw new EmptyStackException();
            else
                return jdbcTemplate.update("INSERT INTO category(categoryName) VALUES(?)",category.getCategoryName());
        }
    }

    int pages = 2;
    int lowerLimit = 0;
    int upperLimit = pages;

    public List<Category> getCategories()
    {
        List<Category> categories=  jdbcTemplate.query("SELECT * FROM category limit ?,?",(rs, rowNum) ->
        {
            return new Category(rs.getInt("categoryId"),rs.getString("categoryName"));
        },lowerLimit,upperLimit);
        lowerLimit = lowerLimit+pages;

        if(categories.size()==0)
        {
            lowerLimit=0;
            List <Category> categories1= jdbcTemplate.query("SELECT * FROM category limit ?,?", (rs, rowNum) ->
                    new Category(rs.getInt("categoryId"),rs.getString("categoryName")),lowerLimit,upperLimit);
            return categories1;
        }
        return categories;
    }



    public int addSubCategory(SubCategory subCategory)
    {
        try {
            jdbcTemplate.queryForObject("SELECT * FROM subCategory WHERE subCategoryName = ?", new BeanPropertyRowMapper<>(Category.class),subCategory.getSubCategoryName());
            return 0;
        }
        catch (Exception e)
        {
            return jdbcTemplate.update("INSERT INTO subCategory(categoryId,subCategoryName) VALUES(?,?)",subCategory.getCategoryId(),subCategory.getSubCategoryName());
        }
    }

    public List<SubCategory> getSubCategories(Category category)
    {
        List<SubCategory> subCategories=  jdbcTemplate.query("SELECT * FROM subCategory WHERE categoryId = ? limit ?,?",(rs, rowNum) ->
                new SubCategory(rs.getInt("categoryId"),rs.getInt("subCategoryId"),rs.getString("subCategoryName")),category.getCategoryId(),lowerLimit,upperLimit);
        lowerLimit = lowerLimit+pages;
        if(subCategories.size()==0)
        {
            lowerLimit=0;
            List<SubCategory> subCategories1=  jdbcTemplate.query("SELECT * FROM subCategory WHERE categoryId = ? limit ?,?",(rs, rowNum) ->
            {
                return new SubCategory(rs.getInt("categoryId"),rs.getInt("subCategoryId"),rs.getString("subCategoryName"));
            },category.getCategoryId(),lowerLimit,upperLimit);
            return subCategories1;
        }
        return subCategories;
    }



    public int addChapter(Chapter chapter){
        return jdbcTemplate.update("INSERT INTO chapter(courseId,chapterNumber,chapterName) VALUES(?,?,?)",chapter.getCourseId(),chapter.getChapterNumber(),chapter.getChapterName());
    }



    public int addOverView(Overview overview){

        try{
            jdbcTemplate.queryForObject("SELECT courseId FROM overview where courseId = ?",new BeanPropertyRowMapper<>(Overview.class),overview.getCourseId());
            return 0;
        }
        catch (Exception e) {
            int chapterCount = jdbcTemplate.queryForObject("SELECT COUNT(courseId) FROM chapter where courseId = ?", Integer.class, overview.getCourseId());
            int lessonCount = jdbcTemplate.queryForObject("SELECT COUNT(courseId) FROM lesson where courseId = ?", Integer.class, overview.getCourseId());


            List<Integer> chapterIDs = jdbcTemplate.queryForList("SELECT chapterId FROM chapter WHERE courseId = ?", Integer.class, overview.getCourseId());

            int testCount = 0;
            for (int i : chapterIDs) {
                testCount += jdbcTemplate.queryForObject("SELECT COUNT(testId) FROM test where chapterId = ?", Integer.class, i);
            }

            return jdbcTemplate.update("INSERT INTO overview(courseId,courseTagLine,description,chapterCount,lessonCount,testCount,learningOutCome,requirements,instructorId) VALUES(?,?,?,?,?,?,?,?,?)",
                    overview.getCourseId(), overview.getCourseTagLine(), overview.getDescription(), chapterCount, lessonCount, testCount, overview.getLearningOutCome(),overview.getRequirements(), overview.getInstructorId());
        }
    }

    public int addPolicy(Policy policy)
    {
        try
        {
            jdbcTemplate.queryForObject("SELECT termsAndConditions FROM policy WHERE termsAndConditions = ? AND privacyPolicy = ?",String.class,policy.getTermsAndConditions(),policy.getPrivacyPolicy());
            return 0;
        }
        catch (Exception e)
        {
            return jdbcTemplate.update("INSERT INTO policy(termsAndConditions,privacyPolicy) VALUES(?,?)",policy.getTermsAndConditions(),policy.getPrivacyPolicy());
        }
    }


}
