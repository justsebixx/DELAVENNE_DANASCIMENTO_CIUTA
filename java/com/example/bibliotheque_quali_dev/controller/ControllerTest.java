package com.example.bibliotheque_quali_dev.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerTest {
    @GetMapping("/test")
        public String test(){
        return "je fais un test";
    }
}
