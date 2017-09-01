package com.w02;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FunctionClass {

    public static void main(String[] args) {
        Student students[] = {
                new Student("23566", "Ivan", 4.75),
                new Student("23567", "Petar", 4.75),
                new Student("23568", "Toshko", 6.0),
                new Student("23569", "Martin", 5.5)
        };

        List<Student> names = Arrays.asList(students);
        Collections.sort(names);
        for(Student s : students) {
            System.out.println(s);
        }
    }
}
