package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@RestController
@SpringBootApplication
public class DemoApplication {

  @RequestMapping("/")
  String home() {
    return "Hello world!";
  }

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
