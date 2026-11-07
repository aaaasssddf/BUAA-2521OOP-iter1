package repository;

import model.Course;

import java.util.*;

public class CourseRepository {
    private final List<Course> courses = new ArrayList<>();
    private int nextCourseNumber = 1;

    public boolean existsCourseById(String id) {
        for (Course course : courses) {
            if (course.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean existsCourseByTeacherIdAndCourseId(String teacherId, String courseId) {
        return getCourseByTeacherIdAndCourseId(teacherId, courseId) != null;
    }

    public Course getCourseByTeacherIdAndCourseId(String teacherId, String courseId) {
        for (Course course : courses) {
            if (course.getTeacher().getId().equals(teacherId)
                    && course.getId().equals(courseId)) {
                return course;
            }
        }
        return null;
    }

    public void addCourse(Course course) {
        courses.add(course);
        nextCourseNumber++;
    }

    public String getNextCourseId() {
        return "C-" + nextCourseNumber;
    }

    public void deleteCourseById(String id) {
        Course course = getCourseById(id);
        if (course != null) {
            courses.remove(course);
        }
    }

    public List<Course> getCoursesByTeacherId(String id) {
        List<Course> result = new ArrayList<>();
        for (Course course : courses) {
            if (course.getTeacher().getId().equals(id)) {
                result.add(course);
            }
        }
        return result;
    }

    public boolean existsCourseByTeacherId(String id, String name) {
        boolean result = false;
        List<Course> teacher_courses = getCoursesByTeacherId(id);
        for (Course course : teacher_courses) {
            if(course.getName().equals(name)){
                result = true;
                break;
            }
        }
        return result;
    }

    public Course getCourseById(String id) {
        for (Course course : courses) {
            if (course.getId().equals(id)) {
                return course;
            }
        }
        return null;
    }

    public int countCoursesByTeacherId(String id) {
        int result = 0;
        List<Course> teacher_courses = getCoursesByTeacherId(id);
        for (Course course : teacher_courses) {
            result++;
        }
        return result;
    }

    public List<Course> getAllCourses(){
        return new ArrayList<>(courses);
    }

}
