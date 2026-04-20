package Model;

import java.util.List;
import java.util.ArrayList;

public class UserManager {

    private static UserManager instance;
    private List<User> users = new ArrayList<>();

    private UserManager() {} // Privat konstruktor för Singleton

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }

}
