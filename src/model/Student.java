package model;

public class Student extends User {
    public Student(String name, String id, String password) {
        super(name, id, password);
    }

    public String toString(){
        return "User id: " + getId() + "\n" +
                "Name: " + getName()+ "\n" +
                "Type: Student";
    }


}
