package com.xf.zhang.springbootsentinel.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SentinelController {

    //@SentinelResource("sentinel")
    @RequestMapping("/sentinel")
    public String testSentinel(){

        return "SUCCESS";
    }

    @RequestMapping("/service")
    public String service(){

        return "SERVICE";
    }
}
