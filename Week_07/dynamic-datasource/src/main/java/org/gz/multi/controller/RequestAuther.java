package org.gz.multi.controller;


import lombok.extern.log4j.Log4j2;
import org.gz.multi.entity.UserInfo;
import org.gz.multi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;


@RestController
@Log4j2
public class RequestAuther {

    @Autowired
    private UserService userService;

    @GetMapping("/user/query/count")
    public long getUserTotal(HttpServletRequest request){
        log.info("Request param is {}", request);
        return userService.queryTotal();
    }


    @GetMapping("/user/get")
    public UserInfo getUser(HttpServletRequest request){
        log.info("Request param is {}", request);
        return userService.queryUser(2);
    }


}
