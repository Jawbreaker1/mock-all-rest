package com.engwall.restservice.MockAllRest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloResource {

    @RequestMapping("/")
    public String index(){
        return "Hello";
    }
}
