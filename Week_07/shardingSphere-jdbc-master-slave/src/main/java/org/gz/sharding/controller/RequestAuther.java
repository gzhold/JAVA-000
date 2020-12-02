package org.gz.sharding.controller;


import lombok.extern.log4j.Log4j2;
import org.gz.sharding.entity.UserInfo;
import org.gz.sharding.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Log4j2
public class RequestAuther {

    @Autowired
    private UserService userService;

    @GetMapping("/user/queryCount")
    public long getUserTotal(HttpServletRequest request){
        log.info("Request param is {}", request);
        return userService.queryTotal();
    }


    @PostMapping("/user/update")
    public int saveUser(HttpServletRequest request) {
        log.info("Update request param is {}", request);
        return  userService.updateUserInfo(1);
    }

    @GetMapping("/user/query/all")
    public List<UserInfo> getAll(HttpServletRequest request){
        return userService.queryAll();
    }

}
