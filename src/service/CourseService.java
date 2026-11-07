package service;

import model.*;
import repository.CourseRepository;
import repository.EnrollmentRepository;
import repository.UserRepository;
import validator.CourseValidator;
import validator.UserValidator;

import java.util.Comparator;
import java.util.List;

public class CourseService {
    private final CourseRepository courseRepository;
    private final SessionService sessionService;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    public CourseService(CourseRepository courseRepository, SessionService sessionService, UserRepository userRepository, EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.sessionService = sessionService;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    //判断两门课时间是否冲突
    private boolean isCourseTimeConflict(String time1, String time2) {
        String[] first = time1.split("_");
        String[] second = time2.split("_");

        // 不同星期，不冲突
        if (!first[0].equals(second[0])) {
            return false;
        }

        String[] firstPeriods = first[1].split("-");
        String[] secondPeriods = second[1].split("-");

        int start1 = Integer.parseInt(firstPeriods[0]);
        int end1 = Integer.parseInt(firstPeriods[1]);
        int start2 = Integer.parseInt(secondPeriods[0]);
        int end2 = Integer.parseInt(secondPeriods[1]);

        // 两个闭区间有重叠
        return start1 <= end2 && start2 <= end1;
    }

    //查找学生选课冲突
    private boolean existsConflictCourseByStudentId(String studentId, String time1) {
        List<Enrollment> enrollments = enrollmentRepository.getEnrollmentsByStudentId(studentId);
        for (Enrollment enrollment : enrollments) {
            Course course = enrollment.getCourse();
            String time2 = course.getTime();
            if (isCourseTimeConflict(time1, time2)) {
                return true;
            }
        }
        return false;
    }

    //查找教师开课冲突
    private boolean existsConflictCourseByTeacherId(String teacherId, String time1) {
        List<Course> courseList = courseRepository.getCoursesByTeacherId(teacherId);
        for (Course course : courseList) {
            String time2 = course.getTime();
            if (isCourseTimeConflict(time1, time2)) {
                return true;
            }
        }
        return false;
    }

    //TODO
    public void createCourse(String[] args) {
        //校验是否有用户在线
        if (!sessionService.isOnline()) {
            System.out.println("No one is online");
            return;
        }

        //权限校验
        User currentUser = sessionService.getCurrentUser();
        if (!(currentUser instanceof Teacher)) {
            System.out.println("Permission denied");
            return;
        }

        String teacherId = currentUser.getId();
        int courseCount = courseRepository.countCoursesByTeacherId(teacherId);
        //TODO 这里可以考虑不在方法里面写死最大课程数量，后面再改
        //校验是否超过最大课程数量
        if (courseCount >= 10) {
            System.out.println("Course count reaches limit");
            return;
        }

        String courseName = args[0];
        String courseTime = args[1];
        String courseCredit = args[2];
        String coursePeriod = args[3];
        String courseType = args[4];

        //校验课程名字合法性
        if (!CourseValidator.isCourseNameValid(args[0])) {
            System.out.println("Illegal course name");
            return;
        }

        if (courseRepository.existsCourseByTeacherId(teacherId, courseName)) {
            System.out.println("Course name exists");
            return;
        }

        if (!CourseValidator.isCourseTimeValid(courseTime)) {
            System.out.println("Illegal course time");
            return;
        }

        //校验是否有时间冲突的课程
        if (existsConflictCourseByTeacherId(teacherId, courseTime)) {
            System.out.println("Course time conflicts");
            return;
        }

        //校验学分合法性
        if (!CourseValidator.isCourseCreditValid(courseCredit)) {
            System.out.println("Illegal course credit");
            return;
        }

        //校验学时合法性
        if (!CourseValidator.isCoursePeriodValid(coursePeriod)) {
            System.out.println("Illegal course period");
            return;
        }

        //校验课程类型合法性
        if (!CourseValidator.isCourseTypeValid(courseType)) {
            System.out.println("Illegal course category");
            return;
        }

        String courseId = courseRepository.getNextCourseId();

        Course newCourse = new Course(courseId, courseName, courseTime, Double.parseDouble(courseCredit), Integer.parseInt(coursePeriod), courseType, (Teacher) currentUser);
        courseRepository.addCourse(newCourse);

        System.out.println("Create course success (courseId: " + courseId + ")");
    }

    public void listCourse() {

        if (!sessionService.isOnline()) {
            System.out.println("No one is online");
            return;
        }
        User currentUser = sessionService.getCurrentUser();

        //老师查看自己所有课程，学生管理员查看所有课程
        if (currentUser instanceof Teacher) {
            printCoursesByTeacherId(currentUser.getId());
        } else {
            printAllCourses();
        }

    }

    private void printCoursesByTeacherId(String teacherId) {
        printCoursesByTeacherId(teacherId, false);
    }

    private void printCoursesByTeacherId(String teacherId, boolean includeTeacherName) {
        List<Course> courseList = courseRepository.getCoursesByTeacherId(teacherId);

        if (courseList.isEmpty()) {
            System.out.println("Course does not exist");
            return;
        }

        courseList.sort(Comparator.comparingInt(Course::getIntegerId));

        for (Course course : courseList) {
            if (includeTeacherName) {
                System.out.print(course.getTeacher().getName() + " ");
            }
            System.out.printf(
                    "%s %s %s %.1f %d %s%n",
                    course.getId(),
                    course.getName(),
                    course.getTime(),
                    course.getCredit(),
                    course.getPeriod(),
                    course.getType()
            );
        }

        System.out.println("List course success");
    }

    //用于学生和管理员的无参数listCourse
    private void printAllCourses() {
        List<Course> courseList = courseRepository.getAllCourses();

        if (courseList.isEmpty()) {
            System.out.println("Course does not exist");
            return;
        }

        courseList.sort(
                Comparator.comparing((Course c) -> c.getTeacher().getName())
                        .thenComparingInt(Course::getIntegerId)
        );

        for (Course course : courseList) {
            System.out.printf(
                    "%s %s %s %s %.1f %d %s%n",
                    course.getTeacher().getName(),
                    course.getId(),
                    course.getName(),
                    course.getTime(),
                    course.getCredit(),
                    course.getPeriod(),
                    course.getType()
            );
        }
        System.out.println("List course success");
    }

    public void listCourse(String userId) {

        if (!sessionService.isOnline()) {
            System.out.println("No one is online");
            return;
        }

        User currentUser = sessionService.getCurrentUser();
        if (currentUser instanceof Student ||
                (currentUser instanceof Teacher && !(currentUser.getId().equals(userId)))) {
            System.out.println("Permission denied");
            return;
        }

        if (currentUser instanceof Teacher) {
            printCoursesByTeacherId(userId);
            return;
        }

        if (currentUser instanceof Administrator) {

            if (!UserValidator.isUserIdValid(userId)) {
                System.out.println("Illegal user id");
                return;
            }

            if (!userRepository.existsUserById(userId)) {
                System.out.println("User does not exist");
                return;
            }

            User user = userRepository.getUserById(userId);
            if (!(user instanceof Teacher)) {
                System.out.println("User id does not belong to a Teacher");
                return;
            }

            printCoursesByTeacherId(userId, true);
        }
    }

    public void selectCourse(String courseId) {

        if (!sessionService.isOnline()) {
            System.out.println("No one is online");
            return;
        }

        User currentUser = sessionService.getCurrentUser();
        if (!(currentUser instanceof Student)) {
            System.out.println("Permission denied");
            return;
        }

        if (!CourseValidator.isCourseIdValid(courseId)) {
            System.out.println("Illegal course id");
            return;
        }

        if (!courseRepository.existsCourseById(courseId)) {
            System.out.println("Course does not exist");
            return;
        }

        Course selectedCourse = courseRepository.getCourseById(courseId);
        String time1 = selectedCourse.getTime();
        if (existsConflictCourseByStudentId(currentUser.getId(), time1)) {
            System.out.println("Course time conflicts");
            return;
        }

        Enrollment newEnrollment = new Enrollment(selectedCourse, (Student) currentUser);
        enrollmentRepository.addEnrollment(newEnrollment);

        System.out.printf("Select course success (courseId: %s)\n", courseId);
    }

    public void cancelCourse(String courseId) {

        //这一部分跟选课的验证内容是一样的
        if (!sessionService.isOnline()) {
            System.out.println("No one is online");
            return;
        }

        User currentUser = sessionService.getCurrentUser();
        if (!(currentUser instanceof Student)) {
            System.out.println("Permission denied");
            return;
        }

        if (!CourseValidator.isCourseIdValid(courseId)) {
            System.out.println("Illegal course id");
            return;
        }

        if (!courseRepository.existsCourseById(courseId) || !enrollmentRepository.existsEnrollmentByStudentIdAndCourseId(currentUser.getId(), courseId)) {
            System.out.println("Course does not exist");
            return;
        }

        enrollmentRepository.removeEnrollmentByStudentIdAndCourseId(currentUser.getId(), courseId);
        System.out.printf("Cancel course success (courseId: %s)\n", courseId);
    }

    public void deleteCourse(String courseId) {

        if (!sessionService.isOnline()) {
            System.out.println("No one is online");
            return;
        }

        User currentUser = sessionService.getCurrentUser();
        if (currentUser instanceof Student) {
            System.out.println("Permission denied");
            return;
        }

        if (!CourseValidator.isCourseIdValid(courseId)) {
            System.out.println("Illegal course id");
            return;
        }

        if ((currentUser instanceof Administrator && !courseRepository.existsCourseById(courseId))
                || (currentUser instanceof Teacher && !courseRepository.existsCourseByTeacherIdAndCourseId(currentUser.getId(), courseId))) {
            System.out.println("Course does not exist");
            return;
        }

        enrollmentRepository.removeEnrollmentsByCourseId(courseId);
        courseRepository.deleteCourseById(courseId);
        System.out.printf("Delete course success (courseId: %s)\n", courseId);

    }
}
