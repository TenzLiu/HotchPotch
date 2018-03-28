package com.tenz.hotchpotch.module.home.entity;

/**
 * Author: TenzLiu
 * Date: 2018-01-30 18:15
 * Description: Module
 */

public class Module {

    private int id;
    private String logo;
    private String name;

    public Module() {
    }

    public Module(int id, String logo, String name) {
        this.id = id;
        this.logo = logo;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
