package model;

public class Course {
    private final String name;
    private final Teacher teacher;
    private final double credit;
    private final int period;
    private final String time;
    private final String type;
    private final String id;

    public Course(String id, String name, String time, double credit, int period, String type, Teacher teacher) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.credit = credit;
        this.period = period;
        this.type = type;
        this.teacher = teacher;
    }

    public String getTime() {
        return time;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public String getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public double getCredit() {
        return credit;
    }

    public int getPeriod() {
        return period;
    }

    public String getType() {
        return type;
    }

    //这个方法可以返回id的整数用于按课程id排序，也可以改为保存一份int形式的课程id，但我懒得加属性了
    public int getIntegerId() {
        return Integer.parseInt(id.substring(2));
    }
}
