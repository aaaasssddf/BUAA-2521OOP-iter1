package service;

import model.Administrator;
import repository.UserRepository;
import model.User;
import validator.UserValidator;

import java.util.LinkedHashMap;

public class SessionService {
    private final UserRepository userRepository;

    //当前登录的用户
    private User currentUser;

    //该字段目前最多只会有一个User
    //<id：User>
    private final LinkedHashMap<String, User> onlineUsers = new LinkedHashMap<String, User>();

    public SessionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void login(String[] args) {
        String id = args[0];
        String password = args[1];

        if (!UserValidator.isUserIdValid(id)) {
            System.out.println("Illegal user id");
            return;
        }

        User user = userRepository.getUserById(id);
        if (user == null) {
            System.out.println("User does not exist");
            return;
        }

        //校验是否已经登陆
        if (user.equals(currentUser)) {
            System.out.println(id + " is online");
            return;
        }

        if (!password.equals(user.getPassword())) {
            System.out.println("Wrong password");
            return;
        }

        onlineUsers.put(user.getId(), user);
        this.currentUser = user;
        System.out.println("Welcome to ACP, " + id);
    }

    public boolean isOnline(){
        return currentUser != null;
    }

    //TODO 这一部分逻辑还需要优化，因为logout命令需要验证用户是否在线，目前实现quit已经可以了
    public void logoutCurrentUser() {
        if (this.currentUser == null) {
            System.out.println("No one is online");
        } else {
            User user = currentUser;
            String userId = user.getId();
            onlineUsers.remove(userId);
            System.out.println(userId + " Bye~");
            currentUser = null;
        }
    }

    public void logout(String id){
        if (currentUser == null) {
            System.out.println("No one is online");
            return;
        }

        //校验当前权限是否是Administrator。
        //这里如果没有人的登陆的话结果也为false
        if(!(currentUser instanceof Administrator)){
            System.out.println("Permission denied");
            return;
        }

        //校验id合法
        if(!UserValidator.isUserIdValid(id)){
            System.out.println("Illegal user id");
            return;
        }

        //校验要强制下线的用户是否存在
        User user = userRepository.getUserById(id);
        if (user == null) {
            System.out.println("User does not exist");
            return;
        }

        //校验要强制下线的用户是否在线
        User loggedUser = onlineUsers.get(user.getId());
        if (loggedUser == null) {
            System.out.println(id + " is not online");
            return;
        }

        onlineUsers.remove(user.getId());
        if (currentUser.getId().equals(id)) {
            currentUser = null;
        }
        System.out.println(id + " Bye~");
    }

    //TODO 这里暂未实现先登陆的先下线
    public void quit() {
        for (User user : onlineUsers.values()) {
            onlineUsers.remove(user.getId());
            System.out.println(user.getId() + " Bye~");
        }
        System.out.println("----- Good Bye! -----");
        System.exit(0);
    }
}
