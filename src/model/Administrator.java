package model;

public class Administrator extends User {
    public Administrator(String name, String id, String password) {
        super(name, id, password);
    }

    public String toString(){
        return "User id: " + getId() + "\n" +
                "Name: " + getName()+ "\n" +
                "Type: Administrator";
    }
}
