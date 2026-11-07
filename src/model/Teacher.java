package model;

import java.util.List;

public class Teacher extends User {

    public Teacher(String name, String id, String password) {
        super(name, id, password);
    }

    public String toString(){
        return "User id: " + getId() + "\n" +
                "Name: " + getName()+ "\n" +
                "Type: Teacher";
    }
}
