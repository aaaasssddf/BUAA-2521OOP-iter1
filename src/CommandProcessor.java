
import service.SessionService;
import service.UserService;
import service.CourseService;

import java.util.Arrays;
import java.util.Map;

public class CommandProcessor {

    private final CourseService courseService;
    private final UserService userService;
    private final SessionService sessionService;

    public CommandProcessor(UserService userService,
                            CourseService courseService,
                            SessionService sessionService) {
        this.userService = userService;
        this.courseService = courseService;
        this.sessionService = sessionService;
    }

    //value表示最小和最大的参数数量
    private static final Map<String, int[]> ARGUMENT_COUNTS = Map.of(
            "quit", new int[]{0, 0},
            "register", new int[]{5, 5},
            "login", new int[]{2, 2},
            "logout", new int[]{0, 1},
            "printInfo", new int[]{0, 1},
            "createCourse", new int[]{5, 5},
            "listCourse", new int[]{0, 1},
            "selectCourse", new int[]{1, 1},
            "cancelCourse", new int[]{1, 1},
            "deleteCourse", new int[]{1, 1}
    );

    public void processLine(String line) {
        String trim = line.trim();
        //使用"\\s+"可以处理参数之间有多个空白字符的情形
        String[] parts = trim.split("\\s+");
        String command = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        //命令存在性分析
        if (!ARGUMENT_COUNTS.containsKey(command)) {
            //不存在该命令
            System.out.println("Command '" + command + "' not found");
            return;
        }

        //命令参数数量分析
        int[] range = ARGUMENT_COUNTS.get(command);
        if (args.length < range[0] || args.length > range[1]) {
            //参数数量小于最小或者大于最大时报错
            System.out.println("Illegal argument count");
            return;
        }

        switch (command) {
            case "quit":
                quitCommand();
                break;
            case "register":
                registerCommand(args);
                break;
            case "login":
                loginCommand(args);
                break;
            case "logout":
                if (args.length == 0) {
                    logoutCommand();
                } else if (args.length == 1) {
                    logoutCommand(args);
                }
                break;
            case "printInfo":
                if (args.length == 0) {
                    printInfoCommand();
                } else if (args.length == 1) {
                    printInfoCommand(args);
                }
                break;
            case "createCourse":
                createCourseCommand(args);
                break;
            case "listCourse":
                if (args.length == 0) {
                    listCourseCommand();
                } else if (args.length == 1) {
                    listCourseCommand(args);
                }
                break;
            case "selectCourse":
                selectCourseCommand(args);
                break;
            case "cancelCourse":
                cancelCourseCommand(args);
                break;
            case "deleteCourse":
                deleteCourseCommand(args);
        }
    }

    private void quitCommand() {
        sessionService.quit();
    }

    private void registerCommand(String[] args) {
        userService.registerUser(args);
    }

    private void loginCommand(String[] args) {
        sessionService.login(args);
    }

    private void logoutCommand() {
        sessionService.logoutCurrentUser();
    }

    private void logoutCommand(String[] args) {
        sessionService.logout(args[0]);
    }

    private void printInfoCommand() {
        userService.printSelfInfo();
    }

    private void printInfoCommand(String[] args) {
        userService.printInfo(args[0]);
    }

    private void createCourseCommand(String[] args) {
        courseService.createCourse(args);
    }

    private void listCourseCommand() {
        courseService.listCourse();
    }

    private void listCourseCommand(String[] args) {
        courseService.listCourse(args[0]);
    }

    private void selectCourseCommand(String[] args) {
        courseService.selectCourse(args[0]);
    }

    private void cancelCourseCommand(String[] args) {
        courseService.cancelCourse(args[0]);
    }

    private void deleteCourseCommand(String[] args) {
        courseService.deleteCourse(args[0]);
    }
}
