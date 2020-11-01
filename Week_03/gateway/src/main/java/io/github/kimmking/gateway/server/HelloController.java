package io.github.kimmking.gateway.server;

import org.springframework.http.HttpRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public String sayHello(HttpServletRequest request){
        String code = request.getHeader("nio");
        System.out.println("Read head params, nio is " + code);
        return "hello world";
    }
}
