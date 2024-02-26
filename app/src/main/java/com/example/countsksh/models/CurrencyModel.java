package com.example.countsksh.models;

public class CurrencyModel {
    private int id;
    private String name;
    private String param1;
    public CurrencyModel(){}

    public CurrencyModel(int id, String name, String param1) {
        this.id = id;
        this.name = name;
        this.param1 = param1;
    }

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

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }
}
