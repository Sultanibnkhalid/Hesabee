package com.example.countsksh.models;

import java.util.ArrayList;

public class AccountsModel {
    private int id;
    private String name;
    private int group;
    private String phone;
    private String img;
    private String emil;
    private double total;
    private double dealCount;
   // private int dayCount;



    //default
    public AccountsModel(){

        this.id=-2;
    }
    //custom constructor

    public AccountsModel(int id, String name, int group, String phone, String img, String emil, double total) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.phone = phone;
        this.img = img;
        this.emil = emil;
        this.total = total;

    }


    //getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getEmil() {
        return emil;
    }

    public void setEmil(String emil) {
        this.emil = emil;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDealCount() {
        return dealCount;
    }

    public void setDealCount(double dealCount) {
        this.dealCount = dealCount;
    }
}
