package org.gz.multi.config;


import lombok.Getter;
import lombok.Setter;

public enum DataSourceType {

    // 主表
    MASTER("master"),
    // 从表1
    SLAVE_1("slave1"),
    // 从表2
    SLAVE_2("slave2");

    @Setter
    @Getter
    private String name;

    DataSourceType(String name) {
        this.name = name;
    }


}
