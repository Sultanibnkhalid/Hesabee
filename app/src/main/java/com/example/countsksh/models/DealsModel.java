package com.example.countsksh.models;
public class DealsModel {
    private  int id;
    private double amount;
    private String date;
    private String details;
    private String img;
//    private double total_in;
//    private double total_out;
    private String dealTime;

    private double budget;
    //default
    public DealsModel(){}
    //custom


    public DealsModel(int id, double amount, String date, String details, String img) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.details = details;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }
}
