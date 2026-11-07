package model;

public class Enrollment {
    private final Course course;
    private final Student student;

    public Enrollment(Course course, Student student) {
        this.course = course;
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public Student getStudent() {
        return student;
    }
}
