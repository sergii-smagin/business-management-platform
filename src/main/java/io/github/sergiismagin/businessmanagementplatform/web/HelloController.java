package io.github.sergiismagin.businessmanagementplatform.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public HelloResponse hello(
            @RequestParam(defaultValue = "Business Management Platform") String name) {

        return new HelloResponse("Hello from " + name);
    }
}
