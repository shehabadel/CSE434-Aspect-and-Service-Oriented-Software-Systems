package com.example.Install.AOP.service;

import org.springframework.stereotype.Service;

@Service
public class MyService {
    
    public void doSomething() {
        System.out.println("MyService: Doing something...");
    }
} 