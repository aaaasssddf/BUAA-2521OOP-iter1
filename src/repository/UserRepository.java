package repository;

import model.Administrator;
import model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserRepository {
    private final HashMap<String, User> users = new HashMap<>();
    private int lastAdministratorNumber = 0;

    public User getUserById(String id) {
        return users.get(id);
    }

    public boolean existsUserById(String id) {
        return users.containsKey(id);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public String getNextAdministratorId() {
        if (lastAdministratorNumber >= 999) {
            return null; // 管理员编号已用尽
        }
        return String.format("AD%03d", lastAdministratorNumber + 1);
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
        if (user instanceof Administrator) {
            lastAdministratorNumber++;
        }
    }
}
    



