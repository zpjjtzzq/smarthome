package com.example.smarthomeapp.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by sglab on 2016/4/24.
 */
public class UserInfo implements java.io.Serializable {
    private Integer userId;
    private String username;
    private String password;
    private String gender;
    private String age;
    private String phone;
    private String email;

    private Set<Room> room = new HashSet<Room>(
            0);

    public UserInfo(){

    }



    public UserInfo(Integer userId, String username, String password,
                    String gender, String age, String phone, String email,
                    Set<Room> room) {
        super();
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.room = room;
    }



    public Integer getUserId() {
        return userId;
    }


    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }


    public Set<Room> getRoom() {
        return room;
    }

    public void setRoom(Set<Room> room) {
        this.room = room;
    }
}
