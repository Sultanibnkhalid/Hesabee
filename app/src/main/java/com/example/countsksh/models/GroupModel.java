package com.example.countsksh.models;

public class GroupModel {
    private int id;
    private String name;
    private String param;

    public GroupModel() {}

    public GroupModel(int id, String name, String param) {
        this.id = id;
        this.name = name;
        this.param = param;
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

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
