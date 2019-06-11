package com.example.pay;

import java.io.Serializable;

/**
 * Stone
 * 2019/6/11
 **/
public class SerializableBean implements Serializable {
    int age;
    String name;
    int sex;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
