package repository;

import model.Enrollment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnrollmentRepository {
    private final List<Enrollment> enrollments = new ArrayList<>();

    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
    }

    public boolean existsEnrollmentByStudentIdAndCourseId(String studentId, String courseId) {
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudent().getId().equals(studentId)
                    && enrollment.getCourse().getId().equals(courseId)) {
                return true;
            }
        }
        return false;
    }

    // 按学生 id 查找该学生的所有选课记录
    public List<Enrollment> getEnrollmentsByStudentId(String studentId) {
        List<Enrollment> result = new ArrayList<>();

        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudent().getId().equals(studentId)) {
                result.add(enrollment);
            }
        }

        return result;
    }

    // 按课程 id 查找所有选课记录
    public List<Enrollment> getEnrollmentsByCourseId(String courseId) {
        List<Enrollment> result = new ArrayList<>();

        for (Enrollment enrollment : enrollments) {
            if (enrollment.getCourse().getId().equals(courseId)) {
                result.add(enrollment);
            }
        }

        return result;
    }

    // 查找一条具体的选课记录
    public Enrollment getEnrollmentByStudentIdAndCourseId(String studentId, String courseId) {
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudent().getId().equals(studentId)
                    && enrollment.getCourse().getId().equals(courseId)) {
                return enrollment;
            }
        }

        return null;
    }

    // 删除一条具体的选课记录
    public boolean removeEnrollmentByStudentIdAndCourseId(String studentId, String courseId) {
        Iterator<Enrollment> iterator = enrollments.iterator();

        while (iterator.hasNext()) {
            Enrollment enrollment = iterator.next();

            if (enrollment.getStudent().getId().equals(studentId)
                    && enrollment.getCourse().getId().equals(courseId)) {
                iterator.remove();
                return true;
            }
        }

        return false;
    }

    // 注销课程时删除该课程的所有选课记录
    public void removeEnrollmentsByCourseId(String courseId) {
        enrollments.removeIf(enrollment ->
                enrollment.getCourse().getId().equals(courseId));
    }

    public List<Enrollment> getAllEnrollments() {
        return new ArrayList<>(enrollments);
    }
}


