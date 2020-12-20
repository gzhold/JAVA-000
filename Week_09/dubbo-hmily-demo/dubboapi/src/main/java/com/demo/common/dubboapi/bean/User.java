package com.demo.common.dubboapi.bean;

import lombok.Data;

import java.io.Serializable;


@Data
public class User implements Serializable {

    private static final long serialVersionUID = 8470370800765879248L;

    private int id;

    private String name;

    public User(){}

    public User(int id,String name){
        this.id = id;
        this.name = name;
    }
}
