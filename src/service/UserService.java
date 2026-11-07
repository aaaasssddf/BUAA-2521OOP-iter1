package service;

import repository.UserRepository;
import model.Administrator;
import model.Student;
import model.Teacher;
import model.User;
import validator.*;

public class UserService {
    private final UserRepository userRepository;
    private final SessionService sessionService;

    public UserService(UserRepository userRepository,  SessionService sessionService) {
        this.userRepository = userRepository;
        this.sessionService = sessionService;
    }

    //关于注册时的合法性检验，可以被复用的关于身份类型、名字、密码、学工号的校验移至Validator包，而下面的三个验证依赖于状态，不可移动
    //确认密码：与密码一致
    private boolean isVerifyPasswordValid(String password, String verifyPassword) {
        return password.equals(verifyPassword);
    }

    //检查用户是否不存在
    private boolean isUserNotExisted(String id) {
        return !userRepository.existsUserById(id);
    }

    //检查管理员id是否连续
    private boolean isAdministratorIdConsecutive(String id) {
        return userRepository.getNextAdministratorId().equals(id);
    }



    //尝试创建一个新用户，在参数没被校验之前是不会创建User对象的
    public void registerUser(String[] args) {
        String id = args[0];
        String name = args[1];
        String password = args[2];
        String verifyPassword = args[3];
        String identity = args[4];

        //这里的校验优先级：id合法性>用户存在>管理员号连续>姓名>密码>确认密码>身份类型存在
        if (!UserValidator.isUserIdValid(id)) {
            System.out.println("Illegal user id");
            return;
        }

        if (!isUserNotExisted(id)) {
            System.out.println("User exists");
            return;
        }

        if (identity.equals("Administrator") && !isAdministratorIdConsecutive(id)) {
            System.out.println("Administrator id is not consecutive");
            return;
        }

        if (!UserValidator.isUserNameValid(name)) {
            System.out.println("Illegal user name");
            return;
        }

        if (!UserValidator.isUserPasswordValid(password)) {
            System.out.println("Illegal password");
            return;
        }

        if (!isVerifyPasswordValid(password, verifyPassword)) {
            System.out.println("Passwords do not match");
            return;
        }

        if (!UserValidator.isUserIdentityValid(identity)) {
            System.out.println("Illegal identity");
            return;
        }

        User newUser = switch (identity) {
            case "Administrator" -> new Administrator(name, id, password);
            case "Student" -> new Student(name, id, password);
            case "Teacher" -> new Teacher(name, id, password);
            default -> throw new IllegalArgumentException(); //这行其实并不可能被运行，加上只是不想看到黄色波浪线（）
        };

        userRepository.addUser(newUser);
        System.out.println("Register success");
    }

    //无参数的打印信息
    public void printSelfInfo(){
        if(!sessionService.isOnline()){
            System.out.println("No one is online");
            return;
        }

        User currentUser = sessionService.getCurrentUser();
        System.out.println(currentUser.toString());
        System.out.println("Print information success");
    }

    //有参数的打印信息
    public void printInfo(String id){
        //校验是否在线
        if(!sessionService.isOnline()){
            System.out.println("No one is online");
            return;
        }

        //校验管理员权限
        User currentUser = sessionService.getCurrentUser();
        if(!(currentUser instanceof Administrator)){
            System.out.println("Permission denied");
            return;
        }

        //校验id合法性
        if(!UserValidator.isUserIdValid(id)){
            System.out.println("Illegal user id");
            return;
        }

        User user = userRepository.getUserById(id);
        if(user == null){
            System.out.println("User does not exist");
            return;
        }

        System.out.println(user.toString());
        System.out.println("Print information success");
    }






}
