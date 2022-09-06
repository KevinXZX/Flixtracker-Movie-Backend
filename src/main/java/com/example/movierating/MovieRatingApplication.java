package com.example.movierating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AutoConfiguration

public class MovieRatingApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(MovieRatingApplication.class, args);
    }

  
}
