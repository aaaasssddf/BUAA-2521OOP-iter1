package validator;

public class UserValidator {
    public static boolean isUserIdentityValid(String identity) {
        return identity.equals("Administrator") || identity.equals("Student") || identity.equals("Teacher");
    }

    public static boolean isUserPasswordValid(String password) {
        if (password == null || !password.matches("[A-Za-z0-9@_%$!#&*]{6,16}")) {
            return false;
        }
        boolean hasLetter = password.matches(".*[A-Za-z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[@_%$!#&*].*");

        int categoryCount = (hasLetter ? 1 : 0) + (hasDigit ? 1 : 0) + (hasSpecial ? 1 : 0);

        return categoryCount >= 2;
    }

    //姓名：4-16位，首位为字母，后面为字母或下划线
    public static boolean isUserNameValid(String name) {
        return name != null && name.matches("[A-Za-z][A-Za-z_]{3,15}");
    }

    //id合法性可以直接对三种情况取或，因为会出现学工号和身份类型均合法，但是身份不匹配，而且id合法性的优先级高于身份类型合法性
    public static boolean isUserIdValid(String id) {
        return isStudentIdValid(id) || isTeacherIdValid(id) || isAdministratorIdValid(id);
    }

    //教师id：5位0-9
    private static boolean isTeacherIdValid(String id) {
        return id.matches("[0-9]{5}") && !id.equals("00000");
    }

    //管理员id：
    private static boolean isAdministratorIdValid(String id) {
        return id.matches("AD[0-9]{3}") && !id.equals("AD000");
    }

    //学生id：先正则判断是本科生还是研究生
    private static boolean isStudentIdValid(String id) {

        if (id.matches("[0-9]{8}")) {
            return isUndergraduateIdValid(id);
        }

        if (id.matches("(SY|ZY|BY|ZB)[0-9]{7}")) {
            return isGraduateIdValid(id);
        }
        return false;
    }

    //本科生id
    private static boolean isUndergraduateIdValid(String id) {
        int year = Integer.parseInt(id.substring(0, 2));
        int college = Integer.parseInt(id.substring(2, 4));
        int classNumber = Integer.parseInt(id.substring(4, 5));
        int serialNumber = Integer.parseInt(id.substring(5, 8));

        if (year < 21 || year > 26 || college < 1 || college > 43 || serialNumber < 1 || serialNumber > 999) {
            return false;
        }

        //北航学院需要特殊判断
        if (college == 37) {
            return classNumber == 1 || classNumber == 3 || classNumber == 4 || classNumber == 5 || classNumber == 6 || classNumber == 7 || classNumber == 9;
        }

        return classNumber >= 1 && classNumber <= 6;
    }

    //研究生id
    private static boolean isGraduateIdValid(String id) {
        String prefix = id.substring(0, 2);
        int year = Integer.parseInt(id.substring(2, 4));
        int college = Integer.parseInt(id.substring(4, 6));
        int classNumber = Integer.parseInt(id.substring(6, 7));
        int serialNumber = Integer.parseInt(id.substring(7, 9));

        if (prefix.equals("ZY") || prefix.equals("SY")) {
            if (year < 23 || year > 26) {
                return false;
            }
        } else if (prefix.equals("BY") || prefix.equals("ZB")) {
            if (year < 21 || year > 26) {
                return false;
            }
        } else {
            return false;
        }

        return college != 37 && college >= 1 && college <= 43 && classNumber >= 1 && classNumber <= 6 && serialNumber >= 1 && serialNumber <= 99;
    }
}
