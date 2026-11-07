package validator;

public class CourseValidator {

    //课程名称合法性校验
    public static boolean isCourseNameValid(String courseName) {
        return courseName.matches("[A-Za-z][A-Za-z0-9_-]{0,19}");
    }

    //课程时间合法性校验
    public static boolean isCourseTimeValid(String courseTime) {
        if (!(courseTime.matches("[1-7]_([1-9]|1[0-4])-([1-9]|1[0-4])"))) {
            return false;
        }

        String[] parts = courseTime.split("_");
        String[] periods = parts[1].split("-");

        int start = Integer.parseInt(periods[0]);
        int end = Integer.parseInt(periods[1]);

        return start <= end;
    }

    //学时合法性
    public static boolean isCoursePeriodValid(String coursePeriod) {
        if (!(coursePeriod.matches("[0-9]+"))) {
            return false;
        }

        try {
            int period = Integer.parseInt(coursePeriod);
            return period >= 1 && period <= 1280;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //学分合法性
    public static boolean isCourseCreditValid(String courseCredit) {
        if (!(courseCredit.matches("[0-9]+(\\.[0-9]+)?"))) {
            return false;
        }

        double credit = Double.parseDouble(courseCredit);
        return Double.isFinite(credit) && credit > 0 && credit <= 12;
    }

    //课程类型合法性
    public static boolean isCourseTypeValid(String courseType) {
        return courseType.equals("COMPULSORY") || courseType.equals("ELECTIVE");
    }

    public static boolean isCourseIdValid(String courseId) {
        return courseId.matches("C-[1-9][0-9]*");
    }

}
