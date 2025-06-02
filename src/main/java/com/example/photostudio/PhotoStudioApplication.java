package com.example.photostudio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PhotoStudioApplication {

    public static void main(String[] args) {
        try{
            SpringApplication.run(PhotoStudioApplication.class, args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
