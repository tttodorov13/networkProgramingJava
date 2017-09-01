package com.w02;

public class Student implements Comparable<Student> {
    private String facultyNumber;
    private String name;
    private Double gpe;

    Student(String facultyNumber, String name, Double gpe) {
        this.facultyNumber = facultyNumber;
        this.name = name;
        this.gpe = gpe;
    }

    @Override
    public int compareTo(Student o) {
        int lastCmp = gpe.compareTo(o.gpe);
        return (lastCmp != 0 ? lastCmp : name.compareTo(o.name));
    }

    public String toString() {
        return String.format("%s %s %.2f", getFacultyNumber(), getName(), getGpe());
    }

    public String getFacultyNumber() {
        return facultyNumber;
    }

    public void setFacultyNumber(String facultyNumber) {
        this.facultyNumber = facultyNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getGpe() {
        return gpe;
    }

    public void setGpe(Double gpe) {
        this.gpe = gpe;
    }

}
